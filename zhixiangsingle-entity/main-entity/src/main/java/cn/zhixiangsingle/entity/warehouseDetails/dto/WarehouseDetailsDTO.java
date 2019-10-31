package cn.zhixiangsingle.entity.warehouseDetails.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.warehouseDetails.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/21 11:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class WarehouseDetailsDTO extends BaseDTO {
    //编号
    private Integer id;
    //批次号每次查询数据库中当前货品最大批次+1
    private String batchNum;
    //食材id
    private Integer ingredientId;
    //入库数量
    private Double warehouseCount;
    //单价
    private Double unitPrice;
    //总价
    private Double totalPrice;
    //生产日期
    private String productionDate;
    //货品称重
    private Double weight;
    //实收重量
    private Double revenueWeight;
    //仓库id
    private Integer ingredientWhouseId;
    //用户id,核检人id
    private Integer mainAccountId;
    //供应商id
    private Integer ingredientSuppId;
    //入库时间
    private String ctTime;
    //规格与型号
    private String modelsInfo;
    //来源 1.录入   2.配送单转换   3.其他
    private String sourceOperation;
    //来源信息: 根据来源 存储相应信息  如是配送单转换 则存储配送单编号
    private String sourceInfo;
    //1 不做库存处理, 当日就出。 2.做库存处理
    private String sourceHandle;
    // '批次库存结余,初始值为入库值',
    private Double inventoryBalance;
    //'库存状态 1正常 2过期 3即将过期',
    private String inventoryStatus;
    // 到期时间
    private String inventoryTimeHour;
    //食材拍照的路径
    private String foodPictures;
    //票据拍照路径
    private String notePictures;
    //索证卫生证拍照路径
    private String sanitaryCertificate;
    //索证合格证拍照路径
    private String certificateOfSoundness;
    //物证是否统一 1.是    2.不是
    private Integer objectCardunify;
}
