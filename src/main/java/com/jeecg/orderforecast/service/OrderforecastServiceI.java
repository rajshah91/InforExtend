package com.jeecg.orderforecast.service;
import com.jeecg.orderforecast.entity.OrderforecastEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface OrderforecastServiceI extends CommonService{
	
 	public void delete(OrderforecastEntity entity) throws Exception;
 	
 	public Serializable save(OrderforecastEntity entity) throws Exception;
 	
 	public void saveOrUpdate(OrderforecastEntity entity) throws Exception;
 	
}
