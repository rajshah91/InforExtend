package com.jeecg.workamount.controller;

import com.google.common.base.Joiner;
import com.jeecg.orderforecast.entity.OrderforecastEntity;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;
import com.jeecg.workamount.entity.WorkamountEntity;
import com.jeecg.workamount.service.WorkamountServiceI;

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
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
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
 * @Description: 员工操作量
 * @author onlineGenerator
 * @date 2019-04-02 16:40:07
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/workamountController")
public class WorkamountController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(WorkamountController.class);

	@Autowired
	private WorkamountServiceI workamountService;
	@Autowired
	private SystemService systemService;

	/**
	 * 员工操作量列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/workamount/workamountList");
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
	public void datagrid(WorkamountEntity workamount, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		/*
		 * CriteriaQuery cq = new CriteriaQuery(WorkamountEntity.class, dataGrid);
		 * //查询条件组装器
		 * org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
		 * workamount, request.getParameterMap()); cq.add();
		 * this.workamountService.getDataGridReturn(cq, true);
		 */
		// 参数
		String areas = request.getParameter("seracharea");
		String regions = request.getParameter("region");
		String departments = request.getParameter("department");
		String offices = request.getParameter("office");
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		String areasql = getAllArea(regions, departments, offices, areas);
		int page = Integer.parseInt(request.getParameter("page"));
		int rows = Integer.parseInt(request.getParameter("rows"));
		TSUser user = ResourceUtil.getSessionUser();
		String wh = "";
		String sqlwhere = "where 1=1 ";
		String sqlpick = "";
		String sqlrecheck = "";
		String sqlso = "";
		String sqlItrn = "";
		if (datestart != null && !"".equals(datestart) && dateend != null && !"".equals(dateend)) {
			sqlpick = " and o.pickenddate+8/24 between to_date('" + datestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + dateend + "','yyyy-MM-dd HH24:mi:ss')";

			sqlrecheck = " and o.recheckenddate+8/24 between to_date('" + datestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + dateend + "','yyyy-MM-dd HH24:mi:ss')";

			sqlso = " and r.editdate+8/24 between to_date('" + datestart + "','yyyy-MM-dd HH24:mi:ss') and to_date('"
					+ dateend + "','yyyy-MM-dd HH24:mi:ss')";

			sqlItrn = " and i.editdate+8/24 between to_date('" + datestart + "','yyyy-MM-dd HH24:mi:ss') and to_date('"
					+ dateend + "','yyyy-MM-dd HH24:mi:ss')";
		}
		// 仓库// 区域
		if (areasql != null && !"".equals(areasql)) {
			sqlwhere += " and c3.description in (" + areasql + ")";
		}
		String sql = "";
		List<UsercontactwhEntity> entities = workamountService.findHql("from UsercontactwhEntity where userid=?",
				user.getId());
		if (entities != null && entities.size() > 0) {
			List<String> list = new ArrayList<>();
			for (UsercontactwhEntity u : entities) {
				list.add(u.getWarehouse().toString());
			}
		}
		List<WorkamountEntity> worklist = new ArrayList<>();
		Map<String, List<WorkamountEntity>> workmap = new HashMap<>();
		for (int i = 0; i < entities.size(); i++) {
			wh = typeNameToTypeCode(entities.get(i).getWarehouse(), "仓库");
			sql = "select * from (select op.fully_qualified_id,count(distinct r.receiptkey) sonamesum,"
					+ "count(distinct r.sku) soskusum," + "count(distinct l.loc) soblocsum,"
					+ "count(distinct l2.loc) soslocsum," + "count(distinct r.toid) solpnsum " + "from " + wh
					+ "_Receiptdetail r " + "left join (select i.toloc,i.addwho from " + wh
					+ "_Itrn i  where i.sourcetype in ('NSPRFRL01','NSPRFPA02') " + sqlItrn
					+ ") s on r.addwho=s.addwho " + "left join " + wh
					+ "_loc l on s.toloc = l.loc and l.locnature <> 'S' " + "left join " + wh
					+ "_loc l2 on s.toloc= l2.loc and l2.locnature = 'S' " + "left join " + wh
					+ "_Loc l3 on s.toloc=l3.loc " + "left join " + wh
					+ "_Codelkup c3 on c3.listname='PHYSICALWH' and c3.code=l3.physicalware left join oper.e_sso_user op on op.sso_user_name=r.addwho "
					+ "" + sqlwhere + sqlso + " group by op.fully_qualified_id) a  " + " full join  (select  "
					+ "o.performancedata01, " + "count(o.performancedata01) picknamesum, "
					+ "count(distinct pk.sku) pickskusum, " + "count(distinct l.loc) pickblocsum, "
					+ "count(distinct l2.loc) pickslocsum, " + "count(distinct pk.id) picklpnsum " + "from " + wh
					+ "_orders o " + "left join " + wh + "_Pickdetail pk on o.orderkey=pk.orderkey " + "left join " + wh
					+ "_loc l on nvl(trim(pk.fromloc),pk.loc) = l.loc and l.locnature <> 'S' " + "left join " + wh
					+ "_loc l2 on nvl(trim(pk.fromloc),pk.loc) = l2.loc and l2.locnature = 'S' " + "left join " + wh
					+ "_Loc l3 on nvl(trim(pk.fromloc),pk.loc)=l3.loc " + "left join " + wh
					+ "_Codelkup c3 on c3.listname='PHYSICALWH' and c3.code=l3.physicalware " + "" + sqlwhere + sqlpick
					+ " group by o.performancedata01) b on a.fully_qualified_id=b.performancedata01  "
					+ " full join (select  " + "o.performancedata04, " + "count(o.performancedata04) rcnamesum, "
					+ "count(distinct pk.sku) rcskusum, " + "count(distinct l.loc) rcblocsum, "
					+ "count(distinct l2.loc) rcslocsum, " + "count(distinct pk.id) rclpnsum " + "from " + wh
					+ "_orders o " + "left join " + wh + "_Pickdetail pk on o.orderkey=pk.orderkey " + "left join " + wh
					+ "_loc l on nvl(trim(pk.fromloc),pk.loc) = l.loc and l.locnature <> 'S' " + "left join " + wh
					+ "_loc l2 on nvl(trim(pk.fromloc),pk.loc) = l2.loc and l2.locnature = 'S' " + "left join " + wh
					+ "_Loc l3 on nvl(trim(pk.fromloc),pk.loc)=l3.loc " + "left join " + wh
					+ "_Codelkup c3 on c3.listname='PHYSICALWH' and c3.code=l3.physicalware " + "" + sqlwhere
					+ sqlrecheck + " group by o.performancedata04) c on  a.fully_qualified_id=c.performancedata04";
			workmap.put(wh, listbypage(sql));
		}
		List<WorkamountEntity> lists = new ArrayList<>();

		List<WorkamountEntity> listspage = new ArrayList<>();
		// 第一层循环拿每个仓的list
		for (List<WorkamountEntity> work : workmap.values()) {
			// 第二层循环拿到每个仓的数据
			for (WorkamountEntity workamountEntity : work) {
				lists.add(workamountEntity);
			}
		}
		// 拿到名字的map集合用以去重
		Map<String, String> nameMap = new HashMap<>();
		for (int i = 0; i < lists.size(); i++) {
			if (!"null".equals(lists.get(i).getUsername()) && lists.get(i).getUsername() != null) {
				nameMap.put(lists.get(i).getUsername(), String.valueOf(i));
			} else if (!"null".equals(lists.get(i).getPickname()) && lists.get(i).getPickname() != null) {
				nameMap.put(lists.get(i).getPickname(), String.valueOf(i));
			} else if (!"null".equals(lists.get(i).getRecheckname()) && lists.get(i).getPickname() != null) {
				nameMap.put(lists.get(i).getRecheckname(), String.valueOf(i));
			} else {
				// 操作员为空的情况
			}
		}
		/*nameMap.put("null", String.valueOf(-1));*/
		for (String s : nameMap.keySet()) {
			WorkamountEntity entity = new WorkamountEntity();
			for (int i = 0; i < lists.size(); i++) {
				/*System.out.println(s+"||"+i+"----------");*/
				/*System.out.println("null".equals(lists.get(i).getUsername())&&"null".equals(lists.get(i).getPickname())&&"null".equals(lists.get(i).getRecheckname()));*/
				if (!"null".equals(lists.get(i).getUsername()) && lists.get(i).getUsername() != null
						&& s.equals(lists.get(i).getUsername())) {
					entity.setUsername(s);
					entity.setSonamesum(sumbyinfor(entity.getSonamesum(), lists.get(i).getSonamesum()));
					entity.setSoskusum(sumbyinfor(entity.getSoskusum(), lists.get(i).getSoskusum()));
					entity.setSoblocsum(sumbyinfor(entity.getSoblocsum(), lists.get(i).getSoblocsum()));
					entity.setSoslocsum(sumbyinfor(entity.getSoslocsum(), lists.get(i).getSoslocsum()));
					entity.setSolpnsum(sumbyinfor(entity.getSolpnsum(), lists.get(i).getSolpnsum()));
				}
				if (!"null".equals(lists.get(i).getPickname()) && lists.get(i).getPickname() != null
						&& s.equals(lists.get(i).getPickname())) {
					entity.setPickname(s);
					entity.setPicknamesum(sumbyinfor(entity.getPicknamesum(), lists.get(i).getPicknamesum()));
					entity.setPickskusum(sumbyinfor(entity.getPickskusum(), lists.get(i).getPickskusum()));
					entity.setPickblocsum(sumbyinfor(entity.getPickblocsum(), lists.get(i).getPickblocsum()));
					entity.setPickslocsum(sumbyinfor(entity.getPickslocsum(), lists.get(i).getPickslocsum()));
					entity.setPicklpnsum(sumbyinfor(entity.getPicklpnsum(), lists.get(i).getPicklpnsum()));
				}
				if (!"null".equals(lists.get(i).getRecheckname()) && lists.get(i).getRecheckname() != null
						&& s.equals(lists.get(i).getRecheckname())) {
					entity.setRecheckname(s);
					entity.setRcnamesum(sumbyinfor(entity.getRcnamesum(), lists.get(i).getRcnamesum()));
					entity.setRcskusum(sumbyinfor(entity.getRcskusum(), lists.get(i).getRcskusum()));
					entity.setRcblocsum(sumbyinfor(entity.getRcblocsum(), lists.get(i).getRcblocsum()));
					entity.setPickslocsum(sumbyinfor(entity.getPickslocsum(), lists.get(i).getPickslocsum()));
					entity.setRclpnsum(sumbyinfor(entity.getRclpnsum(), lists.get(i).getRclpnsum()));
				}
				/*if (("null".equals(lists.get(i).getUsername()) || lists.get(i).getUsername() == null)
						&& ("null".equals(lists.get(i).getPickname()) || lists.get(i).getPickname() == null)
						&& ("null".equals(lists.get(i).getRecheckname()) || lists.get(i).getPickname() == null)) {
					entity.setSonamesum(sumbyinfor(entity.getSonamesum(), lists.get(i).getSonamesum()));
					entity.setSoskusum(sumbyinfor(entity.getSoskusum(), lists.get(i).getSoskusum()));
					entity.setSoblocsum(sumbyinfor(entity.getSoblocsum(), lists.get(i).getSoblocsum()));
					entity.setSoslocsum(sumbyinfor(entity.getSoslocsum(), lists.get(i).getSoslocsum()));
					entity.setSolpnsum(sumbyinfor(entity.getSolpnsum(), lists.get(i).getSolpnsum()));
					//
					entity.setPicknamesum(sumbyinfor(entity.getPicknamesum(), lists.get(i).getPicknamesum()));
					entity.setPickskusum(sumbyinfor(entity.getPickskusum(), lists.get(i).getPickskusum()));
					entity.setPickblocsum(sumbyinfor(entity.getPickblocsum(), lists.get(i).getPickblocsum()));
					entity.setPickslocsum(sumbyinfor(entity.getPickslocsum(), lists.get(i).getPickslocsum()));
					entity.setPicklpnsum(sumbyinfor(entity.getPicklpnsum(), lists.get(i).getPicklpnsum()));
					//
					entity.setRcnamesum(sumbyinfor(entity.getRcnamesum(), lists.get(i).getRcnamesum()));
					entity.setRcskusum(sumbyinfor(entity.getRcskusum(), lists.get(i).getRcskusum()));
					entity.setRcblocsum(sumbyinfor(entity.getRcblocsum(), lists.get(i).getRcblocsum()));
					entity.setPickslocsum(sumbyinfor(entity.getPickslocsum(), lists.get(i).getPickslocsum()));
					entity.setRclpnsum(sumbyinfor(entity.getRclpnsum(), lists.get(i).getRclpnsum()));
				}*/
			}
			worklist.add(entity);
		}

		/*
		 * // 合并的操作 for (int i = 0; i < lists.size() - 1; i++) { for (int j =
		 * lists.size() - 1; j > i; j--) { // 如果名称相同，合并并清除lists的值 boolean flag = false;
		 * System.out.println(lists.get(i).getUsername() + "||" +
		 * lists.get(j).getUsername()); if (lists.get(i).getUsername() != "null" &&
		 * lists.get(j).getUsername() != "null" &&
		 * lists.get(i).getUsername().equals(lists.get(j).getUsername())) {
		 * lists.get(i).setSonamesum(lists.get(i).getSonamesum() +
		 * lists.get(j).getSonamesum());
		 * lists.get(i).setSoskusum(lists.get(i).getSoskusum() +
		 * lists.get(j).getSoskusum());
		 * lists.get(i).setSoblocsum(lists.get(i).getSoblocsum() +
		 * lists.get(j).getSoblocsum());
		 * lists.get(i).setSoslocsum(lists.get(i).getSoslocsum() +
		 * lists.get(j).getSoslocsum());
		 * lists.get(i).setSolpnsum(lists.get(i).getSolpnsum() +
		 * lists.get(j).getSolpnsum()); flag = true; }
		 * System.out.println(lists.get(i).getPickname() + "||" +
		 * lists.get(j).getPickname()); if (lists.get(i).getPickname() != "null" &&
		 * lists.get(j).getPickname() != "null" &&
		 * lists.get(i).getPickname().equals(lists.get(j).getPickname())) {
		 * lists.get(i).setPicknamesum(lists.get(i).getPicknamesum() +
		 * lists.get(j).getPicknamesum());
		 * lists.get(i).setPickskusum(lists.get(i).getPickskusum() +
		 * lists.get(i).getPickskusum());
		 * lists.get(i).setPickblocsum(lists.get(i).getPickblocsum() +
		 * lists.get(j).getPickblocsum());
		 * lists.get(i).setPickslocsum(lists.get(i).getPickslocsum() +
		 * lists.get(j).getPickslocsum());
		 * lists.get(i).setPicklpnsum(lists.get(i).getPicklpnsum() +
		 * lists.get(j).getPicklpnsum()); flag = true; }
		 * System.out.println(lists.get(i).getRecheckname() + "||" +
		 * lists.get(j).getRecheckname()); if (lists.get(i).getRecheckname() != "null"
		 * && lists.get(j).getRecheckname() != "null" &&
		 * lists.get(i).getRecheckname().equals(lists.get(j).getRecheckname())) {
		 * lists.get(i).setRcnamesum(lists.get(i).getRcnamesum() +
		 * lists.get(j).getRcnamesum());
		 * lists.get(i).setRcblocsum(lists.get(i).getRcblocsum() +
		 * lists.get(j).getRcblocsum());
		 * lists.get(i).setRcslocsum(lists.get(i).getRcslocsum() +
		 * lists.get(j).getRcslocsum());
		 * lists.get(i).setRcskusum(lists.get(i).getRcskusum() +
		 * lists.get(j).getRcskusum());
		 * lists.get(i).setRclpnsum(lists.get(i).getRclpnsum() +
		 * lists.get(j).getRclpnsum()); flag = true; } if (flag) {
		 * listremove.add(lists.get(j)); } } }
		 */

		// 分页操作page 1 rows 10 page 2 rows 10
		if (worklist.size() >= page * rows) {
			for (int i = (page - 1) * rows; i < page * rows; i++) {
				listspage.add(worklist.get(i));
			}
		} else {
			for (int i = (page - 1) * rows; i < worklist.size(); i++) {
				listspage.add(worklist.get(i));
			}
		}

		/*
		 * sql =
		 * "select d.fully_qualified_id,d.performancedata01,d.performancedata04,sum(distinct d.sonamesum),sum(d.soskusum),sum(d.soblocsum),sum(d.soslocsum),sum(d.solpnsum),"
		 * +
		 * " sum(distinct d.picknamesum),sum(d.pickskusum),sum(d.pickblocsum),sum(d.pickslocsum),sum(d.picklpnsum),"
		 * +
		 * " sum(distinct d.rcnamesum),sum(d.rcskusum),sum(d.rcblocsum),sum(d.rcslocsum),sum(d.rclpnsum) from ("
		 * + sql +
		 * ") d group by d.fully_qualified_id,d.performancedata01,d.performancedata04";
		 * dataGrid = paging(sql, page, rows, dataGrid);
		 */
		dataGrid.setTotal(worklist.size());
		dataGrid.setPage(page);
		dataGrid.setRows(rows);
		dataGrid.setResults(listspage);
		TagUtil.datagrid(response, dataGrid);
	}

	// 计算的方法
	private String sumbyinfor(String key1, String key2) {
		int k1 = 0;
		int k2 = 0;
		if (key1 != null && !"null".equals(key1)) {
			k1 = Integer.parseInt(key1);
		}
		if (key2 != null && !"null".equals(key2)) {
			k2 = Integer.parseInt(key2);
		}
		return String.valueOf(k1 + k2);
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
				TSDepart depart = workamountService.findUniqueByProperty(TSDepart.class, "orgCode", area);
				areaSql.add("'" + depart.getDepartname() + "'");
			}
		} else if (!("").equals(offices) && offices != null) {
			String[] officeList = offices.split(",");
			for (String office : officeList) {
				TSDepart depart = workamountService.findUniqueByProperty(TSDepart.class, "orgCode", office);
				areaSql.addAll(getAreaByDepart(depart));
			}

		} else if (!("").equals(departments) && departments != null) {
			String[] departmentList = departments.split(",");
			for (String department : departmentList) {
				TSDepart depart = workamountService.findUniqueByProperty(TSDepart.class, "orgCode", department);
				areaSql.addAll(getAreaByDepart(depart));
			}

		} else if (!("").equals(regions) && regions != null) {
			String[] regiontList = regions.split(",");
			for (String region : regiontList) {
				TSDepart depart = workamountService.findUniqueByProperty(TSDepart.class, "orgCode", region);
				areaSql.addAll(getAreaByDepart(depart));
			}
		}

		return Joiner.on(",").join(areaSql);
	}

	private List<String> getAreaByDepart(TSDepart depart) {
		// TODO Auto-generated method stub
		List<String> areaSql = new ArrayList<>();
		List<TSDepart> departs = workamountService.findHql("from TSDepart where TSPDepart.id=?", depart.getId());
		for (TSDepart tsDepart : departs) {
			List<TSDepart> tdeparts = workamountService.findHql("from TSDepart where TSPDepart.id=?", tsDepart.getId());
			if (tdeparts.size() > 0) {
				areaSql.addAll(getAreaByDepart(tsDepart));
			} else {
				areaSql.add("'" + tsDepart.getDepartname() + "'");
			}
		}
		return areaSql;
	}

	// 仓库字典code 转换
	private String typeNameToTypeCode(String typeName, String typegroupname) {
		List<TSTypegroup> tsTypegroup = systemService.findHql("from TSTypegroup where typegroupname=?", typegroupname);
		List<TSType> tsType = systemService.findHql("from TSType where TSTypegroup.id=? and typename=?",
				tsTypegroup.get(0).getId(), typeName);
		return tsType.get(0).getTypecode();
	}

	/**
	 * 优化操作
	 * 
	 * @param sql
	 * @param page
	 * @param rows
	 * @param dataGrid
	 * @return
	 */
	private List<WorkamountEntity> listbypage(String sql) {
		List<WorkamountEntity> list = new ArrayList<>();
		List<Object[]> resultList = workamountService.findListbySql(sql);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] result : resultList) {
			WorkamountEntity entity = new WorkamountEntity();
			if (String.valueOf(result[0]) != "null") {
				entity.setUsername(String.valueOf(result[0]));
			}
			//
			// so
			entity.setSonamesum(String.valueOf(result[1]));
			entity.setSoskusum(String.valueOf(result[2]));
			entity.setSoblocsum(String.valueOf(result[3]));
			entity.setSoslocsum(String.valueOf(result[4]));
			entity.setSolpnsum(String.valueOf(result[5]));
			// pick
			entity.setPickname(String.valueOf(result[6]));
			entity.setPicknamesum(String.valueOf(result[7]));
			entity.setPickskusum(String.valueOf(result[8]));
			entity.setPickblocsum(String.valueOf(result[9]));
			entity.setPickslocsum(String.valueOf(result[10]));
			entity.setPicklpnsum(String.valueOf(result[11]));
			// reheck
			entity.setRecheckname(String.valueOf(result[12]));
			entity.setRcnamesum(String.valueOf(result[13]));
			entity.setRcskusum(String.valueOf(result[14]));
			entity.setRcblocsum(String.valueOf(result[15]));
			entity.setRcslocsum(String.valueOf(result[16]));
			entity.setRclpnsum(String.valueOf(result[17]));
			list.add(entity);
		}
		return list;
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
		List<String> totalList = workamountService.findListbySql(totalSql);
		int total = Integer.parseInt(String.valueOf(totalList.get(0)));

		List<WorkamountEntity> list = new ArrayList<>();
		String resultSql = pagingByOracle(sql, page, rows);
		List<Object[]> resultList = workamountService.findListbySql(resultSql);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] result : resultList) {
			WorkamountEntity entity = new WorkamountEntity();
			if (String.valueOf(result[0]) != "null") {
				entity.setUsername(String.valueOf(result[0]));
			}
			if (String.valueOf(result[1]) != "null") {
				entity.setPickname(String.valueOf(result[1]));
			}
			if (String.valueOf(result[2]) != "null") {
				entity.setRecheckname(String.valueOf(result[2]));
			}
			entity.setSonamesum(String.valueOf(result[3]));
			entity.setSoskusum(String.valueOf(result[4]));
			entity.setSoblocsum(String.valueOf(result[5]));
			entity.setSoslocsum(String.valueOf(result[6]));
			entity.setSolpnsum(String.valueOf(result[7]));
			entity.setPicknamesum(String.valueOf(result[8]));
			entity.setPickskusum(String.valueOf(result[9]));
			entity.setPickblocsum(String.valueOf(result[10]));
			entity.setPickslocsum(String.valueOf(result[11]));
			entity.setPicklpnsum(String.valueOf(result[12]));
			entity.setRcnamesum(String.valueOf(result[13]));
			entity.setRcskusum(String.valueOf(result[14]));
			entity.setRcblocsum(String.valueOf(result[15]));
			entity.setRcslocsum(String.valueOf(result[16]));
			entity.setRclpnsum(String.valueOf(result[17]));
			list.add(entity);
		}
		dataGrid.setPage(page);
		dataGrid.setRows(rows);
		dataGrid.setTotal(total);
		dataGrid.setResults(list);
		return dataGrid;
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

	// null
	private String isnull(String value) {
		if ("null".equals(value)) {
			value = "";
		}
		return value;
	}

	/**
	 * 删除员工操作量
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WorkamountEntity workamount, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		workamount = systemService.getEntity(WorkamountEntity.class, workamount.getId());
		message = "员工操作量删除成功";
		try {
			workamountService.delete(workamount);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "员工操作量删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除员工操作量
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工操作量删除成功";
		try {
			for (String id : ids.split(",")) {
				WorkamountEntity workamount = systemService.getEntity(WorkamountEntity.class, id);
				workamountService.delete(workamount);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "员工操作量删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加员工操作量
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(WorkamountEntity workamount, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工操作量添加成功";
		try {
			workamountService.save(workamount);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "员工操作量添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新员工操作量
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(WorkamountEntity workamount, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工操作量更新成功";
		WorkamountEntity t = workamountService.get(WorkamountEntity.class, workamount.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(workamount, t);
			workamountService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "员工操作量更新失败";
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
		req.setAttribute("controller_name", "workamountController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	// 导出执行sql
	private List<WorkamountEntity> exportSQL(String sql) {

		List<WorkamountEntity> list = new ArrayList<>();
		List<Object[]> resultList = workamountService.findListbySql(sql);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] result : resultList) {
			WorkamountEntity entity = new WorkamountEntity();
			if (String.valueOf(result[0]) != "null") {
				entity.setUsername(String.valueOf(result[0]));
			}
			if (String.valueOf(result[1]) != "null") {
				entity.setPickname(String.valueOf(result[1]));
			}
			if (String.valueOf(result[2]) != "null") {
				entity.setRecheckname(String.valueOf(result[2]));
			}
			entity.setSonamesum(String.valueOf(result[3]));
			entity.setSoskusum(String.valueOf(result[4]));
			entity.setSoblocsum(String.valueOf(result[5]));
			entity.setSoslocsum(String.valueOf(result[6]));
			entity.setSolpnsum(String.valueOf(result[7]));
			entity.setPicknamesum(String.valueOf(result[8]));
			entity.setPickskusum(String.valueOf(result[9]));
			entity.setPickblocsum(String.valueOf(result[10]));
			entity.setPickslocsum(String.valueOf(result[11]));
			entity.setPicklpnsum(String.valueOf(result[12]));
			entity.setRcnamesum(String.valueOf(result[13]));
			entity.setRcskusum(String.valueOf(result[14]));
			entity.setRcblocsum(String.valueOf(result[15]));
			entity.setRcslocsum(String.valueOf(result[16]));
			entity.setRclpnsum(String.valueOf(result[17]));
			list.add(entity);
		}
		return list;
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(WorkamountEntity workamount, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		/*
		 * CriteriaQuery cq = new CriteriaQuery(WorkamountEntity.class, dataGrid);
		 * org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
		 * workamount, request.getParameterMap()); List<WorkamountEntity> workamounts =
		 * this.workamountService.getListByCriteriaQuery(cq, false);
		 */
		// 参数
		String areas = request.getParameter("seracharea");
		String regions = request.getParameter("region");
		String departments = request.getParameter("department");
		String offices = request.getParameter("office");
		String datestart = request.getParameter("datestart");
		String dateend = request.getParameter("dateend");
		String areasql = getAllArea(regions, departments, offices, areas);
		TSUser user = ResourceUtil.getSessionUser();
		String wh = "";
		String sqlwhere = "where 1=1 ";
		String sqlpick = "";
		String sqlrecheck = "";
		String sqlso = "";
		String sqlItrn = "";
		if (datestart != null && !"".equals(datestart) && dateend != null && !"".equals(dateend)) {
			sqlpick = " and o.pickenddate+8/24 between to_date('" + datestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + dateend + "','yyyy-MM-dd HH24:mi:ss')";

			sqlrecheck = " and o.recheckenddate+8/24 between to_date('" + datestart
					+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + dateend + "','yyyy-MM-dd HH24:mi:ss')";

			sqlso = " and r.editdate+8/24 between to_date('" + datestart + "','yyyy-MM-dd HH24:mi:ss') and to_date('"
					+ dateend + "','yyyy-MM-dd HH24:mi:ss')";

			sqlItrn = " and i.editdate+8/24 between to_date('" + datestart + "','yyyy-MM-dd HH24:mi:ss') and to_date('"
					+ dateend + "','yyyy-MM-dd HH24:mi:ss')";
		}
		// 仓库// 区域
		if (areasql != null && !"".equals(areasql)) {
			sqlwhere += " and c3.description in (" + areasql + ")";
		}
		String sql = "";
		List<UsercontactwhEntity> entities = workamountService.findHql("from UsercontactwhEntity where userid=?",
				user.getId());
		if (entities != null && entities.size() > 0) {
			List<String> list = new ArrayList<>();
			for (UsercontactwhEntity u : entities) {
				list.add(u.getWarehouse().toString());
			}
		}
		List<WorkamountEntity> worklist = new ArrayList<>();
		Map<String, List<WorkamountEntity>> workmap = new HashMap<>();
		for (int i = 0; i < entities.size(); i++) {
			wh = typeNameToTypeCode(entities.get(i).getWarehouse(), "仓库");
			sql = "select * from (select op.fully_qualified_id,count(distinct r.receiptkey) sonamesum,"
					+ "count(distinct r.sku) soskusum," + "count(distinct l.loc) soblocsum,"
					+ "count(distinct l2.loc) soslocsum," + "count(distinct r.toid) solpnsum " + "from " + wh
					+ "_Receiptdetail r " + "left join (select i.toloc,i.addwho from " + wh
					+ "_Itrn i  where i.sourcetype in ('NSPRFRL01','NSPRFPA02') " + sqlItrn
					+ ") s on r.addwho=s.addwho " + "left join " + wh
					+ "_loc l on s.toloc = l.loc and l.locnature <> 'S' " + "left join " + wh
					+ "_loc l2 on s.toloc= l2.loc and l2.locnature = 'S' " + "left join " + wh
					+ "_Loc l3 on s.toloc=l3.loc " + "left join " + wh
					+ "_Codelkup c3 on c3.listname='PHYSICALWH' and c3.code=l3.physicalware left join oper.e_sso_user op on op.sso_user_name=r.addwho "
					+ "" + sqlwhere + sqlso + " group by op.fully_qualified_id) a  " + " full join  (select  "
					+ "o.performancedata01, " + "count(o.performancedata01) picknamesum, "
					+ "count(distinct pk.sku) pickskusum, " + "count(distinct l.loc) pickblocsum, "
					+ "count(distinct l2.loc) pickslocsum, " + "count(distinct pk.id) picklpnsum " + "from " + wh
					+ "_orders o " + "left join " + wh + "_Pickdetail pk on o.orderkey=pk.orderkey " + "left join " + wh
					+ "_loc l on nvl(trim(pk.fromloc),pk.loc) = l.loc and l.locnature <> 'S' " + "left join " + wh
					+ "_loc l2 on nvl(trim(pk.fromloc),pk.loc) = l2.loc and l2.locnature = 'S' " + "left join " + wh
					+ "_Loc l3 on nvl(trim(pk.fromloc),pk.loc)=l3.loc " + "left join " + wh
					+ "_Codelkup c3 on c3.listname='PHYSICALWH' and c3.code=l3.physicalware " + "" + sqlwhere + sqlpick
					+ " group by o.performancedata01) b on a.fully_qualified_id=b.performancedata01  "
					+ " full join (select  " + "o.performancedata04, " + "count(o.performancedata04) rcnamesum, "
					+ "count(distinct pk.sku) rcskusum, " + "count(distinct l.loc) rcblocsum, "
					+ "count(distinct l2.loc) rcslocsum, " + "count(distinct pk.id) rclpnsum " + "from " + wh
					+ "_orders o " + "left join " + wh + "_Pickdetail pk on o.orderkey=pk.orderkey " + "left join " + wh
					+ "_loc l on nvl(trim(pk.fromloc),pk.loc) = l.loc and l.locnature <> 'S' " + "left join " + wh
					+ "_loc l2 on nvl(trim(pk.fromloc),pk.loc) = l2.loc and l2.locnature = 'S' " + "left join " + wh
					+ "_Loc l3 on nvl(trim(pk.fromloc),pk.loc)=l3.loc " + "left join " + wh
					+ "_Codelkup c3 on c3.listname='PHYSICALWH' and c3.code=l3.physicalware " + "" + sqlwhere
					+ sqlrecheck + " group by o.performancedata04) c on  a.fully_qualified_id=c.performancedata04";
			workmap.put(wh, listbypage(sql));
		}
		List<WorkamountEntity> lists = new ArrayList<>();

		List<WorkamountEntity> listspage = new ArrayList<>();
		// 第一层循环拿每个仓的list
		for (List<WorkamountEntity> work : workmap.values()) {
			// 第二层循环拿到每个仓的数据
			for (WorkamountEntity workamountEntity : work) {
				lists.add(workamountEntity);
			}
		}
		// 拿到名字的map集合用以去重
		Map<String, String> nameMap = new HashMap<>();
		for (int i = 0; i < lists.size(); i++) {
			if (!"null".equals(lists.get(i).getUsername()) && lists.get(i).getUsername() != null) {
				nameMap.put(lists.get(i).getUsername(), String.valueOf(i));
			} else if (!"null".equals(lists.get(i).getPickname()) && lists.get(i).getPickname() != null) {
				nameMap.put(lists.get(i).getPickname(), String.valueOf(i));
			} else if (!"null".equals(lists.get(i).getRecheckname()) && lists.get(i).getPickname() != null) {
				nameMap.put(lists.get(i).getRecheckname(), String.valueOf(i));
			} else {
				// 操作员为空的情况
				nameMap.put(lists.get(i).getRecheckname(), String.valueOf(i));
			}
		}
		for (String s : nameMap.keySet()) {
			WorkamountEntity entity = new WorkamountEntity();
			for (int i = 0; i < lists.size(); i++) {
				if (!"null".equals(lists.get(i).getUsername()) && lists.get(i).getUsername() != null
						&& s.equals(lists.get(i).getUsername())) {
					entity.setUsername(s);
					entity.setSonamesum(sumbyinfor(entity.getSonamesum(), lists.get(i).getSonamesum()));
					entity.setSoskusum(sumbyinfor(entity.getSoskusum(), lists.get(i).getSoskusum()));
					entity.setSoblocsum(sumbyinfor(entity.getSoblocsum(), lists.get(i).getSoblocsum()));
					entity.setSoslocsum(sumbyinfor(entity.getSoslocsum(), lists.get(i).getSoslocsum()));
					entity.setSolpnsum(sumbyinfor(entity.getSolpnsum(), lists.get(i).getSolpnsum()));
				}
				if (!"null".equals(lists.get(i).getPickname()) && lists.get(i).getPickname() != null
						&& s.equals(lists.get(i).getPickname())) {
					entity.setPickname(s);
					entity.setPicknamesum(sumbyinfor(entity.getPicknamesum(), lists.get(i).getPicknamesum()));
					entity.setPickskusum(sumbyinfor(entity.getPickskusum(), lists.get(i).getPickskusum()));
					entity.setPickblocsum(sumbyinfor(entity.getPickblocsum(), lists.get(i).getPickblocsum()));
					entity.setPickslocsum(sumbyinfor(entity.getPickslocsum(), lists.get(i).getPickslocsum()));
					entity.setPicklpnsum(sumbyinfor(entity.getPicklpnsum(), lists.get(i).getPicklpnsum()));
				}
				if (!"null".equals(lists.get(i).getRecheckname()) && lists.get(i).getRecheckname() != null
						&& s.equals(lists.get(i).getRecheckname())) {
					entity.setRecheckname(s);
					entity.setRcnamesum(sumbyinfor(entity.getRcnamesum(), lists.get(i).getRcnamesum()));
					entity.setRcskusum(sumbyinfor(entity.getRcskusum(), lists.get(i).getRcskusum()));
					entity.setRcblocsum(sumbyinfor(entity.getRcblocsum(), lists.get(i).getRcblocsum()));
					entity.setPickslocsum(sumbyinfor(entity.getPickslocsum(), lists.get(i).getPickslocsum()));
					entity.setRclpnsum(sumbyinfor(entity.getRclpnsum(), lists.get(i).getRclpnsum()));
				}

			}
			worklist.add(entity);
		}

		modelMap.put(NormalExcelConstants.FILE_NAME, "员工操作量");
		modelMap.put(NormalExcelConstants.CLASS, WorkamountEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("员工操作量列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, worklist);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(WorkamountEntity workamount, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "员工操作量");
		modelMap.put(NormalExcelConstants.CLASS, WorkamountEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("员工操作量列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
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
				List<WorkamountEntity> listWorkamountEntitys = ExcelImportUtil.importExcel(file.getInputStream(),
						WorkamountEntity.class, params);
				for (WorkamountEntity workamount : listWorkamountEntitys) {
					workamountService.save(workamount);
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

}
