package com.jeecg.orderexpress.entity;

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
 * @Description: 订单快递表
 * @author onlineGenerator
 * @date 2019-07-10 17:02:14
 * @version V1.0   
 *
 */
@Entity
@Table(name = "order_express", schema = "")
@SuppressWarnings("serial")
public class OrderExpressEntity implements java.io.Serializable {
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
	/**出货单号*/
	@Excel(name="出货单号",width=15)
	private java.lang.String orderkey;
	/**唯一编码*/
	@Excel(name="唯一编码",width=15)
	private java.lang.String uniqueCode;
	/**快递单号*/
	@Excel(name="快递单号",width=15)
	private java.lang.String billCode;
	/**快递公司*/
	@Excel(name="快递公司",width=15,dicCode="express_co")
	private java.lang.String expressCompany;
	/**打印份数*/
	private java.lang.Integer printCopies;
	/**打印机*/
	private java.lang.String printer;
	/**二维码*/
	private java.lang.String qrcode;
	
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

	@Column(name ="WAREHOUSE",nullable=true,length=16)
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
	 *@return: java.lang.String  出货单号
	 */

	@Column(name ="ORDERKEY",nullable=true,length=10)
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
	 *@return: java.lang.String  唯一编码
	 */

	@Column(name ="UNIQUE_CODE",nullable=true,length=32)
	public java.lang.String getUniqueCode(){
		return this.uniqueCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  唯一编码
	 */
	public void setUniqueCode(java.lang.String uniqueCode){
		this.uniqueCode = uniqueCode;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  快递单号
	 */

	@Column(name ="BILL_CODE",nullable=true,length=256)
	public java.lang.String getBillCode(){
		return this.billCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  快递单号
	 */
	public void setBillCode(java.lang.String billCode){
		this.billCode = billCode;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  快递公司
	 */

	@Column(name ="EXPRESS_COMPANY",nullable=true,length=32)
	public java.lang.String getExpressCompany(){
		return this.expressCompany;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  快递公司
	 */
	public void setExpressCompany(java.lang.String expressCompany){
		this.expressCompany = expressCompany;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  打印份数
	 */

	@Column(name ="PRINT_COPIES",nullable=true,length=4)
	public java.lang.Integer getPrintCopies(){
		return this.printCopies;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打印份数
	 */
	public void setPrintCopies(java.lang.Integer printCopies){
		this.printCopies = printCopies;
	}

	public java.lang.String getPrinter() {
		return printer;
	}

	public void setPrinter(java.lang.String printer) {
		this.printer = printer;
	}

	public java.lang.String getQrcode() {
		return qrcode;
	}

	public void setQrcode(java.lang.String qrcode) {
		this.qrcode = qrcode;
	}
}
