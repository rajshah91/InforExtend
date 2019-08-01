<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>订单快递表</title>
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
	<!-- IE兼容 -->
<!-- 	<script src="https://cdn.bootcss.com/babel-polyfill/7.4.4/polyfill.min.js"></script> -->
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
	<div id="orderExpressList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
			<el-form :inline="true" :model="filters" size="mini" ref="filters">
				<el-form-item style="margin-bottom: 8px;" prop="warehouse">
			    <el-select v-model="filters.warehouse" v-model="warehouses" placeholder="请选择仓库" clearable style="width:175px">
	                 <el-option v-for="warehouse in warehouses"  :value="warehouse"></el-option>
	            </el-select>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="orderkey">
					<el-input v-model="filters.orderkey" auto-complete="off" placeholder="请输入出货单号" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="uniqueCode">
					<el-input v-model="filters.uniqueCode" auto-complete="off" placeholder="请输入唯一编码" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="billCode">
					<el-input v-model="filters.billCode" auto-complete="off" placeholder="请输入快递单号" style="width:175px"></el-input>
				</el-form-item>
				<el-form-item style="margin-bottom: 8px;" prop="expressCompany">
					<el-select v-model="filters.expressCompany" placeholder="请选择快递公司" style="width:175px">
				      <el-option :label="option.typename" :value="option.typecode" v-for="option in express_coOptions"></el-option>
				    </el-select>
				</el-form-item>
			    <br>
				<el-form-item>
			    	<el-button type="primary" icon="el-icon-search" v-on:click="getOrderExpresss">查询</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button icon="el-icon-refresh" @click="resetForm('filters')">重置</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" v-on:click="createOrderExpresss">下单</el-button>
			    </el-form-item>
<!-- 			    <el-form-item> -->
<!-- 			    	<el-button type="primary" icon="el-icon-edit" @click="handleAdd">新增</el-button> -->
<!-- 			    </el-form-item> -->
<!-- 			    <el-form-item> -->
<!-- 			    	<el-button type="primary" icon="el-icon-edit" @click="ExportXls">导出</el-button> -->
<!-- 			    </el-form-item> -->
<!-- 			    <el-form-item> -->
<!-- 			    	<el-button type="primary" icon="el-icon-edit" @click="ImportXls">导入</el-button> -->
<!-- 			    </el-form-item> -->
			</el-form>
		</el-row>
		<el-row style="padding: 10px;" size="mini">
			  <el-dropdown trigger="click" style="float:right" label-width="80px">
			          <el-button prop="primary"  size="mini" icon="el-icon-setting"></el-button>
					  <el-dropdown-menu slot="dropdown">
					  <template>
						  <el-checkbox-group v-model="checkList" label-width="100px">
						    <el-checkbox label="仓库" v-model="detailList.warehouse" checked @change="show(detailList.warehouse)"></el-checkbox><br>
						    <el-checkbox label="出货单号" v-model="detailList.orderkey" checked @change="show(detailList.orderkey)"></el-checkbox><br>
						    <el-checkbox label="唯一编码" v-model="detailList.uniqueCode" checked @change="show(detailList.uniqueCode)"></el-checkbox><br>
						    <el-checkbox label="快递单号" v-model="detailList.billCode" checked @change="show(detailList.billCode)"></el-checkbox><br>
						    <el-checkbox label="快递公司" v-model="detailList.expressCompany" checked @change="show(detailList.expressCompany)"></el-checkbox><br>
						    <el-checkbox label="打印份数" v-model="detailList.printCopies" checked @change="show(detailList.printCopies)"></el-checkbox><br>
						    <el-checkbox label="打印机" v-model="detailList.printer" checked @change="show(detailList.printer)"></el-checkbox><br>
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row>
		<!--列表-->
		<el-table :data="orderExpresss"  border stripe size="mini" highlight-current-row @cell-dblclick="changePrinter" v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" label="序号" width="60"></el-table-column>
			<el-table-column prop="warehouse" label="仓库" v-if="columnshow.warehouse" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="orderkey" label="出货单号" v-if="columnshow.orderkey" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="uniqueCode" label="唯一编码" v-if="columnshow.uniqueCode" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="billCode" label="快递单号" v-if="columnshow.billCode" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="expressCompany" label="快递公司" v-if="columnshow.expressCompany" min-width="120" sortable="custom" show-overflow-tooltip :formatter="formatExpress_coDict"></el-table-column>
			<el-table-column prop="printCopies" label="打印份数" v-if="columnshow.printCopies" min-width="120" sortable="custom" show-overflow-tooltip></el-table-column>
			<el-table-column prop="printer" label="打印机" v-if="columnshow.printer"  min-width="120" sortable="custom" show-overflow-tooltip>
			   <template scope="scope">
			      <el-select v-if="editprinter" v-model="scope.row.printer" v-model="editprinters" size="mini" placeholder="请选择打印机" @change="editprint(scope.$index,scope.row)"  clearable style="width:175px">
			            <el-option v-for="printer in editprinters"  :value="printer" ></el-option>
			       </el-select>
			       <span v-if="!editprinter" >{{scope.row.printer}}</span>
			   </template>
			</el-table-column>
			<el-table-column label="操作" width="240">
				<template scope="scope">
					<!-- <el-button size="mini" @click="editOrderExpresss(scope.$index, scope.row)">添加</el-button> -->
 					<el-button size="mini" @click="handlePrint(scope.$index, scope.row)">打印</el-button>
<!-- 					<el-button type="danger" size="mini" @click="handleDel(scope.$index, scope.row)">删除</el-button> -->
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
			<el-form :model="form" label-width="80px" :rules="formRules" ref="form" size="mini" inline="true" style="background-color: #eee; padding: 10px 10px 0 10px;">
					<el-form-item label="唯一编码" prop="uniqueCode">
						<el-input v-model="form.uniqueCode" auto-complete="off" placeholder="请输入唯一编码" :disabled="true"></el-input>
					</el-form-item>
					<el-form-item label="仓库" prop="warehouse">
					    <el-select v-model="form.warehouse" v-model="warehouses" placeholder="请选择仓库" :disabled="state.warehouse"  @change="findPrinterByWarehouse()" clearable style="width:175px">
			                 <el-option v-for="warehouse in warehouses"  :value="warehouse"></el-option>
			            </el-select>
						<!-- <el-input v-model="form.warehouse" auto-complete="off" placeholder="请输入仓库"></el-input> -->
					</el-form-item>
					<el-form-item label="快递公司">
						<el-select v-model="form.expressCompany" placeholder="请选择快递公司" :disabled="state.expressCompany" style="width:175px" @change="expressChange(form.expressCompany)">
					      <el-option :label="option.typename" :value="option.typecode" v-for="option in express_coOptions" ></el-option>
					    </el-select>
					   <!--  <el-input v-if="station"  style="width:175px"></el-input> -->
					</el-form-item>
					<!-- <el-form-item>
					    <el-select v-if="station"  v-model="senderStation" placeholder="请选择中通网点" style="width:175px">
					      <el-option :label="option.typename" :value="option.typecode" v-for="option in station_coOptions" ></el-option>
					    </el-select>
					</el-form-item> -->
					<el-form-item label="打印机">
						<el-select v-model="form.printer" v-model="printers" placeholder="请选择打印机" :disabled="state.printer" clearable style="width:175px">
			                 <el-option v-for="printer in printers"  :value="printer"></el-option>
			            </el-select>
					</el-form-item>
					<br>
					<el-form-item label="出货单号" prop="orderkey">
					  <el-select v-model="form.orderkey" name="orderkey"  v-on:input="valiorderkey(form.orderkey)" clearable
							default-first-option="true" filterable remote reserve-keyword placeholder="请输入出货单号"
							:remote-method="orderkeyQuery" :loading="orderkey_loading" style="width: 175px;">
							<el-option
								v-for="item in orderkey_select"
								:key="item.value"
								:label="item.value"
								:value="item.value">
								 <span style="float: left;margin-right:10px">{{ item.value }}</span>
                                 <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label }}</span>
							</el-option>
						</el-select>
						<el-input v-if="false" v-model="orderno"></el-input>
					</el-form-item>
					<el-form-item label="单号展示">
	                 <el-input 
						  type="textarea"
						  autosize
						  readonly
						  placeholder="请输入内容"
						  v-model="form.orderkeys">
					 </el-input>
					 </el-form-item>
					 <el-form-item>
					 	<el-button type="primary" @click="remove()">移除</el-button>
					 </el-form-item>
					 
<!-- 					<el-form-item label="唯一编码" prop="uniqueCode"> -->
<!-- 						<el-input v-model="form.uniqueCode" auto-complete="off" placeholder="请输入唯一编码"></el-input> -->
<!-- 					</el-form-item> -->
<!-- 					<el-form-item label="快递单号" prop="billCode"> -->
<!-- 						<el-input v-model="form.billCode" auto-complete="off" placeholder="请输入快递单号"></el-input> -->
<!-- 					</el-form-item> -->
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click.native="submitCreateOrder" :loading="formLoading">提交</el-button>
				<el-button @click.native="formVisible = false;station=false;">取消</el-button>
<!-- 				<el-button type="primary" @click.native="formSubmit" :loading="formLoading">提交</el-button> -->
			</div>
		</el-dialog>
		<!--韵达报表界面-->
		<el-dialog  fullscreen z-index="800" :visible.sync="YDVisible" :close-on-click-modal="false">
			<!-- <el-form :model="form" label-width="80px" :rules="formRules" ref="form" size="mini" inline="true" style="background-color: #eee; padding: 10px 10px 0 10px;"> -->
		    <iframe id="YUNDA" width="100%" height="800px" src=''></iframe>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="YDVisible = false">取消</el-button>
<!-- 				<el-button type="primary" @click.native="formSubmit" :loading="formLoading">提交</el-button> -->
			</div>
			<!-- </el-form> -->
		</el-dialog>
	</div>
</body>
<script>
	var vue = new Vue({			
		el:"#orderExpressList",
		data:function() {
			return {
				filters: {
					warehouse:'',
					orderkey:'',
					uniqueCode:'',
					billCode:'',
					expressCompany:'',
				},
				url:{
					list:'${webRoot}/orderExpressController.do?datagrid',
					del:'${webRoot}/orderExpressController.do?doDel',
					batchDel:'${webRoot}/orderExpressController.do?doBatchDel',
					queryDict:'${webRoot}/systemController.do?typeListJson',
					save:'${webRoot}/orderExpressController.do?doAdd',
					edit:'${webRoot}/orderExpressController.do?doUpdate',
					upload:'${webRoot}/systemController/filedeal.do',
					downFile:'${webRoot}/img/server/',
					exportXls:'${webRoot}/orderExpressController.do?exportXls&id=',
					ImportXls:'${webRoot}/orderExpressController.do?upload',
					getwarehouse:'${webRoot}/ordersController.do?getwarehouse',
					getorderkey:'${webRoot}/orderExpressController.do?getorderkey',
				    createOrderToExpress:'${webRoot}/orderExpressController.do?createOrderToExpress',
				    findPrinterByWarehouse:'${webRoot}/orderExpressController.do?getPrinterByWarehouse',
				    printexpress:'${webRoot}/orderExpressController.do?printJasper'
				},
				editprinter:false,
				urlprint:'',
				//出货单动态选择
				listLoading: false,
				orderkey_loading:false,
				orderkey_select:[],
				
				orderno:"",
				
				warehouses:[],
				printers:[],
				editprinters:[],
				printer:"",
				orderExpresss: [],
				total: 0,
				page: 1,
				pageSize:10,
				sort:{
					sort:'id',
					order:'desc'
				},
				listLoading: false,
				sels: [],//列表选中列
				//韵达
				YDVisible: false,//表单界面是否显示
				station:false,
				senderStation:'',
				
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
					warehouse:'warehouse',
					orderkey:'orderkey',
					uniqueCode:'uniqueCode',
					billCode:'billCode',
					expressCompany:'expressCompany',
					printCopies:'printCopies',
					printer:'printer',
				},
				columnshow:{
					warehouse:true,
					orderkey:true,
					uniqueCode:true,
					billCode:true,
					expressCompany:true,
					printCopies:true,
					printer:true
				},
				
				state:{
					warehouse:false,
					expressCompany:false,
					printer:false,	
				},
				//数据字典 
		   		express_coOptions:[],
		   		station_coOptions:[],
			}
		},
		methods: {
			expressChange:function(value){
				if("ZTO"==value){
					this.station=true;
				}else{
					this.station=false;
				}
			},
			handlePrint:function(index,row){
				if("YUNDA"==row.expressCompany){
			          //this.YDVisible=true;
			          this.urlprint="http://172.20.70.249/cognos8/cgi-bin/cognos.cgi?b_action=cognosViewer&run.prompt=false&ui.action=run&ui.object=%2fcontent%2ffolder%5b%40name%3d%273-%e7%b3%bb%e7%bb%9f%e6%9f%a5%e8%af%a2%e6%8a%a5%e8%a1%a8%27%5d%2ffolder%5b%40name%3d%27%e6%98%86%e5%b1%b1%27%5d%2ffolder%5b%40name%3d%27WH6%27%5d%2freport%5b%40name%3d%27%e9%9f%b5%e8%be%be%e5%bf%ab%e9%80%92%e5%8d%95-WH6%27%5d&p_uniqueCode="+row.uniqueCode+"";
			          window.open(this.urlprint);
			       }else{
					this.$http.get(this.url.printexpress,{params:{warehouse:row.warehouse,printername:row.printer,mailno:row.billCode,uniqueCode:row.uniqueCode,expressCompany:row.expressCompany}}).then(function(res)  {
						if(res.data.result=='success'){
							this.$message({
								message: "打印成功！",
								type: 'success',
								duration:1500
							});
						}else{
							this.$message({
								message: "打印失败！",
								type: 'error',
								duration:1500
							});
						};		
					});
			       }
			},
			editprint:function(index,row){
				this.orderExpresss[index].printer=row.printer;
				this.editprinter=!this.editprinter;
			},
			changePrinter:function(row,column,event,cell){
				this.findPrinter(row.warehouse);
				if("打印机"==column.label){
					this.editprinter=!this.editprinter;
				}
			},
			//编号查询货主的信息
			orderkeyQuery: function(query) {
				if (query !== ''&&this.form.warehouse!=='') {
					this.orderkey_loading = true;
			        this.$http.get(this.url.getorderkey,{params:{orderkey:query,warehouse:this.form.warehouse}}).then(function(res)  {
			            this.orderkey_loading = false;
						if(res.data.length==0){
							 this.orderkey_select = [];
						}else{
							this.orderkey_loading = false;
							this.orderkey_select = res.data.map(function(d) {
								return {
									value: d.orderkey, 
									/* label: d.company */
								};
							}.bind(this));
						}
					});
			    } else {
			          this.orderkey_select = [];
			    }
                /* this.valiorderkey(query); */
			},
			getwarehouse:function(){
				this.$http.get(this.url.getwarehouse).then(function(res)  {
					/* console.log(res.data.warehouse); */
					this.warehouses=res.data.warehouse;
				});
			},
			findPrinter:function(value){
				this.$http.get(this.url.findPrinterByWarehouse,{params:{warehouse:value}}).then(function(res)  {
					this.editprinters=res.data.printers;
				});
			},
			findPrinterByWarehouse:function(){
				this.form.printer='';
				this.$http.get(this.url.findPrinterByWarehouse,{params:{warehouse:this.form.warehouse}}).then(function(res)  {
					this.printers=res.data.printers;
				});
			},
			//移除textarea中 的值
			remove:function(){
				this.form.orderkeys=this.form.orderkeys.substring(0,this.form.orderkeys.length-11);
			},
			valiorderkey:function(value){
				if(value!=null&&value!=""&&value.length==10){
					this.form.orderkeys=this.form.orderkeys+value+"\n";
					this.form.orderkey='';
				}
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
				if(value=="uniqueCode"){
				   this.columnshow.uniqueCode=!this.columnshow.uniqueCode;
				}
				if(value=="billCode"){
				   this.columnshow.billCode=!this.columnshow.billCode;
				}
				if(value=="expressCompany"){
				   this.columnshow.expressCompany=!this.columnshow.expressCompany;
				}
				if(value=="printCopies"){
				   this.columnshow.printCopies=!this.columnshow.printCopies;
				}
				if(value=="printer"){
				   this.columnshow.printer=!this.columnshow.printer;
				}
			},
			handleSortChange:function(sort){
				this.sort={
					sort:sort.prop,
					order:sort.order=='ascending'?'asc':'desc'
				};
				this.getOrderExpresss();
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
			formatExpress_coDict: function(row,column,cellValue, index){
				var names="";
				var values=cellValue;
				if(!Array.isArray(cellValue))values=cellValue.split(',');
				for (var i = 0; i < values.length; i++) {
					var value = values[i];
					if(i>0)names+=",";
					for(var j in this.express_coOptions){
						var option=this.express_coOptions[j];
						if(value==option.typecode){
							names+=option.typename;
						}
					}
				}
				return names;
			},
			formatstation_coOptions: function(row,column,cellValue, index){
				var names="";
				var values=cellValue;
				if(!Array.isArray(cellValue))values=cellValue.split(',');
				for (var i = 0; i < values.length; i++) {
					var value = values[i];
					if(i>0)names+=",";
					for(var j in this.station_coOptions){
						var option=this.station_coOptions[j];
						if(value==option.typecode){
							names+=option.typename;
						}
					}
				}
				return names;
			},
			handleCurrentChange:function(val) {
				this.page = val;
				this.getOrderExpresss();
			},
			handleSizeChange:function(val) {
				this.pageSize = val;
				this.page = 1;
				this.getOrderExpresss();
			},
			resetForm:function(formName) {
		        this.$refs[formName].resetFields();
		        this.getOrderExpresss();
		    },
			//获取用户列表
			getOrderExpresss:function() {
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
				fields.push('uniqueCode');
				fields.push('billCode');
				fields.push('expressCompany');
				fields.push('printCopies');
				fields.push('printer');
				let para = {
					params: {
						page: this.page,
						rows: this.pageSize,
						//排序
						sort:this.sort.sort,
						order:this.sort.order,
					 	warehouse:this.filters.warehouse,
					 	orderkey:this.filters.orderkey,
					 	uniqueCode:this.filters.uniqueCode,
					 	billCode:this.filters.billCode,
					 	expressCompany:this.filters.expressCompany,
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
					this.orderExpresss = datas;
					this.listLoading = false;
				});
			},
			
			 //删除
			handleDel: function (index, row) {
				var t=this;
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(function(){
					t.listLoading = true;
					t.$http.get(t.url.del,{params:{ id: row.id }}).then(function(res){
						t.listLoading = false;
						if(res.body.success){
							t.$message({
								message: res.body.msg,
								type: 'success',
								duration:1500
							});
						}else{
							t.$message({
								message: res.body.msg,
								type: 'error',
								duration:1500
							});
						};
						t.getOrderExpresss();
					});
				}).catch(function(){
					
				});
			}, 
			
			//显示编辑界面
			handleEdit: function (index, row) {
				this.form.warehouse='';
				this.formTitle='编辑';
				this.formVisible = true;
				this.form = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {
				this.form.warehouse='';
				this.formTitle='新增';
				this.formVisible = true;
				this.form = {
					uniqueCode:'',
					warehouse:'',
					orderkey:'',
					uniqueCode:'',
					billCode:'',
					expressCompany:'',
				};
			},
			//显示下单界面
			createOrderExpresss: function () {
				this.formTitle='下单';
				this.formVisible = true;
				this.form = {
					warehouse:'',
					orderkey:'',
					orderkeys:'',
 					uniqueCode:'',
					expressCompany:'',
					printer:'',
				};
				this.state={
					warehouse:false,
					expressCompany:false,
					printer:false,	
				};
			},
			editOrderExpresss: function (index,row) {
				this.formTitle='下单';
				this.formVisible = true;
				this.form = {
					warehouse:row.warehouse,
					orderkey:'',
					orderkeys:'',
 					uniqueCode:row.uniqueCode,
					expressCompany:row.expressCompany,
					printer:row.printer,
				};
				this.state={
					warehouse:true,
					expressCompany:true,
					printer:true,	
				};
			},
			
			//下单
			submitCreateOrder: function () {
				if(this.form.warehouse==''){
					this.$message({
						message: '请选择仓库!',
						type: 'warning',
						duration:1500
					})
				}else if(this.form.expressCompany==''){
					this.$message({
						message: '请选择快递公司!',
						type: 'warning',
						duration:1500
					})
				}else if(this.form.orderkeys==''){
					this.$message({
						message: '请选择出库单号！',
						type: 'warning',
						duration:1500
					})
				}else if(this.form.printer==''){
					this.$message({
						message: '请选择打印机！',
						type: 'warning',
						duration:1500
					})
				}else{
					this.formLoading = true;
					this.$http.get(this.url.createOrderToExpress,{
						params:{
							uniqueCode:this.form.uniqueCode,
							warehouse:this.form.warehouse,
							expressCompany:this.form.expressCompany,
							orderkeys:this.form.orderkeys.replace(new RegExp("\n","gm"),";"),
							printer:this.form.printer
							}
					}).then(function(res)  {
						this.formLoading = false;
						if(res.data.result=='success'){
							this.$message({
								message: res.data.message,
								type: 'success',
								duration:1500
							});
							if("YUNDA"==this.form.expressCompany){
						          //this.YDVisible=true;
						          this.urlprint="http://172.20.70.249/cognos8/cgi-bin/cognos.cgi?b_action=cognosViewer&run.prompt=false&ui.action=run&ui.object=%2fcontent%2ffolder%5b%40name%3d%273-%e7%b3%bb%e7%bb%9f%e6%9f%a5%e8%af%a2%e6%8a%a5%e8%a1%a8%27%5d%2ffolder%5b%40name%3d%27%e6%98%86%e5%b1%b1%27%5d%2ffolder%5b%40name%3d%27WH6%27%5d%2freport%5b%40name%3d%27%e9%9f%b5%e8%be%be%e5%bf%ab%e9%80%92%e5%8d%95-WH6%27%5d&p_uniqueCode="+res.data.uniqueCode+"";
						          window.open(this.urlprint);
						       };
						}else{
							this.$message({
								message: res.data.message,
								type: 'error',
								showClose:true
							});
						};
					});
				}
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
								this.getOrderExpresss();
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
						this.getOrderExpresss();
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
				openuploadwin('Excel导入',this.url.ImportXls, "orderExpressList");
			},
			//初始化数据字典
			initDictsData:function(){
	        	var _this = this;
		   		_this.initDictByCode('express_co',_this,'express_coOptions');
		   		_this.initDictByCode('ZTOstation',_this,'station_coOptions');
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
			this.getOrderExpresss();
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