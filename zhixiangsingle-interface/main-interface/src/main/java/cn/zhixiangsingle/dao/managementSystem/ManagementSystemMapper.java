package cn.zhixiangsingle.dao.managementSystem;

import cn.zhixiangsingle.entity.managementSystem.dto.ManagementSystemDTO;
import cn.zhixiangsingle.entity.managementSystem.vo.ManagementSystemVO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.managementSystem
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/16 14:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ManagementSystemMapper {
    Integer findManagementSystemTotal(ManagementSystemDTO managementSystemDTO);

    List<Map<String,Object>> findManagementSystemList(ManagementSystemDTO managementSystemDTO);

    Integer updateByPrimaryKey(ManagementSystemDTO managementSystemDTO);

    Integer insertSelective(ManagementSystemDTO managementSystemDTO);

    Map<String,Object> findManagementSystem(ManagementSystemDTO managementSystemDTO);

    Integer deleteByPrimaryKey(ManagementSystemDTO managementSystemDTO);

    List<ManagementSystemVO> findAllManagementSystem(ManagementSystemDTO managementSystemDTO);
}
