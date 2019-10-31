package cn.zhixiangsingle.config;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.config
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/22 17:16
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class DynamicDataSourceContextHolder {
    private Logger logger = Logger.getLogger(DynamicDataSourceContextHolder.class);
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static List<String> dataSourceIds = new ArrayList<String>();
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }
    public static String getDataSourceType() {
        return contextHolder.get();
    }
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
    public static boolean isContainsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}
