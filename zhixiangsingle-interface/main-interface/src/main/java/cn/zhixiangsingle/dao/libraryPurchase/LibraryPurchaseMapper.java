package cn.zhixiangsingle.dao.libraryPurchase;

import cn.zhixiangsingle.entity.libraryPurchase.dto.LibraryPurchaseDTO;
import cn.zhixiangsingle.entity.libraryPurchase.vo.LibraryPurchaseVO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.libraryPurchase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/24 10:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryPurchaseMapper {
    List<Map<String,Object>> findLibraryPurchaseList(LibraryPurchaseDTO libraryPurchaseDTO);

    Integer findLibraryPurchaseTotal(LibraryPurchaseDTO libraryPurchaseDTO);

    Integer updateByPrimaryKey(LibraryPurchaseDTO libraryPurchaseDTO);

    Integer insertSelective(LibraryPurchaseDTO libraryPurchaseDTO);

    Integer deleteByPrimaryKey(LibraryPurchaseDTO libraryPurchaseDTO);

    List<LibraryPurchaseVO> findAllLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO);
}
