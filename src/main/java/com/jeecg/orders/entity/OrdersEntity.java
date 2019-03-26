package com.jeecg.orders.entity;

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
 * @Description: 出货刷单
 * @author onlineGenerator
 * @date 2019-03-26 10:24:31
 * @version V1.0   
 *
 */
@Entity
@Table(name = "orders", schema = "")
@SuppressWarnings("serial")
public class OrdersEntity implements java.io.Serializable {
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
	/**出货单号*/
	@Excel(name="出货单号",width=15)
	private java.lang.String orderkey;
	/**货主代码*/
	@Excel(name="货主代码",width=15)
	private java.lang.String storerkey;
	/**收货人代码*/
	@Excel(name="收货人代码",width=15)
	private java.lang.String vendor;
	/**订单时间*/
	@Excel(name="订单时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date orderdate;
	/**请求出货时间*/
	@Excel(name="请求出货时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date requestshipdate;
	/**拣货员*/
	@Excel(name="拣货员",width=15)
	private java.lang.String picker;
	/**拣货开始时间*/
	@Excel(name="拣货开始时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date pickstartdate;
	/**拣货完成时间*/
	@Excel(name="拣货完成时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date pickenddate;
	/**贴标员*/
	@Excel(name="贴标员",width=15)
	private java.lang.String labeler;
	/**贴标开始时间*/
	@Excel(name="贴标开始时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date labelstartdate;
	/**贴标完成时间*/
	@Excel(name="贴标完成时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date labelenddate;
	/**复检员*/
	@Excel(name="复检员",width=15)
	private java.lang.String reagents;
	/**复检开始时间*/
	@Excel(name="复检开始时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date reagentstartdate;
	/**复检完成时间*/
	@Excel(name="复检完成时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date reagentenddate;
	/**状态*/
	@Excel(name="状态",width=15,dicCode="ostatus")
	private java.lang.String orderstatus;
	/**仓库*/
	@Excel(name="仓库",width=15)
	private java.lang.String warehouse;
	
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
	 *@return: java.lang.String  出货单号
	 */

	@Column(name ="ORDERKEY",nullable=true,length=32)
	public java.lang.String getOrderkey(){
		return this.orderkey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出货单号
	 */
	public void setOrderkey(java.lang.String orderkey){
		this.orderkey = orderkey;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  货主代码
	 */

	@Column(name ="STORERKEY",nullable=true,length=32)
	public java.lang.String getStorerkey(){
		return this.storerkey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  货主代码
	 */
	public void setStorerkey(java.lang.String storerkey){
		this.storerkey = storerkey;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货人代码
	 */

	@Column(name ="VENDOR",nullable=true,length=32)
	public java.lang.String getVendor(){
		return this.vendor;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货人代码
	 */
	public void setVendor(java.lang.String vendor){
		this.vendor = vendor;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  订单时间
	 */

	@Column(name ="ORDERDATE",nullable=true,length=32)
	public java.util.Date getOrderdate(){
		return this.orderdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  订单时间
	 */
	public void setOrderdate(java.util.Date orderdate){
		this.orderdate = orderdate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  请求出货时间
	 */

	@Column(name ="REQUESTSHIPDATE",nullable=true,length=32)
	public java.util.Date getRequestshipdate(){
		return this.requestshipdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  请求出货时间
	 */
	public void setRequestshipdate(java.util.Date requestshipdate){
		this.requestshipdate = requestshipdate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拣货员
	 */

	@Column(name ="PICKER",nullable=true,length=32)
	public java.lang.String getPicker(){
		return this.picker;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拣货员
	 */
	public void setPicker(java.lang.String picker){
		this.picker = picker;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  拣货开始时间
	 */

	@Column(name ="PICKSTARTDATE",nullable=true,length=32)
	public java.util.Date getPickstartdate(){
		return this.pickstartdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  拣货开始时间
	 */
	public void setPickstartdate(java.util.Date pickstartdate){
		this.pickstartdate = pickstartdate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  拣货完成时间
	 */

	@Column(name ="PICKENDDATE",nullable=true,length=32)
	public java.util.Date getPickenddate(){
		return this.pickenddate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  拣货完成时间
	 */
	public void setPickenddate(java.util.Date pickenddate){
		this.pickenddate = pickenddate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贴标员
	 */

	@Column(name ="LABELER",nullable=true,length=32)
	public java.lang.String getLabeler(){
		return this.labeler;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贴标员
	 */
	public void setLabeler(java.lang.String labeler){
		this.labeler = labeler;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  贴标开始时间
	 */

	@Column(name ="LABELSTARTDATE",nullable=true,length=32)
	public java.util.Date getLabelstartdate(){
		return this.labelstartdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  贴标开始时间
	 */
	public void setLabelstartdate(java.util.Date labelstartdate){
		this.labelstartdate = labelstartdate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  贴标完成时间
	 */

	@Column(name ="LABELENDDATE",nullable=true,length=32)
	public java.util.Date getLabelenddate(){
		return this.labelenddate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  贴标完成时间
	 */
	public void setLabelenddate(java.util.Date labelenddate){
		this.labelenddate = labelenddate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复检员
	 */

	@Column(name ="REAGENTS",nullable=true,length=32)
	public java.lang.String getReagents(){
		return this.reagents;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复检员
	 */
	public void setReagents(java.lang.String reagents){
		this.reagents = reagents;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复检开始时间
	 */

	@Column(name ="REAGENTSTARTDATE",nullable=true,length=32)
	public java.util.Date getReagentstartdate(){
		return this.reagentstartdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复检开始时间
	 */
	public void setReagentstartdate(java.util.Date reagentstartdate){
		this.reagentstartdate = reagentstartdate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复检完成时间
	 */

	@Column(name ="REAGENTENDDATE",nullable=true,length=32)
	public java.util.Date getReagentenddate(){
		return this.reagentenddate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复检完成时间
	 */
	public void setReagentenddate(java.util.Date reagentenddate){
		this.reagentenddate = reagentenddate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */

	@Column(name ="ORDERSTATUS",nullable=true,length=32)
	public java.lang.String getOrderstatus(){
		return this.orderstatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态
	 */
	public void setOrderstatus(java.lang.String orderstatus){
		this.orderstatus = orderstatus;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  仓库
	 */

	@Column(name ="WAREHOUSE",nullable=true,length=32)
	public java.lang.String getWarehouse(){
		return this.warehouse;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  仓库
	 */
	public void setWarehouse(java.lang.String warehouse){
		this.warehouse = warehouse;
	}
	
}
