package com.jeecg.orderexpress.service;
import com.jeecg.orderexpress.entity.OrderExpressEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface OrderExpressServiceI extends CommonService{
	
 	public void delete(OrderExpressEntity entity) throws Exception;
 	
 	public Serializable save(OrderExpressEntity entity) throws Exception;
 	
 	public void saveOrUpdate(OrderExpressEntity entity) throws Exception;
 	
}
