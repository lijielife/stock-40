package com.youku.java.copyright.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youku.java.copyright.bean.Good;
import com.youku.java.copyright.bean.Record;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.mapper.RecordMapper;
import com.youku.java.copyright.util.CommonUtil;
import com.youku.java.copyright.util.Constant.RecordType;
import com.youku.java.copyright.util.DateTool;
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

	public Record selectOne(long id) {
		return recordMapper.selectOne(id);
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
		goodService.updateNumber(record.getGoodId(), record.getNumber(), record.getId());
		return record.getId();
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
		return record.getId();
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
		
		return good;
	}
	
	public List<Record> selectById(long id, long userId, int type, int limit) {
		return recordMapper.selectById(id, userId, type, limit);
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
		
		return record.getId();
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
	
}
