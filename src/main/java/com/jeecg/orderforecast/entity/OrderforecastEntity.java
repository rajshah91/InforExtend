package com.jeecg.orderforecast.entity;

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
 * @Description: 订单预估
 * @author onlineGenerator
 * @date 2019-04-01 15:50:55
 * @version V1.0   
 *
 */
@Entity
@Table(name = "orderforecast", schema = "")
@SuppressWarnings("serial")
public class OrderforecastEntity implements java.io.Serializable {
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
	/**so单号*/
	@Excel(name="so单号",width=15)
	private java.lang.String orderkey;
	/**请求出货日期*/
	@Excel(name="请求出货日期",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date requestshipdate;
	/**订单状态*/
	@Excel(name="订单状态",width=15)
	private java.lang.String orderstatus;
	/**库区*/
	@Excel(name="库区",width=15)
	private java.lang.String area;
	/**货主简称*/
	@Excel(name="货主简称",width=15)
	private java.lang.String storer;
	/**收货人简称*/
	@Excel(name="收货人简称",width=15)
	private java.lang.String vendor;
	/**料号数*/
	@Excel(name="料号数",width=15)
	private java.lang.String skusum;
	/**大库位数*/
	@Excel(name="大库位数",width=15)
	private java.lang.String blocsum;
	/**小库位数*/
	@Excel(name="小库位数",width=15)
	private java.lang.String slocsum;
	/**LPN数*/
	@Excel(name="LPN数",width=15)
	private java.lang.String lpnsum;
	/**拣货员*/
	@Excel(name="拣货员",width=15)
	private java.lang.String pick;
	/**拣货开始时间*/
	@Excel(name="拣货开始时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date pickstartdate;
	/**标准库位时间*/
	@Excel(name="标准库位时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.lang.String stdocdate;
	/**标准预计完成时间*/
	@Excel(name="标准预计完成时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date stdcompletedate;
	/**本次预计完成时间*/
	@Excel(name="本次预计完成时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date nowcompletedate;
	/**实际拣货完成时间*/
	@Excel(name="实际拣货完成时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date factpickdate;
	/**货主代码*/
	@Excel(name="货主代码",width=15)
	private java.lang.String storerkey;
	/**收货人代码*/
	@Excel(name="收货人代码",width=15)
	private java.lang.String altsku;
	/**业务类型*/
	@Excel(name="业务类型",width=15)
	private java.lang.String ordertype;
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  料号数
	 */

	@Column(name ="SKUSUM",nullable=true,length=32)
	public java.lang.String getSkusum(){
		return this.skusum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  料号数
	 */
	public void setSkusum(java.lang.String skusum){
		this.skusum = skusum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  大库位数
	 */

	@Column(name ="BLOCSUM",nullable=true,length=32)
	public java.lang.String getBlocsum(){
		return this.blocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  大库位数
	 */
	public void setBlocsum(java.lang.String blocsum){
		this.blocsum = blocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  小库位数
	 */

	@Column(name ="SLOCSUM",nullable=true,length=32)
	public java.lang.String getSlocsum(){
		return this.slocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  小库位数
	 */
	public void setSlocsum(java.lang.String slocsum){
		this.slocsum = slocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  LPN数
	 */

	@Column(name ="LPNSUM",nullable=true,length=32)
	public java.lang.String getLpnsum(){
		return this.lpnsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  LPN数
	 */
	public void setLpnsum(java.lang.String lpnsum){
		this.lpnsum = lpnsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拣货员
	 */

	@Column(name ="PICK",nullable=true,length=32)
	public java.lang.String getPick(){
		return this.pick;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拣货员
	 */
	public void setPick(java.lang.String pick){
		this.pick = pick;
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
	 *@return: java.util.Date  标准库位时间
	 */

	@Column(name ="STDOCDATE",nullable=true,length=32)
	public java.lang.String getStdocdate(){
		return this.stdocdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  标准库位时间
	 */
	public void setStdocdate(java.lang.String stdocdate){
		this.stdocdate = stdocdate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  标准预计完成时间
	 */

	@Column(name ="STDCOMPLETEDATE",nullable=true,length=32)
	public java.util.Date getStdcompletedate(){
		return this.stdcompletedate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  标准预计完成时间
	 */
	public void setStdcompletedate(java.util.Date stdcompletedate){
		this.stdcompletedate = stdcompletedate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  本次预计完成时间
	 */

	@Column(name ="NOWCOMPLETEDATE",nullable=true,length=32)
	public java.util.Date getNowcompletedate(){
		return this.nowcompletedate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  本次预计完成时间
	 */
	public void setNowcompletedate(java.util.Date nowcompletedate){
		this.nowcompletedate = nowcompletedate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  实际拣货完成时间
	 */

	@Column(name ="FACTPICKDATE",nullable=true,length=32)
	public java.util.Date getFactpickdate(){
		return this.factpickdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  实际拣货完成时间
	 */
	public void setFactpickdate(java.util.Date factpickdate){
		this.factpickdate = factpickdate;
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

	@Column(name ="ALTSKU",nullable=true,length=32)
	public java.lang.String getAltsku(){
		return this.altsku;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收货人代码
	 */
	public void setAltsku(java.lang.String altsku){
		this.altsku = altsku;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型
	 */

	@Column(name ="ORDERTYPE",nullable=true,length=32)
	public java.lang.String getOrdertype(){
		return this.ordertype;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型
	 */
	public void setOrdertype(java.lang.String ordertype){
		this.ordertype = ordertype;
	}
	
}
