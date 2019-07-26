package com.jeecg.orderexpress.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.print.DocFlavor.STRING;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jeecg.ncount.service.ScmNcountServiceI;
import com.jeecg.orderexpress.entity.ExpressServiceEntity;
import com.jeecg.orderexpress.entity.OrderExpressEntity;
import com.jeecg.orderexpress.service.OrderExpressServiceI;
import com.jeecg.webservice.FlksExpressWebService;
import com.jeecg.webservice.InforWebService;

@Service("orderExpressService")
@Transactional(rollbackFor=Exception.class)
public class OrderExpressServiceImpl extends CommonServiceImpl implements OrderExpressServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private FlksExpressWebService flksExpressWebService;
	@Autowired
	private InforWebService inforWebService;
	@Autowired
	private ScmNcountServiceI scmNcountService;
	
 	public void delete(OrderExpressEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(OrderExpressEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(OrderExpressEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}

	@Override
	public String createOrderToExpress(String warehouse, String expressCompany, List<String> orderkeyList, String uniqueCode,String printer) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String orderkeySql="";
		for (int i = 0; i < orderkeyList.size(); i++) {
			if(i==0) {
				orderkeySql+="'"+orderkeyList.get(i)+"'";
			}else {
				orderkeySql+=",'"+orderkeyList.get(i)+"'";
			}
		}
		String wh = typeNameToTypeCode(warehouse, "仓库");
		String sql="select DISTINCT O.SUSR23,O.SUSR27,S.COMPANY,S.ADDRESS1,O.SUSR32||'-'||"
				+ "(select OT.SUSR25 from  "+wh+"_ORDERS OT WHERE OT.ORDERKEY IN ("+orderkeySql+") AND ROWNUM =1) AS RECEIVER," + 
				"O.SUSR31,O.SUSR21||'-'||O.SUSR22 AS RECEIVERCOMPANY,O.SUSR30,S.SUSR20,O.SUSR28,cl.code from "+wh+"_ORDERS O LEFT JOIN "+wh+"_STORER S ON O.STORERKEY=S.STORERKEY AND S.TYPE='1' left join "+wh+"_codelkup cl on  cl.listname = 'EXPTYP_SF' and cl.description = o.notes2 "
				+ " WHERE O.ORDERKEY IN ("+orderkeySql+")";
		List<Object[]> resultList = this.findListbySql(sql);
		if(resultList.size()>1) {
			result="出货订单寄件信息不同，请确认！";	
		}else if(resultList.size()==1) {
			if(checkOrders(warehouse,orderkeySql)) {
				for (String orderkey : orderkeyList) {
					OrderExpressEntity expressEntity=new OrderExpressEntity();
					expressEntity.setWarehouse(warehouse);
					expressEntity.setExpressCompany(expressCompany);
					expressEntity.setUniqueCode(uniqueCode);
					expressEntity.setOrderkey(orderkey);
					expressEntity.setPrinter(printer);
					this.save(expressEntity);
				}
			    //发送信息给快递平台
				ExpressServiceEntity entity =new ExpressServiceEntity();
				entity.setClientcode("INFOREXTEND");
				entity.setClientorderkey(uniqueCode);
				entity.setExpress_company(expressCompany);
				entity.setSender(String.valueOf(resultList.get(0)[0]));
				entity.setSender_phone(String.valueOf(resultList.get(0)[1]));
				entity.setSender_mobile(String.valueOf(resultList.get(0)[1]));
				entity.setSender_company(String.valueOf(resultList.get(0)[2]));
				entity.setSender_province("");
				entity.setSender_city("");
				entity.setSender_country("");
				entity.setSender_zip("");
				entity.setSender_addr(String.valueOf(resultList.get(0)[3]));
				entity.setReceiver(String.valueOf(resultList.get(0)[4]));
				entity.setReceiver_phone(String.valueOf(resultList.get(0)[5]));
				entity.setReceiver_mobile(String.valueOf(resultList.get(0)[5]));
				entity.setReceiver_company(String.valueOf(resultList.get(0)[6]));
				entity.setReceiver_province("");
				entity.setReceiver_city("");
				entity.setReceiver_country("");
				entity.setReceiver_zip("");
				entity.setReceiver_addr(String.valueOf(resultList.get(0)[7]));
				entity.setSku_code(String.valueOf(resultList.get(0)[8]));
				entity.setPackage_number(1);
				entity.setRemark("");
				entity.setTransport_type("");
				entity.setPod("Y");
				if("YJ".equals(String.valueOf(resultList.get(0)[9]))) {
					entity.setPay_type("1");
				}else if("DF".equals(String.valueOf(resultList.get(0)[9]))) {
					entity.setPay_type("2");
				}else {
					entity.setPay_type("1");
				}
				entity.setCase_num(1);
				entity.setMapcode("INFOREXTEND01");
				entity.setService1("7551234567");
				entity.setService2(String.valueOf(resultList.get(0)[10]));
				entity.setBpcode(warehouse.replace("FEILI_wmwhse", "WH"));
				//中通网点
				if("ZTO".equals(expressCompany)) {
					/*entity.setSender_station("ZTO-WUJIANG");*/
				}
				JSONObject sendMessage = (JSONObject) JSONObject.toJSON(entity);
				System.out.println(sendMessage.toString());
				//是否对接快递平台
				String receiveMessage=null;
				if("1".equals(typeNameToTypeCode(expressCompany, "是否对接"))) {
					receiveMessage=flksExpressWebService.createOrderToFlksExpress(sendMessage, uniqueCode);
				}else {
					String billcode = expressCompany + scmNcountService.getNextKey(expressCompany+"billcode", 10);
					receiveMessage="{\"mailno\":\""+billcode+"\",\"clientorderkey\":\"020000000051\",\"resultcode\":\"OK\",\"msg\":\"MMM={'k1':'','k2':'886','k3':'','k4':'T4','k5':'444017102136','k6':'','k7':'93b3f54'}\",\"originaltext\":\"\"}";
				}
				List<OrderExpressEntity> expressEntities=this.findHql("from OrderExpressEntity where uniqueCode=?",uniqueCode);
				
				if(receiveMessage.equals("")) {
					//下单失败
					result="下单失败!";
				}else {
					JSONObject receiveJson=JSONObject.parseObject(receiveMessage);
					if(receiveJson.get("resultcode").toString().equals("OK")) {
						result="下单成功！";
						String billCode=receiveJson.get("mailno").toString();
						for (OrderExpressEntity orderExpressEntity : expressEntities) {
							//中通需要解析返回msg
							if("ZTO".equals(expressCompany)) {
								JSONObject resultJson=JSONObject.parseObject(receiveMessage);
								JSONObject r=JSONObject.parseObject(resultJson.getString("msg"));
								String bagAddr=r.getString("bagAddr");//集包地
								String mark=r.getString("mark");//大头笔
							}else if("SF".equals(expressCompany)){
								orderExpressEntity.setBillCode(billCode);
								String qrcode=receiveJson.get("msg").toString();
								orderExpressEntity.setQrcode(qrcode);
								JSONObject descodeJson=JSONObject.parseObject(qrcode.substring(qrcode.indexOf("{")));
								orderExpressEntity.setDescode(descodeJson.getString("k2").toString());
								this.saveOrUpdate(orderExpressEntity);
							}else {
								orderExpressEntity.setBillCode(billCode);
								this.saveOrUpdate(orderExpressEntity);
							}
						}
						
						//调用接口回填infor
						TSUser user= ResourceUtil.getSessionUser();// 操作人
						inforWebService.sendkeytoInfor(warehouse, user.getUserName(), billCode, uniqueCode, orderkeyList);
					}else {
						result="下单失败!";
					}
				}
				if(result.equals("下单失败!")) {
					//删除数据
					this.deleteAllEntitie(expressEntities);
				}
			}else {
				result="信息有误，无法下单！";
			}
		}else {
			result="信息有误，无法下单！";
		}
		return result;
	}

	//检查订单是否符合要求
	private boolean checkOrders(String warehouse,String orderkeySql) {
		// TODO Auto-generated method stub
		String wh = typeNameToTypeCode(warehouse, "仓库");
		String sql="select o.orderkey from "+wh+"_orders o where (o.uniquerreqnumber is not null or o.mailno is not null) and O.ORDERKEY IN ("+orderkeySql+")";
		List<Object[]> resultList = this.findListbySql(sql);
		if(resultList.size()>0) {
			return false;
		}else {
				String hql="from OrderExpressEntity where warehouse='"+warehouse+"' and orderkey in("+orderkeySql+")";
				List<OrderExpressEntity> expressEntities=this.findHql(hql);
				if(expressEntities.size()>0) {
					return false;
				}else {
					return true;
				}
		}
	}
	
	// 仓库字典code 转换
		private String typeNameToTypeCode(String typeName, String typegroupname) {
			List<TSTypegroup> tsTypegroup = this.findHql("from TSTypegroup where typegroupname=?", typegroupname);
			List<TSType> tsType = this.findHql("from TSType where TSTypegroup.id=? and typename=?",
					tsTypegroup.get(0).getId(), typeName);
			return tsType.get(0).getTypecode();
		}
 	
}