package cn.zhixiangsingle.entity.base.manyResult;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.base.manyResult
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 15:32
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ManyResult implements Serializable {
    private List<?> rows;
}
