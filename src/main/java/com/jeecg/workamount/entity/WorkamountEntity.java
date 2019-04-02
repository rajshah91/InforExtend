package com.jeecg.workamount.entity;

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
 * @Description: 员工操作量
 * @author onlineGenerator
 * @date 2019-04-02 16:40:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "workamount", schema = "")
@SuppressWarnings("serial")
public class WorkamountEntity implements java.io.Serializable {
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
	/**出货票数*/
	@Excel(name="出货票数",width=15)
	private java.lang.String sonamesum;
	/**出货料号数*/
	@Excel(name="出货料号数",width=15)
	private java.lang.String soskusum;
	/**出货大储位数*/
	@Excel(name="出货大储位数",width=15)
	private java.lang.String soblocsum;
	/**出货小储位数*/
	@Excel(name="出货小储位数",width=15)
	private java.lang.String soslocsum;
	/**出货lpn数*/
	@Excel(name="出货lpn数",width=15)
	private java.lang.String solpnsum;
	/**拣货票数*/
	@Excel(name="拣货票数",width=15)
	private java.lang.String picknamesum;
	/**拣货料号数*/
	@Excel(name="拣货料号数",width=15)
	private java.lang.String pickskusum;
	/**拣货大储位数*/
	@Excel(name="拣货大储位数",width=15)
	private java.lang.String pickblocsum;
	/**拣货小储位数*/
	@Excel(name="拣货小储位数",width=15)
	private java.lang.String pickslocsum;
	/**拣货lpn数*/
	@Excel(name="拣货lpn数",width=15)
	private java.lang.String picklpnsum;
	/**复检票数*/
	@Excel(name="复检票数",width=15)
	private java.lang.String rcnamesum;
	/**复检料号数*/
	@Excel(name="复检料号数",width=15)
	private java.lang.String rcskusum;
	/**复检大储位数*/
	@Excel(name="复检大储位数",width=15)
	private java.lang.String rcblocsum;
	/**复检小储位数*/
	@Excel(name="复检小储位数",width=15)
	private java.lang.String rcslocsum;
	/**复检lpn数*/
	@Excel(name="复检lpn数",width=15)
	private java.lang.String rclpnsum;
	/**员工姓名*/
	@Excel(name="员工姓名",width=15)
	private java.lang.String username;
	
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
	 *@return: java.lang.String  出货票数
	 */

	@Column(name ="SONAMESUM",nullable=true,length=32)
	public java.lang.String getSonamesum(){
		return this.sonamesum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出货票数
	 */
	public void setSonamesum(java.lang.String sonamesum){
		this.sonamesum = sonamesum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出货料号数
	 */

	@Column(name ="SOSKUSUM",nullable=true,length=32)
	public java.lang.String getSoskusum(){
		return this.soskusum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出货料号数
	 */
	public void setSoskusum(java.lang.String soskusum){
		this.soskusum = soskusum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出货大储位数
	 */

	@Column(name ="SOBLOCSUM",nullable=true,length=32)
	public java.lang.String getSoblocsum(){
		return this.soblocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出货大储位数
	 */
	public void setSoblocsum(java.lang.String soblocsum){
		this.soblocsum = soblocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出货小储位数
	 */

	@Column(name ="SOSLOCSUM",nullable=true,length=32)
	public java.lang.String getSoslocsum(){
		return this.soslocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出货小储位数
	 */
	public void setSoslocsum(java.lang.String soslocsum){
		this.soslocsum = soslocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出货lpn数
	 */

	@Column(name ="SOLPNSUM",nullable=true,length=32)
	public java.lang.String getSolpnsum(){
		return this.solpnsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出货lpn数
	 */
	public void setSolpnsum(java.lang.String solpnsum){
		this.solpnsum = solpnsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拣货票数
	 */

	@Column(name ="PICKNAMESUM",nullable=true,length=32)
	public java.lang.String getPicknamesum(){
		return this.picknamesum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拣货票数
	 */
	public void setPicknamesum(java.lang.String picknamesum){
		this.picknamesum = picknamesum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拣货料号数
	 */

	@Column(name ="PICKSKUSUM",nullable=true,length=32)
	public java.lang.String getPickskusum(){
		return this.pickskusum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拣货料号数
	 */
	public void setPickskusum(java.lang.String pickskusum){
		this.pickskusum = pickskusum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拣货大储位数
	 */

	@Column(name ="PICKBLOCSUM",nullable=true,length=32)
	public java.lang.String getPickblocsum(){
		return this.pickblocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拣货大储位数
	 */
	public void setPickblocsum(java.lang.String pickblocsum){
		this.pickblocsum = pickblocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拣货小储位数
	 */

	@Column(name ="PICKSLOCSUM",nullable=true,length=32)
	public java.lang.String getPickslocsum(){
		return this.pickslocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拣货小储位数
	 */
	public void setPickslocsum(java.lang.String pickslocsum){
		this.pickslocsum = pickslocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拣货lpn数
	 */

	@Column(name ="PICKLPNSUM",nullable=true,length=32)
	public java.lang.String getPicklpnsum(){
		return this.picklpnsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拣货lpn数
	 */
	public void setPicklpnsum(java.lang.String picklpnsum){
		this.picklpnsum = picklpnsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复检票数
	 */

	@Column(name ="RCNAMESUM",nullable=true,length=32)
	public java.lang.String getRcnamesum(){
		return this.rcnamesum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复检票数
	 */
	public void setRcnamesum(java.lang.String rcnamesum){
		this.rcnamesum = rcnamesum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复检料号数
	 */

	@Column(name ="RCSKUSUM",nullable=true,length=32)
	public java.lang.String getRcskusum(){
		return this.rcskusum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复检料号数
	 */
	public void setRcskusum(java.lang.String rcskusum){
		this.rcskusum = rcskusum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复检大储位数
	 */

	@Column(name ="RCBLOCSUM",nullable=true,length=32)
	public java.lang.String getRcblocsum(){
		return this.rcblocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复检大储位数
	 */
	public void setRcblocsum(java.lang.String rcblocsum){
		this.rcblocsum = rcblocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复检小储位数
	 */

	@Column(name ="RCSLOCSUM",nullable=true,length=32)
	public java.lang.String getRcslocsum(){
		return this.rcslocsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复检小储位数
	 */
	public void setRcslocsum(java.lang.String rcslocsum){
		this.rcslocsum = rcslocsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复检lpn数
	 */

	@Column(name ="RCLPNSUM",nullable=true,length=32)
	public java.lang.String getRclpnsum(){
		return this.rclpnsum;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复检lpn数
	 */
	public void setRclpnsum(java.lang.String rclpnsum){
		this.rclpnsum = rclpnsum;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  员工姓名
	 */

	@Column(name ="USERNAME",nullable=true,length=32)
	public java.lang.String getUsername(){
		return this.username;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工姓名
	 */
	public void setUsername(java.lang.String username){
		this.username = username;
	}
	
}
