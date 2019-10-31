package cn.zhixiangsingle.dao.business;

import cn.zhixiangsingle.entity.business.dto.BusinessDTO;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.business
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 9:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface BusinessMapper {
    Map<String,Object> findBusiness(BusinessDTO businessDTO);

    Integer insertSelective(BusinessDTO businessDTO);

    Integer updateByPrimaryKey(BusinessDTO businessDTO);

    Integer deleteByPrimaryKey(BusinessDTO businessDTO);
}
