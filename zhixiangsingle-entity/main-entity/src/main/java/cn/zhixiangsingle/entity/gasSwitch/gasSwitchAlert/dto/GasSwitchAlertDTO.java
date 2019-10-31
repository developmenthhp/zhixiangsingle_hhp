package cn.zhixiangsingle.entity.gasSwitch.gasSwitchAlert.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.gasSwitch.gasSwitchAlert.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 18:31
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class GasSwitchAlertDTO extends BaseDTO{
    private Integer id;
    //区域名称
    private String area;
    //传感器编号
    private String sensor;
    //开始发生时间
    private String startTime;
    //结束时间
    private String endTime;
    //状态 1.警报中 2.已处理 3.已作废 4.正常
    private Integer status;
    //开启的乐橙抓拍图片
    private String openUrlImg;
    //关闭的乐橙抓拍图片
    private String offUrlImg;
    //描述
    private String description;
}
