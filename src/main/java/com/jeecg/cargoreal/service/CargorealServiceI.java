package com.jeecg.cargoreal.service;
import com.jeecg.cargoreal.entity.CargorealEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface CargorealServiceI extends CommonService{
	
 	public void delete(CargorealEntity entity) throws Exception;
 	
 	public Serializable save(CargorealEntity entity) throws Exception;
 	
 	public void saveOrUpdate(CargorealEntity entity) throws Exception;
 	
}
