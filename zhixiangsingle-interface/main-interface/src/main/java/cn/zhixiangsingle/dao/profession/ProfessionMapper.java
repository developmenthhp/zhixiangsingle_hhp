package cn.zhixiangsingle.dao.profession;

import cn.zhixiangsingle.entity.base.job.Profession;
import cn.zhixiangsingle.entity.base.job.dto.ProfessionDTO;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.profession
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/30 18:05
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ProfessionMapper {
    List<Profession> selectByPrimaryKey(ProfessionDTO professionDTO);

    int updateByPrimaryKeySelective(Profession profession);

    int insertSelective(Profession profession);

    int deleteByPrimaryKey(Integer id);
}
