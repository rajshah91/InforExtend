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
			
			String totalSql="";
			String smallSql="";
			String smallAllSql="";
			String bigSql="";
			String bigAllSql="";
			
			for (int i = 0; i < usercontactwhEntities.size(); i++) {
				if(usercontactwhEntities.get(i).getWarehouse().contains("FEILI_wmwhse")) {
					String wh=typeNameToTypeCode(usercontactwhEntities.get(i).getWarehouse(), "仓库");
					if(i!=0) {
						totalSql+=" union all ";
						smallSql+=" union all ";
						smallAllSql+=" union all ";
						bigSql+=" union all ";
						bigAllSql+=" union all ";
					}
					totalSql+=" select loc.loc,cu.description," + 
							"       (select count(distinct l.lot) from "+wh+"_lotxlocxid l where l.loc = loc.loc) as countloc" + 
							"       from "+wh+"_loc loc " + 
							" left join "+wh+"_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
							" where  loc.disuse <> '0'";
					smallSql+=" select loc.loc,cu.description," + 
							"       (select count(distinct l.lot) from "+wh+"_lotxlocxid l where l.loc = loc.loc) as countloc" + 
							"       from "+wh+"_loc loc " + 
							" left join "+wh+"_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
							" where loc.locnature='S' and loc.disuse <> '0'" ;
					smallAllSql+=" select loc.loc,cu.description" + 
							"       from "+wh+"_loc loc " + 
							" left join "+wh+"_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
							" where loc.locnature='S' and loc.disuse <> '0'";
					bigSql+=" select loc.loc,cu.description," + 
							"       (select count(distinct l.lot) from "+wh+"_lotxlocxid l where l.loc = loc.loc) as countloc" + 
							"       from "+wh+"_loc loc " + 
							" left join "+wh+"_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
							" where loc.locnature <> 'S' and loc.disuse <> '0'";
					bigAllSql+=" select loc.loc,cu.description" + 
							"       from "+wh+"_loc loc " + 
							" left join "+wh+"_CODELKUP cu ON cu.listname='PHYSICALWH' and cu.code=loc.physicalware" + 
							" where loc.locnature <> 'S' and loc.disuse <> '0'";
				}
			}
			
			String sql="select a2.description,ROUND(100*b1.bigcount/b2.bigcountall,1) as bloc," + 
					" ROUND(100*s1.smallcount/s2.smallcountall,1) as sloc," + 
					" ROUND(100*(nvl(b1.bigcount,0)+nvl(s1.smallcount,0))/totalcount,1) as totalrate,a2.totalcountall from ("+ 
					" select sum(a.countloc) as totalcountall,count(distinct a.loc) as totalcount,a.description from (" + 
					totalSql+ 
					" ) a group by a.description ) a2 left join (" + 
					" select count(distinct s.loc) as smallcount,s.description from (" + 
					smallSql+ 
					" ) s where  s.countloc>0 group by s.description ) s1  on  a2.description=s1.description" + 
					" left join (" + 
					" select count(distinct s.loc) as smallcountall,s.description from (" + 
					smallAllSql+ 
					" ) s group by s.description ) s2 on a2.description=s2.description " + 
					" left join (" + 
					" select count(distinct b.loc) as bigcount,b.description from (" + 
					bigSql+ 
					" ) b where  b.countloc>0 group by b.description ) b1  on  a2.description=b1.description" + 
					" left join (" + 
					" select count(distinct b.loc) as bigcountall,b.description from (" + 
					bigAllSql + 
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