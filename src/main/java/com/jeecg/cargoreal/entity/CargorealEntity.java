package com.jeecg.cargoreal.entity;

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
 * @Description: 急货实时
 * @author onlineGenerator
 * @date 2019-04-01 16:08:20
 * @version V1.0   
 *
 */
@Entity
@Table(name = "cargoreal", schema = "")
@SuppressWarnings("serial")
public class CargorealEntity implements java.io.Serializable {
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
	/**仓库*/
	@Excel(name="仓库",width=15)
	private java.lang.String warehouse;
	/**库区*/
	@Excel(name="库区",width=15)
	private java.lang.String area;
	/**so单号*/
	@Excel(name="so单号",width=15)
	private java.lang.String orderkey;
	/**货主简称*/
	@Excel(name="货主简称",width=15)
	private java.lang.String storer;
	/**收货人简称*/
	@Excel(name="收货人简称",width=15)
	private java.lang.String vendor;
	/**请求出货日期*/
	@Excel(name="请求出货日期",width=15,format = "yyyy-MM-dd")
	private java.util.Date requestshipdate;
	/**预警时间*/
	@Excel(name="预警时间",width=15)
	private java.lang.String warningtime;
	/**订单状态*/
	@Excel(name="订单状态",width=15)
	private java.lang.String orderstatus;
	/**订单状态*/
	@Excel(name="操作员",width=15)
	private java.lang.String operator;
	
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
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  库区
	 */

	@Column(name ="AREA",nullable=true,length=32)
	public java.lang.String getArea(){
		return this.area;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  库区
	 */
	public void setArea(java.lang.String area){
		this.area = area;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  so单号
	 */

	@Column(name ="ORDERKEY",nullable=true,length=32)
	public java.lang.String getOrderkey(){
		return this.orderkey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  so单号
	 */
	public void setOrderkey(java.lang.String orderkey){
		this.orderkey = orderkey;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  货主简称
	 */

	@Column(name ="STORER",nullable=true,length=32)
	public java.lang.String getStorer(){
		return this.storer;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  货主简称
	 */
	public void setStorer(java.lang.String storer){
		this.storer = storer;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收货人简称
	 */

	@Column(name ="VENDOR",nullable=true,length=32)
	public java.lang.String getVendor(){
		return this.vendor;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货人简称
	 */
	public void setVendor(java.lang.String vendor){
		this.vendor = vendor;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  请求出货日期
	 */

	@Column(name ="REQUESTSHIPDATE",nullable=true,length=32)
	public java.util.Date getRequestshipdate(){
		return this.requestshipdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  请求出货日期
	 */
	public void setRequestshipdate(java.util.Date requestshipdate){
		this.requestshipdate = requestshipdate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  预警时间
	 */

	@Column(name ="WARNINGTIME",nullable=true,length=32)
	public java.lang.String getWarningtime(){
		return this.warningtime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  预警时间
	 */
	public void setWarningtime(java.lang.String warningtime){
		this.warningtime = warningtime;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  订单状态
	 */

	@Column(name ="ORDERSTATUS",nullable=true,length=32)
	public java.lang.String getOrderstatus(){
		return this.orderstatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  订单状态
	 */
	public void setOrderstatus(java.lang.String orderstatus){
		this.orderstatus = orderstatus;
	}

	@Column(name ="OPERATOR",nullable=true,length=32)
	public java.lang.String getOperator() {
		return operator;
	}

	public void setOperator(java.lang.String operator) {
		this.operator = operator;
	}
	
}
