package cn.zhixiangsingle.impl.unit;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.dao.unit.MeteringUnitMapper;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.unit.dto.MeteringUnitDTO;
import cn.zhixiangsingle.service.unit.MeteringUnitService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.unit
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/4 17:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = MeteringUnitService.class)
public class MeteringUnitServiceImpl implements MeteringUnitService {
    private static final Logger logger = LoggerFactory
            .getLogger(MeteringUnitServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private MeteringUnitMapper meteringUnitMapper;

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param meteringUnitDTO
     * @date: 2019/10/5 10:04
     */
    @Override
    public ResultBean findAll(MeteringUnitDTO meteringUnitDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        List<Map<String,Object>> mainLPLs = Lists.newArrayList();

        ArrayList<Integer> totalAll = Lists.newArrayList();
        meteringUnitDTO.setId(-1);
        if(IsEmptyUtils.isEmpty(meteringUnitDTO.getMeteringName())){
            meteringUnitDTO.setMeteringName("m");
        }
        if(IsEmptyUtils.isEmpty(meteringUnitDTO.getDeleteStatus())){
            meteringUnitDTO.setDeleteStatus("1");
        }

        if(!IsEmptyUtils.isEmpty(meteringUnitDTO.getSdIds())){
            String[] sdIds = meteringUnitDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                meteringUnitDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                meteringUnitDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    meteringUnitDTO.setPicturePrefix(siteData.getPicturePrefix());
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    meteringUnitDTO.setSiteName(siteData.getName());
                    meteringUnitDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = meteringUnitMapper.findMeteringUnitList(meteringUnitDTO);
                    mainLPLs.addAll(mainLPList);

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(meteringUnitDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                meteringUnitDTO.setSdId(siteVO.getSdId());
                meteringUnitDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){

                    logger.info("--"+siteData.getDateSourceName()+"--");
                    meteringUnitDTO.setPicturePrefix(siteData.getPicturePrefix());

                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    meteringUnitDTO.setSiteName(siteData.getName());
                    meteringUnitDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = meteringUnitMapper.findMeteringUnitList(meteringUnitDTO);
                    mainLPLs.addAll(mainLPList);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResult(mainLPLs);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }

    @Override
    public ResultBean findMeteringUnitList(MeteringUnitDTO meteringUnitDTO) {
        return null;
    }

    @Override
    public ResultBean updateMeteringUnit(MeteringUnitDTO meteringUnitDTO) {
        return null;
    }

    @Override
    public ResultBean addMeteringUnit(MeteringUnitDTO meteringUnitDTO) {
        return null;
    }
}
