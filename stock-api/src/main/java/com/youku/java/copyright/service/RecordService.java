package com.youku.java.copyright.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youku.java.copyright.bean.Customer;
import com.youku.java.copyright.bean.Good;
import com.youku.java.copyright.bean.Record;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.copyright.mapper.RecordMapper;
import com.youku.java.copyright.util.CommonUtil;
import com.youku.java.copyright.util.Constant.RecordType;
import com.youku.java.copyright.util.DateTool;
import com.youku.java.copyright.view.RecordView;
import com.youku.java.raptor.exception.InvalidArgumentException;

@Transactional(rollbackFor = Exception.class)
@Service
public class RecordService {

	@Autowired
	private RecordMapper recordMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GoodService goodService;
	
	@Autowired
	private CustomerService customerService;

	public Record selectOne(long id) {
		return recordMapper.selectOne(id);
	}
	
	public void delete(long id, User loginInfo) {
		
		Record record = selectOne(id);
		if(record == null) {
			throw new InvalidArgumentException("找不到的记录");
		}else if(record.getUserId().longValue() != loginInfo.getId().longValue()) {
			throw new PermissionDeniedException("权限不足");
		}
		
		Date today = DateTool.getBegin(new Date());
		if(record.getCreateTime().getTime() < today.getTime()) {
			throw new InvalidArgumentException("只能删除今天创建的数据");
		}
		
		if(record.getType() == RecordType.STOCK) {
			List<Record> list = selectById(record.getId(), record.getUserId(), RecordType.STOCK, 1);
			if(list != null && list.size() > 0) {
				throw new InvalidArgumentException("请先删除最新进货记录");
			}
			
			stockRollBack(record, loginInfo);
		}else if(record.getType() == RecordType.SELL) {
			List<Record> list = selectById(record.getId(), record.getUserId(), RecordType.SELL, 1);
			if(list != null && list.size() > 0) {
				throw new InvalidArgumentException("请先删除最新销售记录");
			}
			
			sellRollBack(record, loginInfo);
		}else if(record.getType() == RecordType.DAMAGE) {
			List<Record> list = selectById(record.getId(), record.getUserId(), RecordType.DAMAGE, 1);
			if(list != null && list.size() > 0) {
				throw new InvalidArgumentException("请先删除最新折损记录");
			}
			
			damageRollBack(record, loginInfo);
		}
	}
	
	/**
	 * 进货
	 * 插入记录数据
	 * 影响用户产品费用，其他费用，库存价格
	 * 影响商品的数量，记录ID，记录剩余数量
	 */
	public long stock(Record record, User loginInfo) {
		//插入记录数据
		record.setType(RecordType.STOCK);
		record.setUserId(loginInfo.getId());
		record.setCreateTime(DateTool.standardSdf().format(new Date()));
		CommonUtil.setDefaultValue(record);
		recordMapper.insert(record);
		
		//影响用户产品费用
		userService.updatePrice(loginInfo.getId(), 
				record.getProductionPrice(), record.getOtherPrice(), 0.0, 0.0, 0.0);
		
		//影响商品数量，记录ID，记录剩余数量
		goodService.updateNumber(record.getGoodId(), record.getNumber(), record.getId(), record.getProductionPrice());
		
		customerService.updatePrice(record.getCustomerId(), record.getProductionPrice(), record.getNumber(), 
				0.0, 0, 0.0, 0.0);
		return record.getId();
	}
	
	public void stockRollBack(Record record, User loginInfo) {
		
		//影响用户产品费用
		userService.updatePrice(record.getUserId(), 
				record.getProductionPrice() * -1, record.getOtherPrice() * -1, 0.0, 0.0, 0.0);
		
		//影响商品数量，记录ID，记录剩余数量
		Good good = goodService.selectOne(record.getGoodId());
		if(good.getRecordId().longValue() == record.getId().longValue()) {
			if(good.getStockNumber().intValue() != record.getNumber().intValue()) {
				throw new InvalidArgumentException("本次进货已被售出，无法删除");
			}
			good.setStockNumber(0);
			good.setRecordId(selectFrontRecordId(record.getId(), record.getUserId()));
		}
		good.setNumber(good.getNumber() - record.getNumber());
		good.setProductionPrice(good.getProductionPrice() - record.getProductionPrice());
		goodService.update(good, loginInfo);
		
		customerService.updatePrice(record.getCustomerId(), record.getProductionPrice() * -1, record.getNumber() * -1, 
				0.0, 0, 0.0, 0.0);
		
		deleteById(record.getId(), loginInfo);
	}
	
	/**
	 * 销售
	 * 插入记录数据
	 * 拿到批次进货数据
	 * 影响用户销售价格，利润
	 * 影响商品的数量，记录ID，记录剩余数量，利润
	 */
	public long sell(Record record, User loginInfo) {
		
		//拿到批次进货数据
		Good good = goodService.selectOne(record.getGoodId());
		if(good.getNumber().intValue() < record.getNumber().intValue()) {
			throw new InvalidArgumentException("库存不足以提供销售");
		}
		good.setSellNumber(good.getSellNumber() + record.getNumber());
		good.setNumber(good.getNumber() - record.getNumber());
		
		Good recordGood = selectByBatch(good.getRecordId(), good.getStockNumber(), record.getNumber(),
				record.getSellPrice(), loginInfo);
		good.setRecordId(recordGood.getRecordId());
		good.setStockNumber(recordGood.getStockNumber());
		good.setProfitPrice(good.getProfitPrice() + recordGood.getProfitPrice());
		good.setSellPrice(good.getSellPrice() + record.getSellPrice());

		//插入记录数据
		record.setType(RecordType.SELL);
		record.setUserId(loginInfo.getId());
		record.setCreateTime(DateTool.standardSdf().format(new Date()));
		record.setProfitPrice(recordGood.getProfitPrice());
		CommonUtil.setDefaultValue(record);
		recordMapper.insert(record);
		
		//影响商品数量，记录ID，记录剩余数量，销售总数，总利润
		goodService.update(good, loginInfo);
		
		//影响用户产品费用
		double profitPrice = recordGood.getProfitPrice();
		userService.updatePrice(loginInfo.getId(), 0.0, 0.0, record.getSellPrice(), 0.0, profitPrice);
		
		customerService.updatePrice(record.getCustomerId(), 0.0, 0, record.getSellPrice(), 
				record.getNumber(), profitPrice, 0.0);
		return record.getId();
	}
	
	public void sellRollBack(Record record, User loginInfo) {
		Good good = goodService.selectOne(record.getGoodId());
		good.setSellNumber(good.getSellNumber() - record.getNumber());
		good.setNumber(good.getNumber() + record.getNumber());
		good.setProfitPrice(good.getProfitPrice() - record.getProfitPrice());
		good.setSellPrice(good.getSellPrice() - record.getSellPrice());
		updateStock(record, good);
		goodService.update(good, loginInfo);
		
		userService.updatePrice(loginInfo.getId(), 0.0, 0.0, record.getSellPrice() * -1, 0.0, 
				record.getProfitPrice() * -1);
		
		customerService.updatePrice(record.getCustomerId(), 0.0, 0, record.getSellPrice() * -1, 
				record.getNumber() * -1, record.getProfitPrice() * -1, 0.0);
		
		deleteById(record.getId(), loginInfo);
	}
	
	
	/**
	 * 退货处理
	 * 插入记录数据
	 * 影响用户折损费
	 */
	public long damage(Record record, User loginInfo) {
		//插入记录数据
		record.setType(RecordType.DAMAGE);
		record.setUserId(loginInfo.getId());
		record.setCreateTime(DateTool.standardSdf().format(new Date()));
		CommonUtil.setDefaultValue(record);
		recordMapper.insert(record);
		
		//影响用户产品费用
		userService.updatePrice(loginInfo.getId(), 
				0.0, 0.0, 0.0, record.getDamagePrice(), 0.0);
		
		//更新商品折损费
		if(record.getGoodId() > 0) {
			Good good = goodService.selectOne(record.getGoodId());
			good.setDamagePrice(good.getDamagePrice() + record.getDamagePrice());
			goodService.update(good, loginInfo);
		}
		
		customerService.updatePrice(record.getCustomerId(), 0.0, 0, 0.0, 0, 0.0, record.getDamagePrice());
		return record.getId();
	}
	
	public void damageRollBack(Record record, User loginInfo) {
		//影响用户产品费用
		userService.updatePrice(loginInfo.getId(), 
					0.0, 0.0, 0.0, record.getDamagePrice() * -1, 0.0);
		

		//更新商品折损费
		if(record.getGoodId() > 0) {
			Good good = goodService.selectOne(record.getGoodId());
			good.setDamagePrice(good.getDamagePrice() - record.getDamagePrice());
			goodService.update(good, loginInfo);
		}
		
		customerService.updatePrice(record.getCustomerId(), 0.0, 0, 0.0, 0, 0.0, record.getDamagePrice() * -1);
		
		deleteById(record.getId(), loginInfo);
	}
	
	/**
	 * 获取该次销售所需要记录在商品上的进货记录id，剩余存货量，利润
	 * @param recordId
	 * @param stockNumber
	 * @param needNumber
	 * @return
	 */
	public Good selectByBatch(long recordId, int stockNumber, int needNumber, double sellPrice, User loginInfo) {
		Good good = new Good();
		//进货的库存总量
		int recordNumber = stockNumber;
		//需要记录的进货ID
		long recordIndex = recordId;
		//产品总成本
		double productionPrice = 0.0;
		
		Record src = selectOne(recordId);
		if(recordNumber > needNumber) {
			productionPrice = (src.getProductionPrice() / src.getNumber()) * needNumber;
		}else {
			productionPrice = (src.getProductionPrice() / src.getNumber()) * recordNumber;
		}
		
		batchWhile: do {
			List<Record> list = selectById(recordIndex, loginInfo.getId(), RecordType.STOCK, 10);
			if(list == null || list.size() <= 0) {
				break batchWhile;
			}
			for(Record record : list) {
				recordNumber += record.getNumber();
				recordIndex = record.getId();

				if(recordNumber > needNumber) {
					productionPrice += (record.getProductionPrice() / record.getNumber()) * 
							(needNumber - (recordNumber - record.getNumber()));
					break batchWhile;
				}else {
					productionPrice += record.getProductionPrice();
				}
			}
			
		} while (recordNumber <= needNumber);
		
		if(recordNumber < needNumber) {
			throw new InvalidArgumentException("库存不足以提供销售");
		}

		good.setRecordId(recordIndex);
		good.setStockNumber(recordNumber - needNumber);
		good.setProfitPrice(sellPrice - productionPrice);
		good.setProductionPrice(productionPrice);
		
		return good;
	}
	
	public void updateStock(Record record, Good good) {
		
		//需要回滚的数量
		//遍历record进货记录找到recordId，和剩余数
		int rollBackNumber = record.getNumber();
		long recordId = record.getId();
	
		do {
			List<Record> records = recordMapper.selectFrontById(recordId, record.getUserId(), 
					RecordType.STOCK, 10);
			if(records == null || records.size() <= 0) {
				good.setStockNumber(0);
				good.setRecordId(0l);
				rollBackNumber = 0;
			}
			for(Record temp : records) {
				rollBackNumber -= temp.getNumber();
				
				if(rollBackNumber <= 0) {
					good.setStockNumber(good.getStockNumber() + temp.getNumber() - (rollBackNumber * -1));
					good.setRecordId(temp.getId());
				}
			}
			
		} while (rollBackNumber > 0);
	}
	
	public List<Record> selectById(long id, long userId, int type, int limit) {
		return recordMapper.selectById(id, userId, type, limit);
	}
	
	public long selectFrontRecordId(long recordId, long userId) {
		Record record = recordMapper.selectFrontRecordId(recordId, userId);
		if(record != null) {
			return record.getId();
		}
		return 0l;
	}
	
	public List<Record> selectByUserid(User loginInfo, int type, int page, int pageSize) {
		return recordMapper.selectByUserid(loginInfo.getId(), type, pageSize, (page-1)*pageSize);
	}
	
	public int countByUserid(User loginInfo, int type) {
		return recordMapper.countByUserid(loginInfo.getId(), type);
	}
	
	public List<Record> selectByTime(Date time, int limit) {
		return recordMapper.selectByTime(time, limit);
	}
	
	public List<RecordView> convert2View(List<Record> records) {
		List<Long> customerIds = CommonUtil.entity(records, "customerId", Long.class);
		List<Long> goodIds = CommonUtil.entity(records, "goodId", Long.class);
		
		List<Customer> customers = customerIds == null || customerIds.size() <=0 ? new ArrayList<Customer>()
				:customerService.selectByIds(customerIds);
		List<Good> goods = goodIds == null || goodIds.size() <=0 ? new ArrayList<Good>()
				:goodService.selectByIds(goodIds);
		
		Map<Long, Customer> customerMap = CommonUtil.entityMap(customers, "id", Long.class);
		Map<Long, Good> goodMap = CommonUtil.entityMap(goods, "id", Long.class);
		
		List<RecordView> recordViews = new ArrayList<RecordView>();
		if(records != null && records.size() > 0) {
			for(Record record : records) {
				recordViews.add(new RecordView(record, customerMap.get(record.getCustomerId()), goodMap.get(record.getGoodId())));
			}
		}
		
		return recordViews;
	}
	
	public void deleteById(long id, User loginInfo) {
		Record record = selectOne(id);
		if(record.getUserId().longValue() != loginInfo.getId().longValue()) {
			throw new PermissionDeniedException("没有权限");
		}
		
		recordMapper.delete(id);
	}
	
}
