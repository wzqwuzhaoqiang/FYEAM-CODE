package com.fuyaogroup.eam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class weixintestController {

	@RequestMapping("/weixinOpen")
	public String messageInfo() {
		return "connect success";
	}
}
