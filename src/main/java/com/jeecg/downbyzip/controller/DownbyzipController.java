package com.jeecg.downbyzip.controller;
import com.jeecg.Util.ZipUtils;
import com.jeecg.downbyzip.entity.DownbyzipEntity;
import com.jeecg.downbyzip.entity.FileBean;
import com.jeecg.downbyzip.service.DownbyzipServiceI;
import com.jeecg.orderforecast.entity.OrderforecastEntity;

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
import java.util.zip.ZipOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.jeecgframework.core.util.ExceptionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**   
 * @Title: Controller  
 * @Description: 收货异常图片下载
 * @author onlineGenerator
 * @date 2019-05-17 10:26:21
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/downbyzipController")
public class DownbyzipController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(DownbyzipController.class);

	@Autowired
	private DownbyzipServiceI downbyzipService;
	@Autowired
	private SystemService systemService;
	


	/**
	 * 收货异常图片下载列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/downbyzip/downbyzipList");
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
	public void datagrid(DownbyzipEntity downbyzip,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DownbyzipEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, downbyzip, request.getParameterMap());
		cq.add();
		this.downbyzipService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除收货异常图片下载
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DownbyzipEntity downbyzip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		downbyzip = systemService.getEntity(DownbyzipEntity.class, downbyzip.getId());
		message = "收货异常图片下载删除成功";
		try{
			downbyzipService.delete(downbyzip);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "收货异常图片下载删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除收货异常图片下载
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "收货异常图片下载删除成功";
		try{
			for(String id:ids.split(",")){
				DownbyzipEntity downbyzip = systemService.getEntity(DownbyzipEntity.class, 
				id
				);
				downbyzipService.delete(downbyzip);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "收货异常图片下载删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加收货异常图片下载
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DownbyzipEntity downbyzip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "收货异常图片下载添加成功";
		try{
			downbyzipService.save(downbyzip);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "收货异常图片下载添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新收货异常图片下载
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DownbyzipEntity downbyzip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "收货异常图片下载更新成功";
		DownbyzipEntity t = downbyzipService.get(DownbyzipEntity.class, downbyzip.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(downbyzip, t);
			downbyzipService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "收货异常图片下载更新失败";
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
		req.setAttribute("controller_name","downbyzipController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DownbyzipEntity downbyzip,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DownbyzipEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, downbyzip, request.getParameterMap());
		List<DownbyzipEntity> downbyzips = this.downbyzipService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"收货异常图片下载");
		modelMap.put(NormalExcelConstants.CLASS,DownbyzipEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("收货异常图片下载列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,downbyzips);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DownbyzipEntity downbyzip,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"收货异常图片下载");
    	modelMap.put(NormalExcelConstants.CLASS,DownbyzipEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("收货异常图片下载列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
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
				List<DownbyzipEntity> listDownbyzipEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DownbyzipEntity.class,params);
				for (DownbyzipEntity downbyzip : listDownbyzipEntitys) {
					downbyzipService.save(downbyzip);
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
     * 打包压缩下载文件
     */
    @RequestMapping(params = "downLoadZipFile")
    public void downLoadZipFile(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String lpn=request.getParameter("lpn");
        String asn=request.getParameter("asn");
        String zipName = asn+lpn+".zip";
        String sql ="select t.photo_file from W01_receiptfeedback t where t.asn='"+asn+"' and t.lpn='"+lpn+"' ";
        
        List<FileBean> fileList = getfile(sql);//查询数据库中记录
        response.setContentType("APPLICATION/OCTET-STREAM");  
        response.setHeader("Content-Disposition","attachment; filename="+zipName);
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try {
            for(Iterator<FileBean> it = fileList.iterator();it.hasNext();){
                FileBean file = it.next();
                ZipUtils.doCompress(file.getFilePath()+file.getFileName(), out);
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            out.close();
        }
    }
	
 // 导出执行sql
 	private List<FileBean> getfile(String sql) {

 		List<FileBean> list = new ArrayList<>();
 		List<Object[]> resultList = downbyzipService.findListbySql(sql);
 		for (Object[] result : resultList) {
 			FileBean entity = new FileBean();
 			entity.setFileName(String.valueOf(result[0]));
 			list.add(entity);
 		}
 		return list;
 	}
}
