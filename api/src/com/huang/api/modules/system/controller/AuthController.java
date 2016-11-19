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
            System.out.println(systemUser.getUserpasswd());
            if (systemUser.getUserpasswd().equals(getParameter("uPasswd")))
            {
                sb.append("\"sucess\":").append(true);
            } else
                sb.append("\"sucess\":").append(false);
        } else
        {
            sb.append("\"sucess\":").append(false);
        }

        sb.append("}");
        System.out.println(sb);
        return outString(sb.toString());
    }

    @RequestMapping("manage")
    public ModelAndView manage()
    {
        return view("index");
    }

}
