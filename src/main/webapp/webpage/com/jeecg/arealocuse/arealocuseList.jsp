<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>库位使用率</title>
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
	<div id="arealocuseList" v-cloak>
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
				<br>
				<el-form-item style="margin-bottom: 8px;" prop="selectdate">
					<el-date-picker type="date" placeholder="选择日期起" v-model="filters.selectdatestart" style="width:175px"></el-date-picker>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="selectdate">
					<el-date-picker type="date" placeholder="选择日期至" v-model="filters.selectdateend" style="width:175px"></el-date-picker>
				</el-form-item>
				<br>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getArealocuses">查询</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button icon="el-icon-refresh" @click="resetForm('filters')">重置</el-button>
			    </el-form-item>
			    <!-- <el-form-item>
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
						    <el-checkbox label="日期" v-model="detailList.selectdate" checked @change="show(detailList.selectdate)"></el-checkbox><br>
						    <el-checkbox label="分区" v-model="detailList.region" checked @change="show(detailList.region)"></el-checkbox><br>
						    <el-checkbox label="业务部" v-model="detailList.operatdepart" checked @change="show(detailList.operatdepart)"></el-checkbox><br>
						    <el-checkbox label="操作科" v-model="detailList.operatsection" checked @change="show(detailList.operatsection)"></el-checkbox><br>
						    <el-checkbox label="库区" v-model="detailList.area" checked @change="show(detailList.area)"></el-checkbox><br>
						    <el-checkbox label="大储位使用" v-model="detailList.bloc" checked @change="show(detailList.bloc)"></el-checkbox><br>
						    <el-checkbox label="小储位使用" v-model="detailList.sloc" checked @change="show(detailList.sloc)"></el-checkbox><br>
						    <el-checkbox label="总使用率" v-model="detailList.totalrate" checked @change="show(detailList.totalrate)"></el-checkbox><br>
						    <el-checkbox label="lpn数" v-model="detailList.lpn" checked @change="show(detailList.lpn)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="arealocuses" border stripe size="mini"  :span-method="objectSpanMethod" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
<!-- 			<el-table-column type="selection" width="55"></el-table-column> -->
<!-- 			<el-table-column type="index" label="序号" width="60"></el-table-column> -->
			<!-- <el-table-column prop="bpmStatus" label="流程状态" v-if="columnshow.bpmStatus" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column> -->
			<el-table-column prop="selectdate" label="日期" v-if="columnshow.selectdate" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatDate"></el-table-column>
			<el-table-column prop="region" label="分区" v-if="columnshow.region" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="operatdepart" label="业务部" v-if="columnshow.operatdepart" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="operatsection" label="操作科" v-if="columnshow.operatsection" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="area" label="库区" v-if="columnshow.area" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="bloc" label="大储位使用" v-if="columnshow.bloc" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="sloc" label="小储位使用" v-if="columnshow.sloc" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="totalrate" label="总使用率" v-if="columnshow.totalrate" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="lpn" label="lpn数" v-if="columnshow.lpn" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column label="操作" width="150">
				<template scope="scope">
<!-- 					<el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button> -->
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
					<el-form-item label="日期" prop="selectdate">
						<el-input v-model="form.selectdate" auto-complete="off" placeholder="请输入日期"></el-input>
					</el-form-item>
					<el-form-item label="分区" prop="region">
						<el-input v-model="form.region" auto-complete="off" placeholder="请输入分区"></el-input>
					</el-form-item>
					<el-form-item label="业务部" prop="operatdepart">
						<el-input v-model="form.operatdepart" auto-complete="off" placeholder="请输入业务部"></el-input>
					</el-form-item>
					<el-form-item label="操作科" prop="operatsection">
						<el-input v-model="form.operatsection" auto-complete="off" placeholder="请输入操作科"></el-input>
					</el-form-item>
					<el-form-item label="库区" prop="area">
						<el-input v-model="form.area" auto-complete="off" placeholder="请输入库区"></el-input>
					</el-form-item>
					<el-form-item label="大储位使用" prop="bloc">
						<el-input v-model="form.bloc" auto-complete="off" placeholder="请输入大储位使用"></el-input>
					</el-form-item>
					<el-form-item label="小储位使用" prop="sloc">
						<el-input v-model="form.sloc" auto-complete="off" placeholder="请输入小储位使用"></el-input>
					</el-form-item>
					<el-form-item label="总使用率" prop="totalrate">
						<el-input v-model="form.totalrate" auto-complete="off" placeholder="请输入总使用率"></el-input>
					</el-form-item>
					<el-form-item label="lpn数" prop="lpn">
						<el-input v-model="form.lpn" auto-complete="off" placeholder="请输入lpn数"></el-input>
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
		el:"#arealocuseList",
		data:function() {
			return {
				filters: {
					region:'',
					department:[],
					office:[],
					area:[],
					selectdatestart:'',
					selectdateend:'',
				},
				url:{
					list:'${webRoot}/arealocuseController.do?datagrid',
					del:'${webRoot}/arealocuseController.do?doDel',
					batchDel:'${webRoot}/arealocuseController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/arealocuseController.do?doAdd',
					edit:'${webRoot}/arealocuseController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/arealocuseController.do?exportXls&id=',
					ImportXls:'${webRoot}/arealocuseController.do?upload',
					findRegion:'${webRoot}/cargorealController.do?doFindRegion',
					findDepartment:'${webRoot}/cargorealController.do?doFindDepartment'
				},
				arealocuses: [],
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
					selectdate:'selectdate',
					region:'region',
					operatdepart:'operatdepart',
					operatsection:'operatsection',
					area:'area',
					bloc:'bloc',
					sloc:'sloc',
					totalrate:'totalrate',
					lpn:'lpn',
				},
				columnshow:{
					selectdate:true,
					region:true,
					operatdepart:true,
					operatsection:true,
					area:true,
					bloc:true,
					sloc:true,
					totalrate:true,
					lpn:true,
				},
				regions:[],
			    departments:[],
			    offices:[],
			    areas:[],
			    
			    rowList: [],
			    spanArr: [],
			    position: 0,
				//数据字典 
			}
		},
		methods: {
			rowspan:function() {
				 console.log("daio",this.arealocuses);
			  		this.arealocuses.map(function(item,index)  {
			  			console.log("daif",item);
			  			console.log("daif",index);
				    	if( index === 0){
				    		this.spanArr.push(1);
				    		this.position = 0;
				    	}else{
				    		console.log("dai2",item.region);
				    		console.log("dai3",this.arealocuses[index-1].region);
				    		if(this.arealocuses[index].region === this.arealocuses[index-1].region ){
				    			this.spanArr[this.position] += 1;
				    			this.spanArr.push(0);
				    			 console.log("dai",this.spanArr);
				    		}else{
				    			this.spanArr.push(1);
				    			this.position = index;
				    		}
				    	}
				    })
			  	},
			    objectSpanMethod:function( row, column, rowIndex, columnIndex ) {  //表格合并行
			    	//console.log("dai1",row.column);
			    	////console.log("dai2",row.columnIndex);
			    	//console.log("dai3",row.rowIndex);
			    	if(row.columnIndex === 1){
			    		//console.log("dai1",row);
			    		console.log("dai4",this.spanArr);
			            const _row = this.spanArr[row.rowIndex]
			            const _col = _row > 0 ? 1 : 0;
			            return {
			                rowspan: _row,
			                colspan: _col
			            }
			        }
			  },
		    //列展示切换
			show:function(value){
				if(value=="selectdate"){
				   this.columnshow.selectdate=!this.columnshow.selectdate;
				}
				if(value=="region"){
				   this.columnshow.region=!this.columnshow.region;
				}
				if(value=="operatdepart"){
				   this.columnshow.operatdepart=!this.columnshow.operatdepart;
				}
				if(value=="operatsection"){
				   this.columnshow.operatsection=!this.columnshow.operatsection;
				}
				if(value=="area"){
				   this.columnshow.area=!this.columnshow.area;
				}
				if(value=="bloc"){
				   this.columnshow.bloc=!this.columnshow.bloc;
				}
				if(value=="sloc"){
				   this.columnshow.sloc=!this.columnshow.sloc;
				}
				if(value=="totalrate"){
				   this.columnshow.totalrate=!this.columnshow.totalrate;
				}
				if(value=="lpn"){
				   this.columnshow.lpn=!this.columnshow.lpn;
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getArealocuses();
			},
			handleDownFile:function(type,filePath){
				var downUrl=this.url.downFile+ filePath +"?down=true";
				window.open(downUrl);
			},
			formatDate: function(row,column,cellValue, index){
// 				return !!cellValue?utilFormatDate(new Date(cellValue), 'yyyy-MM-dd'):'';
				return !!cellValue?cellValue.substring(0,10):'';
			},
			formatDateTime: function(row,column,cellValue, index){
// 				return !!cellValue?utilFormatDate(new Date(cellValue), 'yyyy-MM-dd hh:mm:ss'):'';
				return !!cellValue?cellValue.substring(0,10):'';
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getArealocuses();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getArealocuses();
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
			    this.$set(this.filters,"selectdatestart","");
			    this.$set(this.filters,"selectdateend","");
		        this.getArealocuses();
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
			getArealocuses:function() {
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
				fields.push('selectdate');
				fields.push('region');
				fields.push('operatdepart');
				fields.push('operatsection');
				fields.push('area');
				fields.push('bloc');
				fields.push('sloc');
				fields.push('totalrate');
				fields.push('lpn');
				var para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
						selectdatestart:!this.filters.selectdatestart ? '' : utilFormatDate(new Date(this.filters.selectdatestart), 'yyyy-MM-dd'),//请求出货时间起
						selectdateend:!this.filters.selectdateend ? '' : utilFormatDate(new Date(this.filters.selectdateend), 'yyyy-MM-dd'),//请求出货时间至
						regionlist:this.filters.region,
						departmentlist:this.filters.department.join(','),
						officelist:this.filters.office.join(','),
					 	arealist:this.filters.area.join(','),
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
					this.arealocuses = datas;
					this.listLoading = false;
					this.rowspan();
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
						this.getArealocuses();
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
					selectdate:'',
					region:'',
					operatdepart:'',
					operatsection:'',
					area:'',
					bloc:'',
					sloc:'',
					totalrate:'',
					lpn:'',
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
								this.getArealocuses();
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
						this.getArealocuses();
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
				openuploadwin('Excel导入',this.url.ImportXls, "arealocuseList");
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
			this.getArealocuses();
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