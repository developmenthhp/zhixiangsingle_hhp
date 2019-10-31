package cn.zhixiangsingle.dao.library;

import cn.zhixiangsingle.entity.library.dto.LibraryDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.library
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/19 15:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryMapper {
    Integer findLibraryTotal(LibraryDTO libraryDTO);

    List<Map<String,Object>> findLibraryList(LibraryDTO libraryDTO);

    Integer updateByPrimaryKey(LibraryDTO libraryDTO);

    Integer insertSelective(LibraryDTO libraryDTO);

    Map<String,Object> findLibrary(LibraryDTO libraryDTO);

    Integer deleteByPrimaryKey(LibraryDTO libraryDTO);

    Integer updateLSruplusByMaxIBId(LibraryDTO libraryDTO);

    Integer findMaxId(LibraryDTO libraryDTO);
}
