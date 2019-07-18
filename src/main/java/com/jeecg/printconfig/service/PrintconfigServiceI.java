package com.jeecg.printconfig.service;
import com.jeecg.printconfig.entity.PrintconfigEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PrintconfigServiceI extends CommonService{
	
 	public void delete(PrintconfigEntity entity) throws Exception;
 	
 	public Serializable save(PrintconfigEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PrintconfigEntity entity) throws Exception;
 	
}
