<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>订单预估</title>
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
	<div id="orderforecastList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
			 <el-form-item style="margin-bottom: 8px;" prop="warehouse">
					<el-select v-model="filters.region" placeholder="请选择分区" clearable style="width:175px">
	                 <el-option label="昆山区" value="昆山区"></el-option>
	             </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="warehouse">
					<el-select v-model="filters.operatdepart" placeholder="请选择操作部" clearable style="width:175px">
	                 <el-option label="操作一部" value="depart1"></el-option>
	                 <el-option label="操作二部" value="depart2"></el-option>
	                 <el-option label="操作三部" value="depart3"></el-option>
	                 <el-option label="客服部" value="operation4"></el-option>
	            </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="warehouse">
					<el-select v-model="filters.operatsection" placeholder="请选择操作科" clearable style="width:175px">
	                 <el-option label="操作一部一科" value="section1"></el-option>
	                 <el-option label="操作一部二科" value="section2"></el-option>
	                 <el-option label="操作一部三科" value="section3"></el-option>
	                 <el-option label="操作一部五科" value="section4"></el-option>
	                 <el-option label="操作一部六科" value="section5"></el-option>
	                 <el-option label="操作二部" value="section6"></el-option>
	                 <el-option label="操作三部" value="section7"></el-option>
	                 <el-option label="客服部一科" value="section8"></el-option>
	                 <el-option label="客服部二科" value="section9"></el-option>
	                 <el-option label="客服部三科" value="section10"></el-option>
	                 <el-option label="客服部五科" value="section11"></el-option>
	            </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="area">
					<el-input v-model="filters.area" placeholder="请输入库区"  style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="warehouse">
				    <el-select v-model="filters.warehouse" v-model="warehouses" placeholder="请选择仓库" clearable style="width:175px">
		                 <!-- <el-option label="WH1飞仓" value="FEILI_wmwhse1"></el-option>
	                     <el-option label="WH2飞仓品牌" value="FEILI_wmwhse2"></el-option>
	                     <el-option label="WH5飞仓VMI" value="FEILI_wmwhse5"></el-option>
	                     <el-option label="WH10飞仓昆山外租仓" value="FEILI_wmwhse10"></el-option> -->
	                     <el-option v-for="warehouse in warehouses"  :value="warehouse"></el-option>
		            </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="requestshipdate">
					<el-date-picker type="date" placeholder="选择请求出货日期起" v-model="filters.requestshipdatestart" style="width:175px"></el-date-picker>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="requestshipdate">
					<el-date-picker type="date" placeholder="选择请求出货日期至" v-model="filters.requestshipdateend" style="width:175px"></el-date-picker>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="orderkey">
					<el-input v-model="filters.orderkey" auto-complete="off" placeholder="请输入so单号" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="orderstatus">
				    <el-select v-model="filters.orderstatus" v-model="orderstatus" placeholder="请选择状态" clearable style="width:175px">
	                 <el-option v-for="status in orderstatus"  :value="status"></el-option>
	                </el-select>
					<!-- <el-input v-model="filters.orderstatus" auto-complete="off" placeholder="请输入订单状态" style="width:175px"></el-input> -->
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="storerkey">
					<el-input v-model="filters.storerkey" auto-complete="off" placeholder="请输入货主代码" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="altsku">
					<el-input v-model="filters.altsku" auto-complete="off" placeholder="请输入收货人代码" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="ordertype">
				    <el-select v-model="filters.ordertype" v-model="ordertype" placeholder="请选择状态" clearable style="width:175px">
	                 <el-option v-for="type in ordertype"  :value="type"></el-option>
	                </el-select>
					<!-- <el-input v-model="filters.ordertype" auto-complete="off" placeholder="请输入业务类型" style="width:175px"></el-input> -->
				</el-form-item>
				<br>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="search">查询</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button icon="el-icon-refresh" @click="resetForm('filters')">重置</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="ExportXls">导出</el-button>
			    </el-form-item>
			</el-form>
		</el-row>
		<el-row style="padding: 10px;" size="mini">
			  <el-dropdown trigger="click" style="float:right" label-width="80px">
			          <el-button prop="primary"  size="mini" icon="el-icon-setting"></el-button>
					  <el-dropdown-menu slot="dropdown">
					  <template>
						  <el-checkbox-group v-model="checkList" label-width="100px">
						    <el-checkbox label="仓库" v-model="detailList.warehouse" checked @change="show(detailList.warehouse)"></el-checkbox>
						    <el-checkbox label="so单号" v-model="detailList.orderkey" checked @change="show(detailList.orderkey)"></el-checkbox><br>
						    <el-checkbox label="请求出货日期" v-model="detailList.requestshipdate" checked @change="show(detailList.requestshipdate)"></el-checkbox>
						    <el-checkbox label="订单状态" v-model="detailList.orderstatus" checked @change="show(detailList.orderstatus)"></el-checkbox><br>
						    <el-checkbox label="库区" v-model="detailList.area" checked @change="show(detailList.area)"></el-checkbox>
						    <el-checkbox label="货主简称" v-model="detailList.storer" checked @change="show(detailList.storer)"></el-checkbox><br>
						    <el-checkbox label="收货人简称" v-model="detailList.vendor" checked @change="show(detailList.vendor)"></el-checkbox>
						    <el-checkbox label="料号数" v-model="detailList.skusum" checked @change="show(detailList.skusum)"></el-checkbox><br>
						    <el-checkbox label="大库位数" v-model="detailList.blocsum" checked @change="show(detailList.blocsum)"></el-checkbox>
						    <el-checkbox label="小库位数" v-model="detailList.slocsum" checked @change="show(detailList.slocsum)"></el-checkbox><br>
						    <el-checkbox label="LPN数" v-model="detailList.lpnsum" checked @change="show(detailList.lpnsum)"></el-checkbox>
						    <el-checkbox label="拣货员" v-model="detailList.pick" checked @change="show(detailList.pick)"></el-checkbox><br>
						    <el-checkbox label="拣货开始时间" v-model="detailList.pickstartdate" checked @change="show(detailList.pickstartdate)"></el-checkbox>
						    <el-checkbox label="标准库位时间" v-model="detailList.stdocdate" checked @change="show(detailList.stdocdate)"></el-checkbox><br>
						    <el-checkbox label="标准预计完成时间" v-model="detailList.stdcompletedate" checked @change="show(detailList.stdcompletedate)"></el-checkbox>
						    <el-checkbox label="本次预计完成时间" v-model="detailList.nowcompletedate" checked @change="show(detailList.nowcompletedate)"></el-checkbox><br>
						    <el-checkbox label="实际拣货完成时间" v-model="detailList.factpickdate" checked @change="show(detailList.factpickdate)"></el-checkbox>
<!-- 						    <el-checkbox label="货主代码" v-model="detailList.storerkey" checked @change="show(detailList.storerkey)"></el-checkbox><br>
						    <el-checkbox label="收货人代码" v-model="detailList.altsku" checked @change="show(detailList.altsku)"></el-checkbox> -->
						    <el-checkbox label="业务类型" v-model="detailList.ordertype" checked @change="show(detailList.ordertype)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="orderforecasts" show-summary :summary-method="getSummaries" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<!-- <el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" width="60"></el-table-column> -->
			<el-table-column prop="warehouse" label="仓库" v-if="columnshow.warehouse" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="orderkey" label="so单号" v-if="columnshow.orderkey" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="requestshipdate" label="请求出货日期" v-if="columnshow.requestshipdate" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"></el-table-column>
			<el-table-column prop="orderstatus" label="订单状态" v-if="columnshow.orderstatus" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="area" label="库区" v-if="columnshow.area" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="storer" label="货主简称" v-if="columnshow.storer" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="vendor" label="收货人简称" v-if="columnshow.vendor" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="skusum" label="料号数" v-if="columnshow.skusum" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="blocsum" label="大库位数" v-if="columnshow.blocsum" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="slocsum" label="小库位数" v-if="columnshow.slocsum" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="lpnsum" label="LPN数" v-if="columnshow.lpnsum" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="pick" label="拣货员" v-if="columnshow.pick" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="pickstartdate" label="拣货开始时间" v-if="columnshow.pickstartdate" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"></el-table-column>
			<el-table-column prop="stdocdate" label="标准库位时间" v-if="columnshow.stdocdate" min-width="120" sortable="custom" show-overflow-tooltip ></el-table-column>
			<el-table-column prop="stdcompletedate" label="标准预计完成时间" v-if="columnshow.stdcompletedate" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"></el-table-column>
			<el-table-column prop="nowcompletedate" label="本次预计完成时间" v-if="columnshow.nowcompletedate" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"></el-table-column>
			<el-table-column prop="factpickdate" label="实际拣货完成时间" v-if="columnshow.factpickdate" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"></el-table-column>
			<!-- <el-table-column prop="storerkey" label="货主代码" v-if="columnshow.storerkey" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="altsku" label="收货人代码" v-if="columnshow.altsku" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column> -->
			<el-table-column prop="ordertype" label="业务类型" v-if="columnshow.ordertype" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
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
		<el-dialog :title="formTitle" fullscreen z-index="800" :visible.sync="formVisible" :close-on-click-modal="false">
			<el-form :model="form" label-width="80px" :rules="formRules" ref="form" size="mini">
					<el-form-item label="仓库" prop="warehouse">
						<el-input v-model="form.warehouse" auto-complete="off" placeholder="请输入仓库"></el-input>
					</el-form-item>
					<el-form-item label="so单号" prop="orderkey">
						<el-input v-model="form.orderkey" auto-complete="off" placeholder="请输入so单号"></el-input>
					</el-form-item>
					<el-form-item label="请求出货日期">
						<el-date-picker type="date" placeholder="选择请求出货日期" v-model="form.requestshipdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="订单状态" prop="orderstatus">
						<el-input v-model="form.orderstatus" auto-complete="off" placeholder="请输入订单状态"></el-input>
					</el-form-item>
					<el-form-item label="库区" prop="area">
						<el-input v-model="form.area" auto-complete="off" placeholder="请输入库区"></el-input>
					</el-form-item>
					<el-form-item label="货主简称" prop="storer">
						<el-input v-model="form.storer" auto-complete="off" placeholder="请输入货主简称"></el-input>
					</el-form-item>
					<el-form-item label="收货人简称" prop="vendor">
						<el-input v-model="form.vendor" auto-complete="off" placeholder="请输入收货人简称"></el-input>
					</el-form-item>
					<el-form-item label="料号数" prop="skusum">
						<el-input v-model="form.skusum" auto-complete="off" placeholder="请输入料号数"></el-input>
					</el-form-item>
					<el-form-item label="大库位数" prop="blocsum">
						<el-input v-model="form.blocsum" auto-complete="off" placeholder="请输入大库位数"></el-input>
					</el-form-item>
					<el-form-item label="小库位数" prop="slocsum">
						<el-input v-model="form.slocsum" auto-complete="off" placeholder="请输入小库位数"></el-input>
					</el-form-item>
					<el-form-item label="LPN数" prop="lpnsum">
						<el-input v-model="form.lpnsum" auto-complete="off" placeholder="请输入LPN数"></el-input>
					</el-form-item>
					<el-form-item label="拣货员" prop="pick">
						<el-input v-model="form.pick" auto-complete="off" placeholder="请输入拣货员"></el-input>
					</el-form-item>
					<el-form-item label="拣货开始时间">
						<el-date-picker type="date" placeholder="选择拣货开始时间" v-model="form.pickstartdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="标准库位时间">
						<el-date-picker type="date" placeholder="选择标准库位时间" v-model="form.stdocdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="标准预计完成时间" prop="stdcompletedate">
						<el-input v-model="form.stdcompletedate" auto-complete="off" placeholder="请输入标准预计完成时间"></el-input>
					</el-form-item>
					<el-form-item label="本次预计完成时间">
						<el-date-picker type="date" placeholder="选择本次预计完成时间" v-model="form.nowcompletedate"></el-date-picker>
					</el-form-item>
					<el-form-item label="实际拣货完成时间">
						<el-date-picker type="date" placeholder="选择实际拣货完成时间" v-model="form.factpickdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="货主代码" prop="storerkey">
						<el-input v-model="form.storerkey" auto-complete="off" placeholder="请输入货主代码"></el-input>
					</el-form-item>
					<el-form-item label="收货人代码" prop="altsku">
						<el-input v-model="form.altsku" auto-complete="off" placeholder="请输入收货人代码"></el-input>
					</el-form-item>
					<el-form-item label="业务类型" prop="ordertype">
						<el-input v-model="form.ordertype" auto-complete="off" placeholder="请输入业务类型"></el-input>
					</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="formVisible = false">取消</el-button>
				<el-button type="primary" @click.native="formSubmit" :loading="formLoading">提交</el-button>
			</div>
		</el-dialog>
	</div>
</body>
<script>
	var vue = new Vue({			
		el:"#orderforecastList",
		data:function() {
			return {
				filters: {
					warehouse:'',
					orderkey:'',
					requestshipdatestart:'',
					requestshipdateend:'',
					orderstatus:'',
					storerkey:'',
					altsku:'',
					ordertype:'',
					area:'',
				},
				url:{
					list:'${webRoot}/orderforecastController.do?datagrid',
					del:'${webRoot}/orderforecastController.do?doDel',
					batchDel:'${webRoot}/orderforecastController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/orderforecastController.do?doAdd',
					edit:'${webRoot}/orderforecastController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/orderforecastController.do?exportXls&id=',
					ImportXls:'${webRoot}/orderforecastController.do?upload',
					getorderstatus:'${webRoot}/orderforecastController.do?getorderstatus',
					getordertype:'${webRoot}/orderforecastController.do?getordertype',
					getwarehouse:'${webRoot}/ordersController.do?getwarehouse'
				},
				orderforecasts: [],
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
				
				//
				orderstatus:[],
				ordertype:[],
				warehouses:[],
				//显示列
				checkList:[],
				detailList:{
					warehouse:'warehouse',
					orderkey:'orderkey',
					requestshipdate:'requestshipdate',
					orderstatus:'orderstatus',
					area:'area',
					storer:'storer',
					vendor:'vendor',
					skusum:'skusum',
					blocsum:'blocsum',
					slocsum:'slocsum',
					lpnsum:'lpnsum',
					pick:'pick',
					pickstartdate:'pickstartdate',
					stdocdate:'stdocdate',
					stdcompletedate:'stdcompletedate',
					nowcompletedate:'nowcompletedate',
					factpickdate:'factpickdate',
					storerkey:'storerkey',
					altsku:'altsku',
					ordertype:'ordertype',
				},
				columnshow:{
					warehouse:true,
					orderkey:true,
					requestshipdate:true,
					orderstatus:true,
					area:true,
					storer:true,
					vendor:true,
					skusum:true,
					blocsum:true,
					slocsum:true,
					lpnsum:true,
					pick:true,
					pickstartdate:true,
					stdocdate:true,
					stdcompletedate:true,
					nowcompletedate:true,
					factpickdate:true,
					storerkey:true,
					altsku:true,
					ordertype:true,
				},
				//数据字典 
			}
		},
		methods: {
			getwarehouse:function(){
				this.$http.get(this.url.getwarehouse).then(function(res)  {
					/* console.log(res.data.warehouse); */
					this.warehouses=res.data.warehouse;
				});
			},
			getordertype:function(){
				this.$http.get(this.url.getordertype).then(function(res)  {
					this.ordertype=res.data.ordertype;
				});
			},
			getorderstatus:function(){
				this.$http.get(this.url.getorderstatus).then(function(res)  {
					this.orderstatus=res.data.orderstatus;
				});
			},
			search:function(){
				this.page = 1;
		        this.getOrderforecasts();
			},
			getSummaries:function(param) {
				/* console.log(param); */
			    var _self=this;
		        const sums = [];
		        const count=0;
		        param.columns.forEach(function(column, index)  {
		          if (index === 0) {
		            sums[index] = '汇总';
		            return;
		          }
		          if(index==1){
		        	  var count=1;
		        	  const values = param.data.map(function(item) { return Number(item[column.property])});
			          if (!values.every(function(value) {return isNaN(value)})) {
			            sums[index] = values.reduce(function(prev, curr)  {
			              const value = Number(curr);
			              if (!isNaN(value)) {
			                return count++;
			              } else {
			                return count;
			              }
			            }, 0);
			            sums[index] += '';
			          } else {
			            sums[index] = '暂无数据';
			          }
		          }
		          if(index>=7&&index<=10){
		        	  const values = param.data.map(function(item) { return Number(item[column.property])});
			          if (!values.every(function(value) {return isNaN(value)})) {
			            sums[index] = values.reduce(function(prev, curr)  {
			              const value = Number(curr);
			              if (!isNaN(value)) {
			                return prev + curr;
			              } else {
			                return prev;
			              }
			            }, 0);
			            sums[index] += '';
			          } else {
			            sums[index] = '暂无数据';
			          }
		          }
		        });
		        sums[2]=sums[2];
		        return sums;
		    },
		    //列展示切换
			show:function(value){
				if(value=="id"){
				   this.columnshow.id=!this.columnshow.id;
				}
				if(value=="createName"){
				   this.columnshow.createName=!this.columnshow.createName;
				}
				if(value=="createBy"){
				   this.columnshow.createBy=!this.columnshow.createBy;
				}
				if(value=="createDate"){
				   this.columnshow.createDate=!this.columnshow.createDate;
				}
				if(value=="updateName"){
				   this.columnshow.updateName=!this.columnshow.updateName;
				}
				if(value=="updateBy"){
				   this.columnshow.updateBy=!this.columnshow.updateBy;
				}
				if(value=="updateDate"){
				   this.columnshow.updateDate=!this.columnshow.updateDate;
				}
				if(value=="sysOrgCode"){
				   this.columnshow.sysOrgCode=!this.columnshow.sysOrgCode;
				}
				if(value=="sysCompanyCode"){
				   this.columnshow.sysCompanyCode=!this.columnshow.sysCompanyCode;
				}
				if(value=="bpmStatus"){
				   this.columnshow.bpmStatus=!this.columnshow.bpmStatus;
				}
				if(value=="warehouse"){
				   this.columnshow.warehouse=!this.columnshow.warehouse;
				}
				if(value=="orderkey"){
				   this.columnshow.orderkey=!this.columnshow.orderkey;
				}
				if(value=="requestshipdate"){
				   this.columnshow.requestshipdate=!this.columnshow.requestshipdate;
				}
				if(value=="orderstatus"){
				   this.columnshow.orderstatus=!this.columnshow.orderstatus;
				}
				if(value=="area"){
				   this.columnshow.area=!this.columnshow.area;
				}
				if(value=="storer"){
				   this.columnshow.storer=!this.columnshow.storer;
				}
				if(value=="vendor"){
				   this.columnshow.vendor=!this.columnshow.vendor;
				}
				if(value=="skusum"){
				   this.columnshow.skusum=!this.columnshow.skusum;
				}
				if(value=="blocsum"){
				   this.columnshow.blocsum=!this.columnshow.blocsum;
				}
				if(value=="slocsum"){
				   this.columnshow.slocsum=!this.columnshow.slocsum;
				}
				if(value=="lpnsum"){
				   this.columnshow.lpnsum=!this.columnshow.lpnsum;
				}
				if(value=="pick"){
				   this.columnshow.pick=!this.columnshow.pick;
				}
				if(value=="pickstartdate"){
				   this.columnshow.pickstartdate=!this.columnshow.pickstartdate;
				}
				if(value=="stdocdate"){
				   this.columnshow.stdocdate=!this.columnshow.stdocdate;
				}
				if(value=="stdcompletedate"){
				   this.columnshow.stdcompletedate=!this.columnshow.stdcompletedate;
				}
				if(value=="nowcompletedate"){
				   this.columnshow.nowcompletedate=!this.columnshow.nowcompletedate;
				}
				if(value=="factpickdate"){
				   this.columnshow.factpickdate=!this.columnshow.factpickdate;
				}
				if(value=="storerkey"){
				   this.columnshow.storerkey=!this.columnshow.storerkey;
				}
				if(value=="altsku"){
				   this.columnshow.altsku=!this.columnshow.altsku;
				}
				if(value=="ordertype"){
				   this.columnshow.ordertype=!this.columnshow.ordertype;
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getOrderforecasts();
			},
			handleDownFile:function(type,filePath){
				var downUrl=this.url.downFile+ filePath +"?down=true";
				window.open(downUrl);
			},
			formatDate: function(row,column,cellValue, index){
				return !!cellValue?utilFormatDate(dateToGMT(cellValue), 'yyyy-MM-dd'):'';
			},
			formatDateTime: function(row,column,cellValue, index){
				return !!cellValue?utilFormatDate(dateToGMT(cellValue), 'yyyy-MM-dd hh:mm:ss'):'';
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getOrderforecasts();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getOrderforecasts();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.page = 1;
		        this.getOrderforecasts();
		    },
			//获取用户列表
			getOrderforecasts:function() {
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
				fields.push('warehouse');
				fields.push('orderkey');
				fields.push('requestshipdate');
				fields.push('orderstatus');
				fields.push('area');
				fields.push('storer');
				fields.push('vendor');
				fields.push('skusum');
				fields.push('blocsum');
				fields.push('slocsum');
				fields.push('lpnsum');
				fields.push('pick');
				fields.push('pickstartdate');
				fields.push('stdocdate');
				fields.push('stdcompletedate');
				fields.push('nowcompletedate');
				fields.push('factpickdate');
				fields.push('storerkey');
				fields.push('altsku');
				fields.push('ordertype');
				console.log(this.filters.requestshipdatestart+"||"+this.filters.requestshipdateend);
				var para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	orderkey:this.filters.orderkey,
					 	area:this.filters.area,//库区
					 	warehouse:this.filters.warehouse,//仓库
					 	orderstatus:this.filters.orderstatus,//订单状态
					 	storerkey:this.filters.storerkey,//货主代码
					 	altsku:this.filters.altsku,//收货人代码
					 	ordertype:this.filters.ordertype,//订单类型
					 	requestshipdatestart:!this.filters.requestshipdatestart ? '' : utilFormatDate(new Date(this.filters.requestshipdatestart), 'yyyy-MM-dd'),//请求出货时间起
					 	requestshipdateend:!this.filters.requestshipdateend ? '' : utilFormatDate(new Date(this.filters.requestshipdateend), 'yyyy-MM-dd'),//请求出货时间至
						requestshipdate: !this.filters.requestshipdate ? '' : utilFormatDate(new Date(this.filters.requestshipdate ), 'yyyy-MM-dd hh:mm:ss'),
					 	orderstatus:this.filters.orderstatus,
					 	storerkey:this.filters.storerkey,
					 	altsku:this.filters.altsku,
					 	ordertype:this.filters.ordertype,
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
					this.orderforecasts = datas;
					this.listLoading = false;
				});
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
						this.getOrderforecasts();
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
					warehouse:'',
					orderkey:'',
					requestshipdate:'',
					orderstatus:'',
					area:'',
					storer:'',
					vendor:'',
					skusum:'',
					blocsum:'',
					slocsum:'',
					lpnsum:'',
					pick:'',
					pickstartdate:'',
					stdocdate:'',
					stdcompletedate:'',
					nowcompletedate:'',
					factpickdate:'',
					storerkey:'',
					altsku:'',
					ordertype:'',
				};
			},
			//新增
			formSubmit: function () {
				this.$refs.form.validate(function(valid)  {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(function()  {
							this.formLoading = true;
							var para = Object.assign({}, this.form);
							
							para.requestshipdate = !para.requestshipdate ? '' : utilFormatDate(new Date(para.requestshipdate), 'yyyy-MM-dd');
							para.pickstartdate = !para.pickstartdate ? '' : utilFormatDate(new Date(para.pickstartdate), 'yyyy-MM-dd');
							para.stdocdate = !para.stdocdate ? '' : utilFormatDate(new Date(para.stdocdate), 'yyyy-MM-dd');
							para.nowcompletedate = !para.nowcompletedate ? '' : utilFormatDate(new Date(para.nowcompletedate), 'yyyy-MM-dd');
							para.factpickdate = !para.factpickdate ? '' : utilFormatDate(new Date(para.factpickdate), 'yyyy-MM-dd');
							
							
							this.$http.post(!!para.id?this.url.edit:this.url.save,para,{emulateJSON: true}).then(function(res)  {
								this.formLoading = false;
								this.$message({
									message: '提交成功',
									type: 'success',
									duration:1500
								});
								this.$refs['form'].resetFields();
								this.formVisible = false;
								this.getOrderforecasts();
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
						this.getOrderforecasts();
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
				openuploadwin('Excel导入',this.url.ImportXls, "orderforecastList");
			},
			//初始化数据字典
			initDictsData:function(){
	        	var _this = this;
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
			this.getOrderforecasts();
			this.getorderstatus();//初始化订单状态
			this.getordertype();//初始化订单状态
			this.getwarehouse();
		}
	});
	
	function utilFormatDate(date, pattern) {
		console.log(date);
        pattern = pattern || "yyyy-MM-dd";
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