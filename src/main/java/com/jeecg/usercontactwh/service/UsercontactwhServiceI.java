package com.jeecg.usercontactwh.service;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface UsercontactwhServiceI extends CommonService{
	
 	public void delete(UsercontactwhEntity entity) throws Exception;
 	
 	public Serializable save(UsercontactwhEntity entity) throws Exception;
 	
 	public void saveOrUpdate(UsercontactwhEntity entity) throws Exception;
 	
}
