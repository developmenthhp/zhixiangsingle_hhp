package cn.zhixiangsingle.service.libraryPurchase;

import cn.zhixiangsingle.entity.libraryPurchase.dto.LibraryPurchaseDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.libraryPurchase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/24 10:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryPurchaseService {
    ResultBean findLibraryPurchaseList(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception;

    ResultBean updateLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception;

    ResultBean addLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception;

    ResultBean delLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception;

    List<ResultBean> getLibraryPurchases(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception;
}
