package cn.zhixiangsingle.service.liveShow;

import cn.zhixiangsingle.entity.liveShow.dto.LiveShowDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.liveShow
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 11:58
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LiveShowService {
    ResultBean findLiveShows(LiveShowDTO liveShowDTO) throws Exception;

    ResultBean updateLiveShow(LiveShowDTO liveShowDTO) throws Exception;

    ResultBean addLiveShow(LiveShowDTO liveShowDTO) throws Exception;

    ResultBean findUpdLiveShow(LiveShowDTO liveShowDTO) throws Exception;

    ResultBean delLiveShow(LiveShowDTO liveShowDTO) throws Exception;
}
