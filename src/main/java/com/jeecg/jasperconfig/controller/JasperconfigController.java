package com.jeecg.jasperconfig.controller;
import com.jeecg.jasperconfig.entity.JasperconfigEntity;
import com.jeecg.jasperconfig.service.JasperconfigServiceI;

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
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.poi.excel.ExcelExportUtil;
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
 * @Description: 面单配置
 * @author onlineGenerator
 * @date 2019-07-19 17:15:52
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jasperconfigController")
public class JasperconfigController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(JasperconfigController.class);

	@Autowired
	private JasperconfigServiceI jasperconfigService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 面单配置列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/jasperconfig/jasperconfigList");
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
	public void datagrid(JasperconfigEntity jasperconfig,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JasperconfigEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jasperconfig, request.getParameterMap());
		try{
		//自定义追加查询条件
		
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jasperconfigService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除面单配置
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JasperconfigEntity jasperconfig, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jasperconfig = systemService.getEntity(JasperconfigEntity.class, jasperconfig.getId());
		message = "面单配置删除成功";
		try{
			jasperconfigService.delete(jasperconfig);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "面单配置删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除面单配置
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "面单配置删除成功";
		try{
			for(String id:ids.split(",")){
				JasperconfigEntity jasperconfig = systemService.getEntity(JasperconfigEntity.class, 
				id
				);
				jasperconfigService.delete(jasperconfig);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "面单配置删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加面单配置
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JasperconfigEntity jasperconfig, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "面单配置添加成功";
		try{
			jasperconfigService.save(jasperconfig);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "面单配置添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新面单配置
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JasperconfigEntity jasperconfig, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "面单配置更新成功";
		JasperconfigEntity t = jasperconfigService.get(JasperconfigEntity.class, jasperconfig.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jasperconfig, t);
			jasperconfigService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "面单配置更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 面单配置新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JasperconfigEntity jasperconfig, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jasperconfig.getId())) {
			jasperconfig = jasperconfigService.getEntity(JasperconfigEntity.class, jasperconfig.getId());
			req.setAttribute("jasperconfig", jasperconfig);
		}
		return new ModelAndView("com/jeecg/jasperconfig/jasperconfig-add");
	}
	/**
	 * 面单配置编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JasperconfigEntity jasperconfig, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jasperconfig.getId())) {
			jasperconfig = jasperconfigService.getEntity(JasperconfigEntity.class, jasperconfig.getId());
			req.setAttribute("jasperconfig", jasperconfig);
		}
		return new ModelAndView("com/jeecg/jasperconfig/jasperconfig-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","jasperconfigController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JasperconfigEntity jasperconfig,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JasperconfigEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jasperconfig, request.getParameterMap());
		List<JasperconfigEntity> jasperconfigs = this.jasperconfigService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"面单配置");
		modelMap.put(NormalExcelConstants.CLASS,JasperconfigEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("面单配置列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jasperconfigs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JasperconfigEntity jasperconfig,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"面单配置");
    	modelMap.put(NormalExcelConstants.CLASS,JasperconfigEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("面单配置列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<JasperconfigEntity> listJasperconfigEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JasperconfigEntity.class,params);
				for (JasperconfigEntity jasperconfig : listJasperconfigEntitys) {
					jasperconfigService.save(jasperconfig);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
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
	
	
}
