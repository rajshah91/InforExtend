package com.jeecg.orders.service;
import com.jeecg.orders.entity.OrdersEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface OrdersServiceI extends CommonService{
	
 	public void delete(OrdersEntity entity) throws Exception;
 	
 	public Serializable save(OrdersEntity entity) throws Exception;
 	
 	public void saveOrUpdate(OrdersEntity entity) throws Exception;
 	
}
