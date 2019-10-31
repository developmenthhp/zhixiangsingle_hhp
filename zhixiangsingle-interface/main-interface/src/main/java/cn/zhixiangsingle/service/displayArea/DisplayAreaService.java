package cn.zhixiangsingle.service.displayArea;

import cn.zhixiangsingle.entity.displayArea.dto.DisplayAreaDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.displayArea
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 17:08
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface DisplayAreaService {
    ResultBean findAll(DisplayAreaDTO displayAreaDTO) throws Exception;

    ResultBean findDisplayAreaList(DisplayAreaDTO displayAreaDTO) throws Exception;

    ResultBean updateDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception;

    ResultBean addDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception;

    ResultBean findUpdDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception;

    ResultBean delDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception;
}
