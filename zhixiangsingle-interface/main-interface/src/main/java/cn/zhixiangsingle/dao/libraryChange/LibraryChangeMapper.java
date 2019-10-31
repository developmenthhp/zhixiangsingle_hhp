package cn.zhixiangsingle.dao.libraryChange;

import cn.zhixiangsingle.entity.libraryChange.dto.LibraryChangeDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.libraryChange
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/22 13:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryChangeMapper {
    Integer insertSelective(LibraryChangeDTO libraryChangeDTO);

    Integer findLibraryChangeTotal(LibraryChangeDTO libraryChangeDTO);

    List<Map<String,Object>> findLibraryChangeList(LibraryChangeDTO libraryChangeDTO);

    Integer updateByPrimaryKey(LibraryChangeDTO libraryChangeDTO);

    Map<String,Object> findLibraryChange(LibraryChangeDTO libraryChangeDTO);

    Integer deleteByPrimaryKey(LibraryChangeDTO libraryChangeDTO);
}
