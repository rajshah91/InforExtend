package com.jeecg.orderexpress.service;
import com.jeecg.orderexpress.entity.OrderExpressEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

import javax.print.DocFlavor.STRING;

public interface OrderExpressServiceI extends CommonService{
	
 	public void delete(OrderExpressEntity entity) throws Exception;
 	
 	public Serializable save(OrderExpressEntity entity) throws Exception;
 	
 	public void saveOrUpdate(OrderExpressEntity entity) throws Exception;
 	
 	public String createOrderToExpress(String warehouse,String expressCompany,String orderkeys,String uniqueCode,String printer) throws Exception;
 	
}
