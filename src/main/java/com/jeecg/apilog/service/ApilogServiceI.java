package com.jeecg.apilog.service;
import com.jeecg.apilog.entity.ApilogEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface ApilogServiceI extends CommonService{
	
 	public void delete(ApilogEntity entity) throws Exception;
 	
 	public Serializable save(ApilogEntity entity) throws Exception;
 	
 	public void saveOrUpdate(ApilogEntity entity) throws Exception;
 	
}
