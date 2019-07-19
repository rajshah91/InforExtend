package com.jeecg.orderexpress.controller;

import java.awt.print.Printable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.ncount.service.ScmNcountServiceI;
import com.jeecg.orderexpress.entity.OrderExpressEntity;
import com.jeecg.orderexpress.service.OrderExpressServiceI;
import com.jeecg.printconfig.entity.PrintconfigEntity;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;

/**
 * @Title: Controller
 * @Description: 订单快递表
 * @author onlineGenerator
 * @date 2019-07-10 17:02:14
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/orderExpressController")
public class OrderExpressController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OrderExpressController.class);

	@Autowired
	private OrderExpressServiceI orderExpressService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private ScmNcountServiceI scmNcountService;

	private String companyCode = "02";

	/**
	 * 订单快递表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/orderexpress/orderExpressList");
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
	public void datagrid(OrderExpressEntity orderExpress, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OrderExpressEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, orderExpress,
				request.getParameterMap());
		cq.add();
		this.orderExpressService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除订单快递表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(OrderExpressEntity orderExpress, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		orderExpress = systemService.getEntity(OrderExpressEntity.class, orderExpress.getId());
		message = "订单快递表删除成功";
		try {
			orderExpressService.delete(orderExpress);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单快递表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除订单快递表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "订单快递表删除成功";
		try {
			for (String id : ids.split(",")) {
				OrderExpressEntity orderExpress = systemService.getEntity(OrderExpressEntity.class, id);
				orderExpressService.delete(orderExpress);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单快递表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加订单快递表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(OrderExpressEntity orderExpress, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "订单快递表添加成功";
		try {
			orderExpressService.save(orderExpress);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单快递表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新订单快递表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(OrderExpressEntity orderExpress, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "订单快递表更新成功";
		OrderExpressEntity t = orderExpressService.get(OrderExpressEntity.class, orderExpress.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(orderExpress, t);
			orderExpressService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单快递表更新失败";
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
		req.setAttribute("controller_name", "orderExpressController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(OrderExpressEntity orderExpress, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(OrderExpressEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, orderExpress,
				request.getParameterMap());
		List<OrderExpressEntity> orderExpresss = this.orderExpressService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "订单快递表");
		modelMap.put(NormalExcelConstants.CLASS, OrderExpressEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("订单快递表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, orderExpresss);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(OrderExpressEntity orderExpress, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "订单快递表");
		modelMap.put(NormalExcelConstants.CLASS, OrderExpressEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("订单快递表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
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
				List<OrderExpressEntity> listOrderExpressEntitys = ExcelImportUtil.importExcel(file.getInputStream(),
						OrderExpressEntity.class, params);
				for (OrderExpressEntity orderExpress : listOrderExpressEntitys) {
					orderExpressService.save(orderExpress);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(e.getMessage());
			} finally {
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
	 * 获取infor业务类型
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getorderkey")
	@ResponseBody
	public void getInforCustomer(HttpServletRequest request, HttpServletResponse response) {
		/* JSONObject resultjson = new JSONObject(); */
		JSONArray array = new JSONArray();
		try {
			String warehouse = request.getParameter("warehouse");
			String orderkey = request.getParameter("orderkey");
			// 仓库
			if (orderkey != null && !"".equals(orderkey)) {
				String wh = typeNameToTypeCode(warehouse, "仓库");
				String sql = "select  o.orderkey,o.status from " + wh + "_orders o where o.orderkey like '" + orderkey
						+ "%' and o.uniquerreqnumber is  null and o.mailno is  null order by o.orderkey";
				List<Object[]> list = systemService.findListbySql(sql);
				int j = 1;
				for (Object[] objects : list) {
					if (j <= 10) {
						com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
						object.put("orderkey", isnull(String.valueOf(objects[0])));
						array.add(object);
					}
					j++;
				}
				response.getWriter().write(array.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 仓库字典code 转换
	private String typeNameToTypeCode(String typeName, String typegroupname) {
		List<TSTypegroup> tsTypegroup = systemService.findHql("from TSTypegroup where typegroupname=?", typegroupname);
		List<TSType> tsType = systemService.findHql("from TSType where TSTypegroup.id=? and typename=?",
				tsTypegroup.get(0).getId(), typeName);
		return tsType.get(0).getTypecode();
	}

	// null
	private String isnull(String value) {
		if ("null" == value) {
			value = "";
		}
		return value;
	}

	/**
	 * 下单
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "createOrderToExpress")
	@ResponseBody
	public void createOrderToExpress(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();

		try {
			String warehouse = request.getParameter("warehouse");
			String expressCompany = request.getParameter("expressCompany");
			String orderkeys = request.getParameter("orderkeys");
			String printer = request.getParameter("printer");
			String uniqueCode = companyCode + scmNcountService.getNextKey("uniqueCode", 10);
			String resultMessage=orderExpressService.createOrderToExpress(warehouse, expressCompany, orderkeys, uniqueCode,printer);
			result.put("message", resultMessage);
			if(resultMessage.equals("下单成功！")) {
				result.put("result", "success");
			}else {
				result.put("result", "error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			response.getWriter().write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过仓库获取打印机
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getPrinterByWarehouse")
	@ResponseBody
	public void getPrinterByWarehouse(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultjson=new JSONObject();
		try {
			String warehouse = request.getParameter("warehouse");
			List<PrintconfigEntity> printconfigEntities=orderExpressService.findHql("from PrintconfigEntity where warehouse=?", warehouse);
			
			List<String> list=new ArrayList<>();
			for (PrintconfigEntity printconfigEntity : printconfigEntities) {
				list.add(printconfigEntity.getPrintname());
			}
			resultjson.put("printers", list);
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
