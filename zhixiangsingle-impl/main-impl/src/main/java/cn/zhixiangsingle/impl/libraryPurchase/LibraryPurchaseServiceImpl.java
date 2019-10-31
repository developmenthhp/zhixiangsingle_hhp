package cn.zhixiangsingle.impl.libraryPurchase;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.libraryPurchase.LibraryPurchaseMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.libraryPurchase.dto.LibraryPurchaseDTO;
import cn.zhixiangsingle.entity.libraryPurchase.vo.LibraryPurchaseVO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.libraryPurchase.LibraryPurchaseService;
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
import java.util.*;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.libraryPurchase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/24 10:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = LibraryPurchaseService.class)
public class LibraryPurchaseServiceImpl implements LibraryPurchaseService {
    private static final Logger logger = LoggerFactory
            .getLogger(LibraryPurchaseServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private LibraryPurchaseMapper libraryPurchaseMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/24 10:57
     */
    @Override
    public ResultBean findLibraryPurchaseList(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(libraryPurchaseDTO.getPage())){
            libraryPurchaseDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(libraryPurchaseDTO.getLimit())){
            libraryPurchaseDTO.setLimit(10);
        }
        Integer page = libraryPurchaseDTO.getPage();
        Integer limit = libraryPurchaseDTO.getLimit();
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
        libraryPurchaseDTO.setId(-1);
        libraryPurchaseDTO.setPurchCount(0.0);
        libraryPurchaseDTO.setLowestUnitPrice(0.0);
        libraryPurchaseDTO.setHighestUnitPrice(0.0);
        libraryPurchaseDTO.setAuthorizedPrice(0.0);
        libraryPurchaseDTO.setPurchPeople("p");
        libraryPurchaseDTO.setPurchTime("p");

        if(IsEmptyUtils.isEmpty(libraryPurchaseDTO.getPurchStatus())){
            libraryPurchaseDTO.setPurchStatus("p");
        }
        if(IsEmptyUtils.isEmpty(libraryPurchaseDTO.getIngredientBaseId())){
            libraryPurchaseDTO.setIngredientBaseId(-1);
        }
        if(IsEmptyUtils.isEmpty(libraryPurchaseDTO.getMainCategoryId())){
            libraryPurchaseDTO.setMainCategoryId(-1);
        }
        if(IsEmptyUtils.isEmpty(libraryPurchaseDTO.getSmallCategoryId())){
            libraryPurchaseDTO.setSmallCategoryId(-1);
        }
        if(!IsEmptyUtils.isEmpty(libraryPurchaseDTO.getIngSdId())){
            libraryPurchaseDTO.setSdId(libraryPurchaseDTO.getIngSdId());
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(libraryPurchaseDTO.getIngSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(libraryPurchaseDTO.getIngSdId().toString()).toString(),SiteData.class);
            }
            libraryPurchaseDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                Integer totalStatus = libraryPurchaseMapper.findLibraryPurchaseTotal(libraryPurchaseDTO);
                total = total + totalStatus;
                totalAll.add(totalStatus);
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            for(int i=0;i<startEndNums.size();i++){
                libraryPurchaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                libraryPurchaseDTO.setStart(startEndNums.get(i).get(0));
                libraryPurchaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                libraryPurchaseDTO.setSiteName(siteData.getName());
                libraryPurchaseDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = libraryPurchaseMapper.findLibraryPurchaseList(libraryPurchaseDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);

        }else if(!IsEmptyUtils.isEmpty(libraryPurchaseDTO.getSdIds())){
            String[] sdIds = libraryPurchaseDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                libraryPurchaseDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                libraryPurchaseDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = libraryPurchaseMapper.findLibraryPurchaseTotal(libraryPurchaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                libraryPurchaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                libraryPurchaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                libraryPurchaseDTO.setStart(startEndNums.get(i).get(0));
                libraryPurchaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                libraryPurchaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                libraryPurchaseDTO.setSiteName(siteData.getName());
                libraryPurchaseDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = libraryPurchaseMapper.findLibraryPurchaseList(libraryPurchaseDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(libraryPurchaseDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                libraryPurchaseDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                libraryPurchaseDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = libraryPurchaseMapper.findLibraryPurchaseTotal(libraryPurchaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                logger.info("--"+siteData.getDateSourceName()+"--");
                libraryPurchaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                libraryPurchaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                libraryPurchaseDTO.setStart(startEndNums.get(i).get(0));
                libraryPurchaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                libraryPurchaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                libraryPurchaseDTO.setSiteName(siteData.getName());
                libraryPurchaseDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = libraryPurchaseMapper.findLibraryPurchaseList(libraryPurchaseDTO);
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
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/24 14:09
     */
    @Override
    public ResultBean updateLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(libraryPurchaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(libraryPurchaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("----" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            libraryPurchaseMapper.updateByPrimaryKey(libraryPurchaseDTO);

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
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/24 14:57
     */
    @Override
    public ResultBean addLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        libraryPurchaseDTO.setPurchTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(libraryPurchaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(libraryPurchaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("" + dataSourceName + "");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            libraryPurchaseMapper.insertSelective(libraryPurchaseDTO);
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/24 14:59
     */
    @Override
    public ResultBean delLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(libraryPurchaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(libraryPurchaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-" + dataSourceName + "------");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            libraryPurchaseMapper.deleteByPrimaryKey(libraryPurchaseDTO);
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
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/25 17:04
     */
    @Override
    public List<ResultBean> getLibraryPurchases(LibraryPurchaseDTO libraryPurchaseDTO) throws Exception {
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        List<ResultBean> mainLPLs = Lists.newArrayList();

        ArrayList<Integer> totalAll = Lists.newArrayList();
        libraryPurchaseDTO.setId(-1);
        libraryPurchaseDTO.setPurchTime("p");
        libraryPurchaseDTO.setIngredientBaseId(-1);
        libraryPurchaseDTO.setPurchStatus("p");
        libraryPurchaseDTO.setAuthorizedPrice(0.0);
        libraryPurchaseDTO.setLowestUnitPrice(0.0);
        libraryPurchaseDTO.setHighestUnitPrice(0.0);
        libraryPurchaseDTO.setPurchCount(0.0);
        libraryPurchaseDTO.setMainCategoryId(-1);
        libraryPurchaseDTO.setSmallCategoryId(-1);
        String exportTopTitle = "采购单-"+DateUtils.formatDate(new Date(),"yyyy-MM-dd")+"导出";

        for(String jsonStr:libraryPurchaseDTO.getPurchPeople().split("-")){
            String[] jsonLP = jsonStr.split("_");
            String sdId = jsonLP[0];
            String[] arrays = null;
            if(jsonLP[1].indexOf(",")!=-1){
                arrays = jsonLP[1].split(",");
            }else{
                arrays = new String[1];
                arrays[0] = jsonLP[1];
            }
            Integer[] idArray = new Integer[arrays.length];
            for(int i=0;i<arrays.length;i++){
                idArray[i] = Integer.parseInt(arrays[i]);
            }

            List<Integer> ids= Arrays.asList(idArray);
            libraryPurchaseDTO.setIds(ids);
            libraryPurchaseDTO.setSdId(Integer.parseInt(sdId));
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("-" + dataSourceName + "");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map columnWidth = new HashMap();
                columnWidth.put(0,6000);
                columnWidth.put(1,4000);
                columnWidth.put(2,5000);
                columnWidth.put(3,5000);
                columnWidth.put(4,3000);
                columnWidth.put(5,3000);
                columnWidth.put(6,3000);
                columnWidth.put(7,5000);
                columnWidth.put(8,3000);
                columnWidth.put(9,6000);
                List<List<String>> topTitles = Lists.newArrayList();
                List<String> col0 = Lists.newArrayList();
                List<String> col1 = Lists.newArrayList();
                List<String> col2 = Lists.newArrayList();
                List<String> col3 = Lists.newArrayList();
                List<String> col4 = Lists.newArrayList();
                List<String> col5 = Lists.newArrayList();
                List<String> col6 = Lists.newArrayList();
                List<String> col7 = Lists.newArrayList();
                List<String> col8 = Lists.newArrayList();
                List<String> col9 = Lists.newArrayList();
                col0.add(exportTopTitle);col0.add("编号");
                col1.add(exportTopTitle);col1.add("分类");
                col2.add(exportTopTitle);col2.add("食材名称");
                col3.add(exportTopTitle);col3.add("采购数量");
                col4.add(exportTopTitle);col4.add("预算最低单价");
                col5.add(exportTopTitle);col5.add("预算最高单价");
                col6.add(exportTopTitle);col6.add("核准单价");
                col7.add(exportTopTitle);col7.add("单据状态");
                col8.add(exportTopTitle);col8.add("制单人");
                col9.add(exportTopTitle);col9.add("制单时间");
                topTitles.add(col0);
                topTitles.add(col1);
                topTitles.add(col2);
                topTitles.add(col3);
                topTitles.add(col4);
                topTitles.add(col5);
                topTitles.add(col6);
                topTitles.add(col7);
                topTitles.add(col8);
                topTitles.add(col9);
                ResultBean resultBean = new ResultBean();
                resultBean.setMsg(siteData.getName());
                resultBean.setObj(columnWidth);
                List<LibraryPurchaseVO> mainLPList = libraryPurchaseMapper.findAllLibraryPurchase(libraryPurchaseDTO);
                resultBean.setRows(topTitles);
                resultBean.setResult(mainLPList);
                mainLPLs.add(resultBean);
            }
        }
        DynamicDataSourceContextHolder.setDataSourceType(null);
        return mainLPLs;
    }
}
