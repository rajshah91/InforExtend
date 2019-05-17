package com.jeecg.downbyzip.service;
import com.jeecg.downbyzip.entity.DownbyzipEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface DownbyzipServiceI extends CommonService{
	
 	public void delete(DownbyzipEntity entity) throws Exception;
 	
 	public Serializable save(DownbyzipEntity entity) throws Exception;
 	
 	public void saveOrUpdate(DownbyzipEntity entity) throws Exception;
 	
}
