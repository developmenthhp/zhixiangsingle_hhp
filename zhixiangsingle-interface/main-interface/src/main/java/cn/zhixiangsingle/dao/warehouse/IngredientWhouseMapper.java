package cn.zhixiangsingle.dao.warehouse;

import cn.zhixiangsingle.entity.warehouse.dto.IngredientWhouseDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.warehouse
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/5 11:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IngredientWhouseMapper {
    List<Map<String,Object>> findIngredientWhouseList(IngredientWhouseDTO ingredientWhouseDTO);
}
