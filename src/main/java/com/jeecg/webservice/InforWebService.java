package com.jeecg.webservice;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

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

			String xml = "<Message>" + "<Head>" + "<MessageID />" + "<Date />" + "<MessageType>Utility</MessageType>"
					+ "<Sender>" + "<user>sceadmin</user>" + "<password>sceadmin</password>"
					+ "<SystemID>External</SystemID>" + "<CompanyID />" + "<ReplyToQ />" + "</Sender>" + "<Recipient>"
					+ "<SystemID>FEILI_wmwhse1</SystemID>" + "<CompanyID />" + "<ReplyToQ />" + "</Recipient>"
					+ "</Head>" + "<Body>" + "<Utility>" + "<UtilityHeader>" + "<ProcName>NSPRFOT08</ProcName>"
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

}
