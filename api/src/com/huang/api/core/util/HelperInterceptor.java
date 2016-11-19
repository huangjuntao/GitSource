package com.huang.api.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HelperInterceptor implements HandlerInterceptor
{
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (HandlerMethod.class.isInstance(handler))
        {
            Helper.putSession(request.getSession());
            Helper.putRequest(request);
            Helper.putResponse(response);
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        if (modelAndView != null)
        {
            HandlerMethod method = (HandlerMethod) handler;
            Object handlerObj = method.getBean();
            String className = handlerObj.getClass().getName();

            int packageCursor = className.indexOf("controller");
            if (packageCursor > -1)
            {
                String packagePath = className.substring(0, packageCursor).replace(".", "/");
                modelAndView.setViewName(packagePath.concat("jsp/").concat(modelAndView.getViewName()));
            }
        }

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        if (HandlerMethod.class.isInstance(handler))
        {
            Helper.putRequest(null);
            Helper.putResponse(null);
            Helper.putSession(null);
        }
    }
}
