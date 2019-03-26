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
		<el-row style="margin-top: 15px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
				<el-form-item style="margin-bottom: 8px;" prop="orderkey">
					<el-input v-model="filters.orderkey" auto-complete="off" placeholder="请输入出货单号"></el-input>
				</el-form-item>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getOrderss">查询</el-button>
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
		
		<!--列表-->
		<el-table :data="orderss" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" width="60"></el-table-column>
			<el-table-column prop="orderkey" label="出货单号" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="storerkey" label="货主代码" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="vendor" label="收货人代码" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="orderdate" label="订单时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="requestshipdate" label="请求出货时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="picker" label="拣货员" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="pickstartdate" label="拣货开始时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="pickenddate" label="拣货完成时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="labeler" label="贴标员" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="labelstartdate" label="贴标开始时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="labelenddate" label="贴标完成时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="reagents" label="复检员" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="reagentstartdate" label="复检开始时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="reagentenddate" label="复检完成时间" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDateTime"			></el-table-column>
			<el-table-column prop="orderstatus" label="状态" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="warehouse" label="仓库" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
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
					<el-form-item label="状态" prop="orderstatus">
						<el-input v-model="form.orderstatus" auto-complete="off" placeholder="请输入状态"></el-input>
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
		el:"#ordersList",
		data:function() {
			return {
				filters: {
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
					ImportXls:'${webRoot}/ordersController.do?upload'
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
				
				
				//数据字典 
			}
		},
		methods: {
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
				return !!cellValue?utilFormatDate(new Date(cellValue), 'yyyy-MM-dd hh:mm:ss'):'';
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
				let para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	orderkey:this.filters.orderkey,
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
							let para = Object.assign({}, this.form);
							
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
					let para = { ids: ids };
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