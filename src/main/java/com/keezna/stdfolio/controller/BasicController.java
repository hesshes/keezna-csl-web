package com.keezna.stdfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicController {
	
	@GetMapping("/hello")
	@ResponseBody
	public String hello() {
		return "hello world";
	}
}
