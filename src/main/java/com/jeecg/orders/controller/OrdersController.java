package com.jeecg.orders.controller;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.orders.entity.OrdersEntity;
import com.jeecg.orders.service.OrdersServiceI;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;

import org.jeecgframework.core.util.MyBeanUtils;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.core.util.ExceptionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**   
 * @Title: Controller  
 * @Description: 出货刷单
 * @author onlineGenerator
 * @date 2019-03-27 08:43:42
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/ordersController")
public class OrdersController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

	@Autowired
	private OrdersServiceI ordersService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 出货刷单列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/orders/ordersList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(OrdersEntity orders,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		JSONObject resultjson = new JSONObject();
		String warehouse= request.getParameter("warehouse");
		String orderkey=request.getParameter("orderkey");
		List<Object> data=new ArrayList<>();
		if(orderkey!=null&&orderkey!="") {
			if("FEILI_wmwhse1".equals(warehouse)) {
				data=ordersService.findListbySql("select o.whseid,o.orderkey,o.storerkey,o.susr35,to_char(o.adddate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.requestedshipdate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata01,to_char(o.pickstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.pickenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata02,to_char(o.labelingstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.labelingenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata04,to_char(o.recheckstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.recheckenddate+8/24,'yyyy-MM-dd hh:mm:ss'),c.description " + 
						"from W01_orders o " + 
						"left join W01_orderstatussetup c on c.code=o.status " + 
						"where o.orderkey='"+orderkey+"'");
			}else if("FEILI_wmwhse2".equals(warehouse)){
				data=ordersService.findListbySql("select o.whseid,o.orderkey,o.storerkey,o.susr35,to_char(o.adddate+8/24,'yyyy-MM-dd hh:mm:ss'),o.requestedshipdate,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata01,to_char(o.pickstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.pickenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata02,to_char(o.labelingstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.labelingenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata04,to_char(o.recheckstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.recheckenddate+8/24,'yyyy-MM-dd hh:mm:ss'),c.description " + 
						"from W02_orders o " + 
						"left join (select lottable06,orderkey from W01_orderdetail group by lottable06,orderkey) od on o.orderkey=od.orderkey " + 
						"left join W02_orderstatussetup c on c.code=o.status " + 
						"where o.orderkey='"+orderkey+"'");
			}else if("FEILI_wmwhse5".equals(warehouse)) {
				data=ordersService.findListbySql("select o.whseid,o.orderkey,o.storerkey,o.susr35,to_char(o.adddate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.requestedshipdate,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata01,to_char(o.pickstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.pickenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata02,to_char(o.labelingstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.labelingenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata04,to_char(o.recheckstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),to_char(o.recheckenddate+8/24,'yyyy-MM-dd hh:mm:ss'),c.description " + 
						"from W05_orders o " + 
						"left join (select lottable06,orderkey from W01_orderdetail group by lottable06,orderkey) od on o.orderkey=od.orderkey " + 
						"left join W05_orderstatussetup c on c.code=o.status " + 
						"where o.orderkey='"+orderkey+"'");
			}else if("FEILI_wmwhse10".equals(warehouse)) {
				data=ordersService.findListbySql("select o.whseid,o.orderkey,o.storerkey,o.susr35,to_char(o.adddate+8/24,'yyyy-MM-dd hh:mm:ss'),o.requestedshipdate,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata01,to_char(o.pickstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),o.pickenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata02,to_char(o.labelingstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),o.labelingenddate+8/24,'yyyy-MM-dd hh:mm:ss'), " + 
						"o.performancedata04,to_char(o.recheckstartdate+8/24,'yyyy-MM-dd hh:mm:ss'),o.recheckenddate+8/24,'yyyy-MM-dd hh:mm:ss'),c.description " + 
						"from W010_orders o " + 
						"left join (select lottable06,orderkey from W01_orderdetail group by lottable06,orderkey) od on o.orderkey=od.orderkey " + 
						"left join W010_orderstatussetup c on c.code=o.status " + 
						"where o.orderkey='"+orderkey+"'");
			}
		}
	    try {
	    	resultjson.put("orders", data);
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*dataGrid.setResults(data);
		TagUtil.datagrid(response, dataGrid);*/
	}
	
	
	/**
	 * 删除出货刷单
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(OrdersEntity orders, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		orders = systemService.getEntity(OrdersEntity.class, orders.getId());
		message = "出货刷单删除成功";
		try{
			ordersService.delete(orders);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "出货刷单删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除出货刷单
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出货刷单删除成功";
		try{
			for(String id:ids.split(",")){
				OrdersEntity orders = systemService.getEntity(OrdersEntity.class, 
				id
				);
				ordersService.delete(orders);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "出货刷单删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加出货刷单
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(OrdersEntity orders, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出货刷单添加成功";
		try{
			ordersService.save(orders);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "出货刷单添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新出货刷单
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(OrdersEntity orders, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出货刷单更新成功";
		OrdersEntity t = ordersService.get(OrdersEntity.class, orders.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(orders, t);
			ordersService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "出货刷单更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","ordersController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(OrdersEntity orders,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(OrdersEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, orders, request.getParameterMap());
		List<OrdersEntity> orderss = this.ordersService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"出货刷单");
		modelMap.put(NormalExcelConstants.CLASS,OrdersEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("出货刷单列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,orderss);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(OrdersEntity orders,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"出货刷单");
    	modelMap.put(NormalExcelConstants.CLASS,OrdersEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("出货刷单列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<OrdersEntity> listOrdersEntitys = ExcelImportUtil.importExcel(file.getInputStream(),OrdersEntity.class,params);
				for (OrdersEntity orders : listOrdersEntitys) {
					ordersService.save(orders);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(e.getMessage());
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	/**
	 * 获取名称
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getName")
	@ResponseBody
	public void getName(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultjson = new JSONObject();
		String account=request.getParameter("account");
		try {
			String name=ordersService.getName(account);
			resultjson.put("name", name);
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取名称
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getwarehouse")
	@ResponseBody
	public void getwarehouse(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultjson = new JSONObject();
		try {
			List<UsercontactwhEntity> entities=ordersService.getwarehouse();
			if(entities!=null&&entities.size()>0) {
				List<String> list=new ArrayList<>();
				for (UsercontactwhEntity u : entities) {
					list.add(u.getWarehouse().toString());
				}
				resultjson.put("warehouse", list);
			}
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证单号
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "valiorderkey")
	@ResponseBody
	public void valiorderkey(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultjson = new JSONObject();
		String warehouse=request.getParameter("warehouse");
		String orderkey=request.getParameter("orderkey");
		try {
			//切割字符串
			String str="";
			int length=0;
			while(length<orderkey.length()) {
				String s=orderkey.substring(length, length+10);
				length=length+10;
				str=str+s+"\n";
			}
			boolean success=ordersService.valiorderkey(orderkey, warehouse);
			resultjson.put("success", success);
			resultjson.put("orderkeys", str);
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 刷单操作
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "starton")
	@ResponseBody
	public void starton(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultjson = new JSONObject();
		String orderkeys=request.getParameter("orderkeys");
		String operation=request.getParameter("operation");
		String warehouse=request.getParameter("warehouse");
		String startorend=request.getParameter("startorend");
		String username=request.getParameter("username");
		try {
			String result="";
			if("1".equals(startorend)) {
				if("1".equals(operation)) {
					result=ordersService.starton(warehouse, "pick","start",username,orderkeys);
				}else if("2".equals(operation)){
					result=ordersService.starton(warehouse, "label","start",username,orderkeys);
				}else {
					result=ordersService.starton(warehouse, "recheck","start",username,orderkeys);
				}
			}else {
				if("1".equals(operation)) {
					result=ordersService.starton(warehouse, "pick","end",username,orderkeys);
				}else if("2".equals(operation)){
					result=ordersService.starton(warehouse, "label","end",username,orderkeys);
				}else {
					result=ordersService.starton(warehouse, "recheck","end",username,orderkeys);
				}
			}
			if(result!=null&&result!="") {
				resultjson.put("result", result);
			}else {
				resultjson.put("result", "操作成功");
			}
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
