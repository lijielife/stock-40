package com.youku.java.copyright.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LvsController {
	
    @RequestMapping(value="lvs", method=RequestMethod.GET)
    @ResponseBody
    public Object lvs() {
        return new String("ok");
    }
}
