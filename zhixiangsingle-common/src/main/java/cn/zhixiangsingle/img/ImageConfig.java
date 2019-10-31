package cn.zhixiangsingle.img;

import cn.zhixiangsingle.entity.base.OpslabConfig;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.img
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 19:30
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public enum ImageConfig {
    /** 站点图标在线项目使用 */
    ///home/test001/html 179  /var/www/html 181     "//home/test001/html/"
    SITE_ICON(OpslabConfig.get("IMG_PATH"), "zdIcon", "1002");
    /* 本地使用 */
    //SITE_ICON("/var/www/html/", "zdIcon", "1002");
    private String directory;
    private String key;
    private String code;
    private ImageConfig(String directory, String key, String code) {
        this.directory = directory;
        this.key = key;
        this.code = code;
    }
    public String getDirectory() {
        return directory;
    }
    public String getKey() {
        return key;
    }
    public String getCode() {
        return code;
    }
}
