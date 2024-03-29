package com.jeecg.orderexpress.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.jeecgframework.web.system.pojo.base.TSUser;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.Util.JasperUtil;
import com.jeecg.basicdata.service.BasicDataServiceI;
import com.jeecg.jasperconfig.entity.JasperconfigEntity;
import com.jeecg.ncount.service.ScmNcountServiceI;
import com.jeecg.orderexpress.entity.OrderExpressEntity;
import com.jeecg.orderexpress.service.OrderExpressServiceI;
import com.jeecg.printconfig.entity.PrintconfigEntity;
import com.jeecg.webservice.InforWebService;

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
	@Autowired
	private InforWebService inforWebService;
	

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
		boolean success=true;
		AjaxJson j = new AjaxJson();
		orderExpress = systemService.getEntity(OrderExpressEntity.class, orderExpress.getId());
		message = "订单快递表删除成功";
		try {
			TSUser user = ResourceUtil.getSessionUser();// 操作人
//			List<OrderExpressEntity> expressEntities=orderExpressService.findHql("from OrderExpressEntity where uniqueCode=?", orderExpress.getUniqueCode()); 
//			if(expressEntities.size()<=1) {
				String sql="select o.storerkey from "+typeNameToTypeCode(orderExpress.getWarehouse(), "仓库")+"_orders o where o.orderkey='"+orderExpress.getOrderkey()+"'";
				List<Object[]> resultList = orderExpressService.findListbySql(sql);
				
				Map<String,String> orderkeys=new HashMap<String, String>();
				orderkeys.put(orderExpress.getOrderkey(), String.valueOf(resultList.get(0)));
				if(inforWebService.deleteKeyToInfor(orderExpress.getWarehouse(), user.getUserName(),orderkeys)) {
					orderExpressService.delete(orderExpress);
				}else {
					message = "订单快递表删除失败";
					success=false;
				}
			   /* if(orderExpressService.cancleOrderToExpress(orderExpress.getWarehouse(), orderExpress.getUniqueCode(), user.getUserName(), "取消订单")) {
			    	if(inforWebService.deleteKeyToInfor(orderExpress.getWarehouse(), user.getUserName(),orderkeys)) {
						orderExpressService.delete(orderExpress);
					}else {
						message = "订单快递表删除失败";
						success=false;
					}
			    }else {
			    	message = "订单取消失败";
					success=false;
			    }*/
				
//			}else {
//				message = "订单快递表删除失败";
//				success=false;
//			}
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单快递表删除失败";
			success=false;
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		j.setSuccess(success);
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
	 * 获取infor订单号
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
			String uniqueCode = request.getParameter("uniqueCode");
			String warehouse = request.getParameter("warehouse");
			String expressCompany = request.getParameter("expressCompany");
			String orderkeys = request.getParameter("orderkeys");
			String printer = request.getParameter("printer");

			// 规范订单号(排重)
			List<String> orderkeyList = getOrderKeyList(orderkeys);
			
			String resultMessage="";
			if(uniqueCode.isEmpty()) {
				//新增
				uniqueCode = companyCode + scmNcountService.getNextKey("uniqueCode", 10);
				resultMessage = orderExpressService.createOrderToExpress(warehouse, expressCompany, orderkeyList,
						uniqueCode, printer);
			}else {
				//添加
				resultMessage = orderExpressService.addOrderToExpress(orderkeyList,uniqueCode);	
			}
			
			result.put("message", resultMessage);
			if (resultMessage.equals("下单成功！")) {
				boolean flag=printJasperToFtp(warehouse, printer, uniqueCode, expressCompany);
				result.put("result", "success");
				result.put("uniqueCode", uniqueCode);
			} else {
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

	private List<String> getOrderKeyList(String orderkeys) {
		// TODO Auto-generated method stub
		List<String> orderkeyList = new ArrayList<>();
		if (orderkeys.endsWith(";")) {
			orderkeys = orderkeys.substring(0, orderkeys.length() - 1);
		}
		String[] orderkeyStr = orderkeys.split(";");
		for (String orderkey : orderkeyStr) {
			if (!orderkeyList.contains(orderkey)) {
				orderkeyList.add(orderkey);
			}
		}
		return orderkeyList;
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
		JSONObject resultjson = new JSONObject();
		try {
			String warehouse = request.getParameter("warehouse");
			String expressCompany = request.getParameter("expressCompany");
			if(expressCompany!=null&&expressCompany!="") {
				List<PrintconfigEntity> printconfigEntities = orderExpressService
						.findHql("from PrintconfigEntity where warehouse=? and note=?", warehouse,expressCompany);
				List<String> list = new ArrayList<>();
				for (PrintconfigEntity printconfigEntity : printconfigEntities) {
					list.add(printconfigEntity.getPrintname());
				}
				resultjson.put("printers", list);
			}else {
				List<PrintconfigEntity> printconfigEntities = orderExpressService
						.findHql("from PrintconfigEntity where warehouse=?", warehouse);

				List<String> list = new ArrayList<>();
				for (PrintconfigEntity printconfigEntity : printconfigEntities) {
					list.add(printconfigEntity.getPrintname());
				}
				resultjson.put("printers", list);
			}
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = "printJasper")
	@ResponseBody
	public void printJasper(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultjson = new JSONObject();
		try {
			String warehouse = request.getParameter("warehouse");
			String printername = request.getParameter("printername");
			//String mailno = request.getParameter("mailno");
			String uniqueCode = request.getParameter("uniqueCode");
			String expressCompany = request.getParameter("expressCompany");
			boolean flag=printJasperToFtp(warehouse,printername,uniqueCode,expressCompany);
			if(flag) {
				resultjson.put("result", "success");
			}
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean printJasperToFtp(String warehouse, String printername, String uniqueCode,
			String expressCompany) throws Exception {
		// TODO Auto-generated method stub
		//转换仓库code
		String wh=typeNameToTypeCode(warehouse, "仓库");
		PrintconfigEntity printconfigEntitie = (PrintconfigEntity) systemService
				.findHql("from PrintconfigEntity where warehouse=? and printname=?", warehouse, printername).get(0);
		List<JasperconfigEntity> jasperconfigList = systemService.findHql(
				"from JasperconfigEntity where code=? and active='1' order by priorty asc", expressCompany);
		JasperconfigEntity jasperconfig = null;
		for (JasperconfigEntity config : jasperconfigList) {
			String matchrule = config.getMatchrule();
			String sql = "select express_company,unique_code from order_express where warehouse='" + warehouse
					+ "' and unique_code='" + uniqueCode + "' and " + matchrule;
			List<Map<String, Object>> list = systemService.findForJdbc(sql);
			if (!list.isEmpty()) {
				jasperconfig = config;
				break;
			}
		}
		if (jasperconfig != null) {
			String _FORMAT = jasperconfig.getJasperfile();
			String ip = printconfigEntitie.getFtpaddress();
			String username = printconfigEntitie.getUsername();
			String password = printconfigEntitie.getPassword();
			String workdir = printconfigEntitie.getPath();
			/*String dataquery = jasperconfig.getDataquery().replaceAll(":=id", "'" + mailno + "' ");*/
			//wh
			String  dataquery = jasperconfig.getDataquery().replaceAll(":uniquerreqnumber", "'" + uniqueCode + "' ");
			dataquery = dataquery.replaceAll(":WH", "" + wh + "");
			List<Map<String, Object>> maplist = systemService.findForJdbc(dataquery);
			if(maplist.size()>0&&!maplist.isEmpty()) {
				JasperUtil.generageXMLAndDeliver(maplist, _FORMAT, printername, 1, ip, username, password, workdir);
				String afterjob = jasperconfig.getAfterjob();
				// TODO 打印后续工作，比如标记打印状态与时间
				List<OrderExpressEntity> expressEntities=orderExpressService.findHql("from OrderExpressEntity where uniqueCode=?", uniqueCode);
				for (OrderExpressEntity orderExpressEntity : expressEntities) {
					orderExpressEntity.setPrintCopies(orderExpressEntity.getPrintCopies()==null?1:orderExpressEntity.getPrintCopies()+1);
					orderExpressService.saveOrUpdate(orderExpressEntity);
				}
				return true;
			}
		}
		return false;
	}
}
