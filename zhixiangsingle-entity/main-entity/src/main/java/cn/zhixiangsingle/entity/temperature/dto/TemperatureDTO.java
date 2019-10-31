package cn.zhixiangsingle.entity.temperature.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.temperature.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 14:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class TemperatureDTO extends BaseDTO {
    //
    private Integer id;
    //区域名称
    private String temperatureArea;
    //空气温度值
    private String temperatureVal;
    //空气湿度值
    private String humidityVal;
    //地址值
    private String temperatureSensor;
    //时间 精确到分钟
    private String time;
    //状态 状态 1.正常状态
    //2,超温度上线 3.超温度下线
    // 4.超湿度上线 5.超湿度下线
    // 6.超温度上线和湿度上线 7.超温度上线和湿度下线
    // 8.超温度下线和湿度上线  9.超温度下线和湿度下线
    //10.已处理
    private Integer stat;
}
