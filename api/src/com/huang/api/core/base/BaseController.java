package com.huang.api.core.base;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.huang.api.core.util.Helper;

public class BaseController
{

	private static Logger log = LogManager.getLogger(BaseController.class);

	protected HttpServletRequest getRequest()
	{
		return Helper.getRequest();
	}

	protected HttpServletResponse getResponse()
	{
		return Helper.getResponse();
	}

	protected HttpSession getSession()
	{
		return Helper.getSession();
	}

	protected String getParameter(String parameter)
	{
		return Helper.getRequest().getParameter(parameter);
	}

	public ModelAndView outString(String string)
	{
		try
		{
			if (getResponse() == null)
				return null;
			getResponse().setContentType("text/html;charset=UTF-8");
			PrintWriter out = getResponse().getWriter();
			out.write(string);
			out.flush();
			out.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public ModelAndView view(String path)
	{
		return new ModelAndView(path);
	}

	public void debugOutputUrlParameters()
	{
		log.info("--------------------------------- Debug Parameters------------------------------------");
		log.info("request url: {}{}, getParameters()。", getRequest().getContextPath(), getRequest().getServletPath());

		Enumeration<?> enu = getRequest().getParameterNames();
		Set<String> keySet = new HashSet<String>();
		while (enu.hasMoreElements())
		{
			String paraName = (String) enu.nextElement();
			keySet.add(paraName);
			log.info("{}:{}", paraName, getRequest().getParameter(paraName));
		}

		log.info("--------------------------------- Debug Finish ------------------------------------");
	}

	public ModelAndView actionMethodByName(String actionName)
	{
		try
		{
			Method method = this.getClass().getDeclaredMethod(actionName, new Class[] {});
			if (method != null)
			{
				if (method.getReturnType() == ModelAndView.class && (method.getModifiers() & 1) == 1)
				{
					return (ModelAndView) method.invoke(this);
				}
			}
		} catch (Exception e)
		{
			if (e.getClass() == NoSuchMethodException.class)
			{
				return outString("<PRE>方法调用时发生错误：\n没有这个方法: " + this.getClass().getSimpleName() + "." + actionName + "\n</PRE>");
			} else
			{

				StringWriter sw = new StringWriter();
				e.getCause().printStackTrace(new PrintWriter(sw));
				return outString("<PRE>方法调用时发生错误：\n" + sw.toString() + "</PRE>");
			}
		}
		return outString("方法调用时发生错误: " + this.getClass().getSimpleName() + "." + actionName);
	}
}
