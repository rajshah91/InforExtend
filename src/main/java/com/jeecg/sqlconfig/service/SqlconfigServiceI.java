package com.jeecg.sqlconfig.service;
import com.jeecg.sqlconfig.entity.SqlconfigEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface SqlconfigServiceI extends CommonService{
	
 	public void delete(SqlconfigEntity entity) throws Exception;
 	
 	public Serializable save(SqlconfigEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SqlconfigEntity entity) throws Exception;
 	
}
