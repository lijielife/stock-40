package com.youku.java.copyright.view;

import java.util.HashMap;

import com.youku.java.copyright.bean.Log;

public class LogView extends HashMap<String, Object>{

	private static final long serialVersionUID = 1L;

	public LogView(Log log) {
		put("id", log.getId());
		put("testId", log.getTestId());
	}
}
