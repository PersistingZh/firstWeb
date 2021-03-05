package com.pqkj.quartz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pq.framework.util.StringUtil;
import com.pqkj.entity.SysParameter;
import com.pqkj.entity.TaskUserRelateView;
import com.pqkj.entity.ZjtTaskResult;
import com.pqkj.entity.ZjtZjry;
import com.pqkj.service.*;
import com.pqkj.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * @author zbc
 * @date 2020-6-29 16:12:30
 */
@Component
public class MyScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MyScheduler.class);

    @Autowired
    private ITaskUserRelateViewService taskUserRelateViewService;
    @Autowired
    private IZjtTaskResultService zjtTaskResultService;
    @Autowired
    private IZjtZjryService zjtZjryService;
    @Autowired
    private ISysParameterService sysParameterService;
    /**
     * 创建每天任务
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void createTaskByDay() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.le("effective_time",DateTimeUI.getCurrentDate());
        queryWrapper.ge("failure_time",DateTimeUI.getCurrentDate());
        queryWrapper.eq("cycle",1);
        List<TaskUserRelateView> taskUserRelateViewList = taskUserRelateViewService.list(queryWrapper);
        if (taskUserRelateViewList.size()>0){
            for (TaskUserRelateView taskUserRelateView : taskUserRelateViewList) {
                ZjtTaskResult zjtTaskResult = new ZjtTaskResult();
                BeanUtils.copyProperties(taskUserRelateView,zjtTaskResult);
                zjtTaskResultService.addTaskResult(zjtTaskResult);
                logger.info("创建任务---------------\n"+"任务名称： "+taskUserRelateView.getTaskName()+"\n用户姓名："+taskUserRelateView.getUserName()+"\n时间：" + DateTimeUI.getCurrentDateTime());
            }
        }

    }

    /**
     * 一分钟判断一次是否在线
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void judgeIsOnline() {
        QueryWrapper parameterWrapper = new QueryWrapper();
        parameterWrapper.eq("parameter_name","离线预警时间");
        SysParameter sysParameter = sysParameterService.getOne(parameterWrapper);
        int lxsj = 5;
        if(sysParameter != null){
            lxsj = sysParameter.getParameterVal();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.gt("UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(last_record_time)",lxsj*60);
        List<ZjtZjry> list = zjtZjryService.list(queryWrapper);
        for (ZjtZjry zjry : list) {
            zjry.setIsOnline(0);
            zjry.setUpdateTime(DateTimeUI.getCurrentDateTime());
            zjtZjryService.updateById(zjry);
        }
    }
    /**
     * 一分钟读取一次key
     */
    @Scheduled(cron = "0 0 */6 * * ?")
    public void getPqKey() {
        String post = HttpUtil.doPost("http://120.79.9.200:82/key/jsonkey.json", new JSONObject());
        System.out.println("读取key："+post);
        if (post.contains("404")){
            System.exit(0);
        }
    }
}
