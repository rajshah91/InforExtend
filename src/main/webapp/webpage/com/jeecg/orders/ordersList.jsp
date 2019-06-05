<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>出货刷单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name=viewportcontent="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
	<link rel="stylesheet" href="${webRoot}/plug-in/element-ui/css/index.css">
	<link rel="stylesheet" href="${webRoot}/plug-in/element-ui/css/elementui-ext.css">
	<script src="${webRoot}/plug-in/vue/vue.js"></script>
	<script src="${webRoot}/plug-in/vue/vue-resource.js"></script>
	<script src="${webRoot}/plug-in/element-ui/index.js"></script>
	<!-- Jquery组件引用 -->
	<script src="${webRoot}/plug-in/jquery/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${webRoot}/plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
	<script type="text/javascript" src="${webRoot}/plug-in/mutiLang/zh-cn.js"></script>
	<script type="text/javascript" src="${webRoot}/plug-in/lhgDialog/lhgdialog.min.js?skin=metrole"></script>
	<script type="text/javascript" src="${webRoot}/plug-in/tools/curdtools.js"></script>
	<style>
	.toolbar {
	    padding: 10px;
	    margin: 10px 0;
	}
	.toolbar .el-form-item {
	    margin-bottom: 10px;
	}
	.el-table__header tr th{
		padding:3px 0px;
	}
	[v-cloak] { display: none }
	</style>
</head>
<body style="background-color: #FFFFFF;">
	<div id="ordersList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
			    <el-form-item style="margin-bottom: 8px;" prop="orderkey">
			    <el-select v-model="filters.warehouse" v-model="warehouses" placeholder="请选择仓库" clearable style="width:175px">
	                 <!-- <el-option label="WH1飞仓" value="FEILI_wmwhse1"></el-option>
	                 <el-option label="WH2飞仓品牌" value="FEILI_wmwhse2"></el-option>
	                 <el-option label="WH5飞仓VMI" value="FEILI_wmwhse5"></el-option>
	                 <el-option label="WH10飞仓昆山外租仓" value="FEILI_wmwhse10"></el-option> -->
	                 <el-option v-for="warehouse in warehouses"  :value="warehouse"></el-option>
	             </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="orderkey">
					<el-input v-model="filters.orderkey" auto-complete="off" placeholder="请输入出货单号"></el-input>
				</el-form-item>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="search">查询</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button icon="el-icon-refresh" @click="resetForm('filters')">重置</el-button>
			    </el-form-item>
			</el-form>
		</el-row>
		<el-row style="padding: 10px;" size="mini">
			  <el-button type="primary" size="mini" icon="el-icon-tickets" @click="shuadan">刷单</el-button>
			  <el-dropdown trigger="click" style="float:right" label-width="80px">
			          <el-button prop="primary"  size="mini" icon="el-icon-menu"></el-button>
					  <!-- <span class="el-dropdown-link">点我查看<i class="el-icon-caret-bottom el-icon--right"></i></span> -->
					  <el-dropdown-menu slot="dropdown">
					  <template>
						  <el-checkbox-group v-model="checkList" label-width="100px">
						    <el-checkbox label="出货单号" v-model="detailList.orderkey" checked @change="show(detailList.orderkey)"></el-checkbox>
						    <el-checkbox label="货主代码 " v-model="detailList.storerkey" checked @change="show(detailList.storerkey,name)"></el-checkbox><br>
						    <el-checkbox label="收货人代码" v-model="detailList.vendor" checked @change="show(detailList.vendor)" value="3"></el-checkbox>
						    <el-checkbox label="订单时间"  v-model="detailList.orderdate" checked @change="show(detailList.orderdate)"  value="4"></el-checkbox><br>
						    <el-checkbox label="请求出货时间" v-model="detailList.requestshipdate" checked @change="show(detailList.requestshipdate)" value="5"></el-checkbox>
						  </el-checkbox-group>
					  </template>
					  <!-- <el-dropdown-item class="clearfix"><el-badge class="mark" :value="12" /></el-dropdown-item>
					  <el-dropdown-item class="clearfix">回复<el-badge class="mark" :value="3" /></el-dropdown-item> -->
					  </el-dropdown-menu>
              </el-dropdown>
			  <!-- <el-button type="primary" icon="el-icon-edit" @click="handleAdd">新增</el-button>
			  <el-button type="primary" icon="el-icon-edit" @click="ExportXls">导出</el-button>
			  <el-button type="primary" icon="el-icon-edit" @click="ImportXls">导入</el-button> -->
		</el-row>
		<!--列表-->
		<el-table :data="orderss"  border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="index" label="序号" width="60"></el-table-column>
			<el-table-column prop="warehouse" label="仓库" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="orderkey" label="出货单号" min-width="120" sortable="custom" show-overflow-tooltip v-if="columnshow.orderkey"></el-table-column>
			<el-table-column prop="storerkey" label="货主代码" min-width="120" sortable="custom" show-overflow-tooltip v-if="columnshow.storerkey"></el-table-column>
			<el-table-column prop="vendor" label="收货人代码" min-width="120" sortable="custom" show-overflow-tooltip v-if="columnshow.vendor"></el-table-column>
			<el-table-column prop="orderdate" label="订单时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip 	 v-if="columnshow.orderdate"></el-table-column>
			<el-table-column prop="requestshipdate" label="请求出货时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip  v-if="columnshow.requestshipdate"></el-table-column>
			<el-table-column prop="picker" label="拣货员" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="pickstartdate" label="拣货开始时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip ></el-table-column>
			<el-table-column prop="pickenddate" label="拣货完成时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip ></el-table-column>
			<el-table-column prop="labeler" label="贴标员" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="labelstartdate" label="贴标开始时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip ></el-table-column>
			<el-table-column prop="labelenddate" label="贴标完成时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip ></el-table-column>
			<el-table-column prop="reagents" label="复检员" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="reagentstartdate" label="复检开始时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip ></el-table-column>
			<el-table-column prop="reagentenddate" label="复检完成时间" min-width="140" sortable="custom" :formatter="formatDateTime" show-overflow-tooltip ></el-table-column>
			<el-table-column prop="orderstatus" label="状态" min-width="120" sortable="custom" show-overflow-tooltip ></el-table-column>
			<!-- <el-table-column label="操作" width="150">
				<template scope="scope">
					<el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="mini" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column> -->
		</el-table>
		
		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<!--<el-button type="danger" size="mini" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>-->
			 <el-pagination small background @current-change="handleCurrentChange" @size-change="handleSizeChange" :page-sizes="[10, 20, 50, 100]"
      			:page-size="pageSize" :total="total" layout="sizes, prev, pager, next"  style="float:right;"></el-pagination>
		</el-col>
		
		
		<!--新增界面-->
		<el-dialog :title="formTitle" fullscreen z-index="800" :visible.sync="formVisible" :close-on-click-modal="false" type="expand">
			<el-form :model="form" label-width="80px" :rules="formRules" ref="form" size="mini" inline="true">
					<el-form-item label="出货单号" prop="orderkey">
						<el-input v-model="form.orderkey" auto-complete="off" placeholder="请输入出货单号"></el-input>
					</el-form-item>
					<el-form-item label="货主代码" prop="storerkey">
						<el-input v-model="form.storerkey" auto-complete="off" placeholder="请输入货主代码"></el-input>
					</el-form-item>
					<el-form-item label="收货人代码" prop="vendor">
						<el-input v-model="form.vendor" auto-complete="off" placeholder="请输入收货人代码"></el-input>
					</el-form-item>
					<el-form-item label="订单时间">
						 <el-date-picker type="datetime" placeholder="选择订单时间" v-model="form.orderdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="请求出货时间">
						 <el-date-picker type="datetime" placeholder="选择请求出货时间" v-model="form.requestshipdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="拣货员" prop="picker">
						<el-input v-model="form.picker" auto-complete="off" placeholder="请输入拣货员"></el-input>
					</el-form-item>
					<el-form-item label="拣货开始时间">
						 <el-date-picker type="datetime" placeholder="选择拣货开始时间" v-model="form.pickstartdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="拣货完成时间">
						 <el-date-picker type="datetime" placeholder="选择拣货完成时间" v-model="form.pickenddate"></el-date-picker>
					</el-form-item>
					<el-form-item label="贴标员" prop="labeler">
						<el-input v-model="form.labeler" auto-complete="off" placeholder="请输入贴标员"></el-input>
					</el-form-item>
					<el-form-item label="贴标开始时间">
						 <el-date-picker type="datetime" placeholder="选择贴标开始时间" v-model="form.labelstartdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="贴标完成时间">
						 <el-date-picker type="datetime" placeholder="选择贴标完成时间" v-model="form.labelenddate"></el-date-picker>
					</el-form-item>
					<el-form-item label="复检员" prop="reagents">
						<el-input v-model="form.reagents" auto-complete="off" placeholder="请输入复检员"></el-input>
					</el-form-item>
					<el-form-item label="复检开始时间">
						 <el-date-picker type="datetime" placeholder="选择复检开始时间" v-model="form.reagentstartdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="复检完成时间">
						 <el-date-picker type="datetime" placeholder="选择复检完成时间" v-model="form.reagentenddate"></el-date-picker>
					</el-form-item>
					<el-form-item label="状态">
						<el-select v-model="form.orderstatus" placeholder="请选择状态">
					      <el-option :label="option.typename" :value="option.typecode" v-for="option in ostatusOptions"></el-option>
					    </el-select>
					</el-form-item>
					<el-form-item label="仓库" prop="warehouse">
						<el-input v-model="form.warehouse" auto-complete="off" placeholder="请输入仓库"></el-input>
					</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="formVisible = false">取消</el-button>
				<el-button type="primary" @click.native="formSubmit" :loading="formLoading">提交</el-button>
			</div>
		</el-dialog>
		<el-dialog :visible.sync="PickFormVisible" fullscreen>
             <el-form :model="pickform" :label-width="formLabelWidth"  size="mini" inline="true" style="background-color: #eee; padding: 10px 10px 0 10px;">
	             <el-form-item label="功能">
	                <template>
					    <el-radio-group v-model="radio2">
					    <el-radio :label="0">请选择</el-radio>
					    <el-radio :label="1" @change="undisabled()">开始</el-radio>
					    <el-radio :label="2" @change="disabled()">结束</el-radio>
	                    </el-radio-group>
                     </template>
                     <!--  <el-button type="primary" icon="el-icon-star-on" @click="starton()">开始</el-button>
                     <el-button type="primary" icon="el-icon-star-off" @click="endoff()">结束</el-button> -->
                 </el-form-item><br>
	             <el-form-item label="操作">
		             <template>
					    <el-radio-group v-model="radio1">
					    <el-radio :label="0">请选择</el-radio>
					    <el-radio :label="1">拣货</el-radio>
					    <el-radio :label="2">贴标</el-radio>
					    <el-radio :label="3">复检</el-radio>
	                    </el-radio-group>
                     </template>
                 </el-form-item>
                 <br>
	             <el-form-item label="仓库"  inline="ture">
	             <el-select v-model="pickform.warehouse" v-model="warehouses" placeholder="请选择仓库" clearable style="width:175px">
	                 <el-option v-for="warehouse in warehouses"  :value="warehouse"></el-option>
	             </el-select>
	             </el-form-item>
	             <el-form-item label="账号">
                    <el-input v-model="pickform.operate" :disabled='operatordisabled' autocomplete="off" @change="getname(pickform.operate)"></el-input>
                 </el-form-item>
	             <el-form-item label="姓名">
                    <el-input v-model="pickform.operator" disabled autocomplete="off"></el-input>
                 </el-form-item>
                  <el-form-item label="出货单号">
                    <el-input v-model="pickform.orderkey" maxlength="10" minlength="10" autocomplete="off" v-on:input="valiorderkey(pickform.orderkey)"></el-input>
                 </el-form-item>
                 <br>
                 <!-- <el-form-item label="功能">
                 <template>
				    <el-radio-group v-model="radio2">
				    <el-radio :label="0">请选择</el-radio>
				    <el-radio :label="1">刷单</el-radio>
                    </el-radio-group>
                 </template>
                 </el-form-item> -->
                 
                 <el-form-item label="单号展示">
                 <el-input 
					  type="textarea"
					  autosize
					  readonly
					  placeholder="请输入内容"
					  v-model="pickform.orderkeys">
				 </el-input>
				 </el-form-item>
				 <el-form-item>
				 <div>
                   <el-button type="primary" @click="remove()">移除</el-button><br>
                   <el-button type="primary" style="margin-top:5px" @click="starton()">确定</el-button>
                 </div>
                 </el-form-item>
            </el-form>
            
            <!--  <el-table :data="pickorderss" border stripe size="mini" highlight-current-row v-loading="listLoading"   @selection-change="selsChange" style="width: 100%;margin-top: 3px;">
			<el-table-column prop="orderkey" label="出货单号" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="storerkey" label="货主代码" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="vendor" label="收货人代码" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="orderdate" label="订单时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="requestshipdate" label="请求出货时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="picker" label="拣货员" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="pickstartdate" label="拣货开始时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="pickenddate" label="拣货完成时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="orderstatus" label="状态" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatOstatusDict"></el-table-column>
			<el-table-column prop="warehouse" label="仓库" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
		    </el-table> -->
            
            
        </el-dialog>
	</div>
</body>
<script>
	
	var vue = new Vue({			
		el:"#ordersList",
		data:function() {
			return {
				filters: {
					warehouse:'',
					orderkey:'',
				},
				url:{
					list:'${webRoot}/ordersController.do?datagrid',
					del:'${webRoot}/ordersController.do?doDel',
					batchDel:'${webRoot}/ordersController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/ordersController.do?doAdd',
					edit:'${webRoot}/ordersController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/ordersController.do?exportXls&id=',
					ImportXls:'${webRoot}/ordersController.do?upload',
					getName:'${webRoot}/ordersController.do?getName',
				    valiorderkey:'${webRoot}/ordersController.do?valiorderkey',
				    starton:'${webRoot}/ordersController.do?starton',
				    getfrominfor:'${webRoot}/ordersController.do?getfrominfor',
				    getwarehouse:'${webRoot}/ordersController.do?getwarehouse'
				},
				orderss: [],
				total: 0,
				page: 1,
				pageSize:10,
				sort:{
					sort:'id',
					order:'desc'
				},
				listLoading: false,
				sels: [],//列表选中列
				
				formTitle:'新增',
				formVisible: false,//表单界面是否显示
				formLoading: false,
				formRules: {
				},
				//表单界面数据
				form: {},
				//拣货界面
				//账号
				operatordisabled:false,
				pickform:{
					warehouse:'',
					operate:'',
					operator:'',
					orderkey:'',
					orderkeys:'',
					lastorderkey:''
				},
				warehouses:[],
				//显示列
				checkList:[],
				detailList:{
					orderkey:'orderkey',
					storerkey:'storerkey',
					vendor:'vendor',
					orderdate:'orderdate',
					requestshipdate:'requestshipdate',
				},
				
				columnshow:{
					orderkey:true,
					storerkey:true,
					vendor:true,
					orderdate:true,
					requestshipdate:true,
				},
				//show:false,
				//order
				order:{
					warehouse:'',
					orderkey:'',
					storerkey:'',
					vendor:'',
					orderdate:'',
					requestshipdate:'',
					picker:'',
					pickstartdate:'',
					pickenddate:'',
					labeler:'',
					labelstartdate:'',
					labelenddate:'',
					reagents:'',
					reagentstartdate:'',
					reagentenddate:'',
					orderstatus:'',
				},
				pickorderss:[],
				PickFormVisible:false,
				formLabelWidth:'70px',
				radio1:0,
				radio2:0,
			    orderkeysshow:'',
				//数据字典 
		   		ostatusOptions:[],
			}
		},
		methods: {
			search:function(){
				 this.page = 1;
			     this.getOrderss();
			},
			undisabled:function(){
				//禁用
				this.operatordisabled=false;
			},
			disabled:function(){
				//禁用
				this.operatordisabled=true;
			},
			//移除textarea中 的值
			remove:function(){
				this.pickform.orderkeys=this.pickform.orderkeys.substring(0,this.pickform.orderkeys.length-11);
			},
			//刷单执行
			starton:function(){
				//ajax
				if(this.radio1!=0&&this.radio2!=0&&this.pickform.warehouse!=null&&this.pickform.warehouse!=""){
					if(this.radio2=="1"&&this.pickform.operate==""){
						this.$message({
							 showClose:true,
					          type: 'error',
					          message: "操作人未填",
					          position: 'bottom-right',
					          duration:0
					    });
						return;
					}
					this.PickFormVisible=false;
					var string;
					if(this.pickform.orderkeys!=null&&this.pickform.orderkeys!=""){
						string=this.pickform.orderkeys.replace(new RegExp("\n","gm"),";");
					}
					this.$http.get(this.url.starton,{params:{operation:this.radio1,warehouse:this.pickform.warehouse,startorend:this.radio2,username:this.pickform.operator,orderkeys:string}}).then(function(res)  {
						//返回
						if(res.body.result!=null&&res.body.result!=""){
							this.$message({
								 showClose:true,
						          type: 'error',
						          message: res.body.result,
						          position: 'bottom-right',
						          duration:0
						    });
						}
					});
				}else{
					this.$message({
						showClose:true,
				          type: 'error',
				          message: '数据尚未输入完成',
				          position: 'bottom-right',
				          duration:0
				    });
				}
			},
			//列展示切换
			show:function(value){
				if(value=="orderkey"){
				   this.columnshow.orderkey=!this.columnshow.orderkey;
				}
				if(value=="storerkey"){
					   this.columnshow.storerkey=!this.columnshow.storerkey;
				}
				if(value=="vendor"){
					   this.columnshow.vendor=!this.columnshow.vendor;
				}
				if(value=="orderdate"){
					   this.columnshow.orderdate=!this.columnshow.orderdate;
				}
				if(value=="requestshipdate"){
					   this.columnshow.requestshipdate=!this.columnshow.requestshipdate;
				}
			},
			//
			getwarehouse:function(){
				this.$http.get(this.url.getwarehouse).then(function(res)  {
					/* console.log(res.data.warehouse); */
					this.warehouses=res.data.warehouse;
				});
			},
			//清空
			shuadan:function(){
				//ajax查询当前用户仓库
				this.$http.get(this.url.getwarehouse).then(function(res)  {
					/* console.log(res.data.warehouse); */
					this.warehouses=res.data.warehouse;
				});
				//切回默认
				this.radio1=0;
				this.radio2=0;
				//清空
				this.pickform.warehouse="";
				this.pickform.operate="";
				this.pickform.operator="";
				this.pickform.orderkey="";
				this.pickform.orderkeys="";
				this.PickFormVisible=true;
				this.operatordisabled=false;
			},
			valiorderkey:function(value){
				console.log('1');
				//ajax
				if(value!=null&&this.pickform.warehouse!=null&&value!=""&&this.pickform.warehouse!=""&&value.length==10){
					this.$http.get(this.url.valiorderkey,{params:{orderkey:value,warehouse:this.pickform.warehouse}}).then(function(res)  {
						if(res.body.success){
							this.pickform.orderkeys=this.pickform.orderkeys+res.data.orderkeys;
							this.pickform.lastorderkey=this.pickform.orderkey;
							/* this.$notify({
						          type: 'success',
						          message: '验证成功',
						          position: 'bottom-right',
						          duration:1500
						    }); */
							this.pickform.orderkey="";
						}else{
							this.$message({
								showClose:true,
								  type: 'error',
						          message: '验证失败',
						          position: 'bottom-right',
						          duration:0
						    });
							this.pickform.orderkey="";
						}
					});
				}else if(value==null||this.pickform.warehouse==null||value==""&&this.pickform.warehouse==""){
					this.$message({
						showClose:true,
						  type: 'error',
				          message: '未填写仓库或出货单号错误',
				          position: 'bottom-right',
				          duration:0
				    });
					this.pickform.orderkey="";
				}
				
			},
			getname:function(value){
				//ajax
				if(value!=null&&value!=""){
					this.$http.get(this.url.getName,{params:{account:value}}).then(function(res)  {
						this.pickform.operator=res.data.name;
					});
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getOrderss();
			},
			handleDownFile:function(type,filePath){
				var downUrl=this.url.downFile+ filePath +"?down=true";
				window.open(downUrl);
			},
			formatDate: function(row,column,cellValue, index){
				return !!cellValue?utilFormatDate(new Date(cellValue), 'yyyy-MM-dd'):'';
			},
			formatDateTime: function(row,column,cellValue, index){
				return !!cellValue?utilFormatDate(dateToGMT(cellValue), 'yyyy-MM-dd hh:mm:ss'):'';
			},
			formatOstatusDict: function(row,column,cellValue, index){
				var names="";
				var values=cellValue;
				if(!Array.isArray(cellValue))values=cellValue.split(',');
				for (var i = 0; i < values.length; i++) {
					var value = values[i];
					if(i>0)names+=",";
					for(var j in this.ostatusOptions){
						var option=this.ostatusOptions[j];
						if(value==option.typecode){
							names+=option.typename;
						}
					}
				}
				return names;
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getOrderss();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getOrderss();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.page = 1;
		        this.getOrderss();
		    },
			//获取用户列表
			getOrderss:function() {
				var fields=[];
				fields.push('id');
				fields.push('id');
				fields.push('createName');
				fields.push('createBy');
				fields.push('createDate');
				fields.push('updateName');
				fields.push('updateBy');
				fields.push('updateDate');
				fields.push('sysOrgCode');
				fields.push('sysCompanyCode');
				fields.push('bpmStatus');
				fields.push('orderkey');
				fields.push('storerkey');
				fields.push('vendor');
				fields.push('orderdate');
				fields.push('requestshipdate');
				fields.push('picker');
				fields.push('pickstartdate');
				fields.push('pickenddate');
				fields.push('labeler');
				fields.push('labelstartdate');
				fields.push('labelenddate');
				fields.push('reagents');
				fields.push('reagentstartdate');
				fields.push('reagentenddate');
				fields.push('orderstatus');
				fields.push('warehouse');
				var para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	orderkey:this.filters.orderkey,
					 	warehouse:this.filters.warehouse,
						field:fields.join(',')
					}
				};
				this.listLoading = true;
				this.$http.get(this.url.list,para).then(function(res)  {
					this.total = res.data.total;
					var datas=res.data.rows;
					for (var i = 0; i < datas.length; i++) {
						var data = datas[i];
					}
					this.orderss = datas;
					this.listLoading = false;
				});
// 				this.$http.get(this.url.list,para).then(function(res)  {
// 					this.orderss = res.data.orders;
// 					var datas=res.data.orders;
// 					/* this.orderss[0]="";
// 					console.log(this.orderss[0]); */
// 					if(res.data.orders.length>0){
// 						for (var i = 0; i < res.data.orders.length; i++) {
// 							for(var j=0;j<16;j++){
// 								if(j==0){
// 									this.order.warehouse=datas[i][j];
// 								}
// 								if(j==1){
// 									this.order.orderkey=datas[i][j];
// 								}
// 								if(j==2){
// 									this.order.storerkey=datas[i][j];
// 								}
// 								if(j==3){
// 									this.order.vendor=datas[i][j];
// 								}
// 								if(j==4){
									
// 										this.order.orderdate=datas[i][j];
									
// 								}
// 								if(j==5){
									
// 										this.order.requestshipdate=datas[i][j];
									
// 								}
// 								if(j==6){
// 									this.order.picker=datas[i][j];
// 								}
// 								if(j==7){
									
// 										this.order.pickstartdate=datas[i][j];
									
// 								}
// 								if(j==8){
									
// 										this.order.pickenddate=datas[i][j];
									
// 								}
// 								if(j==9){
// 									this.order.labeler=datas[i][j];
// 								}
// 								if(j==10){
									
// 										this.order.labelstartdate=datas[i][j];
									
// 								}
// 								if(j==11){
									
// 										this.order.labelenddate=datas[i][j];
									
// 								}
// 								if(j==12){
// 									this.order.reagents=datas[i][j];
// 								}
// 								if(j==13){
									
// 										this.order.reagentstartdate=datas[i][j];
									
// 								}
// 								if(j==14){
									
// 										this.order.reagentenddate=datas[i][j];
									
// 								}
// 								if(j==15){
// 									this.order.orderstatus=datas[i][j];
// 								}
// 							}
// 						}
// 						this.orderss[0] = this.order;
// 					}else{
// 						this.orderss[0] ="";
// 					}
// 					this.listLoading = false;
// 				});
			},
			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(function()  {
					this.listLoading = true;
					var para = { id: row.id };
					this.$http.post(this.url.del,para,{emulateJSON: true}).then(function(res)  {
						this.listLoading = false;
						this.$message({
							message: '删除成功',
							type: 'success',
							duration:1500
						});
						this.getOrderss();
					});
				}).catch(function()  {

				});
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.formTitle='编辑';
				this.formVisible = true;
				this.form = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {
				this.formTitle='新增';
				this.formVisible = true;
				this.form = {
					orderkey:'',
					storerkey:'',
					vendor:'',
					orderdate:'',
					requestshipdate:'',
					picker:'',
					pickstartdate:'',
					pickenddate:'',
					labeler:'',
					labelstartdate:'',
					labelenddate:'',
					reagents:'',
					reagentstartdate:'',
					reagentenddate:'',
					orderstatus:'',
					warehouse:'',
				};
			},
			//新增
			formSubmit: function () {
				this.$refs.form.validate(function(valid)  {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(function()  {
							this.formLoading = true;
							var para = Object.assign({}, this.form);
							
							para.orderdate = !para.orderdate ? '' : utilFormatDate(new Date(para.orderdate), 'yyyy-MM-dd hh:mm:ss');
							para.requestshipdate = !para.requestshipdate ? '' : utilFormatDate(new Date(para.requestshipdate), 'yyyy-MM-dd hh:mm:ss');
							para.pickstartdate = !para.pickstartdate ? '' : utilFormatDate(new Date(para.pickstartdate), 'yyyy-MM-dd hh:mm:ss');
							para.pickenddate = !para.pickenddate ? '' : utilFormatDate(new Date(para.pickenddate), 'yyyy-MM-dd hh:mm:ss');
							para.labelstartdate = !para.labelstartdate ? '' : utilFormatDate(new Date(para.labelstartdate), 'yyyy-MM-dd hh:mm:ss');
							para.labelenddate = !para.labelenddate ? '' : utilFormatDate(new Date(para.labelenddate), 'yyyy-MM-dd hh:mm:ss');
							para.reagentstartdate = !para.reagentstartdate ? '' : utilFormatDate(new Date(para.reagentstartdate), 'yyyy-MM-dd hh:mm:ss');
							para.reagentenddate = !para.reagentenddate ? '' : utilFormatDate(new Date(para.reagentenddate), 'yyyy-MM-dd hh:mm:ss');
							
							
							this.$http.post(!!para.id?this.url.edit:this.url.save,para,{emulateJSON: true}).then(function(res)  {
								this.formLoading = false;
								this.$message({
									message: '提交成功',
									type: 'success',
									duration:1500
								});
								this.$refs['form'].resetFields();
								this.formVisible = false;
								this.getOrderss();
							});
						});
					}
				});
			},
			selsChange: function (sels) {
				this.sels = sels;
			},
			//批量删除
			batchRemove: function () {
				var ids = '';
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(function()  {
					this.listLoading = true;
					var para = { ids: ids };
					this.$http.post(this.url.batchDel,para,{emulateJSON: true}).then(function(res)  {
						this.listLoading = false;
						this.$message({
							message: '删除成功',
							type: 'success',
							duration:1500
						});
						this.getOrderss();
					});
				}).catch(function()  {
				});
			},
			//导出
			ExportXls: function() {
					var ids = '';
					window.location.href = this.url.exportXls+ids;
			},
			//导入
			ImportXls: function(){
				openuploadwin('Excel导入',this.url.ImportXls, "ordersList");
			},
			//初始化数据字典
			initDictsData:function(){
	        	var _this = this;
		   		_this.initDictByCode('ostatus',_this,'ostatusOptions');
	        },
	        initDictByCode:function(code,_this,dictOptionsName){
	        	if(!code || !_this[dictOptionsName] || _this[dictOptionsName].length>0)
	        		return;
	        	this.$http.get(this.url.queryDict,{params: {typeGroupName:code}}).then(function(res)  {
	        		var data=res.data;
					if(data.success){
					  _this[dictOptionsName] = data.obj;
					  _this[dictOptionsName].splice(0, 1);//去掉请选择
					}
				});
	        }
		},
		mounted:function() {
			this.initDictsData();
			this.getOrderss();
			this.getwarehouse();
		}
	});
	
	function utilFormatDate(date, pattern) {

        pattern = pattern || "yyyy-MM-dd"
        return pattern.replace(/([yMdhsm])(\1*)/g, function ($0) {
            switch ($0.charAt(0)) {
                case 'y': return padding(date.getFullYear(), $0.length);
                case 'M': return padding(date.getMonth() + 1, $0.length);
                case 'd': return padding(date.getDate(), $0.length);
                case 'w': return date.getDay() + 1;
                case 'h': return padding(date.getHours(), $0.length);
                case 'm': return padding(date.getMinutes(), $0.length);
                case 's': return padding(date.getSeconds(), $0.length);
            }
        });
    };
	function padding(s, len) {
	    var len = len - (s + '').length;
	    for (var i = 0; i < len; i++) { s = '0' + s; }
	    return s;
	};
	function reloadTable(){
		
	}
	function dateToGMT(strDate){
	    var dateStr=strDate.split(" ");  
	    var strGMT = dateStr[0]+" "+dateStr[1]+" "+dateStr[2]+" "+dateStr[5]+" "+dateStr[3]+" GMT+0800";  
	    var date = new Date(Date.parse(strGMT));
	    return date;
	}

</script>
</html>