package cn.zhixiangsingle.service.managementSystem;

import cn.zhixiangsingle.entity.managementSystem.dto.ManagementSystemDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.managementSystem
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/16 14:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ManagementSystemService {
    ResultBean findManagementSystemList(ManagementSystemDTO managementSystemDTO) throws Exception;

    ResultBean updateManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception;

    ResultBean addManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception;

    ResultBean findUpdManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception;

    ResultBean delManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception;

    List<ResultBean> getManagementSystems(ManagementSystemDTO managementSystemDTO) throws Exception;
}
