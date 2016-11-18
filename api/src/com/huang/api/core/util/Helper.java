package com.huang.api.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Helper {

	private static ThreadLocal<HttpSession> sessionLocal = new ThreadLocal<HttpSession>();

	public static HttpSession getSession()
	{
		return sessionLocal.get();
	}

	public static void putSession(HttpSession session)
	{
		sessionLocal.set(session);
	}

	private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();

	public static HttpServletRequest getRequest()
	{
		return requestLocal.get();
	}

	public static void putRequest(HttpServletRequest request)
	{
		requestLocal.set(request);
	}

	private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();

	public static HttpServletResponse getResponse()
	{
		return responseLocal.get();
	}

	public static void putResponse(HttpServletResponse response)
	{
		responseLocal.set(response);
	}
}
