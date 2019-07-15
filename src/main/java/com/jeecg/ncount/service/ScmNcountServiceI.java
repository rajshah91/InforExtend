package com.jeecg.ncount.service;
import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.jeecg.ncount.entity.ScmNcountEntity;

public interface ScmNcountServiceI extends CommonService{
	
 	public void delete(ScmNcountEntity entity) throws Exception;
 	
 	public Serializable save(ScmNcountEntity entity) throws Exception;
 	
 	public void saveOrUpdate(ScmNcountEntity entity) throws Exception;
 	
 	public int getNextKey(String keyName) throws Exception;
 	
 	public String getNextKey(String keyName, int len) throws Exception;
 	
 	public String getNextKeyWithKeyName(String keyName, int len) throws Exception;
 	
 	public String getNextKeyWithYMD(String keyName, int len) throws Exception;

}
