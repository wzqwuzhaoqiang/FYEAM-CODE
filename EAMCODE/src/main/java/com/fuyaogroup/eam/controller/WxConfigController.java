package com.fuyaogroup.eam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping({"/"})
public class WxConfigController {

	@RequestMapping({"WW_verify_jev0OVcJIRHkDxMW.txt"})
    @ResponseBody
    private String returnConfigFile() {
        //把MP_verify_xxxxxx.txt中的内容返回
		log.info("调用了认证文件...............");
        return "jev0OVcJIRHkDxMW";
    }

	
}
