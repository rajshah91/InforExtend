<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>员工操作量</title>
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
	<div id="workamountList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
			 <el-form-item style="margin-bottom: 8px;" prop="area"> 
	            <el-select v-model="filters.region" clearable placeholder="请选择分区" @change="changeRegion" style="width:175px">
					    <el-option
					      v-for="item in regions"
					      :key="item.value"
					      :label="item.label"
					      :value="item.value">
					    </el-option>
 					</el-select>
	        	 </el-form-item>
	        	 <el-form-item style="margin-bottom: 8px;" prop="area"> 
	                <el-select v-model="filters.department" clearable multiple collapse-tags @change="changeDepartment" placeholder="请选择操作部" style="width:175px">
					    <el-option
					      v-for="item in departments"
					      :key="item.value"
					      :label="item.label"
					      :value="item.value">
					    </el-option>
 					</el-select>
	        	 </el-form-item>
	        	 <el-form-item style="margin-bottom: 8px;" prop="area"> 
	                <el-select v-model="filters.office" clearable multiple collapse-tags @change="changeOffice" placeholder="请选择操作科" style="width:175px">
					    <el-option
					      v-for="item in offices"
					      :key="item.value"
					      :label="item.label"
					      :value="item.value">
					    </el-option>
 					</el-select>
	        	 </el-form-item>
	        	 <el-form-item style="margin-bottom: 8px;" prop="area"> 
	                <el-select v-model="filters.area" clearable multiple collapse-tags placeholder="请选择库区" style="width:175px">
					    <el-option
					      v-for="item in areas"
					      :key="item.value"
					      :label="item.label"
					      :value="item.value">
					    </el-option>
 					</el-select>
	        	 </el-form-item>
	        	 <el-form-item style="margin-bottom: 8px;" prop="datestart">
					<el-date-picker type="datetime" placeholder="选择日期起" default-time="08:00:00"  v-model="filters.datestart" style="width:175px"></el-date-picker>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="dateend">
					<el-date-picker type="datetime" placeholder="选择日期至" default-time="08:00:00" v-model="filters.dateend" style="width:175px"></el-date-picker>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="employid">
					<el-input  placeholder="员工工号"  v-model="filters.employid" style="width:175px"></el-input>
				</el-form-item>
				<br>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getWorkamounts">查询</el-button>
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
						    <el-checkbox label="员工姓名" v-model="detailList.username" checked @change="show(detailList.username)"></el-checkbox><br>
						    <el-checkbox label="出货票数" v-model="detailList.sonamesum" checked @change="show(detailList.sonamesum)"></el-checkbox><br>
						    <el-checkbox label="出货料号数" v-model="detailList.soskusum" checked @change="show(detailList.soskusum)"></el-checkbox><br>
						    <el-checkbox label="出货大储位数" v-model="detailList.soblocsum" checked @change="show(detailList.soblocsum)"></el-checkbox><br>
						    <el-checkbox label="出货小储位数" v-model="detailList.soslocsum" checked @change="show(detailList.soslocsum)"></el-checkbox><br>
						    <el-checkbox label="出货lpn数" v-model="detailList.solpnsum" checked @change="show(detailList.solpnsum)"></el-checkbox><br>
						    <el-checkbox label="拣货票数" v-model="detailList.picknamesum" checked @change="show(detailList.picknamesum)"></el-checkbox><br>
						    <el-checkbox label="拣货料号数" v-model="detailList.pickskusum" checked @change="show(detailList.pickskusum)"></el-checkbox><br>
						    <el-checkbox label="拣货大储位数" v-model="detailList.pickblocsum" checked @change="show(detailList.pickblocsum)"></el-checkbox><br>
						    <el-checkbox label="拣货小储位数" v-model="detailList.pickslocsum" checked @change="show(detailList.pickslocsum)"></el-checkbox><br>
						    <el-checkbox label="拣货lpn数" v-model="detailList.picklpnsum" checked @change="show(detailList.picklpnsum)"></el-checkbox><br>
						    <el-checkbox label="复检票数" v-model="detailList.rcnamesum" checked @change="show(detailList.rcnamesum)"></el-checkbox><br>
						    <el-checkbox label="复检料号数" v-model="detailList.rcskusum" checked @change="show(detailList.rcskusum)"></el-checkbox><br>
						    <el-checkbox label="复检大储位数" v-model="detailList.rcblocsum" checked @change="show(detailList.rcblocsum)"></el-checkbox><br>
						    <el-checkbox label="复检小储位数" v-model="detailList.rcslocsum" checked @change="show(detailList.rcslocsum)"></el-checkbox><br>
						    <el-checkbox label="复检lpn数" v-model="detailList.rclpnsum" checked @change="show(detailList.rclpnsum)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="workamounts" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<!-- <el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" width="60"></el-table-column> -->
			<el-table-column prop="username" label="员工姓名" v-if="columnshow.username" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column label="收货">
				<el-table-column prop="sonamesum" label="票数" v-if="columnshow.sonamesum" min-width="70" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="soskusum" label="料号数" v-if="columnshow.soskusum" min-width="90" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="soblocsum" label="大储位数" v-if="columnshow.soblocsum" min-width="100" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="soslocsum" label="小储位数" v-if="columnshow.soslocsum" min-width="100" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="solpnsum" label="lpn数" v-if="columnshow.solpnsum" min-width="90" sortable="custom" show-overflow-tooltip></el-table-column>
			</el-table-column>
			<el-table-column prop="pickname" label="拣货员"  min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column label="拣货">
				<el-table-column prop="picknamesum" label="票数" v-if="columnshow.picknamesum" min-width="70" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="pickskusum" label="料号数" v-if="columnshow.pickskusum" min-width="90" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="pickblocsum" label="大储位数" v-if="columnshow.pickblocsum" min-width="100" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="pickslocsum" label="小储位数" v-if="columnshow.pickslocsum" min-width="100" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="picklpnsum" label="lpn数" v-if="columnshow.picklpnsum" min-width="90" sortable="custom" show-overflow-tooltip></el-table-column>
			</el-table-column>
			<el-table-column prop="recheckname" label="复检员"  min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column label="复检">
				<el-table-column prop="rcnamesum" label="票数" v-if="columnshow.rcnamesum" min-width="70" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="rcskusum" label="料号数" v-if="columnshow.rcskusum" min-width="90" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="rcblocsum" label="大储位数" v-if="columnshow.rcblocsum" min-width="100" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="rcslocsum" label="小储位数" v-if="columnshow.rcslocsum" min-width="100" sortable="custom" show-overflow-tooltip></el-table-column>
				<el-table-column prop="rclpnsum" label="lpn数" v-if="columnshow.rclpnsum" min-width="90" sortable="custom" show-overflow-tooltip></el-table-column>
			</el-table-column>
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
					<el-form-item label="出货票数" prop="sonamesum">
						<el-input v-model="form.sonamesum" auto-complete="off" placeholder="请输入出货票数"></el-input>
					</el-form-item>
					<el-form-item label="出货料号数" prop="soskusum">
						<el-input v-model="form.soskusum" auto-complete="off" placeholder="请输入出货料号数"></el-input>
					</el-form-item>
					<el-form-item label="出货大储位数" prop="soblocsum">
						<el-input v-model="form.soblocsum" auto-complete="off" placeholder="请输入出货大储位数"></el-input>
					</el-form-item>
					<el-form-item label="出货小储位数" prop="soslocsum">
						<el-input v-model="form.soslocsum" auto-complete="off" placeholder="请输入出货小储位数"></el-input>
					</el-form-item>
					<el-form-item label="出货lpn数" prop="solpnsum">
						<el-input v-model="form.solpnsum" auto-complete="off" placeholder="请输入出货lpn数"></el-input>
					</el-form-item>
					<el-form-item label="拣货票数" prop="picknamesum">
						<el-input v-model="form.picknamesum" auto-complete="off" placeholder="请输入拣货票数"></el-input>
					</el-form-item>
					<el-form-item label="拣货料号数" prop="pickskusum">
						<el-input v-model="form.pickskusum" auto-complete="off" placeholder="请输入拣货料号数"></el-input>
					</el-form-item>
					<el-form-item label="拣货大储位数" prop="pickblocsum">
						<el-input v-model="form.pickblocsum" auto-complete="off" placeholder="请输入拣货大储位数"></el-input>
					</el-form-item>
					<el-form-item label="拣货小储位数" prop="pickslocsum">
						<el-input v-model="form.pickslocsum" auto-complete="off" placeholder="请输入拣货小储位数"></el-input>
					</el-form-item>
					<el-form-item label="拣货lpn数" prop="picklpnsum">
						<el-input v-model="form.picklpnsum" auto-complete="off" placeholder="请输入拣货lpn数"></el-input>
					</el-form-item>
					<el-form-item label="复检票数" prop="rcnamesum">
						<el-input v-model="form.rcnamesum" auto-complete="off" placeholder="请输入复检票数"></el-input>
					</el-form-item>
					<el-form-item label="复检料号数" prop="rcskusum">
						<el-input v-model="form.rcskusum" auto-complete="off" placeholder="请输入复检料号数"></el-input>
					</el-form-item>
					<el-form-item label="复检大储位数" prop="rcblocsum">
						<el-input v-model="form.rcblocsum" auto-complete="off" placeholder="请输入复检大储位数"></el-input>
					</el-form-item>
					<el-form-item label="复检小储位数" prop="rcslocsum">
						<el-input v-model="form.rcslocsum" auto-complete="off" placeholder="请输入复检小储位数"></el-input>
					</el-form-item>
					<el-form-item label="复检lpn数" prop="rclpnsum">
						<el-input v-model="form.rclpnsum" auto-complete="off" placeholder="请输入复检lpn数"></el-input>
					</el-form-item>
					<el-form-item label="员工姓名" prop="username">
						<el-input v-model="form.username" auto-complete="off" placeholder="请输入员工姓名"></el-input>
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
		el:"#workamountList",
		data:function() {
			return {
				filters: {
					datestart:new Date().getTime() - 3600 * 1000 * 24 * 1,
					dateend:new Date(),
					region:'',
					department:[],
					office:[],
					area:[],
					employid:'',
				},
				url:{
					list:'${webRoot}/workamountController.do?datagrid',
					del:'${webRoot}/workamountController.do?doDel',
					batchDel:'${webRoot}/workamountController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/workamountController.do?doAdd',
					edit:'${webRoot}/workamountController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/workamountController.do?exportXls',
					ImportXls:'${webRoot}/workamountController.do?upload',
					findRegion:'${webRoot}/cargorealController.do?doFindRegion',
					findDepartment:'${webRoot}/cargorealController.do?doFindDepartment'
				},
				workamounts: [],
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
				
				//显示列
				checkList:[],
				detailList:{
					sonamesum:'sonamesum',
					soskusum:'soskusum',
					soblocsum:'soblocsum',
					soslocsum:'soslocsum',
					solpnsum:'solpnsum',
					picknamesum:'picknamesum',
					pickskusum:'pickskusum',
					pickblocsum:'pickblocsum',
					pickslocsum:'pickslocsum',
					picklpnsum:'picklpnsum',
					rcnamesum:'rcnamesum',
					rcskusum:'rcskusum',
					rcblocsum:'rcblocsum',
					rcslocsum:'rcslocsum',
					rclpnsum:'rclpnsum',
					username:'username',
				},
				columnshow:{
					sonamesum:true,
					soskusum:true,
					soblocsum:true,
					soslocsum:true,
					solpnsum:true,
					picknamesum:true,
					pickskusum:true,
					pickblocsum:true,
					pickslocsum:true,
					picklpnsum:true,
					rcnamesum:true,
					rcskusum:true,
					rcblocsum:true,
					rcslocsum:true,
					rclpnsum:true,
					username:true,
				},
				
				regions:[],
			    departments:[],
			    offices:[],
			    areas:[],
				//数据字典 
			}
		},
		methods: {
			findRegions:function(){
		    	this.$http.get(this.url.findRegion).then(function(res)  {
					this.regions = res.data;
				});
		    },
		    findDepartments:function(){
		    	if(this.filters.region!=''){
			    	this.$http.get(this.url.findDepartment,{params:{regions:this.filters.region}}).then(function(res)  {
						this.departments = res.data;
					});
		    	}
		    },
		    findOffices:function(){
		    	if(this.filters.department!=''){
			    	this.$http.get(this.url.findDepartment,{params:{regions:this.filters.department.join(',')}}).then(function(res)  {
						this.offices = res.data;
					});
		    	}
		    },
		    findAreas:function(){
		    	if(this.filters.office!=''){
			    	this.$http.get(this.url.findDepartment,{params:{regions:this.filters.office.join(',')}}).then(function(res)  {
						this.areas = res.data;
					});
		    	}
		    },
		    changeRegion:function(){
		        this.departments=[];
		    	this.$set(this.filters,"department",[]);
		    	this.offices=[];
			    this.$set(this.filters,"office",[]);
			    this.areas=[];
			    this.$set(this.filters,"area",[]);
		    	this.findDepartments();
		    }, 
		    changeDepartment:function(){
		    	this.offices=[];
			    this.$set(this.filters,"office",[]);
			    this.areas=[];
			    this.$set(this.filters,"area",[]);
		    	this.findOffices();
		    },
		    changeOffice:function(){
		    	this.areas=[];
			    this.$set(this.filters,"area",[]);
		    	this.findAreas();
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
				if(value=="sonamesum"){
				   this.columnshow.sonamesum=!this.columnshow.sonamesum;
				}
				if(value=="soskusum"){
				   this.columnshow.soskusum=!this.columnshow.soskusum;
				}
				if(value=="soblocsum"){
				   this.columnshow.soblocsum=!this.columnshow.soblocsum;
				}
				if(value=="soslocsum"){
				   this.columnshow.soslocsum=!this.columnshow.soslocsum;
				}
				if(value=="solpnsum"){
				   this.columnshow.solpnsum=!this.columnshow.solpnsum;
				}
				if(value=="picknamesum"){
				   this.columnshow.picknamesum=!this.columnshow.picknamesum;
				}
				if(value=="pickskusum"){
				   this.columnshow.pickskusum=!this.columnshow.pickskusum;
				}
				if(value=="pickblocsum"){
				   this.columnshow.pickblocsum=!this.columnshow.pickblocsum;
				}
				if(value=="pickslocsum"){
				   this.columnshow.pickslocsum=!this.columnshow.pickslocsum;
				}
				if(value=="picklpnsum"){
				   this.columnshow.picklpnsum=!this.columnshow.picklpnsum;
				}
				if(value=="rcnamesum"){
				   this.columnshow.rcnamesum=!this.columnshow.rcnamesum;
				}
				if(value=="rcskusum"){
				   this.columnshow.rcskusum=!this.columnshow.rcskusum;
				}
				if(value=="rcblocsum"){
				   this.columnshow.rcblocsum=!this.columnshow.rcblocsum;
				}
				if(value=="rcslocsum"){
				   this.columnshow.rcslocsum=!this.columnshow.rcslocsum;
				}
				if(value=="rclpnsum"){
				   this.columnshow.rclpnsum=!this.columnshow.rclpnsum;
				}
				if(value=="username"){
				   this.columnshow.username=!this.columnshow.username;
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getWorkamounts();
			},
			handleDownFile:function(type,filePath){
				var downUrl=this.url.downFile+ filePath +"?down=true";
				window.open(downUrl);
			},
			formatDate: function(row,column,cellValue, index){
				return !!cellValue?utilFormatDate(new Date(cellValue), 'yyyy-MM-dd'):'';
			},
			formatDateTime: function(row,column,cellValue, index){
				return !!cellValue?utilFormatDate(new Date(cellValue), 'yyyy-MM-dd hh:mm:ss'):'';
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getWorkamounts();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getWorkamounts();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.getWorkamounts();
		    },
			//获取用户列表
			getWorkamounts:function() {
				var fields=[];
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
				fields.push('sonamesum');
				fields.push('soskusum');
				fields.push('soblocsum');
				fields.push('soslocsum');
				fields.push('solpnsum');
				fields.push('picknamesum');
				fields.push('pickskusum');
				fields.push('pickblocsum');
				fields.push('pickslocsum');
				fields.push('picklpnsum');
				fields.push('rcnamesum');
				fields.push('rcskusum');
				fields.push('rcblocsum');
				fields.push('rcslocsum');
				fields.push('rclpnsum');
				fields.push('username');
				fields.push('pickname');
				fields.push('recheckname');
				var para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
						employid:this.filters.employid,
						datestart:!this.filters.datestart ? '' : utilFormatDate(new Date(this.filters.datestart), 'yyyy-MM-dd hh:mm:ss'),
						dateend:!this.filters.dateend ? '' : utilFormatDate(new Date(this.filters.dateend), 'yyyy-MM-dd hh:mm:ss'),
						region:this.filters.region,
						department:this.filters.department.join(','),
						office:this.filters.office.join(','),
					 	seracharea:this.filters.area.join(','),
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
					this.workamounts = datas;
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
						this.getWorkamounts();
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
					sonamesum:'',
					soskusum:'',
					soblocsum:'',
					soslocsum:'',
					solpnsum:'',
					picknamesum:'',
					pickskusum:'',
					pickblocsum:'',
					pickslocsum:'',
					picklpnsum:'',
					rcnamesum:'',
					rcskusum:'',
					rcblocsum:'',
					rcslocsum:'',
					rclpnsum:'',
					username:'',
				};
			},
			//新增
			formSubmit: function () {
				this.$refs.form.validate(function(valid)  {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(function()  {
							this.formLoading = true;
							var para = Object.assign({}, this.form);
							
							
							
							this.$http.post(!!para.id?this.url.edit:this.url.save,para,{emulateJSON: true}).then(function(res)  {
								this.formLoading = false;
								this.$message({
									message: '提交成功',
									type: 'success',
									duration:1500
								});
								this.$refs['form'].resetFields();
								this.formVisible = false;
								this.getWorkamounts();
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
						this.getWorkamounts();
					});
				}).catch(function()  {
				});
			},
			//导出
			ExportXls: function() {
				    var datestart=!this.filters.datestart ? '' : utilFormatDate(new Date(this.filters.datestart), 'yyyy-MM-dd hh:mm:ss');
					var	dateend=!this.filters.dateend ? '' : utilFormatDate(new Date(this.filters.dateend), 'yyyy-MM-dd hh:mm:ss');
					var urllist = "&dateend="+dateend+"&datestart="+datestart+"&region="+this.filters.region+"&department="+this.filters.department.join(',')+"&office="+this.filters.office.join(',')+"&seracharea="+this.filters.area.join(',');
					window.location.href = this.url.exportXls+urllist;
			},
			//导入
			ImportXls: function(){
				openuploadwin('Excel导入',this.url.ImportXls, "workamountList");
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
			/* this.getWorkamounts(); */
			this.findRegions();//初始化区
		}
	});
	
	function utilFormatDate(date, pattern) {
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
</script>
</html>