package com.huang.api.modules.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.huang.api.core.base.BaseController;
import com.huang.api.modules.system.service.SystemUserService;

@Controller
@RequestMapping("auth")
public class AuthController extends BaseController
{
	@Autowired
	private SystemUserService systemUserService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login()
	{
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView auth()
	{
		return null;
	}

}
