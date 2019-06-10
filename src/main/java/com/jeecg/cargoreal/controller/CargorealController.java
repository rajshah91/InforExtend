package com.jeecg.cargoreal.controller;

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
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
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
import com.google.common.base.Joiner;
import com.jeecg.cargoreal.entity.CargorealEntity;
import com.jeecg.cargoreal.service.CargorealServiceI;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;

/**
 * @Title: Controller
 * @Description: 急货实时
 * @author onlineGenerator
 * @date 2019-04-01 16:08:20
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/cargorealController")
public class CargorealController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CargorealController.class);

	@Autowired
	private CargorealServiceI cargorealService;
	@Autowired
	private SystemService systemService;

	/**
	 * 急货实时列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/cargoreal/cargorealList");
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
	public void datagrid(CargorealEntity cargoreal, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		int page = Integer.parseInt(request.getParameter("page"));
		int rows = Integer.parseInt(request.getParameter("rows"));
		String warehouse = request.getParameter("warehouse");
		String requestshipdatestart = request.getParameter("requestshipdatestart");
		String requestshipdateend = request.getParameter("requestshipdateend");
		String adddatestart = request.getParameter("adddatestart");
		String adddateend = request.getParameter("adddateend");
		String orderkey = request.getParameter("orderkey");

		String regions = request.getParameter("region");
		String departments = request.getParameter("department");
		String offices = request.getParameter("office");
		String areas = request.getParameter("area");
		String areaSql = getAllArea(regions, departments, offices, areas);
		TSUser user = ResourceUtil.getSessionUser();// 操作人
		String sqlwhere = "";
		if (requestshipdatestart != null && requestshipdatestart != "" && requestshipdateend != null
				&& requestshipdateend != "") {
			sqlwhere += " and o.requestedshipdate+8/24 between to_date('" + requestshipdatestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + requestshipdateend + "','yyyy-MM-dd HH24:mi:ss') ";
		}
		if (adddatestart != null && adddatestart != "" && adddateend != null && adddateend != "") {
			sqlwhere += " and o.adddate+8/24 between to_date('" + adddatestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + adddateend + "','yyyy-MM-dd HH24:mi:ss') ";
		}
		if (orderkey != null && orderkey != "") {
			sqlwhere += " and o.orderkey='" + orderkey + "' ";
		}
		String sql = "";
		if (!"".equals(areaSql)) {
			if ((warehouse != null && warehouse != "")) {
				String wh = typeNameToTypeCode(warehouse, "仓库");
				sql = "select distinct o.whseid,cu.description as descr,o.orderkey," + "       (select s.susr3 from "
						+ wh + "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
						+ "       (select s2.susr3 from " + wh
						+ "_storer s2  where s2.storerkey=o.susr35 and s2.type='1' ) as s2susr3,"
						+ "       to_char(o.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,"
						+ "       case when o.susr35='CSKJY0101' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3 ,2)"
						+ "         when o.susr35='MSDNY0102' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3.5 ,2)"
						+ "         when o.susr35='SSDZY0600' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 1 ,2)"
						+ "         when o.susr35='KSDZY0201' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 -3 ,2)"
						+ "         else ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 15/60 ,2)"
						+ "         end as earlywarndate,"
						+ "        os.description,o.performancedata01,o.adddate from " + wh + "_orders o"
						+ " left join " + wh + "_pickdetail pd on pd.orderkey=o.orderkey" + " left join " + wh
						+ "_loc loc on loc.loc=nvl(trim(pd.fromloc),pd.loc)" + " left join " + wh
						+ "_orderstatussetup os on os.code=o.status" + " left join " + wh
						+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
						+ " where o.priority='1' and o.status>='14'  and o.type='0' and o.status<'95' and cu.description in ("
						+ areaSql + ")" + sqlwhere + "" + " union all"
						+ " select distinct r.whseid,r.descr,r.orderkey,r.s1susr3,r.s2susr3,"
						+ "       to_char(r.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,r.earlywarndate,"
						+ "       r.description,r.performancedata01,r.adddate from"
						+ " (select o.whseid,cu.description as descr,o.orderkey," + "       (select s.susr3 from " + wh
						+ "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
						+ "       (select s2.susr3 from " + wh
						+ "_storer s2  where s2.storerkey=o.susr35 and s2.type='1' ) as s2susr3,"
						+ "       o.requestedshipdate," + "       case when o.susr35='CSKJY0101' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3 ,2)"
						+ "         when o.susr35='MSDNY0102' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3.5 ,2)"
						+ "         when o.susr35='SSDZY0600' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 1 ,2)"
						+ "         when o.susr35='KSDZY0201' then "
						+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 -3 ,2)"
						+ "         else ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 15/60 ,2)"
						+ "         end as earlywarndate," + "        os.description,o.performancedata01,"
						+ "        case when o.susr35='CSKJY0101' then " + "         o.requestedshipdate - 3.5/24"
						+ "         when o.susr35='MSDNY0102' then " + "         o.requestedshipdate - 4/24"
						+ "         when o.susr35='SSDZY0600' then " + "         o.requestedshipdate - 1.5/24"
						+ "         when o.susr35='KSDZY0201' then " + "         o.requestedshipdate - 3.5/24"
						+ "         else o.requestedshipdate" + "         end as enddate,o.adddate from " + wh
						+ "_orders o" + " left join " + wh + "_pickdetail pd on pd.orderkey=o.orderkey" + " left join "
						+ wh + "_loc loc on loc.loc=nvl(trim(pd.fromloc),pd.loc)" + " left join " + wh
						+ "_orderstatussetup os on os.code=o.status" + " left join " + wh
						+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
						+ " where o.priority <> '1' and o.status>='14' and o.type='0' and o.status<'55' and cu.description in ("
						+ areaSql + ") " + sqlwhere + ""
						+ " and o.susr35 in ('CSKJY0101','MSDNY0102','SSDZY0600','KSDZY0201')) r "
						+ " where sysdate>r.enddate + 8/24" + " union all"
						+ " select distinct r.whseid,r.descr,r.orderkey,r.s1susr3,r.s2susr3,"
						+ "       to_char(r.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,r.earlywarndate,"
						+ "       r.description,r.performancedata01,r.adddate from"
						+ " (select o.whseid,cu.description as descr,o.orderkey," + "       (select s.susr3 from " + wh
						+ "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
						+ "       (select s2.susr3 from " + wh
						+ "_storer s2  where s2.storerkey=o.susr35 and s2.type='1' ) as s2susr3,"
						+ "       o.requestedshipdate,"
						+ "       ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 -15/60,2) as earlywarndate,"
						+ "       os.description,o.performancedata01,"
						+ "        case when o.status>='14' and o.status<'55' then "
						+ "         o.requestedshipdate - 1/24" + "         when o.status>='55' and o.status<'95' then "
						+ "         o.requestedshipdate - 0.5/24" + "         else o.requestedshipdate"
						+ "         end as enddate,o.adddate from " + wh + "_orders o" + " left join " + wh
						+ "_pickdetail pd on pd.orderkey=o.orderkey" + " left join " + wh
						+ "_loc loc on loc.loc=nvl(trim(pd.fromloc),pd.loc)" + " left join " + wh
						+ "_orderstatussetup os on os.code=o.status " + " left join " + wh
						+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
						+ " where o.priority <> '1' and o.status>='14' and o.type='0' and o.status<'95' and cu.description in ("
						+ areaSql + ") " + sqlwhere + ""
						+ " and o.susr35 not in ('CSKJY0101','MSDNY0102','SSDZY0600','KSDZY0201')) r "
						+ " where sysdate>r.enddate + 8/24";
			} else {
				List<UsercontactwhEntity> usercontactwhEntities = cargorealService
						.findHql("from UsercontactwhEntity where userid=?", user.getId());
				if (usercontactwhEntities.size() > 0) {

					for (int i = 0; i < usercontactwhEntities.size(); i++) {
						if (usercontactwhEntities.get(i).getWarehouse().contains("FEILI_wmwhse")) {
							String wh = typeNameToTypeCode(usercontactwhEntities.get(i).getWarehouse(), "仓库");
							if (i != 0) {
								sql += " union all ";
							}

							sql += "select distinct o.whseid,cu.description as descr,o.orderkey,"
									+ "       (select s.susr3 from " + wh
									+ "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
									+ "       (select s2.susr3 from " + wh
									+ "_storer s2  where s2.storerkey=o.susr35 and s2.type='1' ) as s2susr3,"
									+ "       to_char(o.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,"
									+ "       case when o.susr35='CSKJY0101' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3 ,2)"
									+ "         when o.susr35='MSDNY0102' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3.5 ,2)"
									+ "         when o.susr35='SSDZY0600' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 1 ,2)"
									+ "         when o.susr35='KSDZY0201' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 -3 ,2)"
									+ "         else ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 15/60 ,2)"
									+ "         end as earlywarndate,"
									+ "        os.description,o.performancedata01,o.adddate from " + wh + "_orders o"
									+ " left join " + wh + "_pickdetail pd on pd.orderkey=o.orderkey" + " left join "
									+ wh + "_loc loc on loc.loc=nvl(trim(pd.fromloc),pd.loc)" + " left join " + wh
									+ "_orderstatussetup os on os.code=o.status" + " left join " + wh
									+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
									+ " where o.priority='1' and o.status>='14'  and o.type='0' and o.status<'95' and cu.description in ("
									+ areaSql + ")" + sqlwhere + "" + " union all"
									+ " select distinct r.whseid,r.descr,r.orderkey,r.s1susr3,r.s2susr3,"
									+ "       to_char(r.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,r.earlywarndate,"
									+ "       r.description,r.performancedata01,r.adddate from"
									+ " (select o.whseid,cu.description as descr,o.orderkey,"
									+ "       (select s.susr3 from " + wh
									+ "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
									+ "       (select s2.susr3 from " + wh
									+ "_storer s2  where s2.storerkey=o.susr35 and s2.type='1' ) as s2susr3,"
									+ "       o.requestedshipdate," + "       case when o.susr35='CSKJY0101' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3 ,2)"
									+ "         when o.susr35='MSDNY0102' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 3.5 ,2)"
									+ "         when o.susr35='SSDZY0600' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 1 ,2)"
									+ "         when o.susr35='KSDZY0201' then "
									+ "         ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 -3 ,2)"
									+ "         else ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 - 15/60 ,2)"
									+ "         end as earlywarndate," + "        os.description,o.performancedata01,"
									+ "        case when o.susr35='CSKJY0101' then "
									+ "         o.requestedshipdate - 3.5/24"
									+ "         when o.susr35='MSDNY0102' then " + "         o.requestedshipdate - 4/24"
									+ "         when o.susr35='SSDZY0600' then "
									+ "         o.requestedshipdate - 1.5/24"
									+ "         when o.susr35='KSDZY0201' then "
									+ "         o.requestedshipdate - 3.5/24" + "         else o.requestedshipdate"
									+ "         end as enddate,o.adddate from " + wh + "_orders o" + " left join " + wh
									+ "_pickdetail pd on pd.orderkey=o.orderkey" + " left join " + wh
									+ "_loc loc on loc.loc=nvl(trim(pd.fromloc),pd.loc)" + " left join " + wh
									+ "_orderstatussetup os on os.code=o.status" + " left join " + wh
									+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
									+ " where o.priority <> '1' and o.status>='14' and o.type='0' and o.status<'55' and cu.description in ("
									+ areaSql + ") " + sqlwhere + ""
									+ " and o.susr35 in ('CSKJY0101','MSDNY0102','SSDZY0600','KSDZY0201')) r "
									+ " where sysdate>r.enddate + 8/24" + " union all"
									+ " select distinct r.whseid,r.descr,r.orderkey,r.s1susr3,r.s2susr3,"
									+ "       to_char(r.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,r.earlywarndate,"
									+ "       r.description,r.performancedata01,r.adddate from"
									+ " (select o.whseid,cu.description as descr,o.orderkey,"
									+ "       (select s.susr3 from " + wh
									+ "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
									+ "       (select s2.susr3 from " + wh
									+ "_storer s2  where s2.storerkey=o.susr35 and s2.type='1' ) as s2susr3,"
									+ "       o.requestedshipdate,"
									+ "       ROUND((o.requestedshipdate + 8/24 - sysdate) * 24 -15/60,2) as earlywarndate,"
									+ "       os.description,o.performancedata01,"
									+ "        case when o.status>='14' and o.status<'55' then "
									+ "         o.requestedshipdate - 1/24"
									+ "         when o.status>='55' and o.status<'95' then "
									+ "         o.requestedshipdate - 0.5/24" + "         else o.requestedshipdate"
									+ "         end as enddate,o.adddate from " + wh + "_orders o" + " left join " + wh
									+ "_pickdetail pd on pd.orderkey=o.orderkey" + " left join " + wh
									+ "_loc loc on loc.loc=nvl(trim(pd.fromloc),pd.loc)" + " left join " + wh
									+ "_orderstatussetup os on os.code=o.status " + " left join " + wh
									+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
									+ " where o.priority <> '1' and o.status>='14' and o.type='0' and o.status<'95' and cu.description in ("
									+ areaSql + ") " + sqlwhere + ""
									+ " and o.susr35 not in ('CSKJY0101','MSDNY0102','SSDZY0600','KSDZY0201')) r "
									+ " where sysdate>r.enddate + 8/24";
						}
					}
				}
			}
			String sql1 = "select * from (" + sql + ") s order by s.earlywarndate ";
			dataGrid = paging(sql1, page, rows, dataGrid);
		}
		
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
	private String getAllArea(String regions, String departments, String offices, String areas) {
		// TODO Auto-generated method stub
		List<String> areaSql = new ArrayList<>();

		if (!("").equals(areas) && areas != null) {
			String[] areaList = areas.split(",");
			for (String area : areaList) {
				TSDepart depart = cargorealService.findUniqueByProperty(TSDepart.class, "orgCode", area);
				areaSql.add("'" + depart.getDepartname() + "'");
			}
		} else if (!("").equals(offices) && offices != null) {
			String[] officeList = offices.split(",");
			for (String office : officeList) {
				TSDepart depart = cargorealService.findUniqueByProperty(TSDepart.class, "orgCode", office);
				areaSql.addAll(getAreaByDepart(depart));
			}

		} else if (!("").equals(departments) && departments != null) {
			String[] departmentList = departments.split(",");
			for (String department : departmentList) {
				TSDepart depart = cargorealService.findUniqueByProperty(TSDepart.class, "orgCode", department);
				areaSql.addAll(getAreaByDepart(depart));
			}

		} else if (!("").equals(regions) && regions != null) {
			String[] regiontList = regions.split(",");
			for (String region : regiontList) {
				TSDepart depart = cargorealService.findUniqueByProperty(TSDepart.class, "orgCode", region);
				areaSql.addAll(getAreaByDepart(depart));
			}
		}

		return Joiner.on(",").join(areaSql);
	}

	private List<String> getAreaByDepart(TSDepart depart) {
		// TODO Auto-generated method stub
		List<String> areaSql = new ArrayList<>();
		List<TSDepart> departs = cargorealService.findHql("from TSDepart where TSPDepart.id=?", depart.getId());
		for (TSDepart tsDepart : departs) {
			List<TSDepart> tdeparts = cargorealService.findHql("from TSDepart where TSPDepart.id=?", tsDepart.getId());
			if (tdeparts.size() > 0) {
				areaSql.addAll(getAreaByDepart(tsDepart));
			} else {
				areaSql.add("'" + tsDepart.getDepartname() + "'");
			}
		}
		return areaSql;
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
		List<String> totalList = cargorealService.findListbySql(totalSql);
		int total = Integer.parseInt(String.valueOf(totalList.get(0)));
		String resultSql = pagingByOracle(sql, page, rows);
		List<CargorealEntity> cargorealEntities = achieveCargoreaBySql(resultSql);
		dataGrid.setPage(page);
		dataGrid.setRows(rows);
		dataGrid.setTotal(total);
		dataGrid.setResults(cargorealEntities);
		return dataGrid;
	}

	/**
	 * 根据sql 获取数据
	 * 
	 * @param resultSql
	 * @return
	 */
	private List<CargorealEntity> achieveCargoreaBySql(String resultSql) {
		List<CargorealEntity> cargorealEntities = new ArrayList<>();
		// TODO Auto-generated method stub
		List<Object[]> resultList = cargorealService.findListbySql(resultSql);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] result : resultList) {
			CargorealEntity cargorealEntity = new CargorealEntity();
			cargorealEntity.setWarehouse(String.valueOf(result[0]));
			cargorealEntity.setArea(String.valueOf(result[1]));
			cargorealEntity.setOrderkey(String.valueOf(result[2]));
			cargorealEntity.setStorer(String.valueOf(result[3]));
			cargorealEntity.setVendor(String.valueOf(result[4]));
			try {
				if (String.valueOf(result[5]) != "null") {
					cargorealEntity.setRequestshipdate(sdf.parse(String.valueOf(result[5])));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cargorealEntity.setWarningtime(String.valueOf(result[6]));
			cargorealEntity.setOrderstatus(String.valueOf(result[7]));
			cargorealEntity.setOperator(String.valueOf(result[8]));
			cargorealEntities.add(cargorealEntity);
		}
		return cargorealEntities;
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

	// 仓库字典code 转换
	private String typeNameToTypeCode(String typeName, String typegroupname) {
		List<TSTypegroup> tsTypegroup = systemService.findHql("from TSTypegroup where typegroupname=?", typegroupname);
		List<TSType> tsType = systemService.findHql("from TSType where TSTypegroup.id=? and typename=?",
				tsTypegroup.get(0).getId(), typeName);
		return tsType.get(0).getTypecode();
	}

	/**
	 * 删除急货实时
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(CargorealEntity cargoreal, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		cargoreal = systemService.getEntity(CargorealEntity.class, cargoreal.getId());
		message = "急货实时删除成功";
		try {
			cargorealService.delete(cargoreal);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "急货实时删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除急货实时
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "急货实时删除成功";
		try {
			for (String id : ids.split(",")) {
				CargorealEntity cargoreal = systemService.getEntity(CargorealEntity.class, id);
				cargorealService.delete(cargoreal);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "急货实时删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加急货实时
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(CargorealEntity cargoreal, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "急货实时添加成功";
		try {
			cargorealService.save(cargoreal);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "急货实时添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新急货实时
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(CargorealEntity cargoreal, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "急货实时更新成功";
		CargorealEntity t = cargorealService.get(CargorealEntity.class, cargoreal.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(cargoreal, t);
			cargorealService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "急货实时更新失败";
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
		req.setAttribute("controller_name", "cargorealController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(CargorealEntity cargoreal, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		String regions = request.getParameter("region");
		String departments = request.getParameter("department");
		String offices = request.getParameter("office");
		String areas = request.getParameter("area");
		String areaSql = getAllArea(regions, departments, offices, areas);
		TSUser user = ResourceUtil.getSessionUser();// 操作人
		List<CargorealEntity> cargoreals = new ArrayList<>();
		if (!"".equals(areaSql)) {
			List<UsercontactwhEntity> usercontactwhEntities = cargorealService
					.findHql("from UsercontactwhEntity where userid=?", user.getId());
			if (usercontactwhEntities.size() > 0) {
				String sql = "";

				for (int i = 0; i < usercontactwhEntities.size(); i++) {
					if (usercontactwhEntities.get(i).getWarehouse().contains("FEILI_wmwhse")) {
						String wh = typeNameToTypeCode(usercontactwhEntities.get(i).getWarehouse(), "仓库");
						if (i != 0) {
							sql += " union all ";
						}

						sql += "select o.whseid,cu.description as descr,o.orderkey," + "       (select s.susr3 from "
								+ wh + "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
								+ "       (select s2.susr3 from " + wh
								+ "_storer s2  where s2.storerkey=o.storerkey and s2.type='1' ) as s2susr3,"
								+ "       to_char(o.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,"
								+ "       case when o.susr35='CSKJY0101' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-180"
								+ "         when o.susr35='MSDNY0102' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-210"
								+ "         when o.susr35='SSDZY0600' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-60"
								+ "         when o.susr35='KSDZY0201' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-180"
								+ "         else CEIL((o.requestedshipdate - sysdate) * 24 * 60)-15"
								+ "         end as earlywarndate," + "        os.description,o.performancedata01 from "
								+ wh + "_orders o" + " left join " + wh + "_orderdetail od on od.orderkey=o.orderkey"
								+ " left join " + wh + "_pickdetail pd on pd.orderkey=o.orderkey" + " left join " + wh
								+ "_loc loc on loc.loc=pd.loc" + " left join " + wh
								+ "_orderstatussetup os on os.code=o.status" + " left join " + wh
								+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
								+ " where o.priority='1' and o.status>=14 and o.status<95 and cu.description in ("
								+ areaSql + ")" + " union all"
								+ " select r.whseid,r.descr,r.orderkey,r.s1susr3,r.s2susr3,"
								+ "       to_char(r.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,r.earlywarndate,"
								+ "       r.description,r.performancedata01 from"
								+ " (select o.whseid,cu.description as descr,o.orderkey,"
								+ "       (select s.susr3 from " + wh
								+ "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
								+ "       (select s2.susr3 from " + wh
								+ "_storer s2  where s2.storerkey=o.storerkey and s2.type='1' ) as s2susr3,"
								+ "       o.requestedshipdate," + "       case when o.susr35='CSKJY0101' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-180"
								+ "         when o.susr35='MSDNY0102' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-210"
								+ "         when o.susr35='SSDZY0600' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-60"
								+ "         when o.susr35='KSDZY0201' then "
								+ "         CEIL((o.requestedshipdate - sysdate) * 24 * 60)-180"
								+ "         else CEIL((o.requestedshipdate - sysdate) * 24 * 60)-15"
								+ "         end as earlywarndate," + "        os.description,o.performancedata01,"
								+ "        case when o.susr35='CSKJY0101' then "
								+ "         o.requestedshipdate - 3.5/24" + "         when o.susr35='MSDNY0102' then "
								+ "         o.requestedshipdate - 4/24" + "         when o.susr35='SSDZY0600' then "
								+ "         o.requestedshipdate - 1.5/24" + "         when o.susr35='KSDZY0201' then "
								+ "         o.requestedshipdate - 3.5/24" + "         else o.requestedshipdate"
								+ "         end as enddate from " + wh + "_orders o" + " left join " + wh
								+ "_orderdetail od on od.orderkey=o.orderkey" + " left join " + wh
								+ "_pickdetail pd on pd.orderkey=o.orderkey" + " left join " + wh
								+ "_loc loc on loc.loc=pd.loc" + " left join " + wh
								+ "_orderstatussetup os on os.code=o.status" + " left join " + wh
								+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
								+ " where o.priority <> '1' and o.status>=14 and o.status<55 and cu.description in ("
								+ areaSql + ")"
								+ " and o.susr35 in ('CSKJY0101','MSDNY0102','SSDZY0600','KSDZY0201')) r "
								+ " where sysdate>r.enddate" + " union all"
								+ " select r.whseid,r.descr,r.orderkey,r.s1susr3,r.s2susr3,"
								+ "       to_char(r.requestedshipdate+8/24,'yyyy-MM-dd HH24:mi:ss') as requestedshipdate,r.earlywarndate,"
								+ "       r.description,r.performancedata01 from"
								+ " (select o.whseid,cu.description as descr,o.orderkey,"
								+ "       (select s.susr3 from " + wh
								+ "_storer s  where s.storerkey=o.storerkey and s.type='1' ) as s1susr3,"
								+ "       (select s2.susr3 from " + wh
								+ "_storer s2  where s2.storerkey=o.storerkey and s2.type='1' ) as s2susr3,"
								+ "       o.requestedshipdate,"
								+ "       CEIL((o.requestedshipdate - sysdate) * 24 * 60)-15 as earlywarndate,"
								+ "       os.description,o.performancedata01,"
								+ "        case when o.status>=14 and o.status<55 then "
								+ "         o.requestedshipdate - 1/24"
								+ "         when o.status>=55 and o.status<95 then "
								+ "         o.requestedshipdate - 0.5/24" + "         else o.requestedshipdate"
								+ "         end as enddate from " + wh + "_orders o" + " left join " + wh
								+ "_orderdetail od on od.orderkey=o.orderkey" + " left join " + wh
								+ "_pickdetail pd on pd.orderkey=o.orderkey" + " left join " + wh
								+ "_loc loc on loc.loc=pd.loc" + " left join " + wh
								+ "_orderstatussetup os on os.code=o.status " + " left join " + wh
								+ "_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware"
								+ " where o.priority <> '1' and o.status>=14 and o.status<95 and cu.description in ("
								+ areaSql + ")"
								+ " and o.susr35 not in ('CSKJY0101','MSDNY0102','SSDZY0600','KSDZY0201')) r "
								+ " where sysdate>r.enddate";
					}
				}
				cargoreals = achieveCargoreaBySql(sql);
			}
		}
		modelMap.put(NormalExcelConstants.FILE_NAME, "急货实时");
		modelMap.put(NormalExcelConstants.CLASS, CargorealEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("急货实时列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, cargoreals);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(CargorealEntity cargoreal, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "急货实时");
		modelMap.put(NormalExcelConstants.CLASS, CargorealEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("急货实时列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
				List<CargorealEntity> listCargorealEntitys = ExcelImportUtil.importExcel(file.getInputStream(),
						CargorealEntity.class, params);
				for (CargorealEntity cargoreal : listCargorealEntitys) {
					cargorealService.save(cargoreal);
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
	 * 获取分区
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "doFindRegion")
	@ResponseBody
	public void doFindRegion(HttpServletRequest request, HttpServletResponse response) {
		JSONArray resultjson = new JSONArray();
		try {
			TSDepart firstDepart = cargorealService.findUniqueByProperty(TSDepart.class, "orgCode", "A04");
			List<TSDepart> departs = cargorealService.findHql("from TSDepart where TSPDepart.id=?",
					firstDepart.getId());
			for (TSDepart tsDepart : departs) {
				if (!tsDepart.getOrgCode().equals("A04A01")) {
					JSONObject region = new JSONObject();
					region.put("value", tsDepart.getOrgCode());
					region.put("label", tsDepart.getDepartname());
					resultjson.add(region);
				}
			}
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取操作部
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "doFindDepartment")
	@ResponseBody
	public void doFindDepartment(HttpServletRequest request, HttpServletResponse response) {
		String regionsParam = request.getParameter("regions");
		String[] regions = regionsParam.split(",");

		JSONArray resultjson = new JSONArray();
		try {
			for (String region : regions) {
				TSDepart firstDepart = cargorealService.findUniqueByProperty(TSDepart.class, "orgCode", region);
				List<TSDepart> departs = cargorealService.findHql("from TSDepart where TSPDepart.id=?",
						firstDepart.getId());
				for (TSDepart tsDepart : departs) {
					if (!tsDepart.getOrgCode().equals("A04A01")) {
						JSONObject department = new JSONObject();
						department.put("value", tsDepart.getOrgCode());
						department.put("label", tsDepart.getDepartname());
						resultjson.add(department);
					}
				}
			}
			response.getWriter().write(resultjson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
