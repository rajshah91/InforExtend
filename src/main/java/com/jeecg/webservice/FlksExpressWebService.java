package com.jeecg.webservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Utf8;
import com.jeecg.apilog.entity.ApilogEntity;
import com.jeecg.apilog.service.ApilogServiceI;
import com.jeecg.basicdata.entity.BasicDataEntity;
import com.jeecg.basicdata.service.BasicDataServiceI;

@Service("flksExpressWebService")
public class FlksExpressWebService {

	@Autowired
	private ApilogServiceI apilogService;
	@Autowired
	private BasicDataServiceI basicDataService;

	// 测试
	// app_name： INFOREXTEND
	// UID: 019501adeee75f51979e0f5dd6424a29
	// SECRET: b848c0506dc90091589e533e8b5035d7
	// TOKEN: b848c0506dc90091589e533e8b5035d7
//	private String url = "http://172.20.37.40:8080/";
//	private String url = "http://172.20.60.189:8080/";
	
	//正式
//	private String url = "http://172.20.70.158:8080/";
	
	private String uid = "019501adeee75f51979e0f5dd6424a29";
	private String token = "1fb79de6d5c23edd866604d6b8c5c37d";
	private String secret = "b848c0506dc90091589e533e8b5035d7";

	/**
	 * 下单给快递平台
	 * @param sendMessage
	 * @param msgid
	 * @return
	 */
	public String createOrderToFlksExpress(JSONObject sendMessage, String msgid,String key) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String receiveMessage = "";
		BasicDataEntity basicDataEntity=basicDataService.findUniqueByProperty(BasicDataEntity.class, "code", "EXPRESS");
		
		String sig = getDigest(sendMessage.toString(), secret);
		String methodUrl = basicDataEntity.getData() + "api/createOrder";
		HttpURLConnection connection = null;
		OutputStream dataout = null;
		BufferedReader reader = null;
		String line = null;
		try {
			URL url = new URL(methodUrl);
			connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
			connection.setDoOutput(true);// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
			connection.setDoInput(true); // 设置是否从connection读入，默认情况下是true;
			connection.setRequestMethod("POST");// 设置请求方式为post,默认GET请求
			connection.setUseCaches(false);// post请求不能使用缓存设为false
			connection.setConnectTimeout(300000);// 连接主机的超时时间
			connection.setReadTimeout(300000);// 从主机读取数据的超时时间
			connection.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
			connection.setRequestProperty("connection", "Keep-Alive");// 连接复用
			connection.setRequestProperty("charset", "utf-8");

			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer 66cb225f1c3ff0ddfdae31rae2b57488aadfb8b5e7");
			connection.setRequestProperty("x-companyId", "ZT");
			connection.setRequestProperty("UID", uid);
			connection.setRequestProperty("TOKEN", token);
			connection.setRequestProperty("SIG", sig);
			connection.setRequestProperty("MSGTYPE", "JSON");
			connection.setRequestProperty("MSGID", msgid);
			connection.setRequestProperty("VERSION", "1.2");
			connection.setRequestProperty("STAMP", format.format(new Date()));
			connection.connect();// 建立TCP连接,getOutputStream会隐含的进行connect,所以此处可以不要

			dataout = new DataOutputStream(connection.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数
			dataout.write(sendMessage.toString().getBytes("utf-8"));
			dataout.flush();
			dataout.close();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 发送http请求
				StringBuilder result = new StringBuilder();
				// 循环读取流
				while ((line = reader.readLine()) != null) {
					result.append(line).append(System.getProperty("line.separator"));//
				}
				receiveMessage = result.toString();
				System.out.println("dai"+receiveMessage);
			    saveLog(sendMessage.toString(), changeStringLength(receiveMessage), true, "FLKSExpress", "createOrder", key);	
			}
		} catch (IOException e) {
			e.printStackTrace();
			saveLog(sendMessage.toString(), changeStringLength(receiveMessage), false, "FLKSExpress", "createOrder", key);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connection.disconnect();
		}
		return receiveMessage;
	}

	/**
	 * 给快递平台删单指令
	 * @param sendMessage
	 * @param msgid
	 * @return
	 */
	public String deleteOrderToFlksExpress(JSONObject sendMessage, String msgid) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String receiveMessage = "";
		String sig = getDigest(sendMessage.toString(), secret);
		BasicDataEntity basicDataEntity=basicDataService.findUniqueByProperty(BasicDataEntity.class, "code", "EXPRESS");
		String methodUrl = basicDataEntity.getData() + "api/delOrder";
		HttpURLConnection connection = null;
		OutputStream dataout = null;
		BufferedReader reader = null;
		String line = null;
		try {
			URL url = new URL(methodUrl);
			connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
			connection.setDoOutput(true);// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
			connection.setDoInput(true); // 设置是否从connection读入，默认情况下是true;
			connection.setRequestMethod("POST");// 设置请求方式为post,默认GET请求
			connection.setUseCaches(false);// post请求不能使用缓存设为false
			connection.setConnectTimeout(300000);// 连接主机的超时时间
			connection.setReadTimeout(300000);// 从主机读取数据的超时时间
			connection.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
			connection.setRequestProperty("connection", "Keep-Alive");// 连接复用
			connection.setRequestProperty("charset", "utf-8");

			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer 66cb225f1c3ff0ddfdae31rae2b57488aadfb8b5e7");
			connection.setRequestProperty("x-companyId", "ZT");
			connection.setRequestProperty("UID", uid);
			connection.setRequestProperty("TOKEN", token);
			connection.setRequestProperty("SIG", sig);
			connection.setRequestProperty("MSGTYPE", "JSON");
			connection.setRequestProperty("MSGID", msgid);
			connection.setRequestProperty("VERSION", "1.2");
			connection.setRequestProperty("STAMP", format.format(new Date()));
			connection.connect();// 建立TCP连接,getOutputStream会隐含的进行connect,所以此处可以不要

			dataout = new DataOutputStream(connection.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数
			dataout.write(sendMessage.toString().getBytes("utf-8"));
			dataout.flush();
			dataout.close();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 发送http请求
				StringBuilder result = new StringBuilder();
				// 循环读取流
				while ((line = reader.readLine()) != null) {
					result.append(line).append(System.getProperty("line.separator"));//
				}
				receiveMessage = result.toString();
				System.out.println("dai"+receiveMessage);
			    saveLog(sendMessage.toString(), changeStringLength(receiveMessage), true, "FLKSExpress", "delOrder", "");	
			}
		} catch (IOException e) {
			e.printStackTrace();
			saveLog(sendMessage.toString(), changeStringLength(receiveMessage), false, "FLKSExpress", "delOrder", "");
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connection.disconnect();
		}
		return receiveMessage;
	}
	
	/**
	 *  
	 * 
	 * @param data[报文部分]，SECRET 用户密钥字符串  
	 */
	public String getDigest(String data, String SECRET) {
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update((data + SECRET.toLowerCase()).getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}

	/**
	 * 将byte转为16进制
	 * 
	 * @param bytes
	 * @return
	 */
	private String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

	/**
	 * 保存接口记录
	 */
	private void saveLog(String sendMessage, String receiveMessage, boolean result, String partner, String serviceName,
			String username) {
		ApilogEntity apilogEntity = new ApilogEntity();
		apilogEntity.setSendxml(sendMessage);
		apilogEntity.setReceivexml(receiveMessage);
		if (result) {
			apilogEntity.setResult("成功");
		} else {
			apilogEntity.setResult("失败");
		}
		apilogEntity.setPartner(partner);
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