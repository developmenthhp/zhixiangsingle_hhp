package cn.zhixiangsingle.entity.companyHonor.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.companyHonor.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 11:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class CompanyHonorDTO extends BaseDTO {
    //公司荣誉id
    private Integer id;
    //证书图片
    private String certificateImg;
    //证牌图片
    private String zhengPaiImg;
    //用户的id
    private Integer accountId;
}
