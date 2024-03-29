package com.jeecg.orderexpress.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.jeecg.basicdata.entity.BasicDataEntity;
import com.jeecg.basicdata.service.BasicDataServiceI;
import com.jeecg.ncount.service.ScmNcountServiceI;
import com.jeecg.orderexpress.entity.CancleExpressEntity;
import com.jeecg.orderexpress.entity.ExpressServiceEntity;
import com.jeecg.orderexpress.entity.OrderExpressEntity;
import com.jeecg.orderexpress.service.OrderExpressServiceI;
import com.jeecg.webservice.FlksExpressWebService;
import com.jeecg.webservice.InforWebService;

@Service("orderExpressService")
@Transactional(rollbackFor = Exception.class)
public class OrderExpressServiceImpl extends CommonServiceImpl implements OrderExpressServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private FlksExpressWebService flksExpressWebService;
	@Autowired
	private InforWebService inforWebService;
	@Autowired
	private ScmNcountServiceI scmNcountService;
	@Autowired
	private BasicDataServiceI basicDataService;

	public void delete(OrderExpressEntity entity) throws Exception {
		super.delete(entity);
	}

	public Serializable save(OrderExpressEntity entity) throws Exception {
		Serializable t = super.save(entity);
		return t;
	}

	public void saveOrUpdate(OrderExpressEntity entity) throws Exception {
		super.saveOrUpdate(entity);
	}

	@Override
	public String createOrderToExpress(String warehouse, String expressCompany, List<String> orderkeyList,
			String uniqueCode, String printer) throws Exception {
		// TODO Auto-generated method stub
		String result = "";
		String orderkeySql = "";
		for (int i = 0; i < orderkeyList.size(); i++) {
			if (i == 0) {
				orderkeySql += "'" + orderkeyList.get(i) + "'";
			} else {
				orderkeySql += ",'" + orderkeyList.get(i) + "'";
			}
		}
		String wh = typeNameToTypeCode(warehouse, "仓库");//
//		String sql = "";
//		if ("ZTO".equals(expressCompany)) {
//			sql = "select DISTINCT O.SUSR23,O.SUSR27,S.COMPANY,S.ADDRESS1,O.SUSR32 AS RECEIVER,"
//					+ "O.SUSR31,O.SUSR21||'-'||O.SUSR22 AS RECEIVERCOMPANY,O.SUSR30,S.SUSR20,O.SUSR28,'' as cd, substr(o.susr29,0,instr(o.susr29,'/',1,1)-1) as province,"
//					+ "                substr(o.susr29,instr(o.susr29,'/',1,1)+1,instr(o.susr29,'/',1,2)-(instr(o.susr29,'/',1,1)+1)) as city,"
//					+ "                substr(o.susr29,instr(o.susr29,'/',1,2)+1,length(o.susr29)-(instr(o.susr29,'/',1,2)))  as country,"
//					+ "(select substr(OH.susr25, 0, 6) from "+ wh +"_ORDERS OH WHERE OH.ORDERKEY IN ("+orderkeySql+") AND rownum=1)||"
//					+ "(select listagg(to_char(substr(OT.susr25,7,length(OT.susr25))),'/') WITHIN GROUP (ORDER BY OT.orderkey) as ff from "+ wh + "_ORDERS OT WHERE OT.ORDERKEY IN ("
//					+ orderkeySql + ")) AS remark" 
//					+ " from "
//					+ wh + "_ORDERS O LEFT JOIN " + wh + "_STORER S ON O.STORERKEY=S.STORERKEY AND S.TYPE='1'  "
//					+ " WHERE O.ORDERKEY IN (" + orderkeySql + ")";
//		}else if ("YUNDA".equals(expressCompany)) {
//			sql = "select DISTINCT O.SUSR23,O.SUSR27,S.COMPANY,S.ADDRESS1,O.SUSR32 AS RECEIVER,"
//					+ "O.SUSR31,O.SUSR21||'-'||O.SUSR22 AS RECEIVERCOMPANY,O.SUSR30,S.SUSR20,O.SUSR28,'' as cd, substr(o.susr29,0,instr(o.susr29,'/',1,1)-1) as province,"
//					+ "                substr(o.susr29,instr(o.susr29,'/',1,1)+1,instr(o.susr29,'/',1,2)-(instr(o.susr29,'/',1,1)+1)) as city,"
//					+ "                substr(o.susr29,instr(o.susr29,'/',1,2)+1,length(o.susr29)-(instr(o.susr29,'/',1,2)))  as country,"
//					+ "(select substr(OH.susr25, 0, 6) from "+ wh +"_ORDERS OH WHERE OH.ORDERKEY IN ("+orderkeySql+") AND rownum=1)||"
//					+ "(select listagg(to_char(substr(OT.susr25,7,length(OT.susr25))),'/') WITHIN GROUP (ORDER BY OT.orderkey) as ff from "+ wh + "_ORDERS OT WHERE OT.ORDERKEY IN ("
//					+ orderkeySql + ")) AS remark" 
//					+ " from "
//					+ wh + "_ORDERS O LEFT JOIN " + wh + "_STORER S ON O.STORERKEY=S.STORERKEY AND S.TYPE='1'  "
//					+ " WHERE O.ORDERKEY IN (" + orderkeySql + ")";
//		} else {
//			//SF
//			sql = "select DISTINCT O.SUSR23,O.SUSR27,S.COMPANY,S.ADDRESS1,O.SUSR32 AS RECEIVER,"
//					+ "O.SUSR31,O.SUSR21||'-'||O.SUSR22 AS RECEIVERCOMPANY,O.SUSR30,S.SUSR20,O.SUSR28,cl.code,'' as province,'' as city,'' as country,"
//					+ "(select substr(OH.susr25, 0, 6) from "+ wh +"_ORDERS OH WHERE OH.ORDERKEY IN ("+orderkeySql+") AND rownum=1)||"
//					+ "(select listagg(to_char(substr(OT.susr25,7,length(OT.susr25))),'/') WITHIN GROUP (ORDER BY OT.orderkey) as ff from "+ wh + "_ORDERS OT WHERE OT.ORDERKEY IN ("
//					+ orderkeySql + ")) AS remark" + 
//					" from "
//					+ wh + "_ORDERS O LEFT JOIN " + wh
//					+ "_STORER S ON O.STORERKEY=S.STORERKEY AND S.TYPE='1' left join " + wh
//					+ "_codelkup cl on  cl.listname = 'EXPTYP_SF' and cl.description = o.notes2 "
//					+ " WHERE O.ORDERKEY IN (" + orderkeySql + ")";
//		}
	    //List<Object[]> resultList = this.findListbySql(sql);
		List<Object[]> resultList = queryOrderExpressEntity(wh,orderkeySql,expressCompany);
		if (resultList.size() > 1) {
			result = "出货订单寄件信息不同，请确认！";
		} else if (resultList.size() == 1) {
			boolean flag=true;
			if (checkOrders(warehouse, orderkeySql)) {
				for (String orderkey : orderkeyList) {
					OrderExpressEntity expressEntity = new OrderExpressEntity();
					expressEntity.setWarehouse(warehouse);
					expressEntity.setExpressCompany(expressCompany);
					expressEntity.setUniqueCode(uniqueCode);
					expressEntity.setOrderkey(orderkey);
					expressEntity.setPrinter(printer);
					this.save(expressEntity);
				}
				// 发送信息给快递平台
				ExpressServiceEntity entity = new ExpressServiceEntity();
				entity.setClientcode("INFOREXTEND");
				entity.setClientorderkey(uniqueCode);
				entity.setExpress_company(expressCompany);
				entity.setSender(String.valueOf(resultList.get(0)[0])=="null"?"":String.valueOf(resultList.get(0)[0]));
				entity.setSender_phone(String.valueOf(resultList.get(0)[1])=="null"?"":String.valueOf(resultList.get(0)[1]));
				entity.setSender_mobile(String.valueOf(resultList.get(0)[1])=="null"?"":String.valueOf(resultList.get(0)[1]));
				entity.setSender_company(String.valueOf(resultList.get(0)[2])=="null"?"":String.valueOf(resultList.get(0)[2]));
				entity.setSender_province("江苏省");
				entity.setSender_city("苏州市");
				entity.setSender_country("昆山市");
				entity.setSender_zip("");
				entity.setSender_addr(String.valueOf(resultList.get(0)[3])=="null"?"":String.valueOf(resultList.get(0)[3]));
				entity.setReceiver(String.valueOf(resultList.get(0)[4])=="null"?"":String.valueOf(resultList.get(0)[4]));
				entity.setReceiver_phone(String.valueOf(resultList.get(0)[5])=="null"?"":String.valueOf(resultList.get(0)[5]));
				entity.setReceiver_mobile(String.valueOf(resultList.get(0)[5])=="null"?"":String.valueOf(resultList.get(0)[5]));
				entity.setReceiver_company(String.valueOf(resultList.get(0)[6])=="null"?"":String.valueOf(resultList.get(0)[6]));
				if ("null" != String.valueOf(resultList.get(0)[11])) {
					entity.setReceiver_province(String.valueOf(resultList.get(0)[11]));
				} else {
					entity.setReceiver_province("");
				}
				if ("null" != String.valueOf(resultList.get(0)[12])) {
					entity.setReceiver_city(String.valueOf(resultList.get(0)[12]));
				} else {
					entity.setReceiver_city("");
				}
				if ("null" != String.valueOf(resultList.get(0)[13])) {
					entity.setReceiver_country(String.valueOf(resultList.get(0)[13]));
				} else {
					entity.setReceiver_country("");
				}
				entity.setReceiver_zip("");
				entity.setReceiver_addr(String.valueOf(resultList.get(0)[7])=="null"?"":String.valueOf(resultList.get(0)[7]));
				entity.setSku_code(String.valueOf(resultList.get(0)[8])=="null"?"":String.valueOf(resultList.get(0)[8]));
				entity.setPackage_number(1);
				entity.setTransport_type("");
				entity.setPod("N");
				if ("YJ".equals(String.valueOf(resultList.get(0)[9]))) {
					entity.setPay_type("1");
				} else if ("DF".equals(String.valueOf(resultList.get(0)[9]))) {
					entity.setPay_type("2");
				} else {
					entity.setPay_type("1");
				}
				entity.setCase_num(1);
				entity.setMapcode("INFOREXTEND01");
				if ("SF".equals(expressCompany)) {
					//数据库配置月结卡号
					BasicDataEntity basicDataEntity=basicDataService.findUniqueByProperty(BasicDataEntity.class, "code", "SF-MONTH-CARD");
					entity.setService1(basicDataEntity.getData());// 月结卡号，后续优化5125001307(正式)7551234567(测试)
					entity.setService2(String.valueOf(resultList.get(0)[10])=="null"?"":String.valueOf(resultList.get(0)[10]));
				}
				entity.setRemark(String.valueOf(resultList.get(0)[14])=="null"?"":String.valueOf(resultList.get(0)[14]));
				entity.setBpcode(warehouse.replace("FEILI_wmwhse", "WH"));
				// 中通网点
				if ("ZTO".equals(expressCompany)) {
					entity.setSender_station("ZTO-KUNSHAN");
				}
				JSONObject sendMessage = (JSONObject) JSONObject.toJSON(entity);
				System.out.println(sendMessage.toString());
				// 是否对接快递平台
				String receiveMessage = null;
				//if ("1".equals(typeNameToTypeName(expressCompany, "是否对接"))) {
				if(!"TOCOGNOS".equals(printer)) {
					//判断打印机是否为TOCOGNOS
					receiveMessage = flksExpressWebService.createOrderToFlksExpress(sendMessage, uniqueCode,orderkeySql);
				} else {
					String billcode = expressCompany + scmNcountService.getNextKey(expressCompany + "billcode", 10);
					receiveMessage = "{\"mailno\":\"" + billcode
							+ "\",\"clientorderkey\":\"020000000051\",\"resultcode\":\"OK\",\"msg\":\"{\\\"position_no\\\":\\\"\\\",\\\"position\\\":\\\"\\\",\\\"package_wdjc\\\":\\\"\\\"}\"}";
				}
				List<OrderExpressEntity> expressEntities = this.findHql("from OrderExpressEntity where uniqueCode=?",
						uniqueCode);

				if (receiveMessage.equals("")) {
					// 下单失败
					result = "下单失败!";
				} else {
					JSONObject receiveJson = JSONObject.parseObject(receiveMessage);
					if (receiveJson.get("resultcode").toString().equals("OK")) {
						result = "下单成功！";
						String billCode = receiveJson.get("mailno").toString();
						for (OrderExpressEntity orderExpressEntity : expressEntities) {
							// 中通需要解析返回msg
							if ("ZTO".equals(expressCompany)) {
								JSONObject resultJson = JSONObject.parseObject(receiveMessage);
								JSONObject r = JSONObject.parseObject(resultJson.getString("msg"));
								orderExpressEntity.setBillCode(billCode);
								orderExpressEntity.setBagAddr(r.getString("bagAddr"));
								orderExpressEntity.setMark(r.getString("mark"));
								this.saveOrUpdate(orderExpressEntity);
								flag=false;
							} else if ("SF".equals(expressCompany)) {
								orderExpressEntity.setBillCode(billCode);
								String qrcode = receiveJson.get("msg").toString();
								orderExpressEntity.setQrcode(qrcode);
								JSONObject descodeJson = JSONObject.parseObject(qrcode.substring(qrcode.indexOf("{")));
								orderExpressEntity.setDescode(descodeJson.getString("k2").toString());
								this.saveOrUpdate(orderExpressEntity);
								flag=false;
							} else if ("YUNDA".equals(expressCompany)) {
								JSONObject resultJson = JSONObject.parseObject(receiveMessage);
								JSONObject r = JSONObject.parseObject(resultJson.getString("msg"));
								orderExpressEntity.setBillCode(billCode);
								orderExpressEntity.setBagAddr(r.getString("package_wdjc"));
								orderExpressEntity
										.setMark(r.getString("position") + "   " + r.getString("position_no"));
								this.saveOrUpdate(orderExpressEntity);
								flag=false;
							} else {
								orderExpressEntity.setBillCode(billCode);
								this.saveOrUpdate(orderExpressEntity);
								flag=false;
							}
						}

						// 调用接口回填infor
						TSUser user = ResourceUtil.getSessionUser();// 操作人
						inforWebService.sendkeytoInfor(warehouse, user.getUserName(), billCode, uniqueCode,
								orderkeyList);
					} else {
						String msg =receiveJson.getString("msg");
						result = "下单失败!"+msg;
					}
				}
				if (flag) {
					// 删除数据
					this.deleteAllEntitie(expressEntities);
				}
			} else {
				result = "信息有误，无法下单！";
			}
		} else {
			result = "信息有误，无法下单！";
		}
		return result;
	}

	/**
	 * 查找订单快递数据
	 * @param wh
	 * @param orderkeySql
	 * @param expressCompany
	 * @return
	 */
	private List<Object[]> queryOrderExpressEntity(String wh, String orderkeySql, String expressCompany) {
		// TODO Auto-generated method stub
		String sql = "";
		if ("ZTO".equals(expressCompany)) {
			sql = "select DISTINCT O.SUSR23,O.SUSR27,S.COMPANY,S.ADDRESS1,O.SUSR32 AS RECEIVER,"
					+ "O.SUSR31,O.SUSR21||'-'||O.SUSR22 AS RECEIVERCOMPANY,O.SUSR30,S.SUSR20,O.SUSR28,'' as cd, substr(o.susr29,0,instr(o.susr29,'/',1,1)-1) as province,"
					+ "                substr(o.susr29,instr(o.susr29,'/',1,1)+1,instr(o.susr29,'/',1,2)-(instr(o.susr29,'/',1,1)+1)) as city,"
					+ "                substr(o.susr29,instr(o.susr29,'/',1,2)+1,length(o.susr29)-(instr(o.susr29,'/',1,2)))  as country,"
					+ "(select substr(OH.susr25, 0, 6) from "+ wh +"_ORDERS OH WHERE OH.ORDERKEY IN ("+orderkeySql+") AND rownum=1)||"
					+ "(select listagg(to_char(substr(OT.susr25,7,length(OT.susr25))),'/') WITHIN GROUP (ORDER BY OT.orderkey) as ff from "+ wh + "_ORDERS OT WHERE OT.ORDERKEY IN ("
					+ orderkeySql + ")) AS remark" 
					+ " from "
					+ wh + "_ORDERS O LEFT JOIN " + wh + "_STORER S ON O.STORERKEY=S.STORERKEY AND S.TYPE='1'  "
					+ " WHERE O.ORDERKEY IN (" + orderkeySql + ")";
		}else if ("YUNDA".equals(expressCompany)) {
			sql = "select DISTINCT O.SUSR23,O.SUSR27,S.COMPANY,S.ADDRESS1,O.SUSR32 AS RECEIVER,"
					+ "O.SUSR31,O.SUSR21||'-'||O.SUSR22 AS RECEIVERCOMPANY,O.SUSR30,S.SUSR20,O.SUSR28,'' as cd, substr(o.susr29,0,instr(o.susr29,'/',1,1)-1) as province,"
					+ "                substr(o.susr29,instr(o.susr29,'/',1,1)+1,instr(o.susr29,'/',1,2)-(instr(o.susr29,'/',1,1)+1)) as city,"
					+ "                substr(o.susr29,instr(o.susr29,'/',1,2)+1,length(o.susr29)-(instr(o.susr29,'/',1,2)))  as country,"
					+ "(select substr(OH.susr25, 0, 6) from "+ wh +"_ORDERS OH WHERE OH.ORDERKEY IN ("+orderkeySql+") AND rownum=1)||"
					+ "(select listagg(to_char(substr(OT.susr25,7,length(OT.susr25))),'/') WITHIN GROUP (ORDER BY OT.orderkey) as ff from "+ wh + "_ORDERS OT WHERE OT.ORDERKEY IN ("
					+ orderkeySql + ")) AS remark" 
					+ " from "
					+ wh + "_ORDERS O LEFT JOIN " + wh + "_STORER S ON O.STORERKEY=S.STORERKEY AND S.TYPE='1'  "
					+ " WHERE O.ORDERKEY IN (" + orderkeySql + ")";
		} else {
			//SF
			sql = "select DISTINCT O.SUSR23,O.SUSR27,S.COMPANY,S.ADDRESS1,O.SUSR32 AS RECEIVER,"
					+ "O.SUSR31,O.SUSR21||'-'||O.SUSR22 AS RECEIVERCOMPANY,O.SUSR30,S.SUSR20,O.SUSR28,cl.code,'' as province,'' as city,'' as country,"
					+ "(select substr(OH.susr25, 0, 6) from "+ wh +"_ORDERS OH WHERE OH.ORDERKEY IN ("+orderkeySql+") AND rownum=1)||"
					+ "(select listagg(to_char(substr(OT.susr25,7,length(OT.susr25))),'/') WITHIN GROUP (ORDER BY OT.orderkey) as ff from "+ wh + "_ORDERS OT WHERE OT.ORDERKEY IN ("
					+ orderkeySql + ")) AS remark" + 
					" from "
					+ wh + "_ORDERS O LEFT JOIN " + wh
					+ "_STORER S ON O.STORERKEY=S.STORERKEY AND S.TYPE='1' left join " + wh
					+ "_codelkup cl on  cl.listname = 'EXPTYP_SF' and cl.description = o.notes2 "
					+ " WHERE O.ORDERKEY IN (" + orderkeySql + ")";
		}
		List<Object[]> resultList = this.findListbySql(sql);
		return resultList;
	}

	// 检查订单是否符合要求
	private boolean checkOrders(String warehouse, String orderkeySql) {
		// TODO Auto-generated method stub
		String wh = typeNameToTypeCode(warehouse, "仓库");
		String sql = "select o.orderkey from " + wh
				+ "_orders o where (o.uniquerreqnumber is not null or o.mailno is not null) and O.ORDERKEY IN ("
				+ orderkeySql + ")";
		List<Object[]> resultList = this.findListbySql(sql);
		if (resultList.size() > 0) {
			return false;
		} else {
			String hql = "from OrderExpressEntity where warehouse='" + warehouse + "' and orderkey in(" + orderkeySql
					+ ")";
			List<OrderExpressEntity> expressEntities = this.findHql(hql);
			if (expressEntities.size() > 0) {
				return false;
			} else {
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

	private String typeNameToTypeName(String typeName, String typegroupname) {
		List<TSTypegroup> tsTypegroup = this.findHql("from TSTypegroup where typegroupname=?", typegroupname);
		List<TSType> tsType = this.findHql("from TSType where TSTypegroup.id=? and typecode=?",
				tsTypegroup.get(0).getId(), typeName);
		return tsType.get(0).getTypename();
	}

	@Override
	public String addOrderToExpress(List<String> orderkeyList, String uniqueCode) throws Exception {
		// TODO Auto-generated method stub
		List<OrderExpressEntity> orderExpressEntities=this.findHql("from OrderExpressEntity where uniqueCode=?", uniqueCode);
		String result = "";
		String orderkeySql = "";
		for (int i = 0; i < orderkeyList.size(); i++) {
			if (i == 0) {
				orderkeySql += "'" + orderkeyList.get(i) + "'";
			} else {
				orderkeySql += ",'" + orderkeyList.get(i) + "'";
			}
		}
		//判断单号是否符合条件
		if (checkOrders(orderExpressEntities.get(0).getWarehouse(), orderkeySql)) {
			for (OrderExpressEntity orderExpressEntity : orderExpressEntities) {
				orderkeySql+= ",'" + orderExpressEntity.getOrderkey() + "'";
			}
			List<Object[]> resultList = queryOrderExpressEntity(typeNameToTypeCode(orderExpressEntities.get(0).getWarehouse(),"仓库"), orderkeySql, orderExpressEntities.get(0).getExpressCompany());
			if (resultList.size() == 1) {
				// 调用接口回填infor
				TSUser user = ResourceUtil.getSessionUser();// 操作人
				inforWebService.sendkeytoInfor(orderExpressEntities.get(0).getWarehouse(), user.getUserName(), orderExpressEntities.get(0).getBillCode(), uniqueCode,
						orderkeyList);
				for (String orderkey : orderkeyList) {
					OrderExpressEntity expressEntity = new OrderExpressEntity();
					expressEntity.setWarehouse(orderExpressEntities.get(0).getWarehouse());
					expressEntity.setExpressCompany(orderExpressEntities.get(0).getExpressCompany());
					expressEntity.setUniqueCode(uniqueCode);
					expressEntity.setBillCode(orderExpressEntities.get(0).getBillCode());
					expressEntity.setOrderkey(orderkey);
					expressEntity.setPrintCopies(orderExpressEntities.get(0).getPrintCopies());
					expressEntity.setPrinter(orderExpressEntities.get(0).getPrinter());
					expressEntity.setQrcode(orderExpressEntities.get(0).getQrcode());
					expressEntity.setDescode(orderExpressEntities.get(0).getDescode());
					expressEntity.setBagAddr(orderExpressEntities.get(0).getBagAddr());
					expressEntity.setMark(orderExpressEntities.get(0).getMark());
					this.save(expressEntity);
				}
				result = "下单成功！";
			} else {
				result = "出货订单寄件信息不同，请确认！";
			}
		}else {
			result="单号错误！";
		}
		return result;
	}

	@Override
	public boolean cancleOrderToExpress(String warehouse, String uniqueCode, String operator, String remark)
			throws Exception {
		//
	    CancleExpressEntity cancleExpressEntity=new CancleExpressEntity();
	    cancleExpressEntity.setBpcode(warehouse.replace("FEILI_wmwhse", "WH"));
	    cancleExpressEntity.setClientcode("INFOREXTEND");
	    cancleExpressEntity.setClientorderkey(uniqueCode);
	    cancleExpressEntity.setRemark(remark);
	    cancleExpressEntity.setOperator(operator);
	    JSONObject sendMessage = (JSONObject) JSONObject.toJSON(cancleExpressEntity);
	    String receiveMessage=flksExpressWebService.deleteOrderToFlksExpress(sendMessage, uniqueCode);
	    JSONObject receiveJson = JSONObject.parseObject(receiveMessage);
	    if (receiveJson.get("resultcode").toString().equals("OK")) {
	    	return true;
	    }
		return false;
	}
}