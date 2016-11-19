package com.huang.api.modules.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.huang.api.core.base.BaseController;
import com.huang.api.modules.system.model.SystemUser;
import com.huang.api.modules.system.service.SystemUserService;

@Controller
@RequestMapping("auth")
public class AuthController extends BaseController
{
	@Autowired
	private SystemUserService systemUserService;

	private final String SESSION_AUTH_ATTRIBUTE = "SESSION_AUTH";

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login()
	{
		return view("login");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView auth()
	{
		debugOutputUrlParameters();
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		if (getParameter("uName") != null && !getParameter("uName").equals("") && getParameter("uPasswd") != null && !getParameter("uPasswd").equals(""))
		{
			SystemUser systemUser = systemUserService.findByUserName(getParameter("uName"));
			if (systemUser != null && systemUser.getUserpasswd().equals(getParameter("uPasswd")))
			{
				getSession().setAttribute(SESSION_AUTH_ATTRIBUTE, systemUser.getUserName() + " [" + systemUser.getRealName() + "]");

				sb.append("\"sucess\":").append(true);
			} else
			{
				sb.append("\"sucess\":").append(false);
				sb.append(",\"error\":\"用户名或密码错误\"");
			}
		} else
		{
			sb.append("\"sucess\":").append(false);
			sb.append(",\"error\":\"缺少参数\"");
		}

		sb.append("}");

		return outString(sb.toString());
	}

	@RequestMapping("manage")
	public ModelAndView manage()
	{
		return view("index");
	}

}
