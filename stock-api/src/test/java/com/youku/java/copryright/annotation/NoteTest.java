package com.youku.java.copryright.annotation;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.youku.java.copyright.util.CommonUtil;

public class NoteTest {

	@Test
	public void note() {
		Map<String,String> noteMap = CommonUtil.getNoteMap(Test.class);
		Assert.assertEquals("equals",noteMap.get("id"),"主键ID");
	}

}
