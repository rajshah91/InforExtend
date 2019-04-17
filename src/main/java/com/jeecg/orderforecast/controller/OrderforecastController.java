package com.jeecg.orderforecast.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.orderforecast.entity.OrderforecastEntity;
import com.jeecg.orderforecast.service.OrderforecastServiceI;
import com.jeecg.orders.entity.OrdersEntity;
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
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
 * @Description: 订单预估
 * @author onlineGenerator
 * @date 2019-04-01 15:50:55
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/orderforecastController")
public class OrderforecastController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OrderforecastController.class);

	@Autowired
	private OrderforecastServiceI orderforecastService;
	@Autowired
	private SystemService systemService;

	/**
	 * 订单预估列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/orderforecast/orderforecastList");
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
	public void datagrid(OrderforecastEntity orderforecast, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		// 界面参数
		String warehouse = request.getParameter("warehouse");
		String orderkey = request.getParameter("orderkey");
		String area = request.getParameter("area");
		String orderstatus = request.getParameter("orderstatus");
		String storerkey = request.getParameter("storerkey");
		String altsku = request.getParameter("altsku");
		String ordertype = request.getParameter("ordertype");
		String requestshipdatestart = request.getParameter("requestshipdatestart");
		String requestshipdateend = request.getParameter("requestshipdateend");
		int page = Integer.parseInt(request.getParameter("page"));
		int rows = Integer.parseInt(request.getParameter("rows"));
		String wh = "";
		String sqlwhere = "where 1=1 ";
		// 仓库
		if (warehouse != null && warehouse != "") {
			wh = typeNameToTypeCode(warehouse, "仓库");
		}
		// 订单
		if (orderkey != null && orderkey != "") {
			sqlwhere += " and o.orderkey='" + orderkey + "' ";
		}
		// 区域
		if (area != null && area != "") {
			sqlwhere += " and loc.physicalware='" + area + "'";
		}
		// 订单状态
		if (orderstatus != null && orderstatus != "") {
			sqlwhere += " and os.description='" + orderstatus + "'";
		}
		// 货主代码
		if (storerkey != null && storerkey != "") {
			sqlwhere += " and o.storerkey='" + storerkey + "'";
		}
		// 收货人代码
		if (altsku != null && altsku != "") {
			sqlwhere += " and s1.storerkey='" + altsku + "'";
		}
		// 业务类型
		if (ordertype != null && ordertype != "") {
			sqlwhere += " and cl.description='" + ordertype + "'";
		}
		// 请求出货时间
		if (requestshipdatestart != null && requestshipdatestart != "" && requestshipdateend != null
				&& requestshipdateend != "") {
			sqlwhere += " and o.requestedshipdate between to_date('" + requestshipdatestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + requestshipdateend + "','yyyy-MM-dd HH24:mi:ss')";
		}
		// sql拼接
		String sql = "select  o.whseid, " + "to_char(o.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss'), "
				+ "os.description orderstatus, " + "lp.physicalware, " + "o.orderkey, " + "s.susr3 Storer, "
				+ "s1.susr3 Vendor, " + "cl.description,  " + "count(distinct pk.sku), " + "count(distinct l.loc), "
				+ "count(distinct l2.loc), " + "count(distinct pk.id),o.performancedata01,  "
				+ "to_char(o.pickstartdate+8/24,'yyyy-MM-dd HH24:mi:ss'), "
				+ "o.monthtime / 60,to_char(o.pickstartdate + 1 / 3 + o.monthtime / 3600 / 24,'yyyy-MM-dd HH24:mi:ss'), "
				+ "to_char(o.pickstartdate + 1 / 3 + o.daytime / 3600 / 24,'yyyy-MM-dd HH24:mi:ss'),  "
				+ "to_char(o.pickenddate+8/24,'yyyy-MM-dd HH24:mi:ss')  " + "from " + wh + "_orders o " + "left join "
				+ wh + "_pickdetail pk on pk.orderkey = o.orderkey "
				+ "left join (select orderkey,listagg(to_char(physicalware),'\') within group(order by  physicalware) as physicalware from (select distinct  pd.orderkey,c2.description as physicalware "
				+ "          from " + wh + "_Pickdetail pd left join " + wh
				+ "_loc loc on pd.loc = loc.loc left join "+wh+"_Codelkup c2 on loc.physicalware=c2.code and c2.listname='PHYSICALWH'  where loc.physicalware is not null and pd.orderkey='0000003940' )group by orderkey) lp "
				+ "          on lp.orderkey=o.orderkey " + "left join " + wh
				+ "_loc l on pk.fromloc = l.loc and l.locnature <> 'S'   " + "left join " + wh
				+ "_loc l2 on pk.fromloc = l2.loc and l2.locnature = 'S' " + "left join " + wh
				+ "_storer s on o.storerkey = s.storerkey and s.type = '1'   " + "left join " + wh
				+ "_storer s1 on o.susr35 = s1.storerkey and s1.type = '1' " + "left join " + wh
				+ "_codelkup cl on o.type = cl.code and cl.listname = 'ORDERTYPE' " + "left join " + wh
				+ "_orderstatussetup os on os.code=o.status  "+sqlwhere+""
				+ " group by o.whseid,o.requestedshipdate,os.description,lp.physicalware,o.orderkey,s.susr3,s1.susr3,cl.description,o.performancedata01,o.pickstartdate,o.pickenddate,   o.pickstartdate,o.monthtime,o.daytime";

		if (warehouse != null && warehouse != "") {
			dataGrid = paging(sql, page, rows, dataGrid);
		}
		TagUtil.datagrid(response, dataGrid);
	}

	// 仓库字典code 转换
	private String typeNameToTypeCode(String typeName, String typegroupname) {
		List<TSTypegroup> tsTypegroup = systemService.findHql("from TSTypegroup where typegroupname=?", typegroupname);
		List<TSType> tsType = systemService.findHql("from TSType where TSTypegroup.id=? and typename=?",
				tsTypegroup.get(0).getId(), typeName);
		return tsType.get(0).getTypecode();
	}

	/**
	 * 获取分页数据
	 * 
	 * @param sql
	 * @param page
	 * @param rows
	 * @param dataGrid
	 * @return
	 */
	private DataGrid paging(String sql, int page, int rows, DataGrid dataGrid) {
		String totalSql = "select count(0) from (" + sql + ")";
		List<String> totalList = orderforecastService.findListbySql(totalSql);
		int total = Integer.parseInt(String.valueOf(totalList.get(0)));

		List<OrderforecastEntity> list = new ArrayList<>();
		String resultSql = pagingByOracle(sql, page, rows);
		List<Object[]> resultList = orderforecastService.findListbySql(resultSql);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] result : resultList) {
			OrderforecastEntity entity = new OrderforecastEntity();
			entity.setWarehouse(String.valueOf(result[0]));
			entity.setOrderstatus(String.valueOf(result[2]));
			entity.setArea(String.valueOf(result[3]));
			entity.setOrderkey(String.valueOf(result[4]));
			entity.setStorer(String.valueOf(result[5]));
			entity.setVendor(String.valueOf(result[6]));
			entity.setOrdertype(String.valueOf(result[7]));
			entity.setSkusum(String.valueOf(result[8]));
			entity.setBlocsum(String.valueOf(result[9]));
			entity.setSlocsum(String.valueOf(result[10]));
			entity.setLpnsum(String.valueOf(result[11]));
			entity.setPick(String.valueOf(result[12]));
			entity.setStdocdate(String.valueOf(result[14]));
			try {
				if (String.valueOf(result[1]) != "null") {
					entity.setRequestshipdate(sdf.parse(String.valueOf(result[1])));
				}
				if (String.valueOf(result[13]) != "null") {
					entity.setPickstartdate(sdf.parse(String.valueOf(result[13])));
				}
				if (String.valueOf(result[15]) != "null") {
					entity.setStdcompletedate(sdf.parse(String.valueOf(result[15])));
				}
				if (String.valueOf(result[16]) != "null") {
					entity.setNowcompletedate(sdf.parse(String.valueOf(result[16])));
				}
				if (String.valueOf(result[17]) != "null") {
					entity.setFactpickdate(sdf.parse(String.valueOf(result[17])));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(entity);
		}

		dataGrid.setPage(page);
		dataGrid.setRows(rows);
		dataGrid.setTotal(total);
		dataGrid.setResults(list);
		return dataGrid;
	}

	// null
	private String isnull(String value) {
		if ("null" == value) {
			value = "";
		}
		return value;
	}

	//
	private List<OrderforecastEntity> exportSQL(String sql) {

		List<OrderforecastEntity> list = new ArrayList<>();
		List<Object[]> resultList = orderforecastService.findListbySql(sql);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] result : resultList) {
			OrderforecastEntity entity = new OrderforecastEntity();
			entity.setWarehouse(isnull(String.valueOf(result[0])));
			entity.setOrderstatus(isnull(String.valueOf(result[2])));
			entity.setArea(isnull(String.valueOf(result[3])));
			entity.setOrderkey(isnull(String.valueOf(result[4])));
			entity.setStorer(isnull(String.valueOf(result[5])));
			entity.setVendor(isnull(String.valueOf(result[6])));
			entity.setOrdertype(isnull(String.valueOf(result[7])));
			entity.setSkusum(isnull(String.valueOf(result[8])));
			entity.setBlocsum(isnull(String.valueOf(result[9])));
			entity.setSlocsum(isnull(String.valueOf(result[10])));
			entity.setLpnsum(isnull(String.valueOf(result[11])));
			entity.setPick(isnull(String.valueOf(result[12])));
			entity.setStdocdate(isnull(String.valueOf(result[14])));
			try {
				if (String.valueOf(result[1]) != "null") {
					entity.setRequestshipdate(sdf.parse(String.valueOf(result[1])));
				}
				if (String.valueOf(result[13]) != "null") {
					entity.setPickstartdate(sdf.parse(String.valueOf(result[13])));
				}
				if (String.valueOf(result[15]) != "null") {
					entity.setStdcompletedate(sdf.parse(String.valueOf(result[15])));
				}
				if (String.valueOf(result[16]) != "null") {
					entity.setNowcompletedate(sdf.parse(String.valueOf(result[16])));
				}
				if (String.valueOf(result[17]) != "null") {
					entity.setFactpickdate(sdf.parse(String.valueOf(result[17])));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(entity);
		}
		return list;
	}

	/**
	 * 拼接分页sql
	 * 
	 * @param sql
	 * @param page
	 * @param pageSize
	 * @return
	 */
	private String pagingByOracle(String sql, int page, int pageSize) {
		// 当前页最大值
		int maxPage = page * pageSize;
		int minPage = (page - 1) * pageSize;
		String endSql = "select * from (" + "select t.*,rownum rn from (" + sql + ") t" + " where rownum<=" + maxPage
				+ ") where rn>" + minPage;
		return endSql;
	}

	/**
	 * 删除订单预估
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(OrderforecastEntity orderforecast, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		orderforecast = systemService.getEntity(OrderforecastEntity.class, orderforecast.getId());
		message = "订单预估删除成功";
		try {
			orderforecastService.delete(orderforecast);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单预估删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除订单预估
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "订单预估删除成功";
		try {
			for (String id : ids.split(",")) {
				OrderforecastEntity orderforecast = systemService.getEntity(OrderforecastEntity.class, id);
				orderforecastService.delete(orderforecast);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单预估删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加订单预估
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(OrderforecastEntity orderforecast, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "订单预估添加成功";
		try {
			orderforecastService.save(orderforecast);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单预估添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新订单预估
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(OrderforecastEntity orderforecast, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "订单预估更新成功";
		OrderforecastEntity t = orderforecastService.get(OrderforecastEntity.class, orderforecast.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(orderforecast, t);
			orderforecastService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "订单预估更新失败";
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
		req.setAttribute("controller_name", "orderforecastController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(OrderforecastEntity orderforecast, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		/*
		 * CriteriaQuery cq = new CriteriaQuery(OrderforecastEntity.class, dataGrid);
		 * org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
		 * orderforecast, request.getParameterMap()); List<OrderforecastEntity>
		 * orderforecasts = this.orderforecastService.getListByCriteriaQuery(cq,false);
		 */
		// 界面参数
		String warehouse = request.getParameter("warehouse");
		String orderkey = request.getParameter("orderkey");
		String area = request.getParameter("area");
		String orderstatus = request.getParameter("orderstatus");
		String storerkey = request.getParameter("storerkey");
		String altsku = request.getParameter("altsku");
		String ordertype = request.getParameter("ordertype");
		String requestshipdatestart = request.getParameter("requestshipdatestart");
		String requestshipdateend = request.getParameter("requestshipdateend");
		String wh = "";
		String sqlwhere = "where 1=1 ";
		// 仓库
		if (warehouse != null && warehouse != "") {
			wh = typeNameToTypeCode(warehouse, "仓库");
		}
		// 订单
		if (orderkey != null && orderkey != "") {
			sqlwhere += "o.orderkey='" + orderkey + "' ";
		}
		// 区域
		if (area != null && area != "") {
			sqlwhere += " and loc.physicalware='" + area + "'";
		}
		// 订单状态
		if (orderstatus != null && orderstatus != "") {
			sqlwhere += " and os.description='" + orderstatus + "'";
		}
		// 货主代码
		if (storerkey != null && storerkey != "") {
			sqlwhere += " and o.storerkey='" + storerkey + "'";
		}
		// 收货人代码
		if (altsku != null && altsku != "") {
			sqlwhere += " and s1.storerkey='" + altsku + "'";
		}
		// 业务类型
		if (ordertype != null && ordertype != "") {
			sqlwhere += " and cl.description='" + ordertype + "'";
		}
		// 请求出货时间
		if (requestshipdatestart != null && requestshipdatestart != "" && requestshipdateend != null
				&& requestshipdateend != "") {
			sqlwhere += " and o.requestedshipdate between to_date('" + requestshipdatestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + requestshipdateend + "','yyyy-MM-dd HH24:mi:ss')";
		}
		// sql拼接
		String sql = "select o.whseid, to_char(o.requestedshipdate,'yyyy-MM-dd HH24:mi:ss'), os.description orderstatus, loc.physicalware,o.orderkey,s.susr3 Storer,s1.susr3 Vendor,cl.description,"
				+ "  count(distinct pk.sku),count(distinct l.loc),count(distinct l2.loc),count(distinct pk.id),o.performancedata01,"
				+ "  to_char(o.pickstartdate,'yyyy-MM-dd HH24:mi:ss'),o.monthtime / 60,to_char(o.pickstartdate + 1 / 3 + o.monthtime / 3600 / 24,'yyyy-MM-dd HH24:mi:ss'),to_char(o.pickstartdate + 1 / 3 + o.daytime / 3600 / 24,'yyyy-MM-dd HH24:mi:ss'),"
				+ "  to_char(o.pickenddate,'yyyy-MM-dd HH24:mi:ss')  from " + wh + "_orders o left join " + wh
				+ "_pickdetail pk on pk.orderkey = o.orderkey " + "  left join " + wh
				+ "_loc loc on pk.loc = loc.loc  left join " + wh
				+ "_loc l on pk.fromloc = l.loc and l.locnature <> 'S' " + "  left join " + wh
				+ "_loc l2 on pk.fromloc = l2.loc and l2.locnature = 'S' left join " + wh
				+ "_storer s on o.storerkey = s.storerkey and s.type = '1' " + "  left join " + wh
				+ "_storer s1 on o.susr35 = s1.storerkey and s1.type = '1' left join " + wh
				+ "_codelkup cl on o.type = cl.listname and cl.listname = 'ORDERTYPE' " + "left join " + wh
				+ "_orderstatussetup os on os.code=o.status" + "  " + sqlwhere
				+ " group by o.whseid,o.requestedshipdate,os.description,loc.physicalware, "
				+ "  o.orderkey,s.susr3,s1.susr3,cl.description,o.performancedata01,o.pickstartdate,o.pickenddate, "
				+ "  o.pickstartdate,o.monthtime,o.daytime";
		List<OrderforecastEntity> orderforecasts = new ArrayList<>();
		if (warehouse != null && warehouse != "") {
			orderforecasts = exportSQL(sql);
		}
		modelMap.put(NormalExcelConstants.FILE_NAME, "订单预估");
		modelMap.put(NormalExcelConstants.CLASS, OrderforecastEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("订单预估列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, orderforecasts);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(OrderforecastEntity orderforecast, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "订单预估");
		modelMap.put(NormalExcelConstants.CLASS, OrderforecastEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("订单预估列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
				List<OrderforecastEntity> listOrderforecastEntitys = ExcelImportUtil.importExcel(file.getInputStream(),
						OrderforecastEntity.class, params);
				for (OrderforecastEntity orderforecast : listOrderforecastEntitys) {
					orderforecastService.save(orderforecast);
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
	 * 获取infor状态
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getorderstatus")
	@ResponseBody
	public void getorderstatus(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultjson = new JSONObject();
		try {
			List<Object[]> list = systemService.findListbySql("select o.description from W01_ORDERSTATUSSETUP o");
			resultjson.put("orderstatus", list);
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取infor业务类型
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getordertype")
	@ResponseBody
	public void getordertype(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultjson = new JSONObject();
		try {
			List<Object[]> list = systemService
					.findListbySql("select c.description from W01_Codelkup c where c.listname='ORDERTYPE'");
			resultjson.put("ordertype", list);
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// getInforCustomer

	/**
	 * 获取infor业务类型
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getInforStorerkey")
	@ResponseBody
	public void getInforCustomer(HttpServletRequest request, HttpServletResponse response) {
		/* JSONObject resultjson = new JSONObject(); */
		JSONArray array = new JSONArray();
		try {
			String warehouse = request.getParameter("warehouse");
			String storerkey = request.getParameter("storerkey");
			// 仓库
			if (warehouse != null && warehouse != "") {
				String wh = typeNameToTypeCode(warehouse, "仓库");
				List<Object[]> list = systemService.findListbySql(
						"select s.storerkey,s.company from W01_Storer s where s.type='1' and s.storerkey like '"
								+ storerkey + "%'");
				int i = 1;
				for (Object[] objects : list) {
					if (i <= 10) {
						com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
						object.put("storerkey", isnull(String.valueOf(objects[0])));
						object.put("company", isnull(String.valueOf(objects[1])));
						array.add(object);
					}
					i++;
				}
				response.getWriter().write(array.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
