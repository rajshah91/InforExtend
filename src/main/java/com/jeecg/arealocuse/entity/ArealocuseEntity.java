package com.jeecg.arealocuse.entity;

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
 * @Description: 库位使用率
 * @author onlineGenerator
 * @date 2019-04-03 09:03:18
 * @version V1.0   
 *
 */
@Entity
@Table(name = "arealocuse", schema = "")
@SuppressWarnings("serial")
public class ArealocuseEntity implements java.io.Serializable {
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
	/**日期*/
	@Excel(name="日期",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date selectdate;
	/**分区*/
	@Excel(name="分区",width=15)
	private java.lang.String region;
	/**业务部*/
	@Excel(name="业务部",width=15)
	private java.lang.String operatdepart;
	/**操作科*/
	@Excel(name="操作科",width=15)
	private java.lang.String operatsection;
	/**库区*/
	@Excel(name="库区",width=15)
	private java.lang.String area;
	/**大储位使用*/
	@Excel(name="大储位使用",width=15)
	private java.lang.String bloc;
	/**小储位使用*/
	@Excel(name="小储位使用",width=15)
	private java.lang.String sloc;
	/**总使用率*/
	@Excel(name="总使用率",width=15)
	private java.lang.String totalrate;
	/**lpn数*/
	@Excel(name="lpn数",width=15)
	private java.lang.String lpn;
	
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日期
	 */

	@Column(name ="SELECTDATE",nullable=true,length=32)
	public java.util.Date getSelectdate(){
		return this.selectdate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日期
	 */
	public void setSelectdate(java.util.Date selectdate){
		this.selectdate = selectdate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  分区
	 */

	@Column(name ="REGION",nullable=true,length=32)
	public java.lang.String getRegion(){
		return this.region;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  分区
	 */
	public void setRegion(java.lang.String region){
		this.region = region;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务部
	 */

	@Column(name ="OPERATDEPART",nullable=true,length=32)
	public java.lang.String getOperatdepart(){
		return this.operatdepart;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务部
	 */
	public void setOperatdepart(java.lang.String operatdepart){
		this.operatdepart = operatdepart;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作科
	 */

	@Column(name ="OPERATSECTION",nullable=true,length=32)
	public java.lang.String getOperatsection(){
		return this.operatsection;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作科
	 */
	public void setOperatsection(java.lang.String operatsection){
		this.operatsection = operatsection;
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
	 *@return: java.lang.String  大储位使用
	 */

	@Column(name ="BLOC",nullable=true,length=32)
	public java.lang.String getBloc(){
		return this.bloc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  大储位使用
	 */
	public void setBloc(java.lang.String bloc){
		this.bloc = bloc;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  小储位使用
	 */

	@Column(name ="SLOC",nullable=true,length=32)
	public java.lang.String getSloc(){
		return this.sloc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  小储位使用
	 */
	public void setSloc(java.lang.String sloc){
		this.sloc = sloc;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  总使用率
	 */

	@Column(name ="TOTALRATE",nullable=true,length=32)
	public java.lang.String getTotalrate(){
		return this.totalrate;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  总使用率
	 */
	public void setTotalrate(java.lang.String totalrate){
		this.totalrate = totalrate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  lpn数
	 */

	@Column(name ="LPN",nullable=true,length=32)
	public java.lang.String getLpn(){
		return this.lpn;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  lpn数
	 */
	public void setLpn(java.lang.String lpn){
		this.lpn = lpn;
	}
	
}
