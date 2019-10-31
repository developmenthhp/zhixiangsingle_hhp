package cn.zhixiangsingle.service.purchase.scjcxx;

import cn.zhixiangsingle.entity.purchase.scjcxx.dto.IngredientBaseDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.purchase.scjcxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/20 9:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IngredientBaseService {
    ResultBean updateIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception;

    ResultBean addIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception;

    ResultBean findIngredientBaseList(IngredientBaseDTO ingredientBaseDTO) throws Exception;

    List<ResultBean> getIngredientBases(IngredientBaseDTO ingredientBaseDTO) throws Exception;

    ResultBean selectCommon(IngredientBaseDTO ingredientBaseDTO) throws Exception;

    ResultBean delIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception;

    ResultBean findUpdIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception;

    ResultBean findAllIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception;
}
