package com.huang.api.core.util;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.huang.api.modules.system.model.SystemUser;
import com.huang.api.modules.system.service.SystemUserService;

public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent>
{
	private static Logger logger = LogManager.getLogger(InstantiationTracingBeanPostProcessor.class);

	public void onApplicationEvent(ContextRefreshedEvent context)
	{
		ApplicationContext app = context.getApplicationContext();

		if (app.getParent() == null)
		{
			logger.info("框架启动完成！准备启动用户认证体系....");

			final String SYS_ADMIN_USER = "SYS_AU";
			SystemUserService systemUserService = (SystemUserService) app.getBean("systemUserService");
			SystemUser systemUser = systemUserService.get(SYS_ADMIN_USER);
			if (systemUser == null)
			{
				systemUser = new SystemUser();
				systemUser.setId(SYS_ADMIN_USER);
				systemUser.setRealName("管理员");
				systemUser.setUserName("admin");
				systemUser.setUserpasswd("admin");
				systemUser.setCreateTime(new Date());

				systemUserService.save(systemUser);
			}
		}
	}

}
