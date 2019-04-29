package com.jeecg.arealocuse.controller;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.jeecgframework.core.util.StringUtil;
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

import com.google.common.base.Joiner;
import com.jeecg.arealocuse.entity.ArealocuseEntity;
import com.jeecg.arealocuse.service.ArealocuseServiceI;

/**   
 * @Title: Controller  
 * @Description: 库位使用率
 * @author onlineGenerator
 * @date 2019-04-03 09:03:18
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/arealocuseController")
public class ArealocuseController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ArealocuseController.class);

	@Autowired
	private ArealocuseServiceI arealocuseService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 库位使用率列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/arealocuse/arealocuseList");
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
	public void datagrid(ArealocuseEntity arealocuse,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ArealocuseEntity.class, dataGrid);
		
		String regionlist = request.getParameter("regionlist");
		String departmentlist = request.getParameter("departmentlist");
		String officelist = request.getParameter("officelist");
		String arealist = request.getParameter("arealist");
		String[] areas = getAllArea(regionlist, departmentlist, officelist, arealist);
		
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, arealocuse, request.getParameterMap());
		if(areas.length>0) {
			cq.in("area", areas);
		}
		try {
			String selectdatestart = request.getParameter("selectdatestart");
			String selectdateend = request.getParameter("selectdateend");
			if (StringUtil.isNotEmpty(selectdatestart)) {
				cq.ge("selectdate", new SimpleDateFormat("yyyy-MM-dd").parse(selectdatestart));
			}
			if (StringUtil.isNotEmpty(selectdateend)) {
				cq.le("selectdate", new SimpleDateFormat("yyyy-MM-dd").parse(selectdateend));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cq.add();
		this.arealocuseService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 
	 * 获取所有库区
	 * 
	 * @param regions
	 * @param departments
	 * @param offices
	 * @param areas
	 * @return
	 */
	private String[] getAllArea(String regions, String departments, String offices, String areas) {
		// TODO Auto-generated method stub
		List<String> areaSql = new ArrayList<>();

		if (!("").equals(areas) && areas != null) {
			String[] areaList = areas.split(",");
			for (String area : areaList) {
				TSDepart depart = arealocuseService.findUniqueByProperty(TSDepart.class, "orgCode", area);
				areaSql.add(depart.getDepartname());
			}
		} else if (!("").equals(offices) && offices != null) {
			String[] officeList = offices.split(",");
			for (String office : officeList) {
				TSDepart depart = arealocuseService.findUniqueByProperty(TSDepart.class, "orgCode", office);
				areaSql.addAll(getAreaByDepart(depart));
			}

		} else if (!("").equals(departments) && departments != null) {
			String[] departmentList = departments.split(",");
			for (String department : departmentList) {
				TSDepart depart = arealocuseService.findUniqueByProperty(TSDepart.class, "orgCode", department);
				areaSql.addAll(getAreaByDepart(depart));
			}

		} else if (!("").equals(regions) && regions != null) {
			String[] regiontList = regions.split(",");
			for (String region : regiontList) {
				TSDepart depart = arealocuseService.findUniqueByProperty(TSDepart.class, "orgCode", region);
				areaSql.addAll(getAreaByDepart(depart));
			}
		}

		String[] area = new String[areaSql.size()];
		return areaSql.toArray(area);
	}

	private List<String> getAreaByDepart(TSDepart depart) {
		// TODO Auto-generated method stub
		List<String> areaSql = new ArrayList<>();
		List<TSDepart> departs = arealocuseService.findHql("from TSDepart where TSPDepart.id=?", depart.getId());
		for (TSDepart tsDepart : departs) {
			List<TSDepart> tdeparts = arealocuseService.findHql("from TSDepart where TSPDepart.id=?", tsDepart.getId());
			if (tdeparts.size() > 0) {
				areaSql.addAll(getAreaByDepart(tsDepart));
			} else {
				areaSql.add(tsDepart.getDepartname());
			}
		}
		return areaSql;
	}
	
	/**
	 * 删除库位使用率
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(ArealocuseEntity arealocuse, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		arealocuse = systemService.getEntity(ArealocuseEntity.class, arealocuse.getId());
		message = "库位使用率删除成功";
		try{
			arealocuseService.delete(arealocuse);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "库位使用率删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除库位使用率
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "库位使用率删除成功";
		try{
			for(String id:ids.split(",")){
				ArealocuseEntity arealocuse = systemService.getEntity(ArealocuseEntity.class, 
				id
				);
				arealocuseService.delete(arealocuse);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "库位使用率删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加库位使用率
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(ArealocuseEntity arealocuse, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "库位使用率添加成功";
		try{
			arealocuseService.save(arealocuse);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "库位使用率添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新库位使用率
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(ArealocuseEntity arealocuse, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "库位使用率更新成功";
		ArealocuseEntity t = arealocuseService.get(ArealocuseEntity.class, arealocuse.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(arealocuse, t);
			arealocuseService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "库位使用率更新失败";
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
		req.setAttribute("controller_name","arealocuseController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(ArealocuseEntity arealocuse,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(ArealocuseEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, arealocuse, request.getParameterMap());
		List<ArealocuseEntity> arealocuses = this.arealocuseService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"库位使用率");
		modelMap.put(NormalExcelConstants.CLASS,ArealocuseEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("库位使用率列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,arealocuses);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ArealocuseEntity arealocuse,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"库位使用率");
    	modelMap.put(NormalExcelConstants.CLASS,ArealocuseEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("库位使用率列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<ArealocuseEntity> listArealocuseEntitys = ExcelImportUtil.importExcel(file.getInputStream(),ArealocuseEntity.class,params);
				for (ArealocuseEntity arealocuse : listArealocuseEntitys) {
					arealocuseService.save(arealocuse);
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
	
	
	
}
