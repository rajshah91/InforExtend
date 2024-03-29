package com.jeecg.orders.service;
import com.jeecg.orders.entity.OrdersEntity;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;

import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;
import java.util.List;

public interface OrdersServiceI extends CommonService{
	
 	public void delete(OrdersEntity entity) throws Exception;
 	
 	public Serializable save(OrdersEntity entity) throws Exception;
 	
 	public void saveOrUpdate(OrdersEntity entity) throws Exception;
 	
 	//获取账号名称
 	public String getName(String account) throws Exception;
 	
 	//验证出货单号
 	public boolean valiorderkey(String orderkey,String warehouse) throws Exception;
 	
 	//刷单操作
 	public String starton(String warehouse,String operation,String startorend,String username,String orderkeys,String usernow) throws Exception;
    
 	//获取仓库
 	public List<UsercontactwhEntity> getwarehouse();
}
