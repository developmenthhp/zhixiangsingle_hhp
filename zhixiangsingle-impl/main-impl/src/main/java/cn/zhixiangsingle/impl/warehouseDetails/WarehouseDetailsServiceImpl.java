package cn.zhixiangsingle.impl.warehouseDetails;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.dao.warehouseDetails.WarehouseDetailsMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.warehouseDetails.dto.WarehouseDetailsDTO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.warehouseDetails.WarehouseDetailsService;
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
 * @Package cn.zhixiangsingle.impl.warehouseDetails
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/21 11:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = WarehouseDetailsService.class)
public class WarehouseDetailsServiceImpl implements WarehouseDetailsService {
    private static final Logger logger = LoggerFactory
            .getLogger(WarehouseDetailsServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private WarehouseDetailsMapper warehouseDetailsMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/21 14:17
     */
    @Override
    public ResultBean findByIngredientId(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getPage())){
            warehouseDetailsDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(warehouseDetailsDTO.getLimit())){
            warehouseDetailsDTO.setLimit(10);
        }
        Integer start = (warehouseDetailsDTO.getPage()-1)*warehouseDetailsDTO.getLimit();

        Integer page = warehouseDetailsDTO.getPage();
        Integer limit = warehouseDetailsDTO.getLimit();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        warehouseDetailsDTO.setBatchNum("b");
        warehouseDetailsDTO.setId(-1);
        warehouseDetailsDTO.setProductionDate("p");
        warehouseDetailsDTO.setWarehouseCount(0.0);
        warehouseDetailsDTO.setUnitPrice(0.0);
        warehouseDetailsDTO.setInventoryBalance(0.0);
        warehouseDetailsDTO.setInventoryStatus("2");

        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(warehouseDetailsDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(warehouseDetailsDTO.getSdId().toString()).toString(),SiteData.class);
            }
            warehouseDetailsDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                Integer total = warehouseDetailsMapper.findByIngredientIdTotal(warehouseDetailsDTO);
                warehouseDetailsDTO.setStart(start);
                warehouseDetailsDTO.setEnd(warehouseDetailsDTO.getLimit());
                List<Map<String,Object>> morningMeetingResult = warehouseDetailsMapper.findByIngredientId(warehouseDetailsDTO);
                resultBean.setTotal(total);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 16:56
     */
    @Override
    public ResultBean findWarehouseDetailsList(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getPage())){
            warehouseDetailsDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(warehouseDetailsDTO.getLimit())){
            warehouseDetailsDTO.setLimit(10);
        }
        Integer page = warehouseDetailsDTO.getPage();
        Integer limit = warehouseDetailsDTO.getLimit();
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
        warehouseDetailsDTO.setId(-1);
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getIngredientId())){
            warehouseDetailsDTO.setIngredientId(-1);
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getIngredientSuppId())){
            warehouseDetailsDTO.setIngredientSuppId(-1);
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getIngredientWhouseId())){
            warehouseDetailsDTO.setIngredientWhouseId(-1);
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getBatchNum())){
            warehouseDetailsDTO.setBatchNum("b");
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getMainAccountId())){
            warehouseDetailsDTO.setMainAccountId(-1);
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSourceOperation())){
            warehouseDetailsDTO.setSourceOperation("s");
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSourceHandle())){
            warehouseDetailsDTO.setSourceHandle("s");
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getInventoryStatus())){
            warehouseDetailsDTO.setInventoryStatus("i");
        }
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getObjectCardunify())){
            warehouseDetailsDTO.setObjectCardunify(-1);
        }
        warehouseDetailsDTO.setInventoryBalance(0.0);
        warehouseDetailsDTO.setUnitPrice(0.0);
        warehouseDetailsDTO.setWarehouseCount(0.0);
        warehouseDetailsDTO.setProductionDate("p");
        if(!IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSdIds())){
            String[] sdIds = warehouseDetailsDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                warehouseDetailsDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                warehouseDetailsDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = warehouseDetailsMapper.findWarehouseDetailsTotal(warehouseDetailsDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                warehouseDetailsDTO.setSdId(Integer.parseInt(sourceNames[i]));
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                warehouseDetailsDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                warehouseDetailsDTO.setStart(startEndNums.get(i).get(0));
                warehouseDetailsDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                warehouseDetailsDTO.setSdId(Integer.parseInt(sourceNames[i]));
                warehouseDetailsDTO.setSiteName(siteData.getName());
                warehouseDetailsDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = warehouseDetailsMapper.findWarehouseDetailsList(warehouseDetailsDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(warehouseDetailsDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                warehouseDetailsDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                warehouseDetailsDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = warehouseDetailsMapper.findWarehouseDetailsTotal(warehouseDetailsDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                logger.info("--"+siteData.getDateSourceName()+"--");
                warehouseDetailsDTO.setPicturePrefix(siteData.getPicturePrefix());
                warehouseDetailsDTO.setSdId(Integer.parseInt(sourceNames[i]));
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                warehouseDetailsDTO.setStart(startEndNums.get(i).get(0));
                warehouseDetailsDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                warehouseDetailsDTO.setSdId(Integer.parseInt(sourceNames[i]));
                warehouseDetailsDTO.setSiteName(siteData.getName());
                warehouseDetailsDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = warehouseDetailsMapper.findWarehouseDetailsList(warehouseDetailsDTO);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 16:57
     */
    @Override
    public ResultBean updateWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(warehouseDetailsDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(warehouseDetailsDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("----" + dataSourceName + "-----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            warehouseDetailsMapper.updateByPrimaryKey(warehouseDetailsDTO);

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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 17:00
     */
    @Override
    public ResultBean addWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = warehouseDetailsDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                warehouseDetailsDTO.setSdId(Integer.parseInt(sdId));
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
                    logger.info("---" + dataSourceName + "----");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    warehouseDetailsMapper.insertSelective(warehouseDetailsDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(warehouseDetailsDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(warehouseDetailsDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("" + dataSourceName + "-");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                warehouseDetailsMapper.insertSelective(warehouseDetailsDTO);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 17:02
     */
    @Override
    public ResultBean findUpdWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        warehouseDetailsDTO.setInventoryBalance(0.0);
        warehouseDetailsDTO.setUnitPrice(0.0);
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(warehouseDetailsDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(warehouseDetailsDTO.getSdId().toString()).toString(),SiteData.class);
            }
            warehouseDetailsDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> morningMeetingResult = warehouseDetailsMapper.findWarehouseDetails(warehouseDetailsDTO);

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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 17:03
     */
    @Override
    public ResultBean delWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(warehouseDetailsDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(warehouseDetailsDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-" + dataSourceName + "---");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            warehouseDetailsMapper.deleteByPrimaryKey(warehouseDetailsDTO);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/23 14:53
     */
    @Override
    public ResultBean findPrintQRCodeData(WarehouseDetailsDTO warehouseDetailsDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getPage())){
            warehouseDetailsDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(warehouseDetailsDTO.getLimit())){
            warehouseDetailsDTO.setLimit(10);
        }
        Integer start = (warehouseDetailsDTO.getPage()-1)*warehouseDetailsDTO.getLimit();

        Integer page = warehouseDetailsDTO.getPage();
        Integer limit = warehouseDetailsDTO.getLimit();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }

        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(warehouseDetailsDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(warehouseDetailsDTO.getSdId().toString()).toString(),SiteData.class);
            }
            warehouseDetailsDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("---"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                Integer total = warehouseDetailsMapper.findPrintQRCodeDataTotal(warehouseDetailsDTO);
                warehouseDetailsDTO.setStart(start);
                warehouseDetailsDTO.setEnd(warehouseDetailsDTO.getLimit());
                List<Map<String,Object>> morningMeetingResult = warehouseDetailsMapper.findPrintQRCodeData(warehouseDetailsDTO);
                resultBean.setTotal(total);
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
}
