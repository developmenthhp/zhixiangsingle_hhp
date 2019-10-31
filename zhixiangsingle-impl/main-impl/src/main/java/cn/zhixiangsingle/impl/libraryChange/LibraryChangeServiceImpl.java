package cn.zhixiangsingle.impl.libraryChange;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.library.LibraryMapper;
import cn.zhixiangsingle.dao.libraryChange.LibraryChangeMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.dao.warehouseDetails.WarehouseDetailsMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.library.dto.LibraryDTO;
import cn.zhixiangsingle.entity.libraryChange.dto.LibraryChangeDTO;
import cn.zhixiangsingle.entity.warehouseDetails.dto.WarehouseDetailsDTO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.libraryChange.LibraryChangeService;
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
 * @Package cn.zhixiangsingle.impl.libraryChange
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/22 13:45
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = LibraryChangeService.class)
public class LibraryChangeServiceImpl implements LibraryChangeService {
    private static final Logger logger = LoggerFactory
            .getLogger(LibraryChangeServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private LibraryChangeMapper libraryChangeMapper;
    @Autowired
    private WarehouseDetailsMapper warehouseDetailsMapper;
    @Autowired
    private LibraryMapper libraryMapper;
    @Override
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 17:38
     */
    public ResultBean findLibraryChangeList(LibraryChangeDTO libraryChangeDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(libraryChangeDTO.getPage())){
            libraryChangeDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(libraryChangeDTO.getLimit())){
            libraryChangeDTO.setLimit(10);
        }
        Integer page = libraryChangeDTO.getPage();
        Integer limit = libraryChangeDTO.getLimit();
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
        libraryChangeDTO.setId(-1);

        if(IsEmptyUtils.isEmpty(libraryChangeDTO.getChangeBatch())){
            libraryChangeDTO.setChangeBatch(-1);
        }
        if(IsEmptyUtils.isEmpty(libraryChangeDTO.getChangeMode())){
            libraryChangeDTO.setChangeMode("c");
        }
        if(IsEmptyUtils.isEmpty(libraryChangeDTO.getChangePeopleId())){
            libraryChangeDTO.setChangePeopleId(-1);
        }
        if(IsEmptyUtils.isEmpty(libraryChangeDTO.getIngredientBaseId())){
            libraryChangeDTO.setIngredientBaseId(-1);
        }
        libraryChangeDTO.setChangeNumber(0.0);
        libraryChangeDTO.setChangePrice(0.0);
        libraryChangeDTO.setChangeRemarks("c");
        libraryChangeDTO.setChangeTime("c");
        if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getSdIds())){
            String[] sdIds = libraryChangeDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                libraryChangeDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                libraryChangeDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = libraryChangeMapper.findLibraryChangeTotal(libraryChangeDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                libraryChangeDTO.setSdId(Integer.parseInt(sourceNames[i]));
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                libraryChangeDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                libraryChangeDTO.setStart(startEndNums.get(i).get(0));
                libraryChangeDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                libraryChangeDTO.setSdId(Integer.parseInt(sourceNames[i]));
                libraryChangeDTO.setSiteName(siteData.getName());
                libraryChangeDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = libraryChangeMapper.findLibraryChangeList(libraryChangeDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(libraryChangeDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                libraryChangeDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                libraryChangeDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = libraryChangeMapper.findLibraryChangeTotal(libraryChangeDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                logger.info("--"+siteData.getDateSourceName()+"--");
                libraryChangeDTO.setPicturePrefix(siteData.getPicturePrefix());
                libraryChangeDTO.setSdId(Integer.parseInt(sourceNames[i]));
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                libraryChangeDTO.setStart(startEndNums.get(i).get(0));
                libraryChangeDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                libraryChangeDTO.setSdId(Integer.parseInt(sourceNames[i]));
                libraryChangeDTO.setSiteName(siteData.getName());
                libraryChangeDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = libraryChangeMapper.findLibraryChangeList(libraryChangeDTO);
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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 17:39
     */
    @Override
    public ResultBean updateLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(libraryChangeDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(libraryChangeDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-----" + dataSourceName + "------");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            libraryChangeMapper.updateByPrimaryKey(libraryChangeDTO);

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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 15:43
     */
    @Override
    public ResultBean addLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getSdIds())){
            String[] sdIds = libraryChangeDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                libraryChangeDTO.setSdId(Integer.parseInt(sdId));
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
                    logger.info("--" + dataSourceName + "----");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    libraryChangeMapper.insertSelective(libraryChangeDTO);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
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
            if(!IsEmptyUtils.isEmpty(jobj.get(libraryChangeDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(libraryChangeDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                libraryChangeMapper.insertSelective(libraryChangeDTO);
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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 17:41
     */
    @Override
    public ResultBean findUpdLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        libraryChangeDTO.setChangeBatch(-1);
        libraryChangeDTO.setChangeMode("c");
        libraryChangeDTO.setChangePeopleId(-1);
        libraryChangeDTO.setIngredientBaseId(-1);
        libraryChangeDTO.setChangeNumber(0.0);
        libraryChangeDTO.setChangePrice(0.0);
        libraryChangeDTO.setChangeRemarks("c");
        libraryChangeDTO.setChangeTime("c");
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(libraryChangeDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(libraryChangeDTO.getSdId().toString()).toString(),SiteData.class);
            }
            libraryChangeDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> morningMeetingResult = libraryChangeMapper.findLibraryChange(libraryChangeDTO);

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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 17:42
     */
    @Override
    public ResultBean delLibraryChange(LibraryChangeDTO libraryChangeDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(libraryChangeDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(libraryChangeDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-" + dataSourceName + "-----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            libraryChangeMapper.deleteByPrimaryKey(libraryChangeDTO);
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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 18:56
     */
    @Override
    public ResultBean addByDetails(LibraryChangeDTO libraryChangeDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        libraryChangeDTO.setChangeTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(libraryChangeDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(libraryChangeDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-" + dataSourceName + "-");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            WarehouseDetailsDTO warehouseDetailsDTO = new WarehouseDetailsDTO();
            warehouseDetailsDTO.setId(libraryChangeDTO.getChangeBatch());
            warehouseDetailsDTO.setSdId(libraryChangeDTO.getSdId());
            warehouseDetailsDTO.setUnitPrice(libraryChangeDTO.getChangePrice());

            LibraryDTO libraryDTO = new LibraryDTO();
            libraryDTO.setIngredientBaseId(libraryChangeDTO.getIngredientBaseId());
            libraryDTO.setSdId(libraryChangeDTO.getSdId());
            Integer maxId = libraryMapper.findMaxId(libraryDTO);
            libraryDTO.setId(maxId);
            if(libraryChangeDTO.getChangeMode().equals("1")){
                warehouseDetailsDTO.setInventoryBalance(libraryChangeDTO.getBitchCount()+libraryChangeDTO.getChangeNumber());
                libraryDTO.setLibrarySurplus(libraryChangeDTO.getChangeNumber()*-1);
                libraryMapper.updateLSruplusByMaxIBId(libraryDTO);
            }else if(libraryChangeDTO.getChangeMode().equals("2")){
                libraryDTO.setLibrarySurplus(libraryChangeDTO.getChangeNumber());
                libraryMapper.updateLSruplusByMaxIBId(libraryDTO);
                warehouseDetailsDTO.setInventoryBalance(libraryChangeDTO.getBitchCount()-libraryChangeDTO.getChangeNumber());
            }
            warehouseDetailsMapper.updateByPrimaryKey(warehouseDetailsDTO);
            libraryChangeMapper.insertSelective(libraryChangeDTO);
            warehouseDetailsDTO.setWarehouseCount(0.0);
            warehouseDetailsDTO.setProductionDate("p");
            warehouseDetailsDTO.setObjectCardunify(-1);
            warehouseDetailsDTO.setInventoryStatus("s");
            warehouseDetailsDTO.setBatchNum("b");
            warehouseDetailsDTO.setIngredientId(-1);
            Map<String,Object> wDetails = warehouseDetailsMapper.findWarehouseDetails(warehouseDetailsDTO);
            resultBean.setSuccess(true);
            resultBean.setResult(wDetails);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        return resultBean;
    }
}
