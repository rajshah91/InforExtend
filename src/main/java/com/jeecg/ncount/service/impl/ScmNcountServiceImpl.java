package com.jeecg.ncount.service.impl;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeecg.ncount.entity.ScmNcountEntity;
import com.jeecg.ncount.service.ScmNcountServiceI;

@Service("scmNcountService")
@Transactional
public class ScmNcountServiceImpl extends CommonServiceImpl implements ScmNcountServiceI {

	
 	public void delete(ScmNcountEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(ScmNcountEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(ScmNcountEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(ScmNcountEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(ScmNcountEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(ScmNcountEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(ScmNcountEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("create_name", t.getCreateName());
		map.put("create_by", t.getCreateBy());
		map.put("create_date", t.getCreateDate());
		map.put("update_name", t.getUpdateName());
		map.put("update_by", t.getUpdateBy());
		map.put("update_date", t.getUpdateDate());
		map.put("sys_org_code", t.getSysOrgCode());
		map.put("sys_company_code", t.getSysCompanyCode());
		map.put("bpm_status", t.getBpmStatus());
		map.put("keyname", t.getKeyname());
		map.put("keyvalue", t.getKeyvalue());
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,ScmNcountEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{create_name}",String.valueOf(t.getCreateName()));
 		sql  = sql.replace("#{create_by}",String.valueOf(t.getCreateBy()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_name}",String.valueOf(t.getUpdateName()));
 		sql  = sql.replace("#{update_by}",String.valueOf(t.getUpdateBy()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{sys_org_code}",String.valueOf(t.getSysOrgCode()));
 		sql  = sql.replace("#{sys_company_code}",String.valueOf(t.getSysCompanyCode()));
 		sql  = sql.replace("#{bpm_status}",String.valueOf(t.getBpmStatus()));
 		sql  = sql.replace("#{keyname}",String.valueOf(t.getKeyname()));
 		sql  = sql.replace("#{keyvalue}",String.valueOf(t.getKeyvalue()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
 	
 	/**
	 * 执行JAVA增强
	 */
 	private void executeJavaExtend(String cgJavaType,String cgJavaValue,Map<String,Object> data) throws Exception {
 		if(StringUtil.isNotEmpty(cgJavaValue)){
			Object obj = null;
			try {
				if("class".equals(cgJavaType)){
					//因新增时已经校验了实例化是否可以成功，所以这块就不需要再做一次判断
					obj = MyClassLoader.getClassByScn(cgJavaValue).newInstance();
				}else if("spring".equals(cgJavaType)){
					obj = ApplicationContextUtil.getContext().getBean(cgJavaValue);
				}
				if(obj instanceof CgformEnhanceJavaInter){
					CgformEnhanceJavaInter javaInter = (CgformEnhanceJavaInter) obj;
					javaInter.execute("scm_ncount",data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			} 
		}
 	}
 	
 	@Transactional(propagation=Propagation.REQUIRES_NEW)
 	public int getNextKey(String keyName) throws Exception{
 		String hql = "from ScmNcountEntity where keyname = ?";
		List<ScmNcountEntity> scmNcounterEntitylist = this.findHql(hql, keyName);
		int keyvalue = 1;
		ScmNcountEntity mne = null;
		if(scmNcounterEntitylist.size()>0){
			mne = scmNcounterEntitylist.get(0);
			keyvalue = mne.getKeyvalue();
			keyvalue++;
			mne.setKeyvalue(keyvalue);
		}else{
			mne = new ScmNcountEntity();
			mne.setKeyname(keyName);
			mne.setKeyvalue(keyvalue);
		}
		this.saveOrUpdate(mne);
		return keyvalue;
 	}
 	
	@Override
	public String getNextKey(String keyName, int len) throws Exception {
		return String.format("%0"+len+"d", getNextKey(keyName));
	}

	@Override
	public String getNextKeyWithKeyName(String keyName, int len) throws Exception {
		int keyvalue = getNextKey(keyName);
		if(len<=keyName.length()) return keyName+keyvalue;
		//String.format("%, args)
		return keyName+String.format("%0"+(len-keyName.length())+"d", keyvalue);
	}

	@Override
	public String getNextKeyWithYMD(String keyName, int len) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sf.format(c.getTime());
		int keyvalue = getNextKey(keyName+ymd);
		return ymd + String.format("%0" + (len-ymd.length()) + "d", keyvalue);
	}

}