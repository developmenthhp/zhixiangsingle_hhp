package cn.zhixiangsingle.service.libraryChange;

import cn.zhixiangsingle.entity.libraryChange.dto.LibraryChangeDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.libraryChange
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/22 13:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LibraryChangeService {
    ResultBean findLibraryChangeList(LibraryChangeDTO libraryChangeDTO) throws Exception;

    ResultBean updateLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception;

    ResultBean addLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception;

    ResultBean findUpdLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception;

    ResultBean delLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception;

    ResultBean addByDetails(LibraryChangeDTO libraryChangeDTO) throws Exception;
}
