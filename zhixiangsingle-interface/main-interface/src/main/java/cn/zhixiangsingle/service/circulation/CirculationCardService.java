package cn.zhixiangsingle.service.circulation;

import cn.zhixiangsingle.entity.circulation.dto.CirculationCardDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.circulation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CirculationCardService {
    ResultBean updateCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception;

    ResultBean addCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception;

    ResultBean delCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception;

    ResultBean findUpdCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception;
}
