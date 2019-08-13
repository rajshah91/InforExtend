package com.jeecg.orderexpress.entity;

/*
 * 取消接口的数据类
 */
public class CancleExpressEntity {

	private String clientcode;//客户系统代码（飞力达提供） 
	private String clientorderkey;//客户系统单号 
	private String remark;//取消原因
	private String bpcode;
	private String operator;//操作人
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBpcode() {
		return bpcode;
	}
	public void setBpcode(String bpcode) {
		this.bpcode = bpcode;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
