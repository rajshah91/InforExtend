package com.jeecg.webservice;

import java.util.Date;
import java.util.List;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecg.apilog.entity.ApilogEntity;
import com.jeecg.apilog.service.ApilogServiceI;
import com.jeecg.orderexpress.entity.OrderExpressEntity;

@Service("inforWebService")
public class InforWebService {

	@Autowired
	private ApilogServiceI apilogService;

	// 接口地址
	// private String url =
	// "http://sce.feili.com/WMSWebService/services/WmsWebService";
	// 测试
	private String url = "http://scetest.feili.com:9180/WMSWebService/services/WmsWebService";

	/**
	 * 检查info用户是否正确
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public String LoginInfor(String userName, String passWord) {
		String sendXml = null;
		String receiveXml = null;
		try {
			ServiceClient serviceClient = new ServiceClient();
			// 创建服务地址WebService的URL,注意不是WSDL的URL
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// 确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
			options.setAction("http://com.ssaglobal.com/callBackEnd");

			OMFactory fac = OMAbstractFactory.getOMFactory();

			// uri--即为wsdl文档的targetNamespace，命名空间，perfix--可不填
			OMNamespace omNs = fac.createOMNamespace("http://com.ssaglobal.com/", "");
			// 指定方法
			OMElement method = fac.createOMElement("callBackEnd", omNs);
			// 指定方法的参数
			OMElement mobileCode = fac.createOMElement("in0", omNs);
			mobileCode.setText("MessageProcessor");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in1", omNs);
			mobileCode.setText("Utility");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in2", omNs);
			mobileCode.setText("functionOperation");
			method.addChild(mobileCode);

			sendXml = "<Message>" + "<Head>" + "<MessageID/>" + "<Date/>" + "<MessageType>Utility</MessageType>"
					+ "<Sender>" + "<user>sceadmin</user>" + "<password>sceadmin</password>"
					+ "<SystemID>External</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Sender>" + "<Recipient>"
					+ "<SystemID>FEILI_wmwhse1</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Recipient>" + "</Head>"
					+ "<Body>" + "<Utility>" + "<UtilityHeader>" + "<ProcName>NSPRFOT08</ProcName>"
					+ "<callerID>1</callerID>" + "<USERID>" + userName + "</USERID>" + "<LOCALE>zh_cn</LOCALE>"
					+ "<PASSWORD>" + passWord + "</PASSWORD>" + "<INSTANCE>1</INSTANCE>"
					+ "<sendDelimiter>`</sendDelimiter>" + "</UtilityHeader>" + "</Utility>" + "</Body>" + "</Message>";
			mobileCode = fac.createOMElement("in3", omNs);
			mobileCode.setText(sendXml);
			method.addChild(mobileCode);
			method.build();

			// 远程调用web服务
			OMElement resultXml = serviceClient.sendReceive(method);
			receiveXml = resultXml.toString();
			receiveXml = formatXml(receiveXml);
			// saveLog(sendXml, changeStringLength(receiveXml), true, "INFOR",
			// "functionOperation","");
			return receiveXml;
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			// saveLog(sendXml, changeStringLength(receiveXml), false, "INFOR",
			// "functionOperation","");
			return null;
		}
	}

	/**
	 * 获取info用户的用户名
	 * 
	 * @param userName
	 * @return
	 */
	public String getUserNameFromInfor(String userName) {
		String sendXml = null;
		String receiveXml = null;
		try {
			ServiceClient serviceClient = new ServiceClient();
			// 创建服务地址WebService的URL,注意不是WSDL的URL
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// 确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
			options.setAction("http://com.ssaglobal.com/callBackEnd");

			OMFactory fac = OMAbstractFactory.getOMFactory();

			// uri--即为wsdl文档的targetNamespace，命名空间，perfix--可不填
			OMNamespace omNs = fac.createOMNamespace("http://com.ssaglobal.com/", "");
			// 指定方法
			OMElement method = fac.createOMElement("callBackEnd", omNs);
			// 指定方法的参数
			OMElement mobileCode = fac.createOMElement("in0", omNs);
			mobileCode.setText("MessageProcessor");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in1", omNs);
			mobileCode.setText("Utility");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in2", omNs);
			mobileCode.setText("getUserName");
			method.addChild(mobileCode);

			sendXml = "<Message>" + "<Head>" + "<MessageID/>" + "<Date/>" + "<MessageType>Utility</MessageType>"
					+ "<Sender>" + "<user>sceadmin</user>" + "<password>sceadmin</password>"
					+ "<SystemID>External</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Sender>" + "<Recipient>"
					+ "<SystemID>ENTERPRISE</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Recipient>" + "</Head>"
					+ "<Body>" + "<Utility>" + "<UtilityHeader>" + "<USERID>" + userName + "</USERID>"
					+ "</UtilityHeader>" + "</Utility>" + "</Body>" + "</Message>";
			mobileCode = fac.createOMElement("in3", omNs);
			mobileCode.setText(sendXml);
			method.addChild(mobileCode);
			method.build();

			// 远程调用web服务
			OMElement resultXml = serviceClient.sendReceive(method);
			receiveXml = resultXml.toString();
			receiveXml = formatXml(receiveXml);
			/*
			 * saveLog(sendXml, changeStringLength(receiveXml), true, "INFOR",
			 * "getUserName","");
			 */
			return readStringXmlForUserName(receiveXml);
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			saveLog(sendXml, changeStringLength(receiveXml), false, "INFOR", "getUserName", "");
			return null;
		}
	}

	private String readStringXmlForUserName(String xml) {
		Document doc = null;
		String result = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element root = doc.getRootElement(); // 获取根节点
			Element out = root.element("out");
			Element Message = out.element("Message");
			Element Body = Message.element("Body");
			Element Utility = Body.element("Utility");
			Element UtilityHeader = Utility.element("UtilityHeader");
			result = UtilityHeader.elementText("FULLY_QUALIFIED_ID");
			if (result == null || result.isEmpty()) {
				result = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 刷单
	 * 
	 * @param userName
	 * @param opertionType
	 * @param isStartOrEnd
	 * @param orderKeys
	 * @return
	 */
	public String startOnFromInfor(String warehouse, String userName, String opertionType, String isStartOrEnd,
			String orderKeys, String usernow) {
		String sendXml = null;
		String receiveXml = null;
		try {
			ServiceClient serviceClient = new ServiceClient();
			// 创建服务地址WebService的URL,注意不是WSDL的URL
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// 确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
			options.setAction("http://com.ssaglobal.com/callBackEnd");

			OMFactory fac = OMAbstractFactory.getOMFactory();

			// uri--即为wsdl文档的targetNamespace，命名空间，perfix--可不填
			OMNamespace omNs = fac.createOMNamespace("http://com.ssaglobal.com/", "");
			// 指定方法
			OMElement method = fac.createOMElement("callBackEnd", omNs);
			// 指定方法的参数
			OMElement mobileCode = fac.createOMElement("in0", omNs);
			mobileCode.setText("MessageProcessor");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in1", omNs);
			mobileCode.setText("Utility");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in2", omNs);
			mobileCode.setText("storeOrderIsStartOrEnd");
			method.addChild(mobileCode);

			sendXml = "<Message>" + "<Head>" + "<MessageID/>" + "<Date/>" + "<MessageType>Utility</MessageType>"
					+ "<Sender>" + "<User>" + usernow + "</User>" + "<Password>sceadmin</Password>"
					+ "<SystemID>External</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Sender>" + "<Recipient>"
					+ "<SystemID>" + warehouse + "</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Recipient>"
					+ "</Head>" + "<Body>" + "<Utility>" + "<UtilityHeader>" + "<USERNAME>" + userName + "</USERNAME>"
					+ "<OPERTIONTYPE>" + opertionType + "</OPERTIONTYPE>" + "<ISSTARTOREND>" + isStartOrEnd
					+ "</ISSTARTOREND>" + "<ORDERKEYS>" + orderKeys + "</ORDERKEYS>" + "</UtilityHeader>" + "</Utility>"
					+ "</Body>" + "</Message>";
			mobileCode = fac.createOMElement("in3", omNs);
			mobileCode.setText(sendXml);
			method.addChild(mobileCode);
			method.build();

			// 远程调用web服务
			OMElement resultXml = serviceClient.sendReceive(method);
			receiveXml = resultXml.toString();
			receiveXml = formatXml(receiveXml);
			saveLog(sendXml, changeStringLength(receiveXml), true, "INFOR", "storeOrderIsStartOrEnd", userName);
			return readStringXmlForStartOn(receiveXml);
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			saveLog(sendXml, changeStringLength(receiveXml), false, "INFOR", "storeOrderIsStartOrEnd", userName);
			return "失败";
		}
	}

	/**
	 * 回传mailno
	 * 
	 * @param warehouse
	 * @param userName
	 * @param mailno
	 * @param orderKeys
	 * @return
	 */
	public String sendkeytoInfor(String warehouse, String userName, String mailno, String uniquerReqNumber,
			List<String> orderkeyList) {
		String sendXml = null;
		String receiveXml = null;
		try {
			ServiceClient serviceClient = new ServiceClient();
			// 创建服务地址WebService的URL,注意不是WSDL的URL
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// 确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
			options.setAction("http://com.ssaglobal.com/callBackEnd");

			OMFactory fac = OMAbstractFactory.getOMFactory();

			// uri--即为wsdl文档的targetNamespace，命名空间，perfix--可不填
			OMNamespace omNs = fac.createOMNamespace("http://com.ssaglobal.com/", "");
			// 指定方法
			OMElement method = fac.createOMElement("callBackEnd", omNs);
			// 指定方法的参数
			OMElement mobileCode = fac.createOMElement("in0", omNs);
			mobileCode.setText("MessageProcessor");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in1", omNs);
			mobileCode.setText("ShipmentOrder");
			method.addChild(mobileCode);
			mobileCode = fac.createOMElement("in2", omNs);
			mobileCode.setText("store");
			method.addChild(mobileCode);

			//
			sendXml="<Message><Head><MessageID/><Date/><MessageType>ShipmentOrder</MessageType>" + 
					"  <Sender><SystemID>EXceed</SystemID>" + 
					"   <User>"+userName+"</User><Password></Password>" + 
					"   <CompanyID/><ReplyToQ/></Sender>" + 
					"  <Recipient><SystemID>"+warehouse+"</SystemID>" + 
					"   <Verb>Process</Verb><CompanyID/>" + 
					"   <ReplyToQ/><DetailedErrors>true</DetailedErrors>" + 
					"  </Recipient></Head><Body><ShipmentOrder>";
			// 多个订单
			for (String s : orderkeyList) {
				sendXml += "<ShipmentOrderHeader>" + "    <OrderKey>" + s + "</OrderKey>" + "    <MailNo>" + mailno
						+ "</MailNo>" + "    <UniquerReqNumber>" + uniquerReqNumber + "</UniquerReqNumber>"
						+ "   </ShipmentOrderHeader>";
			}
			/*// 多个订单
			for (String s : orderkeyList) {
				// 验证此单号是否已做过下单,
				if (vailorderkey(s)) {
					// nothing,此订单不回传
					return "此订单" + s + "已下单";
				} else {
					sendXml += "<ShipmentOrderHeader>" + "    <OrderKey>" + s + "</OrderKey>" + "    <MailNo>" + mailno
							+ "</MailNo>" + "    <UniquerReqNumber>" + uniquerReqNumber + "</UniquerReqNumber>"
							+ "   </ShipmentOrderHeader>" + "   <ShipmentOrderHeader>";
				}

			}*/

			sendXml += "</ShipmentOrder></Body></Message>";

			mobileCode = fac.createOMElement("in3", omNs);
			mobileCode.setText(sendXml);
			method.addChild(mobileCode);
			method.build();

			// 远程调用web服务
			OMElement resultXml = serviceClient.sendReceive(method);
			receiveXml = resultXml.toString();
			receiveXml = formatXml(receiveXml);
			saveLog(sendXml, changeStringLength(receiveXml), true, "INFOR", "sendkeytoInfor", userName);
			return readStringXmlForStartOn(receiveXml);
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			saveLog(sendXml, changeStringLength(receiveXml), false, "INFOR", "sendkeytoInfor", userName);
			return "失败";
		}
	}

	private boolean vailorderkey(String orderkey) {
		OrderExpressEntity expressEntity = apilogService.findUniqueByProperty(OrderExpressEntity.class, "orderkey",
				orderkey);
		if (expressEntity != null) {
			return true;
		}
		return false;
	}

	private String readStringXmlForStartOn(String xml) {
		Document doc = null;
		String result = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element root = doc.getRootElement(); // 获取根节点
			Element out = root.element("out");
			Element Message = out.element("Message");
			Element Body = Message.element("Body");
			if (Body.elementText("Result") != null && Body.elementText("Result").equals("ERROR")) {
				result = Body.elementText("Error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 格式化返回的报文
	private String formatXml(String xml) {
		String reg1 = "&lt;";
		xml = xml.replace(reg1, "<");
		String reg2 = "&gt;";
		xml = xml.replace(reg2, ">");
		return xml;
	}

	/**
	 * 保存接口记录
	 */
	private void saveLog(String sendXml, String receiveXml, boolean result, String partner, String serviceName,
			String username) {
		ApilogEntity apilogEntity = new ApilogEntity();
		apilogEntity.setSendxml(sendXml);
		apilogEntity.setReceivexml(receiveXml);
		if (result) {
			apilogEntity.setResult("成功");
		} else {
			apilogEntity.setResult("失败");
		}
		apilogEntity.setPartner("INFOR");
		apilogEntity.setServicename(serviceName);
		apilogEntity.setCreateDate(new Date());
		apilogEntity.setOperator(username);
		try {
			apilogService.save(apilogEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 改变字符长度
	private String changeStringLength(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		} else {
			return str.substring(0, str.length() > 1000 ? 1000 : str.length());
		}
	}
}
