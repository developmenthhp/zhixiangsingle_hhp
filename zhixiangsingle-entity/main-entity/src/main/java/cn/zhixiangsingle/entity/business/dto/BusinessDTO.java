package cn.zhixiangsingle.entity.business.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.business.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 9:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class BusinessDTO extends BaseDTO {
    //营业执照的编号
    private Integer id;
    //注册号
    private String registration;
    //法定代表人
    private String legalRepresentative;
    //注册资本
    private String registeredCapital;
    //公司名称
    private String companyName;
    //地址
    private String address;
    //营业期限
    private String deadline;
    //经营范围
    private String businessScope;
    //主体类型
    private String mainTypes;
    //营业执照上传图片
    private String path;
    //用户的id
    private Integer accountId;
    //预警时间
    private String earlyWarningTime;
    private String licenceImg;
    private String circulationCardImg;
}
