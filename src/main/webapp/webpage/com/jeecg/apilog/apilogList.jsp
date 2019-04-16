<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>接口日志文档</title>
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
	<div id="apilogList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
				<el-form-item style="margin-bottom: 8px;" prop="createName">
					<el-input v-model="filters.createName" auto-complete="off" placeholder="请输入创建人名称" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="result">
					<el-input v-model="filters.result" auto-complete="off" placeholder="请输入结果" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="partner">
					<el-input v-model="filters.partner" auto-complete="off" placeholder="请输入交互的对象" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="servicename">
					<el-input v-model="filters.servicename" auto-complete="off" placeholder="请输入接口名称" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="operator">
					<el-input v-model="filters.operator" auto-complete="off" placeholder="请输入操作人" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getApilogs">查询</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button icon="el-icon-refresh" @click="resetForm('filters')">重置</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="handleAdd">新增</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="ExportXls">导出</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="ImportXls">导入</el-button>
			    </el-form-item>
			</el-form>
		</el-row>
		<el-row style="padding: 10px;" size="mini">
			  <el-dropdown trigger="click" style="float:right" label-width="80px">
			          <el-button prop="primary"  size="mini" icon="el-icon-setting"></el-button>
					  <el-dropdown-menu slot="dropdown">
					  <template>
						  <el-checkbox-group v-model="checkList" label-width="100px">
						    <el-checkbox label="创建人名称" v-model="detailList.createName" checked @change="show(detailList.createName)"></el-checkbox><br>
						    <el-checkbox label="流程状态" v-model="detailList.bpmStatus" checked @change="show(detailList.bpmStatus)"></el-checkbox><br>
						    <el-checkbox label="状态" v-model="detailList.status" checked @change="show(detailList.status)"></el-checkbox><br>
						    <el-checkbox label="发送的数据" v-model="detailList.sendxml" checked @change="show(detailList.sendxml)"></el-checkbox><br>
						    <el-checkbox label="接受的数据" v-model="detailList.receivexml" checked @change="show(detailList.receivexml)"></el-checkbox><br>
						    <el-checkbox label="结果" v-model="detailList.result" checked @change="show(detailList.result)"></el-checkbox><br>
						    <el-checkbox label="交互的对象" v-model="detailList.partner" checked @change="show(detailList.partner)"></el-checkbox><br>
						    <el-checkbox label="接口名称" v-model="detailList.servicename" checked @change="show(detailList.servicename)"></el-checkbox><br>
						    <el-checkbox label="操作人" v-model="detailList.operator" checked @change="show(detailList.operator)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="apilogs" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<!-- <el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" width="60"></el-table-column> -->
			<el-table-column prop="createDate" label="创建时间" v-if="columnshow.createName" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="createName" label="创建人名称" v-if="columnshow.createName" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<!-- <el-table-column prop="bpmStatus" label="流程状态" v-if="columnshow.bpmStatus" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column> -->
			<!-- <el-table-column prop="status" label="状态" v-if="columnshow.status" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column> -->
			<el-table-column prop="sendxml" label="发送的数据" v-if="columnshow.sendxml" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="receivexml" label="接受的数据" v-if="columnshow.receivexml" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="result" label="结果" v-if="columnshow.result" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="partner" label="交互的对象" v-if="columnshow.partner" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="servicename" label="接口名称" v-if="columnshow.servicename" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="operator" label="操作人" v-if="columnshow.operator" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column label="操作" width="150">
				<template scope="scope">
					<el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="mini" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
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
					<el-form-item label="状态" prop="status">
						<el-input v-model="form.status" auto-complete="off" placeholder="请输入状态"></el-input>
					</el-form-item>
					<el-form-item label="发送的数据" prop="sendxml">
						<el-input v-model="form.sendxml" auto-complete="off" placeholder="请输入发送的数据"></el-input>
					</el-form-item>
					<el-form-item label="接受的数据" prop="receivexml">
						<el-input v-model="form.receivexml" auto-complete="off" placeholder="请输入接受的数据"></el-input>
					</el-form-item>
					<el-form-item label="结果" prop="result">
						<el-input v-model="form.result" auto-complete="off" placeholder="请输入结果"></el-input>
					</el-form-item>
					<el-form-item label="交互的对象" prop="partner">
						<el-input v-model="form.partner" auto-complete="off" placeholder="请输入交互的对象"></el-input>
					</el-form-item>
					<el-form-item label="接口名称" prop="servicename">
						<el-input v-model="form.servicename" auto-complete="off" placeholder="请输入接口名称"></el-input>
					</el-form-item>
					<el-form-item label="操作人" prop="operator">
						<el-input v-model="form.operator" auto-complete="off" placeholder="请输入操作人"></el-input>
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
		el:"#apilogList",
		data:function() {
			return {
				filters: {
					createName:'',
					result:'',
					partner:'',
					servicename:'',
					operator:'',
				},
				url:{
					list:'${webRoot}/apilogController.do?datagrid',
					del:'${webRoot}/apilogController.do?doDel',
					batchDel:'${webRoot}/apilogController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/apilogController.do?doAdd',
					edit:'${webRoot}/apilogController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/apilogController.do?exportXls&id=',
					ImportXls:'${webRoot}/apilogController.do?upload'
				},
				apilogs: [],
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
					status:'status',
					sendxml:'sendxml',
					receivexml:'receivexml',
					result:'result',
					partner:'partner',
					servicename:'servicename',
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
					status:true,
					sendxml:true,
					receivexml:true,
					result:true,
					partner:true,
					servicename:true,
					operator:true,
				},
				
				
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
				if(value=="status"){
				   this.columnshow.status=!this.columnshow.status;
				}
				if(value=="sendxml"){
				   this.columnshow.sendxml=!this.columnshow.sendxml;
				}
				if(value=="receivexml"){
				   this.columnshow.receivexml=!this.columnshow.receivexml;
				}
				if(value=="result"){
				   this.columnshow.result=!this.columnshow.result;
				}
				if(value=="partner"){
				   this.columnshow.partner=!this.columnshow.partner;
				}
				if(value=="servicename"){
				   this.columnshow.servicename=!this.columnshow.servicename;
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
				this.getApilogs();
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
				this.getApilogs();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getApilogs();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.getApilogs();
		    },
			//获取用户列表
			getApilogs:function() {
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
				fields.push('status');
				fields.push('sendxml');
				fields.push('receivexml');
				fields.push('result');
				fields.push('partner');
				fields.push('servicename');
				fields.push('operator');
				let para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	createName:this.filters.createName,
					 	result:this.filters.result,
					 	partner:this.filters.partner,
					 	servicename:this.filters.servicename,
					 	operator:this.filters.operator,
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
					this.apilogs = datas;
					this.listLoading = false;
				});
			},
			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(function()  {
					this.listLoading = true;
					let para = { id: row.id };
					this.$http.post(this.url.del,para,{emulateJSON: true}).then(function(res)  {
						this.listLoading = false;
						this.$message({
							message: '删除成功',
							type: 'success',
							duration:1500
						});
						this.getApilogs();
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
					status:'',
					sendxml:'',
					receivexml:'',
					result:'',
					partner:'',
					servicename:'',
					operator:'',
				};
			},
			//新增
			formSubmit: function () {
				this.$refs.form.validate(function(valid)  {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(function()  {
							this.formLoading = true;
							let para = Object.assign({}, this.form);
							
							
							
							this.$http.post(!!para.id?this.url.edit:this.url.save,para,{emulateJSON: true}).then(function(res)  {
								this.formLoading = false;
								this.$message({
									message: '提交成功',
									type: 'success',
									duration:1500
								});
								this.$refs['form'].resetFields();
								this.formVisible = false;
								this.getApilogs();
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
					let para = { ids: ids };
					this.$http.post(this.url.batchDel,para,{emulateJSON: true}).then(function(res)  {
						this.listLoading = false;
						this.$message({
							message: '删除成功',
							type: 'success',
							duration:1500
						});
						this.getApilogs();
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
				openuploadwin('Excel导入',this.url.ImportXls, "apilogList");
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
			this.getApilogs();
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