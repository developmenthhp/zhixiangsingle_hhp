package cn.zhixiangsingle.service.warehouseDetails;

import cn.zhixiangsingle.entity.warehouseDetails.dto.WarehouseDetailsDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.warehouseDetails
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/21 11:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface WarehouseDetailsService {
    ResultBean findByIngredientId(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception;

    ResultBean findWarehouseDetailsList(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception;

    ResultBean updateWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception;

    ResultBean addWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception;

    ResultBean findUpdWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception;

    ResultBean delWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception;

    ResultBean findPrintQRCodeData(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception;
}
