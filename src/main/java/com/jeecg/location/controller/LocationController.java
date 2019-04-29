package com.jeecg.location.controller;

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
import org.jeecgframework.web.system.pojo.base.TSDepart;
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
import com.jeecg.location.entity.LocationEntity;
import com.jeecg.location.service.LocationServiceI;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;

/**
 * @Title: Controller
 * @Description: 库位表
 * @author onlineGenerator
 * @date 2019-04-29 09:43:13
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/locationController")
public class LocationController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	private LocationServiceI locationService;
	@Autowired
	private SystemService systemService;

	/**
	 * 库位表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/location/locationList");
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
	public void datagrid(LocationEntity location, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(LocationEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, location, request.getParameterMap());
		cq.add();
		this.locationService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除库位表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(LocationEntity location, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		location = systemService.getEntity(LocationEntity.class, location.getId());
		message = "库位表删除成功";
		try {
			locationService.delete(location);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "库位表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除库位表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "库位表删除成功";
		try {
			for (String id : ids.split(",")) {
				LocationEntity location = systemService.getEntity(LocationEntity.class, id);
				locationService.delete(location);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "库位表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加库位表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(LocationEntity location, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "库位表添加成功";
		try {
			locationService.save(location);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "库位表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新库位表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(LocationEntity location, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "库位表更新成功";
		LocationEntity t = locationService.get(LocationEntity.class, location.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(location, t);
			locationService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "库位表更新失败";
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
		req.setAttribute("controller_name", "locationController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(LocationEntity location, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(LocationEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, location, request.getParameterMap());
		List<LocationEntity> locations = this.locationService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "库位表");
		modelMap.put(NormalExcelConstants.CLASS, LocationEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("库位表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, locations);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(LocationEntity location, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "库位表");
		modelMap.put(NormalExcelConstants.CLASS, LocationEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("库位表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
				List<LocationEntity> listLocationEntitys = ExcelImportUtil.importExcel(file.getInputStream(),
						LocationEntity.class, params);
				for (LocationEntity location : listLocationEntitys) {
					locationService.save(location);
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
	 * 获取库区
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "achieveDepart")
	@ResponseBody
	public void achieveDepart(HttpServletRequest request, HttpServletResponse response) {
		String departname=request.getParameter("departname");
		JSONArray resultjson = new JSONArray();
		try {
			TSDepart firstDepart = locationService.findUniqueByProperty(TSDepart.class, "orgCode", "A04");
			List<TSDepart> departs = locationService.findHql("from TSDepart where TSPDepart.id=?", firstDepart.getId());
			Map<String, String> departMap = new HashMap<>();

			for (TSDepart tsDepart : departs) {
				if (!tsDepart.getOrgCode().equals("A04A01")) {
					departMap.putAll(getAreaByDepart(tsDepart,departname));
				}
			}

			for (Map.Entry<String, String> entry : departMap.entrySet()) {
				JSONObject department = new JSONObject();
				department.put("key", entry.getKey());
				department.put("value", entry.getValue());
				resultjson.add(department);
			}
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> getAreaByDepart(TSDepart depart,String departname) {
		// TODO Auto-generated method stub
		Map<String, String> areaSql = new HashMap();
		List<TSDepart> departs = locationService.findHql("from TSDepart where TSPDepart.id=?", depart.getId());
		for (TSDepart tsDepart : departs) {
			List<TSDepart> tdeparts = locationService.findHql("from TSDepart where TSPDepart.id=?", tsDepart.getId());
			if (tdeparts.size() > 0) {
				areaSql.putAll(getAreaByDepart(tsDepart,departname));
			} else {
				if(tsDepart.getDepartname()!=null&&tsDepart.getDepartname().contains(departname)) {
					areaSql.put(tsDepart.getId(), tsDepart.getDepartname());
				}
			}
		}
		return areaSql;
	}
}
