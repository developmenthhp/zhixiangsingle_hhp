package cn.zhixiangsingle.service.warehouse;

import cn.zhixiangsingle.entity.warehouse.dto.IngredientWhouseDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.warehouse
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/5 11:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IngredientWhouseService {
    ResultBean findIngredientWhouseList(IngredientWhouseDTO ingredientWhouseDTO) throws Exception;

    ResultBean findAll(IngredientWhouseDTO ingredientWhouseDTO) throws Exception;

    ResultBean updateIngredientWhouse(IngredientWhouseDTO ingredientWhouseDTO) throws Exception;

    ResultBean addIngredientWhouse(IngredientWhouseDTO ingredientWhouseDTO) throws Exception;
}
