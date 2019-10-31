package cn.zhixiangsingle.job;

import cn.zhixiangsingle.config.QuartzSchedulerBase;
import cn.zhixiangsingle.config.SpringBeanFactoryUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.haiKang.HaiKang;
import cn.zhixiangsingle.entity.base.leCheng.LeCheng;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.impl.redis.RedisServiceImpl;
import cn.zhixiangsingle.web.service.haiKangManager.impl.HaiKangManagerServiceImpl;
import cn.zhixiangsingle.web.service.userManager.impl.UserManagerServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.job
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 13:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class UpdateHKTokenJob implements Job {
    private static final Logger logger = LoggerFactory
            .getLogger(UpdateTokenJob.class);
    @Autowired
    private QuartzSchedulerBase quartzSchedulerBase;
    @Autowired
    private RedisServiceImpl redisService;
    @Bean(name = "redisServiceImpl")
    public RedisServiceImpl getRedisServiceImpl(){
        return redisService;
    }
    @Bean(name = "quartzSchedulerBase")
    public QuartzSchedulerBase getQuartzSchedulerBase(){
        return quartzSchedulerBase;
    }
    public UpdateHKTokenJob(){
        RedisServiceImpl redisServiceImpl = SpringBeanFactoryUtils.getBean("redisServiceImpl",RedisServiceImpl.class);
        this.redisService = redisServiceImpl;
        QuartzSchedulerBase quartzScheduler = SpringBeanFactoryUtils.getBean("quartzSchedulerBase",QuartzSchedulerBase.class);
        this.quartzSchedulerBase = quartzScheduler;
    }
    private void before(){
        System.out.println("更新海康token任务开始执行_____update token start");
    }
    @Override
    public void execute(JobExecutionContext arg0){
        before();
        try {
            StringBuffer stringBufferJson = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                stringBufferJson.append(line);
            }
            JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
            for(String str:jobj.keySet()){
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(str))){
                    siteData = JSON.parseObject(jobj.get(str).toString(),SiteData.class);

                    if(IsEmptyUtils.isEmpty(redisService.get(str+"_hkToken"))){
                        if(!IsEmptyUtils.isEmpty(siteData.getHaiKang())){
                            HaiKang haiKang = siteData.getHaiKang();

                            HaiKangManagerServiceImpl haiKangManagerService = new HaiKangManagerServiceImpl();
                            HashMap<String, String> map=new HashMap<String, String>();
                            map.put("appKey",haiKang.getAppKey());
                            map.put("appSecret", haiKang.getAppSecret());
                            map.put("host",haiKang.getHkHost());
                            //获取token
                            JSONObject token=haiKangManagerService.tokenGet(map);
                            JSONObject jsondata= token.getJSONObject("data");
                            logger.info("null token first"+jsondata.get("expireTime").toString());
                            redisService.set(str+"_hkToken",jsondata.get("accessToken").toString());
                        }
                    }else{
                        if(!IsEmptyUtils.isEmpty(siteData.getHaiKang())){
                            HaiKang haiKang = siteData.getHaiKang();

                            HaiKangManagerServiceImpl haiKangManagerService = new HaiKangManagerServiceImpl();
                            HashMap<String, String> map=new HashMap<String, String>();
                            map.put("appKey",haiKang.getAppKey());
                            map.put("appSecret", haiKang.getAppSecret());
                            map.put("host",haiKang.getHkHost());
                            //获取token
                            JSONObject token=haiKangManagerService.tokenGet(map);
                            logger.info(token+"");
                            JSONObject jsondata= token.getJSONObject("data");

                            redisService.set(str+"_hkToken",jsondata.get("accessToken").toString());
                        }
                        //更改定时器执行时间 每6天执行
                        quartzSchedulerBase.modifyJob("updateHKTokenJob","noMapper","0 0 0 /6 * ? *");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        after();
    }
    private void after(){
        System.out.println("更新海康token任务执行完毕-------------   update token end");
    }
}
