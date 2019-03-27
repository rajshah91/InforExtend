package com.jeecg.webservice;

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

public class InforWebService {

	// 接口地址
	private static String url = "http://scetest.feili.com:9180/WMSWebService/services/WmsWebService";

	public static String LoginInfor(String userName, String passWord) {
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

			String xml = "<Message>" + "<Head>" + "<MessageID/>" + "<Date/>" + "<MessageType>Utility</MessageType>"
					+ "<Sender>" + "<user>sceadmin</user>" + "<password>sceadmin</password>"
					+ "<SystemID>External</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Sender>" + "<Recipient>"
					+ "<SystemID>FEILI_wmwhse1</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Recipient>" + "</Head>"
					+ "<Body>" + "<Utility>" + "<UtilityHeader>" + "<ProcName>NSPRFOT08</ProcName>"
					+ "<callerID>1</callerID>" + "<USERID>" + userName + "</USERID>" + "<LOCALE>zh_cn</LOCALE>"
					+ "<PASSWORD>" + passWord + "</PASSWORD>" + "<INSTANCE>1</INSTANCE>"
					+ "<sendDelimiter>`</sendDelimiter>" + "</UtilityHeader>" + "</Utility>" + "</Body>" + "</Message>";
			mobileCode = fac.createOMElement("in3", omNs);
			mobileCode.setText(xml);
			method.addChild(mobileCode);
			method.build();

			// 远程调用web服务
			OMElement resultXml = serviceClient.sendReceive(method);
			String result = resultXml.toString();
			String reg1 = "&lt;";
			result = result.replace(reg1, "<");
			String reg2 = "&gt;";
			result = result.replace(reg2, ">");
			return result;
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			return null;
		}
	}

	public static String getUserNameFromInfor(String userName) {
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

			String xml = "<Message>" + "<Head>" + "<MessageID/>" + "<Date/>" + "<MessageType>Utility</MessageType>"
					+ "<Sender>" + "<user>sceadmin</user>" + "<password>sceadmin</password>"
					+ "<SystemID>External</SystemID>" + "<CompanyID/>" + "<ReplyToQ/>" + "</Sender>" + "<Recipient>"
					+ "<SystemID>ENTERPRISE</SystemID>" + "<CompanyID />" + "<ReplyToQ />" + "</Recipient>" + "</Head>"
					+ "<Body>" + "<Utility>" + "<UtilityHeader>" + "<USERID>" + userName + "</USERID>"
					+ "</UtilityHeader>" + "</Utility>" + "</Body>" + "</Message>";
			mobileCode = fac.createOMElement("in3", omNs);
			mobileCode.setText(xml);
			method.addChild(mobileCode);
			method.build();

			// 远程调用web服务
			OMElement resultXml = serviceClient.sendReceive(method);
			String result = resultXml.toString();
			String reg1 = "&lt;";
			result = result.replace(reg1, "<");
			String reg2 = "&gt;";
			result = result.replace(reg2, ">");
			return readStringXmlForUserName(result);
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			return null;
		}
	}

	private static String readStringXmlForUserName(String xml) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
