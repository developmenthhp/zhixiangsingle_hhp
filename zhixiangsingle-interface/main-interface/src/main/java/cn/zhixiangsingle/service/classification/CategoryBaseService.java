package cn.zhixiangsingle.service.classification;

import cn.zhixiangsingle.entity.classification.dto.CategoryBaseDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.classification
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/23 18:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CategoryBaseService {
    ResultBean updateCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception;

    ResultBean addCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception;

    ResultBean findCategoryBaseList(CategoryBaseDTO categoryBaseDTO) throws Exception ;

    ResultBean findAll(CategoryBaseDTO categoryBaseDTO) throws Exception;

    ResultBean findUpdCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception;

    ResultBean delCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception;
}
