package cn.zhixiangsingle.entity.liveShow.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.liveShow.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 12:03
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class LiveShowDTO extends BaseDTO {
    //实景展示id
    private Integer id;
    //区域id
    private Integer areaId;
    //拍照图片
    private String img;
    //用户的id
    private Integer  accountId;
}
