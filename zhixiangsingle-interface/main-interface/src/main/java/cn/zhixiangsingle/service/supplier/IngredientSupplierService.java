package cn.zhixiangsingle.service.supplier;

import cn.zhixiangsingle.entity.supplier.dto.IngredientSupplierDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.supplier
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/5 13:26
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IngredientSupplierService {
    ResultBean updateIngredientSupplier(IngredientSupplierDTO ingredientSupplierDTO) throws Exception;

    ResultBean addIngredientSupplier(IngredientSupplierDTO ingredientSupplierDTO) throws Exception;

    ResultBean findIngredientSupplierList(IngredientSupplierDTO ingredientSupplierDTO) throws Exception;

    ResultBean findAll(IngredientSupplierDTO ingredientSupplierDTO) throws Exception;
}
