package com.jeecg.apilog.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.io.UnsupportedEncodingException;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 接口日志文档
 * @author onlineGenerator
 * @date 2019-04-01 14:52:38
 * @version V1.0   
 *
 */
@Entity
@Table(name = "apilog", schema = "")
@SuppressWarnings("serial")
public class ApilogEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private java.lang.String updateName;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**所属部门*/
	private java.lang.String sysOrgCode;
	/**所属公司*/
	private java.lang.String sysCompanyCode;
	/**流程状态*/
	private java.lang.String bpmStatus;
	/**状态*/
	@Excel(name="状态",width=15)
	private java.lang.String status;
	/**发送的数据*/
	@Excel(name="发送的数据",width=15)
	private java.lang.String sendxml;
	/**接受的数据*/
	@Excel(name="接受的数据",width=15)
	private java.lang.String receivexml;
	/**结果*/
	@Excel(name="结果",width=15)
	private java.lang.String result;
	/**交互的对象*/
	@Excel(name="交互的对象",width=15)
	private java.lang.String partner;
	/**接口名称*/
	@Excel(name="接口名称",width=15)
	private java.lang.String servicename;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */

	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public java.lang.String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(java.lang.String createName){
		this.createName = createName;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */

	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */

	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */

	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public java.lang.String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(java.lang.String updateName){
		this.updateName = updateName;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */

	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */

	@Column(name ="UPDATE_DATE",nullable=true,length=20)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属部门
	 */

	@Column(name ="SYS_ORG_CODE",nullable=true,length=50)
	public java.lang.String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属部门
	 */
	public void setSysOrgCode(java.lang.String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属公司
	 */

	@Column(name ="SYS_COMPANY_CODE",nullable=true,length=50)
	public java.lang.String getSysCompanyCode(){
		return this.sysCompanyCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属公司
	 */
	public void setSysCompanyCode(java.lang.String sysCompanyCode){
		this.sysCompanyCode = sysCompanyCode;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */

	@Column(name ="BPM_STATUS",nullable=true,length=32)
	public java.lang.String getBpmStatus(){
		return this.bpmStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setBpmStatus(java.lang.String bpmStatus){
		this.bpmStatus = bpmStatus;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */

	@Column(name ="STATUS",nullable=true,length=32)
	public java.lang.String getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发送的数据
	 */

	@Column(name ="SENDXML",nullable=true,length=1024)
	public java.lang.String getSendxml(){
		return this.sendxml;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发送的数据
	 */
	public void setSendxml(java.lang.String sendxml){
		this.sendxml = sendxml;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接受的数据
	 */

	@Column(name ="RECEIVEXML",nullable=true,length=1024)
	public java.lang.String getReceivexml(){
		return this.receivexml;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接受的数据
	 */
	public void setReceivexml(java.lang.String receivexml){
		this.receivexml = receivexml;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结果
	 */

	@Column(name ="RESULT",nullable=true,length=32)
	public java.lang.String getResult(){
		return this.result;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结果
	 */
	public void setResult(java.lang.String result){
		this.result = result;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  交互的对象
	 */

	@Column(name ="PARTNER",nullable=true,length=32)
	public java.lang.String getPartner(){
		return this.partner;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交互的对象
	 */
	public void setPartner(java.lang.String partner){
		this.partner = partner;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口名称
	 */

	@Column(name ="SERVICENAME",nullable=true,length=32)
	public java.lang.String getServicename(){
		return this.servicename;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口名称
	 */
	public void setServicename(java.lang.String servicename){
		this.servicename = servicename;
	}
	
}
