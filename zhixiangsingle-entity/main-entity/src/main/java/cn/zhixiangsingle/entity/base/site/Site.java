package cn.zhixiangsingle.entity.base.site;

import cn.zhixiangsingle.entity.base.common.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 10:10
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="site")
public class Site extends BaseEntity {
    private Integer sdId;//试点id
    private String name;//站点名称
    private String address;//站点地址描述
    private String photo;//站点图标
}
