package com.jeecg.basicdata.service;
import com.jeecg.basicdata.entity.BasicDataEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface BasicDataServiceI extends CommonService{
	
 	public void delete(BasicDataEntity entity) throws Exception;
 	
 	public Serializable save(BasicDataEntity entity) throws Exception;
 	
 	public void saveOrUpdate(BasicDataEntity entity) throws Exception;
 	
}
