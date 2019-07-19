package com.jeecg.jasperconfig.service;
import com.jeecg.jasperconfig.entity.JasperconfigEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface JasperconfigServiceI extends CommonService{
	
 	public void delete(JasperconfigEntity entity) throws Exception;
 	
 	public Serializable save(JasperconfigEntity entity) throws Exception;
 	
 	public void saveOrUpdate(JasperconfigEntity entity) throws Exception;
 	
}
