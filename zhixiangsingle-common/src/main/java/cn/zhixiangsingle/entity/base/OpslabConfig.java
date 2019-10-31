package cn.zhixiangsingle.entity.base;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 19:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OpslabConfig {
    public static Logger logger = Logger.getLogger(OpslabConfig.class);
    /*获取CLASS_PATH*/
    public static String CLASS_PATH = "";
    public static Map<String, String> INIT_MAP = new HashMap();
    static {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = contextClassLoader.getResource("myOverallParam.properties").openStream()) {
            CLASS_PATH = new File(contextClassLoader.getResource("").toURI()).getPath();


            INIT_MAP = PropertiesUtil.properties(inputStream);


        } catch (IOException | URISyntaxException e) {
            logger.error("init config error:" + e.getMessage());
        }
//        System.out.println(CLASS_PATH);
//        System.out.println(CollectionHelper.join(INIT_MAP, "\n", ":"));
    }
    /*主机特征码*/
    public static final String HOST_FEATURE = INIT_MAP.get("HOST_FEATURE");
    /*日期时间类型格式*/
    public static final String DATETIME_FORMAT = INIT_MAP.get("DATETIME_FORMAT");
    /*日期类型格式*/
    public static final String DATE_FORMAT = INIT_MAP.get("DATE_FORMAT");
    /*时间类型的格式*/
    public static final String TIME_FORMAT = INIT_MAP.get("TIME_FORMAT");
    /**
     * 获取配置信息
     * @param key
     * @return
     */
    public static String get(String key){
        return INIT_MAP.get(key);
    }
}
