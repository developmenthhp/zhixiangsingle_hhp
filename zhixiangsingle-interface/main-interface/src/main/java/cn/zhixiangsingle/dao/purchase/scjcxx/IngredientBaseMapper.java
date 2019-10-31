package cn.zhixiangsingle.dao.purchase.scjcxx;

import cn.zhixiangsingle.entity.purchase.scjcxx.dto.IngredientBaseDTO;
import cn.zhixiangsingle.entity.purchase.scjcxx.vo.IngredientBaseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.purchase.scjcxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/20 11:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IngredientBaseMapper {
    Integer findIngredientBaseTotal(IngredientBaseDTO ingredientBaseDTO);

    List<Map<String,Object>> findIngredientBaseList(IngredientBaseDTO ingredientBaseDTO);

    List<IngredientBaseVO> findAllIngredientBase(IngredientBaseDTO ingredientBaseDTO);

    Integer findIngredientBaseFullNameTotal(IngredientBaseDTO ingredientBaseDTO);

    Integer insertSelective(IngredientBaseDTO ingredientBaseDTO);

    Integer updateByPrimaryKey(IngredientBaseDTO ingredientBaseDTO);

    Map<String,Object> findIngredientBase(IngredientBaseDTO ingredientBaseDTO);

    Integer deleteByPrimaryKey(@Param("id") Integer id);
}
