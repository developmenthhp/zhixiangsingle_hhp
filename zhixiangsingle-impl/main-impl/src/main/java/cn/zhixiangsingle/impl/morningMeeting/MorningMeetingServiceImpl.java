package cn.zhixiangsingle.impl.morningMeeting;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.morningMeeting.MorningMeetingMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.morningMeeting.dto.MorningMeetingDTO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.morningMeeting.MorningMeetingService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.morningMeeting
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/18 10:13
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = MorningMeetingService.class)
public class MorningMeetingServiceImpl implements MorningMeetingService {
    private static final Logger logger = LoggerFactory
            .getLogger(MorningMeetingServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private MorningMeetingMapper morningMeetingMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:43
     */
    @Override
    public ResultBean findMorningMeetingList(MorningMeetingDTO morningMeetingDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(morningMeetingDTO.getPage())){
            morningMeetingDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(morningMeetingDTO.getLimit())){
            morningMeetingDTO.setLimit(10);
        }
        Integer page = morningMeetingDTO.getPage();
        Integer limit = morningMeetingDTO.getLimit();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        morningMeetingDTO.setId(-1);
        if(IsEmptyUtils.isEmpty(morningMeetingDTO.getAddress())){
            morningMeetingDTO.setAddress("a");
        }
        morningMeetingDTO.setMeetingTime("m");
        morningMeetingDTO.setSnapPicture("s");
        if(!IsEmptyUtils.isEmpty(morningMeetingDTO.getSdIds())){
            String[] sdIds = morningMeetingDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                morningMeetingDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                morningMeetingDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(sdId);
                    }else{
                        stringBuffer.append(","+sdId);
                    }
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    Integer totalStatus = morningMeetingMapper.findMorningMeetingTotal(morningMeetingDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                morningMeetingDTO.setSdId(Integer.parseInt(sourceNames[i]));
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                morningMeetingDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                morningMeetingDTO.setStart(startEndNums.get(i).get(0));
                morningMeetingDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                morningMeetingDTO.setSdId(Integer.parseInt(sourceNames[i]));
                morningMeetingDTO.setSiteName(siteData.getName());
                morningMeetingDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = morningMeetingMapper.findMorningMeetingList(morningMeetingDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(morningMeetingDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                morningMeetingDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                morningMeetingDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(siteVO.getSdId());
                    }else{
                        stringBuffer.append(","+siteVO.getSdId());
                    }
                    logger.info("null sdIds...--"+dataSourceName+"----");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    Integer totalStatus = morningMeetingMapper.findMorningMeetingTotal(morningMeetingDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                logger.info("--"+siteData.getDateSourceName()+"--");
                morningMeetingDTO.setPicturePrefix(siteData.getPicturePrefix());
                morningMeetingDTO.setSdId(Integer.parseInt(sourceNames[i]));
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                morningMeetingDTO.setStart(startEndNums.get(i).get(0));
                morningMeetingDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                morningMeetingDTO.setSdId(Integer.parseInt(sourceNames[i]));
                morningMeetingDTO.setSiteName(siteData.getName());
                morningMeetingDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = morningMeetingMapper.findMorningMeetingList(morningMeetingDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResult(mainLPLs);
        resultBean.setTotal(total);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:44
     */
    @Override
    public ResultBean updateMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(morningMeetingDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(morningMeetingDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-----" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            morningMeetingMapper.updateByPrimaryKey(morningMeetingDTO);

            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:45
     */
    @Override
    public ResultBean addMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        morningMeetingDTO.setMeetingTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        if(!IsEmptyUtils.isEmpty(morningMeetingDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = morningMeetingDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                morningMeetingDTO.setSdId(Integer.parseInt(sdId));
                StringBuffer stringBufferJson = new StringBuffer();
                InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    stringBufferJson.append(line);
                }
                JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                    logger.info("----" + dataSourceName + "----");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    morningMeetingMapper.insertSelective(morningMeetingDTO);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            if(!IsEmptyUtils.isEmpty(msgStr)){
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage()+"(其中"+msgStr+"已提交，请勿重复提交)");
            }else{
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            }
        }else{
            StringBuffer stringBufferJson = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                stringBufferJson.append(line);
            }
            JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(morningMeetingDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(morningMeetingDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("----" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                morningMeetingMapper.insertSelective(morningMeetingDTO);
                resultBean.setSuccess(true);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
                DynamicDataSourceContextHolder.setDataSourceType(null);
            }
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:48
     */
    @Override
    public ResultBean findUpdMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        morningMeetingDTO.setMeetingTime("m");
        morningMeetingDTO.setSnapPicture("s");
        morningMeetingDTO.setAddress("a");
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(morningMeetingDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(morningMeetingDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(morningMeetingDTO.getSdId().toString()).toString(),SiteData.class);
            }
            morningMeetingDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> morningMeetingResult = morningMeetingMapper.findMorningMeeting(morningMeetingDTO);

                resultBean.setResult(morningMeetingResult);
            }
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:50
     */
    @Override
    public ResultBean delMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(morningMeetingDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(morningMeetingDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-----" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            morningMeetingMapper.deleteByPrimaryKey(morningMeetingDTO);
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
}
