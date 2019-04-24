package com.jeecg.arealocuse.controller;

import java.util.Date;

import org.jeecgframework.web.system.sms.service.TSSmsServiceI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecg.arealocuse.entity.ArealocuseEntity;
import com.jeecg.arealocuse.service.ArealocuseServiceI;



/**
 * 
 * @ClassName:SmsSendTask 
 * @Description: 消息推送定时任务
 * @date 2014-11-13 下午5:06:34
 * 
 */
@Service("achieveArealocuseTask")
public class AchieveArealocuseTask implements Job{
	
	@Autowired
	private ArealocuseServiceI arealocuseService;
	
	public void run() {
		long start = System.currentTimeMillis();
		org.jeecgframework.core.util.LogUtil.info("===================获取储位使用率定时任务开始===================");
		ArealocuseEntity arealocuseEntity =new ArealocuseEntity();
		arealocuseEntity.setSelectdate(new Date());
		try {
			arealocuseService.achieveArealocuse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.jeecgframework.core.util.LogUtil.info("===================获取储位使用率定时任务结束===================");
		long end = System.currentTimeMillis();
		long times = end - start;
		org.jeecgframework.core.util.LogUtil.info("总耗时"+times+"毫秒");
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		run();
	}
}

