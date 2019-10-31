package cn.zhixiangsingle.service.library;

import cn.zhixiangsingle.entity.library.dto.LibraryDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.library
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/19 15:38
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryService {
    ResultBean findLibraryList(LibraryDTO libraryDTO) throws Exception;

    ResultBean updateLibrary(LibraryDTO libraryDTO) throws Exception;

    ResultBean addLibrary(LibraryDTO libraryDTO) throws Exception;

    ResultBean findUpdLibrary(LibraryDTO libraryDTO) throws Exception;

    ResultBean delLibrary(LibraryDTO libraryDTO) throws Exception;
}
