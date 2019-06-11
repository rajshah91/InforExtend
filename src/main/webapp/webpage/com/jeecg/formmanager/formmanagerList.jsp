<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>报表配置管理</title>
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
	<div id="formmanagerList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
				<el-form-item style="margin-bottom: 8px;" prop="whseid">
					<el-select v-model="filters.whseid" placeholder="请选择仓库" style="width:175px">
				      <el-option :label="option.typename" :value="option.typecode" v-for="option in warehouseOptions"></el-option>
				    </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="ipaddress">
					<el-select v-model="filters.ipaddress" placeholder="请选择服务器地址" style="width:175px">
				      <el-option :label="option.typename" :value="option.typecode" v-for="option in ipcodeOptions"></el-option>
				    </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="reportaddress">
					<el-input v-model="filters.reportaddress" auto-complete="off" placeholder="请输入报表路径" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="reportname">
					<el-input v-model="filters.reportname" auto-complete="off" placeholder="请输入报表名称" style="width:175px"></el-input>
				</el-form-item>
			    <br>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getFormmanagers">查询</el-button>
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
						    <el-checkbox label="仓库" v-model="detailList.whseid" checked @change="show(detailList.whseid)"></el-checkbox><br>
						    <el-checkbox label="服务器地址" v-model="detailList.ipaddress" checked @change="show(detailList.ipaddress)"></el-checkbox><br>
						    <el-checkbox label="报表路径" v-model="detailList.reportaddress" checked @change="show(detailList.reportaddress)"></el-checkbox><br>
						    <el-checkbox label="报表名称" v-model="detailList.reportname" checked @change="show(detailList.reportname)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="formmanagers" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" label='序号' width="60"></el-table-column>
			<el-table-column prop="whseid" label="仓库" v-if="columnshow.whseid" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatWarehouseDict"></el-table-column>
			<el-table-column prop="ipaddress" label="服务器地址" v-if="columnshow.ipaddress" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatIpcodeDict"></el-table-column>
			<el-table-column prop="reportaddress" label="报表路径" v-if="columnshow.reportaddress" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="reportname" label="报表名称" v-if="columnshow.reportname" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
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
			<el-form :model="form" label-width="90px" :rules="formRules" ref="form" size="mini" inline="true">
					<el-form-item label="仓库">
						<el-select v-model="form.whseid" placeholder="请选择仓库">
					      <el-option :label="option.typename" :value="option.typecode" v-for="option in warehouseOptions"></el-option>
					    </el-select>
					</el-form-item>
					<el-form-item label="服务器地址">
						<el-select v-model="form.ipaddress" placeholder="请选择服务器地址">
					      <el-option :label="option.typename" :value="option.typecode" v-for="option in ipcodeOptions"></el-option>
					    </el-select>
					</el-form-item>
					<el-form-item label="报表路径" prop="reportaddress">
						<el-input v-model="form.reportaddress" auto-complete="off" placeholder="请输入报表路径"></el-input>
					</el-form-item>
					<el-form-item label="报表名称" prop="reportname">
						<el-input v-model="form.reportname" auto-complete="off" placeholder="请输入报表名称"></el-input>
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
		el:"#formmanagerList",
		data:function() {
			return {
				filters: {
					whseid:'',
					ipaddress:'',
					reportaddress:'',
					reportname:'',
				},
				url:{
					list:'${webRoot}/formmanagerController.do?datagrid',
					del:'${webRoot}/formmanagerController.do?doDel',
					batchDel:'${webRoot}/formmanagerController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/formmanagerController.do?doAdd',
					edit:'${webRoot}/formmanagerController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/formmanagerController.do?exportXls&id=',
					ImportXls:'${webRoot}/formmanagerController.do?upload'
				},
				formmanagers: [],
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
					whseid:'whseid',
					ipaddress:'ipaddress',
					reportaddress:'reportaddress',
					reportname:'reportname',
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
					whseid:true,
					ipaddress:true,
					reportaddress:true,
					reportname:true,
				},
				
				
				//数据字典 
		   		warehouseOptions:[],
		   		ipcodeOptions:[],
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
				if(value=="whseid"){
				   this.columnshow.whseid=!this.columnshow.whseid;
				}
				if(value=="ipaddress"){
				   this.columnshow.ipaddress=!this.columnshow.ipaddress;
				}
				if(value=="reportaddress"){
				   this.columnshow.reportaddress=!this.columnshow.reportaddress;
				}
				if(value=="reportname"){
				   this.columnshow.reportname=!this.columnshow.reportname;
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getFormmanagers();
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
			formatWarehouseDict: function(row,column,cellValue, index){
				var names="";
				var values=cellValue;
				if(!Array.isArray(cellValue))values=cellValue.split(',');
				for (var i = 0; i < values.length; i++) {
					var value = values[i];
					if(i>0)names+=",";
					for(var j in this.warehouseOptions){
						var option=this.warehouseOptions[j];
						if(value==option.typecode){
							names+=option.typename;
						}
					}
				}
				return names;
			},
			formatIpcodeDict: function(row,column,cellValue, index){
				var names="";
				var values=cellValue;
				if(!Array.isArray(cellValue))values=cellValue.split(',');
				for (var i = 0; i < values.length; i++) {
					var value = values[i];
					if(i>0)names+=",";
					for(var j in this.ipcodeOptions){
						var option=this.ipcodeOptions[j];
						if(value==option.typecode){
							names+=option.typename;
						}
					}
				}
				return names;
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getFormmanagers();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getFormmanagers();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.getFormmanagers();
		    },
			//获取用户列表
			getFormmanagers:function() {
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
				fields.push('whseid');
				fields.push('ipaddress');
				fields.push('reportaddress');
				fields.push('reportname');
				let para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	whseid:this.filters.whseid,
					 	ipaddress:this.filters.ipaddress,
					 	reportaddress:this.filters.reportaddress,
					 	reportname:this.filters.reportname,
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
					this.formmanagers = datas;
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
						this.getFormmanagers();
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
					whseid:'',
					ipaddress:'',
					reportaddress:'',
					reportname:'',
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
								this.getFormmanagers();
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
						this.getFormmanagers();
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
				openuploadwin('Excel导入',this.url.ImportXls, "formmanagerList");
			},
			//初始化数据字典
			initDictsData:function(){
	        	var _this = this;
		   		_this.initDictByCode('warehouse',_this,'warehouseOptions');
		   		_this.initDictByCode('ipcode',_this,'ipcodeOptions');
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
			this.getFormmanagers();
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