package cn.zhixiangsingle.dao.warehouseDetails;

import cn.zhixiangsingle.entity.warehouseDetails.dto.WarehouseDetailsDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.warehouseDetails
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/21 11:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface WarehouseDetailsMapper {
    List<Map<String,Object>> findByIngredientId(WarehouseDetailsDTO warehouseDetailsDTO);

    Integer findByIngredientIdTotal(WarehouseDetailsDTO warehouseDetailsDTO);

    Integer findWarehouseDetailsTotal(WarehouseDetailsDTO warehouseDetailsDTO);

    List<Map<String,Object>> findWarehouseDetailsList(WarehouseDetailsDTO warehouseDetailsDTO);

    Integer updateByPrimaryKey(WarehouseDetailsDTO warehouseDetailsDTO);

    Integer insertSelective(WarehouseDetailsDTO warehouseDetailsDTO);

    Map<String,Object> findWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO);

    Integer deleteByPrimaryKey(WarehouseDetailsDTO warehouseDetailsDTO);

    Integer findPrintQRCodeDataTotal(WarehouseDetailsDTO warehouseDetailsDTO);

    List<Map<String,Object>> findPrintQRCodeData(WarehouseDetailsDTO warehouseDetailsDTO);
}
