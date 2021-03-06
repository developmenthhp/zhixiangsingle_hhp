package cn.zhixiangsingle.entity.base.site;

import cn.zhixiangsingle.entity.base.haiKang.HaiKang;
import cn.zhixiangsingle.entity.base.leCheng.LeCheng;
import cn.zhixiangsingle.entity.base.youTu.YouTu;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.base.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/20 11:47
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class SiteData implements Serializable {
    private String dateSourceName;
    private String name;
    private String photo;
    private String picturePrefix;
    private Integer sdId;
    private YouTu youTu;
    private HaiKang haiKang;
    private LeCheng leCheng;
    private String lcOrHK;
    private String localPath;
    private String singlePath;
    private String imgPath;
}
