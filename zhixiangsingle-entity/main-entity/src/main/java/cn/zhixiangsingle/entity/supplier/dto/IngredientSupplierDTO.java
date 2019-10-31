package cn.zhixiangsingle.entity.supplier.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.supplier.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/5 13:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class IngredientSupplierDTO extends BaseDTO {
    //ID
    private Integer id;
    //商家名称
    private String supplierName;
    //联系人姓名全称
    private String principalName;
    //资质预警日期
    private String promptTime;
    //联系人电话
    private String principalTel;
    //供应商家实际地址
    private String suppAdress;
    //使用状态 1正常  0.禁用删除
    private String deleteStatus;
    //创建时间
    private String foundTime;
    //证件扫描路径，支持多张，存储半路径
    private String supDocumentPhoto;
    //运作主体id
    private Integer sdId;
    //资质到期日期
    private String expireTime;
    //注册号
    private String registrationNumber;
    //累计违规次数
    private String violationCount;
}
