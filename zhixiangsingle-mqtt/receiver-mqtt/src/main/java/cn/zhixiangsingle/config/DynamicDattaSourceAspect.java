package cn.zhixiangsingle.config;

import cn.zhixiangsingle.annotation.TargetDataSource;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.config
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/22 17:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Aspect
@Order(-1)
@Component
public class DynamicDattaSourceAspect {
    private Logger logger = Logger.getLogger(DynamicDattaSourceAspect.class);
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        String dbid = targetDataSource.name();
        if (!DynamicDataSourceContextHolder.isContainsDataSource(dbid)) {
        } else {
            DynamicDataSourceContextHolder.setDataSourceType(dbid);
        }
    }
    @After("@annotation(targetDataSource)")
    public void clearDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
