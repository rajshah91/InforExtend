package com.jeecg.orders.service;
import com.jeecg.orders.entity.OrdersEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface OrdersServiceI extends CommonService{
	
 	public void delete(OrdersEntity entity) throws Exception;
 	
 	public Serializable save(OrdersEntity entity) throws Exception;
 	
 	public void saveOrUpdate(OrdersEntity entity) throws Exception;
 	
 	//获取账号名称
 	public String getName(String account) throws Exception;
 	
 	//验证出货单号
 	public void valiorderkey(String orderkey) throws Exception;
}
