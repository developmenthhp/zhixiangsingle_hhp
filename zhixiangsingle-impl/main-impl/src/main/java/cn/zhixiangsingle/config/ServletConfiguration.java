package cn.zhixiangsingle.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.config
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/22 17:35
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
public class ServletConfiguration {
    @Value("${druid.username}")
    private String userName;
    @Value("${druid.password}")
    private String password;
    @Value("${druid.allow}")
    private String allow;
    @Value("${druid.reset.enable}")
    private String resetEnable;
    @Bean
    public ServletRegistrationBean druidStatViewServletBean() {
        ServletRegistrationBean statViewServletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> params = new HashMap<>();
        params.put("loginUsername", userName);
        params.put("loginPassword", password);
        params.put("allow", allow);
        params.put("resetEnable", resetEnable);
        statViewServletRegistrationBean.setInitParameters(params);
        return statViewServletRegistrationBean;
    }
}
