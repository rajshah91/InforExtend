<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>面单配置</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<t:base type="bootstrap,bootstrap-table,layer,validform,bootstrap-form"></t:base>
</head>
 <body style="overflow:hidden;overflow-y:auto;">
 <div class="container" style="width:100%;">
	<div class="panel-heading"></div>
	<div class="panel-body">
	<form class="form-horizontal" role="form" id="formobj" action="jasperconfigController.do?doAdd" method="POST">
		<input type="hidden" id="btn_sub" class="btn_sub"/>
		<input type="hidden" id="id" name="id"/>
		<div class="form-group">
			<label for="code" class="col-sm-3 control-label">代码：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="code" name="code" type="text" maxlength="32" class="form-control input-sm" placeholder="请输入代码"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="jasperfile" class="col-sm-3 control-label">jasperfile：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="jasperfile" name="jasperfile" type="text" maxlength="200" class="form-control input-sm" placeholder="请输入jasperfile"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="priorty" class="col-sm-3 control-label">优先级：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="priorty" name="priorty" type="text" maxlength="32" class="form-control input-sm" placeholder="请输入优先级"  datatype="n"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="matchrule" class="col-sm-3 control-label">匹配规则：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="matchrule" name="matchrule" type="text" maxlength="2000" class="form-control input-sm" placeholder="请输入匹配规则"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="dataquery" class="col-sm-3 control-label">数据源：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="dataquery" name="dataquery" type="text" maxlength="2000" class="form-control input-sm" placeholder="请输入数据源"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="afterjob" class="col-sm-3 control-label">后续操作：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="afterjob" name="afterjob" type="text" maxlength="2000" class="form-control input-sm" placeholder="请输入后续操作"  ignore="ignore" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="active" class="col-sm-3 control-label">是否启用：</label>
			<div class="col-sm-7">
				<div class="input-group" style="width:100%">
					<input id="active" name="active" type="text" maxlength="32" class="form-control input-sm" placeholder="请输入是否启用"  ignore="ignore" />
				</div>
			</div>
		</div>
	</form>
	</div>
 </div>
<script type="text/javascript">
var subDlgIndex = '';
$(document).ready(function() {
	//单选框/多选框初始化
	$('.i-checks').iCheck({
		labelHover : false,
		cursor : true,
		checkboxClass : 'icheckbox_square-green',
		radioClass : 'iradio_square-green',
		increaseArea : '20%'
	});
	
	//表单提交
	$("#formobj").Validform({
		tiptype:function(msg,o,cssctl){
			if(o.type==3){
				validationMessage(o.obj,msg);
			}else{
				removeMessage(o.obj);
			}
		},
		btnSubmit : "#btn_sub",
		btnReset : "#btn_reset",
		ajaxPost : true,
		beforeSubmit : function(curform) {
		},
		usePlugin : {
			passwordstrength : {
				minLen : 6,
				maxLen : 18,
				trigger : function(obj, error) {
					if (error) {
						obj.parent().next().find(".Validform_checktip").show();
						obj.find(".passwordStrength").hide();
					} else {
						$(".passwordStrength").show();
						obj.parent().next().find(".Validform_checktip").hide();
					}
				}
			}
		},
		callback : function(data) {
			var win = frameElement.api.opener;
			if (data.success == true) {
				frameElement.api.close();
			    win.reloadTable();
			    win.tip(data.msg);
			} else {
			    if (data.responseText == '' || data.responseText == undefined) {
			        $.messager.alert('错误', data.msg);
			        $.Hidemsg();
			    } else {
			        try {
			            var emsg = data.responseText.substring(data.responseText.indexOf('错误描述'), data.responseText.indexOf('错误信息'));
			            $.messager.alert('错误', emsg);
			            $.Hidemsg();
			        } catch (ex) {
			            $.messager.alert('错误', data.responseText + "");
			            $.Hidemsg();
			        }
			    }
			    return false;
			}
		}
	});
		
});
</script>
</body>
</html>