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
<script src="${webRoot}/plug-in/vue/qrcode.js"></script>
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
		
<!-- 		<template> -->
<!-- 			<div class="img" v-show="listShareShow"> -->
<!-- 			<qriously :value="feedbackqrcode" :size="160" :backgroundAlpha="backgroundAlpha" v-show="true"/> -->
<!-- 			</div> -->
<!-- 		</template> -->
		<div>
			<div id="qrcode" style="margin: 10px 100px 10px"> 
			</div>
			<div style="margin: 10px 112px 10px">
			收货异常反馈 
			</div>
		</div>
			
	</div>
</body>
<script>
	var qrcode = null;
	var vue = new Vue({			
		el:"#downbyzipList",
		data:function() {
			return {
				filters: {
					asn:'',
					lpn:'',
				},
				url:{
					downloadzip:'${webRoot}/downbyzipController.do?downLoadZipFile',
					queryReceiptFeedBack:'${webRoot}/downbyzipController.do?queryReceiptFeedBackUrl'
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
				
				//数据字典 
			}
		},
		methods: {
			//下载压缩文件
			downloadzip: function() {
					window.location.href = this.url.downloadzip+"&lpn="+this.filters.lpn+"&asn="+this.filters.asn;
			},
			showQrcode: function () {
	            if (qrcode !== null) return
	            this.$http.get(this.url.queryReceiptFeedBack).then(function(res)  {
	            	 qrcode = new QRCode('qrcode', {
	 		            text: res.data.receiptFeedBackUrl,
	 		            width: 100,
	 		            height: 100,
	 		            colorDark: '#17233d',
	 		            colorLight: '#f8f8f9',
	 		            correctLevel: QRCode.CorrectLevel.H
	 		         })
				});
	           
	       }
		},
		mounted:function() {
			this.showQrcode();
		}
	});
</script>
</html>