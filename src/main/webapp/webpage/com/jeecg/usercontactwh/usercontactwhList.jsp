<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户仓库权限关联表</title>
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
	<div id="usercontactwhList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
				<el-form-item style="margin-bottom: 8px;" prop="userid">
					<el-input v-model="filters.userid" auto-complete="off" placeholder="请输入用户id" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getUsercontactwhs">查询</el-button>
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
						    <el-checkbox label="用户id" v-model="detailList.userid" checked @change="show(detailList.userid)"></el-checkbox><br>
						    <el-checkbox label="用户名" v-model="detailList.username" checked @change="show(detailList.username)"></el-checkbox><br>
						    <el-checkbox label="仓库" v-model="detailList.warehouse" checked @change="show(detailList.warehouse)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="usercontactwhs" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" width="60"></el-table-column>
			<el-table-column prop="userid" label="用户id" v-if="columnshow.userid" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="username" label="用户名" v-if="columnshow.username" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="warehouse" label="仓库" v-if="columnshow.warehouse" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
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
			<el-form :model="form" label-width="80px" :rules="formRules" ref="form" size="mini" inline ="true">
					<el-form-item label="用户id" prop="userid">
						<el-input v-model="form.userid" auto-complete="off" placeholder="请输入用户id"></el-input>
					</el-form-item>
					<el-form-item label="用户名" prop="username">
						<el-input v-model="form.username" auto-complete="off" placeholder="请输入用户名"></el-input>
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
	</div>
</body>
<script>
	var vue = new Vue({			
		el:"#usercontactwhList",
		data:function() {
			return {
				filters: {
					userid:'',
				},
				url:{
					list:'${webRoot}/usercontactwhController.do?datagrid',
					del:'${webRoot}/usercontactwhController.do?doDel',
					batchDel:'${webRoot}/usercontactwhController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/usercontactwhController.do?doAdd',
					edit:'${webRoot}/usercontactwhController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/usercontactwhController.do?exportXls&id=',
					ImportXls:'${webRoot}/usercontactwhController.do?upload'
				},
				usercontactwhs: [],
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
					userid:'userid',
					username:'username',
					warehouse:'warehouse',
				},
				columnshow:{
					userid:true,
					username:true,
					warehouse:true,
				},
				
				
				//数据字典 
			}
		},
		methods: {
		    //列展示切换
			show:function(value){
				if(value=="userid"){
				   this.columnshow.userid=!this.columnshow.userid;
				}
				if(value=="username"){
				   this.columnshow.username=!this.columnshow.username;
				}
				if(value=="warehouse"){
				   this.columnshow.warehouse=!this.columnshow.warehouse;
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getUsercontactwhs();
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
				this.getUsercontactwhs();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getUsercontactwhs();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.getUsercontactwhs();
		    },
			//获取用户列表
			getUsercontactwhs:function() {
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
				fields.push('userid');
				fields.push('username');
				fields.push('warehouse');
				var para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	userid:this.filters.userid,
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
					this.usercontactwhs = datas;
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
						this.getUsercontactwhs();
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
					userid:'',
					username:'',
					warehouse:'',
				};
			},
			//新增
			formSubmit: function () {
				console.log(1111);
				this.$refs.form.validate((valid) =>  {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() =>  {
							this.formLoading = true;
							var para = Object.assign({}, this.form);
							
							
							this.$http.post(!!para.id?this.url.edit:this.url.save,para,{emulateJSON: true}).then((res) =>   {
								this.formLoading = false;
								this.$message({
									message: '提交成功',
									type: 'success',
									duration:1500
								});
								this.$refs['form'].resetFields();
								this.formVisible = false;
								this.getUsercontactwhs();
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
						this.getUsercontactwhs();
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
				openuploadwin('Excel导入',this.url.ImportXls, "usercontactwhList");
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
			this.getUsercontactwhs();
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