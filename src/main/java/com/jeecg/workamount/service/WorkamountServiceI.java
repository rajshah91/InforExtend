package com.jeecg.workamount.service;
import com.jeecg.workamount.entity.WorkamountEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface WorkamountServiceI extends CommonService{
	
 	public void delete(WorkamountEntity entity) throws Exception;
 	
 	public Serializable save(WorkamountEntity entity) throws Exception;
 	
 	public void saveOrUpdate(WorkamountEntity entity) throws Exception;
 	
}
