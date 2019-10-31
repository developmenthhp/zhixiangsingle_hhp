package cn.zhixiangsingle.entity.companyInformation.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.companyInformation.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 11:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class CompanyInformationDTO extends BaseDTO {
    //公司信息id
    private Integer id;
    //公司名称
    private String companyName;
    //详细地址
    private String detailedAddress;
    //负责人
    private String principal;
    //联系方式
    private String contactWay;
    //餐饮类型 1，社会餐饮，2其他餐饮
    private Integer diningType;
    //营业时间
    private String businessHours;
    //营业状态:1正在营业，2暂停营业
    private Integer operatingState;
    //食品安全员姓名
    private String foodsafetyName;
    //食品安全员照片
    private String foodsafeyImg;
    //食品安全员手机
    private String foodsafeyPhone ;
    //监管人员姓名
    private String supervisorName;
    //监管人员电话
    private String supervisorPhone;
    //监管人员的照片
    private String supervisorImg;
    //食品安全承诺书
    private String commitmentFoodSafetyImg;
    //用户的id
    private Integer accountId;
}
