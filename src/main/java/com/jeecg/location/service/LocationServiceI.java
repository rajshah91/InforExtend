package com.jeecg.location.service;
import com.jeecg.location.entity.LocationEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface LocationServiceI extends CommonService{
	
 	public void delete(LocationEntity entity) throws Exception;
 	
 	public Serializable save(LocationEntity entity) throws Exception;
 	
 	public void saveOrUpdate(LocationEntity entity) throws Exception;
 	
}
