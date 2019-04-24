package com.jeecg.arealocuse.service;
import com.jeecg.arealocuse.entity.ArealocuseEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface ArealocuseServiceI extends CommonService{
	
 	public void delete(ArealocuseEntity entity) throws Exception;
 	
 	public Serializable save(ArealocuseEntity entity) throws Exception;
 	
 	public void saveOrUpdate(ArealocuseEntity entity) throws Exception;
 	
 	public void achieveArealocuse() throws Exception;
}
