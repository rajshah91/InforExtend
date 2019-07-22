package com.jeecg.orderexpress.entity;

/**   
 * @Title: Entity
 * @Description: 订单快递表
 * @author onlineGenerator
 * @date 2019-07-10 17:02:14
 * @version V1.0   
 *
 */
public class ExpressServiceEntity implements java.io.Serializable {
	
	private String clientcode;//客户系统代码（飞力达提供） 
	private String clientorderkey;//客户系统单号 
	private String express_company;// 快递公司代码[SF|DEPPON|ZTO|SUR\YUNDA] 
	private String sender;//发件人姓名 
	private String sender_phone;//发件人电话 
	private String sender_mobile;//发件人手机 
	private String sender_company;//发件人公司  
	private String sender_province;//发件省 
	private String sender_city;//发件市 
	private String sender_zip;//发件邮编 
	private String sender_country;//发件县 
	private String sender_addr;//发件人详细地址 
	private String receiver;//收件人 
	private String receiver_phone;//收件人电话 
	private String receiver_mobile;//收件人手机号 
	private String receiver_company;//收件人公司 
	private String receiver_province;//收件人省 
	private String receiver_city;//收件人市 
	private String receiver_zip;//收件人邮编 
	private String receiver_country;//收件人县 
	private String receiver_addr;//收件人详细地址 
	private String sku_code;//货品名称 
	private float weight;//货物重量（KG） 
	private float volume;//货物体积（立方厘米）
	private String remark;//货物信息备注 
	private int package_size;//包裹尺寸（立方厘米） 
	private int package_number;//包裹数量 
	private String transport_type;//运输方式[参见公司运输方式对照表] 
	private String pod; //签回单是否需要[Y|N] 
	private String pay_type;//付款方式[参见公司付款对照表] 
	private int case_num;//件数（此项大于1部分快递公司会产生子母件）
	private String mapcode;//INFOREXTEND01 
	private String service1;//7551234567
	private String bpcode;
	
	public String getClientcode() {
		return clientcode;
	}
	public void setClientcode(String clientcode) {
		this.clientcode = clientcode;
	}
	public String getClientorderkey() {
		return clientorderkey;
	}
	public void setClientorderkey(String clientorderkey) {
		this.clientorderkey = clientorderkey;
	}
	public String getExpress_company() {
		return express_company;
	}
	public void setExpress_company(String express_company) {
		this.express_company = express_company;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSender_phone() {
		return sender_phone;
	}
	public void setSender_phone(String sender_phone) {
		this.sender_phone = sender_phone;
	}
	public String getSender_mobile() {
		return sender_mobile;
	}
	public void setSender_mobile(String sender_mobile) {
		this.sender_mobile = sender_mobile;
	}
	public String getSender_company() {
		return sender_company;
	}
	public void setSender_company(String sender_company) {
		this.sender_company = sender_company;
	}
	public String getSender_province() {
		return sender_province;
	}
	public void setSender_province(String sender_province) {
		this.sender_province = sender_province;
	}
	public String getSender_city() {
		return sender_city;
	}
	public void setSender_city(String sender_city) {
		this.sender_city = sender_city;
	}
	public String getSender_zip() {
		return sender_zip;
	}
	public void setSender_zip(String sender_zip) {
		this.sender_zip = sender_zip;
	}
	public String getSender_country() {
		return sender_country;
	}
	public void setSender_country(String sender_country) {
		this.sender_country = sender_country;
	}
	public String getSender_addr() {
		return sender_addr;
	}
	public void setSender_addr(String sender_addr) {
		this.sender_addr = sender_addr;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiver_phone() {
		return receiver_phone;
	}
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}
	public String getReceiver_mobile() {
		return receiver_mobile;
	}
	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}
	public String getReceiver_company() {
		return receiver_company;
	}
	public void setReceiver_company(String receiver_company) {
		this.receiver_company = receiver_company;
	}
	public String getReceiver_province() {
		return receiver_province;
	}
	public void setReceiver_province(String receiver_province) {
		this.receiver_province = receiver_province;
	}
	public String getReceiver_city() {
		return receiver_city;
	}
	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}
	public String getReceiver_zip() {
		return receiver_zip;
	}
	public void setReceiver_zip(String receiver_zip) {
		this.receiver_zip = receiver_zip;
	}
	public String getReceiver_country() {
		return receiver_country;
	}
	public void setReceiver_country(String receiver_country) {
		this.receiver_country = receiver_country;
	}
	public String getReceiver_addr() {
		return receiver_addr;
	}
	public void setReceiver_addr(String receiver_addr) {
		this.receiver_addr = receiver_addr;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getPackage_size() {
		return package_size;
	}
	public void setPackage_size(int package_size) {
		this.package_size = package_size;
	}
	public int getPackage_number() {
		return package_number;
	}
	public void setPackage_number(int package_number) {
		this.package_number = package_number;
	}
	public String getTransport_type() {
		return transport_type;
	}
	public void setTransport_type(String transport_type) {
		this.transport_type = transport_type;
	}
	public String getPod() {
		return pod;
	}
	public void setPod(String pod) {
		this.pod = pod;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public int getCase_num() {
		return case_num;
	}
	public void setCase_num(int case_num) {
		this.case_num = case_num;
	}
	public String getMapcode() {
		return mapcode;
	}
	public void setMapcode(String mapcode) {
		this.mapcode = mapcode;
	}
	public String getService1() {
		return service1;
	}
	public void setService1(String service1) {
		this.service1 = service1;
	}
	public String getBpcode() {
		return bpcode;
	}
	public void setBpcode(String bpcode) {
		this.bpcode = bpcode;
	}
	
}
