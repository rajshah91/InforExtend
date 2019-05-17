package com.jeecg.formmanager.service;
import com.jeecg.formmanager.entity.FormmanagerEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface FormmanagerServiceI extends CommonService{
	
 	public void delete(FormmanagerEntity entity) throws Exception;
 	
 	public Serializable save(FormmanagerEntity entity) throws Exception;
 	
 	public void saveOrUpdate(FormmanagerEntity entity) throws Exception;
 	
}
