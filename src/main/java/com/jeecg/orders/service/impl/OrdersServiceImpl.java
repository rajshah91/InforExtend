package com.jeecg.orders.service.impl;
import com.jeecg.orders.service.OrdersServiceI;
import com.jeecg.webservice.InforWebService;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.orders.entity.OrdersEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/*import java.util.HashMap;
import java.util.Map;
import java.util.UUID;*/
import java.io.Serializable;
/*import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
*/
/*import org.jeecgframework.minidao.util.FreemarkerParseFactory;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
/*import org.jeecgframework.core.util.ResourceUtil;*/

@Service("ordersService")
@Transactional
public class OrdersServiceImpl extends CommonServiceImpl implements OrdersServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/*@Autowired
	private InforWebService inforWebService;*/
	
 	public void delete(OrdersEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(OrdersEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(OrdersEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}

	@Override
	public String getName(String account) throws Exception {
		//获取名称
		//调取接口
		String name=InforWebService.getUserNameFromInfor(account);
		return name;
	}

	@Override
	public boolean valiorderkey(String orderkey,String warehouse) throws Exception {
		//验证出货单号
		return true;
	}

	@Override
	public void starton(String warehouse, String operation,String startorend,String username) throws Exception {
		// 刷单操作
		
	}
 	
}