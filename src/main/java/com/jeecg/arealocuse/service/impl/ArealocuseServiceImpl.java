package com.jeecg.arealocuse.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecg.arealocuse.entity.ArealocuseEntity;
import com.jeecg.arealocuse.service.ArealocuseServiceI;
import com.jeecg.cargoreal.entity.CargorealEntity;
import com.jeecg.usercontactwh.entity.UsercontactwhEntity;

@Service("arealocuseService")
@Transactional
public class ArealocuseServiceImpl extends CommonServiceImpl implements ArealocuseServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(ArealocuseEntity entity) throws Exception{
 		super.delete(entity);
 	}
 	
 	public Serializable save(ArealocuseEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(ArealocuseEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 	}

	@Override
	public void achieveArealocuse() throws Exception {
		// TODO Auto-generated method stub
		List<UsercontactwhEntity> usercontactwhEntities=this.findHql("from UsercontactwhEntity where username=?","admin");
		if(usercontactwhEntities.size()>0) {
			
			
			for (int i = 0; i < usercontactwhEntities.size(); i++) {
				if(usercontactwhEntities.get(i).getWarehouse().contains("FEILI_wmwhse")) {
					String wh=typeNameToTypeCode(usercontactwhEntities.get(i).getWarehouse(), "仓库");
				}
			}
			
			String sql="select a2.description,ROUND(b1.bigcount/b2.bigcountall,1) as bloc," + 
					" ROUND(s1.smallcount/s2.smallcountall,1) as sloc," + 
					" ROUND((b1.bigcount+s1.smallcount)/(b2.bigcountall+s2.smallcountall),1) as totalrate,a2.totalcountall from ("+ 
					" select sum(a.countloc) as totalcountall,count(distinct a.loc) as totalcount,a.description from (" + 
					" select loc.loc,cu.description," + 
					"       (select count(distinct l.lot) from w01_lotxlocxid l where l.loc = loc.loc) as countloc" + 
					"       from w01_loc loc " + 
					" left join w01_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
					" where  loc.disuse <> '0'" + 
					" ) a group by a.description ) a2 left join (" + 
					" select count(distinct s.loc) as smallcount,s.description from (" + 
					" select loc.loc,cu.description," + 
					"       (select count(distinct l.lot) from w01_lotxlocxid l where l.loc = loc.loc) as countloc" + 
					"       from w01_loc loc " + 
					" left join w01_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
					" where loc.locnature='S' and loc.disuse <> '0'" + 
					" ) s where  s.countloc>0 group by s.description ) s1  on  a2.description=s1.description" + 
					" left join (" + 
					" select count(distinct s.loc) as smallcountall,s.description from (" + 
					" select loc.loc,cu.description" + 
					"       from w01_loc loc " + 
					" left join w01_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
					" where loc.locnature='S' and loc.disuse <> '0'" + 
					" ) s group by s.description ) s2 on a2.description=s2.description " + 
					" left join (" + 
					" select count(distinct b.loc) as bigcount,b.description from (" + 
					" select loc.loc,cu.description," + 
					"       (select count(distinct l.lot) from w01_lotxlocxid l where l.loc = loc.loc) as countloc" + 
					"       from w01_loc loc " + 
					" left join w01_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
					" where loc.locnature <> 'S' and loc.disuse <> '0'" + 
					" ) b where  b.countloc>0 group by b.description ) b1  on  a2.description=b1.description" + 
					" left join (" + 
					" select count(distinct b.loc) as bigcountall,b.description from (" + 
					" select loc.loc,cu.description" + 
					"       from w01_loc loc " + 
					" left join w01_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
					" where loc.locnature <> 'S' and loc.disuse <> '0'" + 
					" ) b group by b.description ) b2 on a2.description=b2.description";
			
			List<Object[]> resultList= this.findListbySql(sql);
			for (Object[] result : resultList) {
				ArealocuseEntity arealocuseEntity=new ArealocuseEntity();
				arealocuseEntity.setSelectdate(new Date());
				arealocuseEntity.setArea(String.valueOf(result[0]));
				TSDepart areaDepart=this.findUniqueByProperty(TSDepart.class, "departname", arealocuseEntity.getArea());
				if(areaDepart!=null) {
					TSDepart operatsection=this.get(TSDepart.class, areaDepart.getTSPDepart().getId());
					arealocuseEntity.setOperatsection(operatsection.getDepartname());
					if(operatsection!=null) {
						TSDepart operatdepart=this.get(TSDepart.class, operatsection.getTSPDepart().getId());
						arealocuseEntity.setOperatdepart(operatdepart.getDepartname());
						if(operatdepart!=null) {
							TSDepart region=this.get(TSDepart.class, operatdepart.getTSPDepart().getId());
							arealocuseEntity.setRegion(region.getDepartname());
						}
					}
				}
				arealocuseEntity.setBloc(String.valueOf(result[1]));
				arealocuseEntity.setSloc(String.valueOf(result[2]));
				arealocuseEntity.setTotalrate(String.valueOf(result[3]));
				arealocuseEntity.setLpn(String.valueOf(result[4]));
				this.save(arealocuseEntity);
			}
		}
	}
 	
	//仓库字典code 转换
	private String typeNameToTypeCode(String typeName,String typegroupname) {
		List<TSTypegroup> tsTypegroup=this.findHql("from TSTypegroup where typegroupname=?", typegroupname);
		List<TSType> tsType=this.findHql("from TSType where TSTypegroup.id=? and typename=?",tsTypegroup.get(0).getId(),typeName);
		return tsType.get(0).getTypecode();
	}
		
}