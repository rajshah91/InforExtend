<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>急货实时</title>
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
	<div id="cargorealList" v-cloak>
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
<!-- 			    <el-form-item style="margin-bottom: 8px;" prop="warehouse"> -->
<!-- 					<el-select v-model="filters.region" placeholder="请选择分区" clearable style="width:175px"> -->
<!-- 	                 <el-option label="昆山区" value="昆山区"></el-option> -->
<!-- 	             </el-select> -->
<!-- 				</el-form-item> -->
<!-- 				<el-form-item style="margin-bottom: 8px;" prop="warehouse"> -->
<!-- 					<el-select v-model="filters.operatdepart" placeholder="请选择操作部" clearable style="width:175px"> -->
<!-- 	                 <el-option label="操作一部" value="depart1"></el-option> -->
<!-- 	                 <el-option label="操作二部" value="depart2"></el-option> -->
<!-- 	                 <el-option label="操作三部" value="depart3"></el-option> -->
<!-- 	                 <el-option label="客服部" value="operation4"></el-option> -->
<!-- 	            </el-select> -->
<!-- 				</el-form-item> -->
<!-- 				<el-form-item style="margin-bottom: 8px;" prop="warehouse"> -->
<!-- 					<el-select v-model="filters.operatsection" placeholder="请选择操作科" clearable style="width:175px"> -->
<!-- 	                 <el-option label="操作一部一科" value="section1"></el-option> -->
<!-- 	                 <el-option label="操作一部二科" value="section2"></el-option> -->
<!-- 	                 <el-option label="操作一部三科" value="section3"></el-option> -->
<!-- 	                 <el-option label="操作一部五科" value="section4"></el-option> -->
<!-- 	                 <el-option label="操作一部六科" value="section5"></el-option> -->
<!-- 	                 <el-option label="操作二部" value="section6"></el-option> -->
<!-- 	                 <el-option label="操作三部" value="section7"></el-option> -->
<!-- 	                 <el-option label="客服部一科" value="section8"></el-option> -->
<!-- 	                 <el-option label="客服部二科" value="section9"></el-option> -->
<!-- 	                 <el-option label="客服部三科" value="section10"></el-option> -->
<!-- 	                 <el-option label="客服部五科" value="section11"></el-option> -->
<!-- 	            </el-select> -->
<!-- 				</el-form-item> -->
<!-- 				<el-form-item style="margin-bottom: 8px;" prop="area"> -->
<!-- 					<el-select v-model="filters.area" placeholder="请选择库区" clearable style="width:175px"> -->
<!-- 	                 <el-option label="" value=""></el-option> -->
<!-- 	                 </el-select> -->
<!-- 				</el-form-item> -->
				<br>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getCargoreals">查询</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button icon="el-icon-refresh" @click="resetForm('filters')">重置</el-button>
			    </el-form-item>
			   <!--  <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="handleAdd">新增</el-button>
			    </el-form-item> -->
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="ExportXls">导出</el-button>
			    </el-form-item>
			    <!-- <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="ImportXls">导入</el-button>
			    </el-form-item> -->
			</el-form>
		</el-row>
		<el-row style="padding: 10px;" size="mini">
			  <el-dropdown trigger="click" style="float:right" label-width="80px">
			          <el-button prop="primary"  size="mini" icon="el-icon-setting"></el-button>
					  <el-dropdown-menu slot="dropdown">
					  <template>
						  <el-checkbox-group v-model="checkList" label-width="100px">
						    <el-checkbox label="仓库" v-model="detailList.warehouse" checked @change="show(detailList.warehouse)"></el-checkbox><br>
						    <el-checkbox label="库区" v-model="detailList.area" checked @change="show(detailList.area)"></el-checkbox><br>
						    <el-checkbox label="so单号" v-model="detailList.orderkey" checked @change="show(detailList.orderkey)"></el-checkbox><br>
						    <el-checkbox label="货主简称" v-model="detailList.storer" checked @change="show(detailList.storer)"></el-checkbox><br>
						    <el-checkbox label="收货人简称" v-model="detailList.vendor" checked @change="show(detailList.vendor)"></el-checkbox><br>
						    <el-checkbox label="请求出货日期" v-model="detailList.requestshipdate" checked @change="show(detailList.requestshipdate)"></el-checkbox><br>
						    <el-checkbox label="预警时间" v-model="detailList.warningtime" checked @change="show(detailList.warningtime)"></el-checkbox><br>
						    <el-checkbox label="订单状态" v-model="detailList.orderstatus" checked @change="show(detailList.orderstatus)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="cargoreals" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
<!-- 			<el-table-column type="selection" width="55"></el-table-column> -->
			<el-table-column type="index" width="60" label="序号"></el-table-column>
			<el-table-column prop="warehouse" label="仓库" v-if="columnshow.warehouse" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="area" label="库区" v-if="columnshow.area" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="orderkey" label="so单号" v-if="columnshow.orderkey" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="storer" label="货主简称" v-if="columnshow.storer" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="vendor" label="收货人简称" v-if="columnshow.vendor" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="requestshipdate" label="请求出货日期" v-if="columnshow.requestshipdate" min-width="140" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"></el-table-column>
			<el-table-column prop="warningtime" label="预警时间" v-if="columnshow.warningtime" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="orderstatus" label="订单状态" v-if="columnshow.orderstatus" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="operator" label="操作人" v-if="columnshow.operator" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
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
					<el-form-item label="库区" prop="area">
						<el-input v-model="form.area" auto-complete="off" placeholder="请输入库区"></el-input>
					</el-form-item>
					<el-form-item label="so单号" prop="orderkey">
						<el-input v-model="form.orderkey" auto-complete="off" placeholder="请输入so单号"></el-input>
					</el-form-item>
					<el-form-item label="货主简称" prop="storer">
						<el-input v-model="form.storer" auto-complete="off" placeholder="请输入货主简称"></el-input>
					</el-form-item>
					<el-form-item label="收货人简称" prop="vendor">
						<el-input v-model="form.vendor" auto-complete="off" placeholder="请输入收货人简称"></el-input>
					</el-form-item>
					<el-form-item label="请求出货日期">
						<el-date-picker type="date" placeholder="选择请求出货日期" v-model="form.requestshipdate"></el-date-picker>
					</el-form-item>
					<el-form-item label="预警时间">
						<el-date-picker type="date" placeholder="选择预警时间" v-model="form.warningtime"></el-date-picker>
					</el-form-item>
					<el-form-item label="订单状态" prop="orderstatus">
						<el-input v-model="form.orderstatus" auto-complete="off" placeholder="请输入订单状态"></el-input>
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
		el:"#cargorealList",
		data:function() {
			return {
				filters: {
					region:'',
					department:[],
					office:[],
					area:[],
				},
				url:{
					list:'${webRoot}/cargorealController.do?datagrid',
					del:'${webRoot}/cargorealController.do?doDel',
					batchDel:'${webRoot}/cargorealController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/cargorealController.do?doAdd',
					edit:'${webRoot}/cargorealController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/cargorealController.do?exportXls&id=',
					ImportXls:'${webRoot}/cargorealController.do?upload',
					findRegion:'${webRoot}/cargorealController.do?doFindRegion',
					findDepartment:'${webRoot}/cargorealController.do?doFindDepartment'
				},
				cargoreals: [],
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
					id:'id',
					createName:'createName',
					createBy:'createBy',
					createDate:'createDate',
					updateName:'updateName',
					updateBy:'updateBy',
					updateDate:'updateDate',
					sysOrgCode:'sysOrgCode',
					sysCompanyCode:'sysCompanyCode',
					bpmStatus:'bpmStatus',
					warehouse:'warehouse',
					area:'area',
					orderkey:'orderkey',
					storer:'storer',
					vendor:'vendor',
					requestshipdate:'requestshipdate',
					warningtime:'warningtime',
					orderstatus:'orderstatus',
					operator:'operator',
				},
				columnshow:{
					id:true,
					createName:true,
					createBy:true,
					createDate:true,
					updateName:true,
					updateBy:true,
					updateDate:true,
					sysOrgCode:true,
					sysCompanyCode:true,
					bpmStatus:true,
					warehouse:true,
					area:true,
					orderkey:true,
					storer:true,
					vendor:true,
					requestshipdate:true,
					warningtime:true,
					orderstatus:true,
					operator:true,
				},
				
				regions:[],
			    departments:[],
			    offices:[],
			    areas:[],
			//数据字典 
			}
		},
		methods: {
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
				if(value=="area"){
				   this.columnshow.area=!this.columnshow.area;
				}
				if(value=="orderkey"){
				   this.columnshow.orderkey=!this.columnshow.orderkey;
				}
				if(value=="storer"){
				   this.columnshow.storer=!this.columnshow.storer;
				}
				if(value=="vendor"){
				   this.columnshow.vendor=!this.columnshow.vendor;
				}
				if(value=="requestshipdate"){
				   this.columnshow.requestshipdate=!this.columnshow.requestshipdate;
				}
				if(value=="warningtime"){
				   this.columnshow.warningtime=!this.columnshow.warningtime;
				}
				if(value=="orderstatus"){
				   this.columnshow.orderstatus=!this.columnshow.orderstatus;
				}
				if(value=="operator"){
				   this.columnshow.operator=!this.columnshow.operator;
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getCargoreals();
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
				this.getCargoreals();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getCargoreals();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.page = 1;
		        this.$set(this.filters,"region",[]);
		        this.departments=[];
		    	this.$set(this.filters,"department",[]);
		    	this.offices=[];
			    this.$set(this.filters,"office",[]);
			    this.areas=[];
		        this.getCargoreals();
		    },
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
			//获取用户列表
			getCargoreals:function() {
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
				fields.push('area');
				fields.push('orderkey');
				fields.push('storer');
				fields.push('vendor');
				fields.push('requestshipdate');
				fields.push('warningtime');
				fields.push('orderstatus');
				fields.push('operator');
				var para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
						region:this.filters.region,
						department:this.filters.department.join(','),
						office:this.filters.office.join(','),
					 	area:this.filters.area.join(','),
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
					this.cargoreals = datas;
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
						this.getCargoreals();
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
					area:'',
					orderkey:'',
					storer:'',
					vendor:'',
					requestshipdate:'',
					warningtime:'',
					orderstatus:'',
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
							para.warningtime = !para.warningtime ? '' : utilFormatDate(new Date(para.warningtime), 'yyyy-MM-dd');
							
							
							this.$http.post(!!para.id?this.url.edit:this.url.save,para,{emulateJSON: true}).then(function(res)  {
								this.formLoading = false;
								this.$message({
									message: '提交成功',
									type: 'success',
									duration:1500
								});
								this.$refs['form'].resetFields();
								this.formVisible = false;
								this.getCargoreals();
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
						this.getCargoreals();
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
				openuploadwin('Excel导入',this.url.ImportXls, "cargorealList");
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
			this.findRegions();
			this.getCargoreals();
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