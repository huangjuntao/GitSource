package com.huang.api.modules.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huang.api.modules.system.dao.SystemUserDao;
import com.huang.api.modules.system.model.SystemUser;

@Service
public class SystemUserService
{
	@Autowired
	private SystemUserDao systemUserDao;

	public SystemUser get(String id)
	{
		return systemUserDao.get(id);
	}

	public void save(SystemUser systemUser)
	{
		systemUserDao.save(systemUser);
	}

}
