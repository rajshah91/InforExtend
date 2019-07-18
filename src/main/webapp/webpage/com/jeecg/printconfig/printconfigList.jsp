<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>打印机配置表</title>
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
	<div id="printconfigList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
				<el-form-item style="margin-bottom: 8px;" prop="printname">
					<el-input v-model="filters.printname" auto-complete="off" placeholder="请输入打印机名称" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="note">
					<el-input v-model="filters.note" auto-complete="off" placeholder="请输入说明" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="printtype">
					<el-select v-model="filters.printtype" placeholder="请选择打印机类型" style="width:175px" clearable>
				      <el-option :label="option.typename" :value="option.typecode" v-for="option in printtypeOptions"></el-option>
				    </el-select>
				</el-form-item>
			    <br>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getPrintconfigs">查询</el-button>
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
						    <el-checkbox label="打印机名称" v-model="detailList.printname" checked @change="show(detailList.printname)"></el-checkbox><br>
						    <el-checkbox label="说明" v-model="detailList.note" checked @change="show(detailList.note)"></el-checkbox><br>
						    <el-checkbox label="打印机类型" v-model="detailList.printtype" checked @change="show(detailList.printtype)"></el-checkbox><br>
						    <el-checkbox label="ftp服务器地址" v-model="detailList.ftpaddress" checked @change="show(detailList.ftpaddress)"></el-checkbox><br>
						    <el-checkbox label="用户名" v-model="detailList.username" checked @change="show(detailList.username)"></el-checkbox><br>
						    <el-checkbox label="密码" v-model="detailList.password" checked @change="show(detailList.password)"></el-checkbox><br>
						    <el-checkbox label="路径" v-model="detailList.path" checked @change="show(detailList.path)"></el-checkbox><br>
						    <el-checkbox label="仓库" v-model="detailList.warehouse" checked @change="show(detailList.warehouse)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="printconfigs" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<!-- <el-table-column type="selection" width="55"></el-table-column> -->
			<!-- <el-table-column type="index" width="60" label=""></el-table-column> -->
			<el-table-column prop="warehouse" label="仓库" v-if="columnshow.warehouse" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="printname" label="打印机名称" v-if="columnshow.printname" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="note" label="说明" v-if="columnshow.note" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="printtype" label="打印机类型" v-if="columnshow.printtype" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatPrinttypeDict"></el-table-column>
			<!-- <el-table-column prop="ftpaddress" label="ftp服务器地址" v-if="columnshow.ftpaddress" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="username" label="用户名" v-if="columnshow.username" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="password" label="密码" v-if="columnshow.password" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="path" label="路径" v-if="columnshow.path" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column> -->
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
			<el-form :model="form" label-width="100px" :rules="formRules" ref="form" size="mini" inline="true">
			<el-tabs v-model="editorActiveTab" type="border-card">
			<el-tab-pane label="打印机配置" name="first">
			        <el-form-item label="仓库" prop="warehouse">
			         <el-select v-model="form.warehouse" v-model="warehouses" placeholder="请选择仓库" clearable style="width:175px">
	                 	<el-option v-for="warehouse in warehouses"  :value="warehouse"></el-option>
	                 </el-select>
						<!-- <el-input v-model="form.warehouse" auto-complete="off" placeholder="请输入仓库"></el-input> -->
					</el-form-item>
					<el-form-item label="打印机名称" prop="printname">
						<el-input v-model="form.printname" auto-complete="off" placeholder="请输入打印机名称"></el-input>
					</el-form-item>
					<br>
					<el-form-item label="说明" prop="note">
						<el-input v-model="form.note" auto-complete="off" placeholder="请输入说明"></el-input>
					</el-form-item>
					<el-form-item label="打印机类型">
						<el-select v-model="form.printtype" placeholder="请选择打印机类型" style="width:175px">
					      <el-option :label="option.typename" :value="option.typecode" v-for="option in printtypeOptions"></el-option>
					    </el-select>
					</el-form-item>
			</el-tab-pane>
			<el-tab-pane label="ftp设置">
					<el-form-item label="ftp服务器地址" prop="ftpaddress">
						<el-input v-model="form.ftpaddress" auto-complete="off" placeholder="请输入ftp服务器地址"></el-input>
					</el-form-item>
					<el-form-item label="路径" prop="path">
						<el-input v-model="form.path" auto-complete="off" placeholder="请输入路径"></el-input>
					</el-form-item>
					<br>
					<el-form-item label="用户名" prop="username">
						<el-input v-model="form.username" auto-complete="off" placeholder="请输入用户名"></el-input>
					</el-form-item>
					<el-form-item label="密码" prop="password">
						<el-input v-model="form.password" auto-complete="off" placeholder="请输入密码"></el-input>
					</el-form-item>
			</el-tab-pane>
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
		el:"#printconfigList",
		data:function() {
			return {
				filters: {
					printname:'',
					note:'',
					printtype:'',
				},
				url:{
					list:'${webRoot}/printconfigController.do?datagrid',
					del:'${webRoot}/printconfigController.do?doDel',
					batchDel:'${webRoot}/printconfigController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/printconfigController.do?doAdd',
					edit:'${webRoot}/printconfigController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/printconfigController.do?exportXls&id=',
					ImportXls:'${webRoot}/printconfigController.do?upload',
					getwarehouse:'${webRoot}/ordersController.do?getwarehouse'
				},
				editorActiveTab:'first',
				warehouses:[],
				printconfigs: [],
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
					printname:'printname',
					note:'note',
					printtype:'printtype',
					ftpaddress:'ftpaddress',
					username:'username',
					password:'password',
					path:'path',
					warehouse:'warehouse',
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
					printname:true,
					note:true,
					printtype:true,
					ftpaddress:true,
					username:true,
					password:true,
					path:true,
					warehouse:true,
				},
				
				
				//数据字典 
				printtypeOptions:[],
			}
		},
		methods: {
			getwarehouse:function(){
				this.$http.get(this.url.getwarehouse).then(function(res)  {
					/* console.log(res.data.warehouse); */
					this.warehouses=res.data.warehouse;
				});
			},
			formatPrinttypeDict: function(row,column,cellValue, index){
				var names="";
				var values=cellValue;
				if(!Array.isArray(cellValue))values=cellValue.split(',');
				for (var i = 0; i < values.length; i++) {
					var value = values[i];
					if(i>0)names+=",";
					for(var j in this.printtypeOptions){
						var option=this.printtypeOptions[j];
						if(value==option.typecode){
							names+=option.typename;
						}
					}
				}
				return names;
			},
		    //列展示切换
			show:function(value){
				if(value=="printname"){
				   this.columnshow.printname=!this.columnshow.printname;
				}
				if(value=="note"){
				   this.columnshow.note=!this.columnshow.note;
				}
				if(value=="printtype"){
				   this.columnshow.printtype=!this.columnshow.printtype;
				}
				if(value=="ftpaddress"){
				   this.columnshow.ftpaddress=!this.columnshow.ftpaddress;
				}
				if(value=="username"){
				   this.columnshow.username=!this.columnshow.username;
				}
				if(value=="password"){
				   this.columnshow.password=!this.columnshow.password;
				}
				if(value=="path"){
				   this.columnshow.path=!this.columnshow.path;
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
				this.getPrintconfigs();
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
				this.getPrintconfigs();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getPrintconfigs();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.getPrintconfigs();
		    },
			//获取用户列表
			getPrintconfigs:function() {
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
				fields.push('printname');
				fields.push('note');
				fields.push('printtype');
				fields.push('ftpaddress');
				fields.push('username');
				fields.push('password');
				fields.push('path');
				fields.push('warehouse');
				let para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	printname:this.filters.printname,
					 	note:this.filters.note,
					 	printtype:this.filters.printtype,
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
					this.printconfigs = datas;
					this.listLoading = false;
				});
			},
			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(()=>  {
					this.listLoading = true;
					let para = { id: row.id };
					this.$http.post(this.url.del,para,{emulateJSON: true}).then((res) => {
						this.listLoading = false;
						this.$message({
							message: '删除成功',
							type: 'success',
							duration:1500
						});
						this.getPrintconfigs();
					});
				}).catch(()=>  {

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
					printname:'',
					note:'',
					printtype:'',
					ftpaddress:'',
					username:'',
					password:'',
					path:'',
					warehouse:'',
				};
			},
			//新增
			formSubmit: function () {
				this.$refs.form.validate((valid)=>  {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(()=>  {
							this.formLoading = true;
							let para = Object.assign({}, this.form);
							
							
							
							this.$http.post(!!para.id?this.url.edit:this.url.save,para,{emulateJSON: true}).then((res)=>  {
								this.formLoading = false;
								this.$message({
									message: '提交成功',
									type: 'success',
									duration:1500
								});
								this.$refs['form'].resetFields();
								this.formVisible = false;
								this.getPrintconfigs();
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
						this.getPrintconfigs();
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
				openuploadwin('Excel导入',this.url.ImportXls, "printconfigList");
			},
			//初始化数据字典
			initDictsData:function(){
	        	var _this = this;
	        	_this.initDictByCode('printtype',_this,'printtypeOptions');
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
			this.getPrintconfigs();
			this.getwarehouse();
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