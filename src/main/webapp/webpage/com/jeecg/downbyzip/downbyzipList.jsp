<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>收货异常图片下载</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name=viewportcontent=
	"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
<link rel="stylesheet"
	href="${webRoot}/plug-in/element-ui/css/index.css">
<link rel="stylesheet"
	href="${webRoot}/plug-in/element-ui/css/elementui-ext.css">
<script src="${webRoot}/plug-in/vue/vue.js"></script>
<script src="${webRoot}/plug-in/vue/vue-resource.js"></script>
<script src="${webRoot}/plug-in/vue/qrious.js"></script>
<script src="${webRoot}/plug-in/element-ui/index.js"></script>
<!-- Jquery组件引用 -->
<script src="${webRoot}/plug-in/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript"
	src="${webRoot}/plug-in/jquery-plugs/i18n/jquery.i18n.properties.js"></script>
<script type="text/javascript"
	src="${webRoot}/plug-in/mutiLang/zh-cn.js"></script>
<script type="text/javascript"
	src="${webRoot}/plug-in/lhgDialog/lhgdialog.min.js?skin=metrole"></script>
<script type="text/javascript"
	src="${webRoot}/plug-in/tools/curdtools.js"></script>
<style>
.toolbar {
	padding: 10px;
	margin: 10px 0;
}

.toolbar .el-form-item {
	margin-bottom: 10px;
}

.el-table__header tr th {
	padding: 3px 0px;
}

[v-cloak] {
	display: none
}
</style>
</head>
<body style="background-color: #FFFFFF;">
	<div id="downbyzipList" v-cloak>
		<!--工具条-->
		<el-row style="background-color: #eee; padding: 10px 10px 0 10px;">
		<el-form :inline="true" :model="filters" size="mini" ref="filters">
		<el-form-item style="margin-bottom: 8px;" prop="asn"> <el-input
			v-model="filters.asn" auto-complete="off" placeholder="请输入收货单号"
			style="width:175px"></el-input> </el-form-item> <el-form-item
			style="margin-bottom: 8px;" prop="lpn"> <el-input
			v-model="filters.lpn" auto-complete="off" placeholder="请输入lpn"
			style="width:175px"></el-input> </el-form-item> 
		<!-- <el-form-item> <el-button type="primary"
			icon="el-icon-search" v-on:click="getDownbyzips">查询</el-button> </el-form-item> <el-form-item>
		<el-button icon="el-icon-refresh" @click="resetForm('filters')">重置</el-button> 
		</el-form-item>--> <el-form-item> <el-button type="primary" icon="el-icon-download"
			@click="downloadzip">ZIP下载</el-button>  </el-form-item>
		</el-form> </el-row>
		
		<template>
			<div class="img" v-show="listShareShow">
			<qriously :value="feedbackqrcode" :size="160" :backgroundAlpha="backgroundAlpha" v-show="true"/>
			</div>
		</template>
			<!-- <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="handleAdd">新增</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="ExportXls">导出</el-button>
			    </el-form-item>
			    <el-form-item>
			    	<el-button type="primary" icon="el-icon-edit" @click="ImportXls">导入</el-button>
			    </el-form-item> --> </el-form> </el-row>
		<!-- <el-row style="padding: 10px;" size="mini">
			  <el-dropdown trigger="click" style="float:right" label-width="80px">
			          <el-button prop="primary"  size="mini" icon="el-icon-setting"></el-button>
					  <el-dropdown-menu slot="dropdown">
					  <template>
						  <el-checkbox-group v-model="checkList" label-width="100px">
						  </el-checkbox-group>
					  </template>
					  </el-dropdown-menu>
              </el-dropdown>
		</el-row> -->
		<!--列表-->
		<!-- <el-table :data="downbyzips" border stripe size="mini" highlight-current-row v-loading="listLoading" @sort-change="handleSortChange"  @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column type="index" width="60"></el-table-column>
			<el-table-column label="操作" width="150">
				<template scope="scope">
					<el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="mini" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table> -->
		<!-- <iframe width="100%" height="500px"
			src="http://172.30.19.14:80/ibmcognos/cgi-bin/cognos.cgi?b_action=cognosViewer&ui.action=run&ui.object=%2fcontent%2ffolder%5b%40name%3d%27WH1TEST%27%5d%2ffolder%5b%40name%3d%27infor%27%5d%2freport%5b%40name%3d%27%e6%94%b6%e8%b4%a7%e5%bc%82%e5%b8%b8%e6%8a%a5%e8%a1%a8TEST-WH1%27%5d"></iframe> -->
		<!--工具条-->
		<!-- <el-col :span="24" class="toolbar">
			<el-button type="danger" size="mini" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
			 <el-pagination small background @current-change="handleCurrentChange" @size-change="handleSizeChange" :page-sizes="[10, 20, 50, 100]"
      			:page-size="pageSize" :total="total" layout="sizes, prev, pager, next"  style="float:right;"></el-pagination>
		</el-col> -->
	</div>
</body>
<script>
	var vue = new Vue({			
		el:"#downbyzipList",
		data:function() {
			return {
				filters: {
					asn:'',
					lpn:'',
				},
				url:{
					downloadzip:'${webRoot}/downbyzipController.do?downLoadZipFile'
				},
				downbyzips: [],
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
					asn:'asn',
					lpn:'lpn',
				},
				columnshow:{
					asn:true,
					lpn:true,
				},
				
				feedbackqrcode:'http://172.20.70.32/ReceiptFeedBack.apk',
	            // 背景透明度，默认透明 0 
	            backgroundAlpha: 1,
	            listShareShow:true,
				//数据字典 
			}
		},
		methods: {
			//下载压缩文件
			downloadzip: function() {
					window.location.href = this.url.downloadzip+"&lpn="+this.filters.lpn+"&asn="+this.filters.asn;
			},
		},
		mounted:function() {
			
		}
	});
</script>
</html>