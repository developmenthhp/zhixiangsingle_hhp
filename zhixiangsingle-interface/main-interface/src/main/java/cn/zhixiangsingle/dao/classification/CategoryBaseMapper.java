package cn.zhixiangsingle.dao.classification;

import cn.zhixiangsingle.entity.classification.dto.CategoryBaseDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.classification
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/27 12:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CategoryBaseMapper {
    Integer findCategoryBaseTotal(CategoryBaseDTO categoryBaseDTO);

    List<Map<String,Object>> findCategoryBaseList(CategoryBaseDTO categoryBaseDTO);

    Integer updateByPrimaryKey(CategoryBaseDTO categoryBaseDTO);

    Integer insertSelective(CategoryBaseDTO categoryBaseDTO);

    Map<String,Object> findCategoryBase(CategoryBaseDTO categoryBaseDTO);
}
