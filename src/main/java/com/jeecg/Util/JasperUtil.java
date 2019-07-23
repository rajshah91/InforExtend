package com.jeecg.Util;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class JasperUtil {
	
	//生成XML文件并投递FTP
    public static boolean generageXMLAndDeliver(List<Map<String, Object>> data, String _FORMAT, String _PRINTERNAME, int _QUANTITY, String ip, String username, String password, String workdir) {
        Document doc = DocumentHelper.createDocument();
        Element rootElement = DocumentHelper.createElement("labels");
        rootElement.addAttribute("_FORMAT", _FORMAT);
        rootElement.addAttribute("_PRINTERNAME", _PRINTERNAME);
        rootElement.addAttribute("_QUANTITY", String.valueOf(_QUANTITY));
        doc.setRootElement(rootElement);
        for (Map<String, Object> map : data) {
            Element labelElement = DocumentHelper.createElement("label");
            for (String key : map.keySet()) {
                Object o = map.get(key);
                Element element = DocumentHelper.createElement(key.toUpperCase());
                if(o!=null){
                element.setText(String.valueOf(o));
                }else{
                    element.setText("");
                }
                element.setData(o);
                labelElement.add(element);
            }
            rootElement.add(labelElement);
        }
        doc.setXMLEncoding("utf-8");
        if(JasperFtpUtil.setJasper( ip,  username,  password,  workdir,  doc, _PRINTERNAME)){
            return true;
        }else{
            return false;
        }

    }

}
