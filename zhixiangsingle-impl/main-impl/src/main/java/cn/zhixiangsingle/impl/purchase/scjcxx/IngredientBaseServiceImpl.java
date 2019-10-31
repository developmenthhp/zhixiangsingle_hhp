package cn.zhixiangsingle.impl.purchase.scjcxx;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.purchase.scjcxx.IngredientBaseMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.purchase.scjcxx.dto.IngredientBaseDTO;
import cn.zhixiangsingle.entity.purchase.scjcxx.vo.IngredientBaseVO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.purchase.scjcxx.IngredientBaseService;
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
 * @Package cn.zhixiangsingle.impl.purchase.scjcxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/20 11:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = IngredientBaseService.class)
public class IngredientBaseServiceImpl implements IngredientBaseService {
    private static final Logger logger = LoggerFactory
            .getLogger(IngredientBaseServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private IngredientBaseMapper ingredientBaseMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/14 19:04
     */
    @Override
    public ResultBean updateIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(ingredientBaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(ingredientBaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("--" + dataSourceName + "--");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            ingredientBaseMapper.updateByPrimaryKey(ingredientBaseDTO);

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
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/14 19:04
     */
    @Override
    public ResultBean addIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(ingredientBaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(ingredientBaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("--" + dataSourceName + "--");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            ingredientBaseDTO.setCreateDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            ingredientBaseMapper.insertSelective(ingredientBaseDTO);

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
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/9/20 15:22
     */
    @Override
    public ResultBean findIngredientBaseList(IngredientBaseDTO ingredientBaseDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getPage())){
            ingredientBaseDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(ingredientBaseDTO.getLimit())){
            ingredientBaseDTO.setLimit(10);
        }
        Integer page = ingredientBaseDTO.getPage();
        Integer limit = ingredientBaseDTO.getLimit();
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
        ingredientBaseDTO.setId(-1);
        ingredientBaseDTO.setScspImgPath("s");
        ingredientBaseDTO.setBasePrice(2.0);
        ingredientBaseDTO.setRatedTerm(1);
        ingredientBaseDTO.setRatedTermDW("d");
        ingredientBaseDTO.setInventoryLimit(1.0);
        ingredientBaseDTO.setCheckStatus("s");
        ingredientBaseDTO.setCreateDate("c");
        ingredientBaseDTO.setMeteringName("n");
        ingredientBaseDTO.setUnit("u");
        ingredientBaseDTO.setDeleteStatus("1");
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getIngredientName())){
            ingredientBaseDTO.setIngredientName("n");
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getMainCategoryId())){
            ingredientBaseDTO.setMainCategoryId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSmallCategoryId())){
            ingredientBaseDTO.setSmallCategoryId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getWhouseId())){
            ingredientBaseDTO.setWhouseId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSuppId())){
            ingredientBaseDTO.setSuppId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getCheckStatus())){
            ingredientBaseDTO.setCheckStatus("s");
        }
        if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getIngSdId())){
            ingredientBaseDTO.setSdId(ingredientBaseDTO.getIngSdId());
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(ingredientBaseDTO.getIngSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(ingredientBaseDTO.getIngSdId().toString()).toString(),SiteData.class);
            }
            ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                Integer totalStatus = ingredientBaseMapper.findIngredientBaseTotal(ingredientBaseDTO);
                total = total + totalStatus;
                totalAll.add(totalStatus);
            }

            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);

            for(int i=0;i<startEndNums.size();i++){
                ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                ingredientBaseDTO.setStart(startEndNums.get(i).get(0));
                ingredientBaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                ingredientBaseDTO.setSiteName(siteData.getName());
                ingredientBaseDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = ingredientBaseMapper.findIngredientBaseList(ingredientBaseDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);

        }else if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdIds())){
            String[] sdIds = ingredientBaseDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                ingredientBaseDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = ingredientBaseMapper.findIngredientBaseTotal(ingredientBaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                ingredientBaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                ingredientBaseDTO.setStart(startEndNums.get(i).get(0));
                ingredientBaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                ingredientBaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                ingredientBaseDTO.setSiteName(siteData.getName());
                ingredientBaseDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = ingredientBaseMapper.findIngredientBaseList(ingredientBaseDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(ingredientBaseDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                ingredientBaseDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = ingredientBaseMapper.findIngredientBaseTotal(ingredientBaseDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                logger.info("--"+siteData.getDateSourceName()+"--");
                ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                ingredientBaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                ingredientBaseDTO.setStart(startEndNums.get(i).get(0));
                ingredientBaseDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                ingredientBaseDTO.setSdId(Integer.parseInt(sourceNames[i]));
                ingredientBaseDTO.setSiteName(siteData.getName());
                ingredientBaseDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = ingredientBaseMapper.findIngredientBaseList(ingredientBaseDTO);
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
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/14 19:05
     */
    @Override
    public List<ResultBean> getIngredientBases(IngredientBaseDTO ingredientBaseDTO) throws Exception {
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
        ingredientBaseDTO.setBasePrice(2.0);
        ingredientBaseDTO.setRatedTerm(1);
        ingredientBaseDTO.setRatedTermDW("d");
        ingredientBaseDTO.setInventoryLimit(1.0);
        ingredientBaseDTO.setCheckStatus("s");
        ingredientBaseDTO.setMeteringName("n");
        ingredientBaseDTO.setDeleteStatus("1");
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getIngredientName())){
            ingredientBaseDTO.setIngredientName("n");
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getMainCategoryId())){
            ingredientBaseDTO.setMainCategoryId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSmallCategoryId())){
            ingredientBaseDTO.setSmallCategoryId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getWhouseId())){
            ingredientBaseDTO.setWhouseId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSuppId())){
            ingredientBaseDTO.setSuppId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getCheckStatus())){
            ingredientBaseDTO.setCheckStatus("s");
        }
        String exportTopTitle = "食材基础信息-"+DateUtils.formatDate(new Date(),"yyyy-MM-dd")+"导出";
        if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdIds())){
            String[] sdIds = ingredientBaseDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                ingredientBaseDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    ingredientBaseDTO.setSiteName(siteData.getName());

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
                    col0.add(exportTopTitle);col0.add("食材");
                    col1.add(exportTopTitle);col1.add("单位");
                    col2.add(exportTopTitle);col2.add("一级分类");
                    col3.add(exportTopTitle);col3.add("二级分类");
                    col4.add(exportTopTitle);col4.add("基础价格(元)");
                    col5.add(exportTopTitle);col5.add("保质日期");
                    col6.add(exportTopTitle);col6.add("保质日期单位");
                    col7.add(exportTopTitle);col7.add("存储警戒");
                    col8.add(exportTopTitle);col8.add("检测状态");
                    topTitles.add(col0);
                    topTitles.add(col1);
                    topTitles.add(col2);
                    topTitles.add(col3);
                    topTitles.add(col4);
                    topTitles.add(col5);
                    topTitles.add(col6);
                    topTitles.add(col7);
                    topTitles.add(col8);
                    ResultBean resultBean = new ResultBean();
                    resultBean.setMsg(siteData.getName());
                    resultBean.setObj(columnWidth);
                    List<IngredientBaseVO> mainLPList = ingredientBaseMapper.findAllIngredientBase(ingredientBaseDTO);
                    resultBean.setRows(topTitles);
                    resultBean.setResult(mainLPList);
                    mainLPLs.add(resultBean);

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(ingredientBaseDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                ingredientBaseDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }

                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){

                    logger.info("--"+siteData.getDateSourceName()+"--");

                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    ingredientBaseDTO.setSiteName(siteData.getName());

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
                    col0.add(exportTopTitle);col0.add("食材");
                    col1.add(exportTopTitle);col1.add("单位");
                    col2.add(exportTopTitle);col2.add("一级分类");
                    col3.add(exportTopTitle);col3.add("二级分类");
                    col4.add(exportTopTitle);col4.add("基础价格(元)");
                    col5.add(exportTopTitle);col5.add("保质日期");
                    col6.add(exportTopTitle);col6.add("保质日期单位");
                    col7.add(exportTopTitle);col7.add("存储警戒");
                    col8.add(exportTopTitle);col8.add("检测状态");
                    topTitles.add(col0);
                    topTitles.add(col1);
                    topTitles.add(col2);
                    topTitles.add(col3);
                    topTitles.add(col4);
                    topTitles.add(col5);
                    topTitles.add(col6);
                    topTitles.add(col7);
                    topTitles.add(col8);
                    ResultBean resultBean = new ResultBean();
                    resultBean.setMsg(siteData.getName());
                    resultBean.setObj(columnWidth);
                    List<IngredientBaseVO> mainLPList = ingredientBaseMapper.findAllIngredientBase(ingredientBaseDTO);
                    resultBean.setRows(topTitles);
                    resultBean.setResult(mainLPList);
                    mainLPLs.add(resultBean);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        return mainLPLs;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/14 19:05
     */
    @Override
    public ResultBean selectCommon(IngredientBaseDTO ingredientBaseDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        ingredientBaseDTO.setDeleteStatus("1");
        StringBuffer returnMsgName = new StringBuffer();
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdIds())){
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg("请选择站点");
        }else{
            StringBuffer stringBufferJson = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                stringBufferJson.append(line);
            }
            JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
            String[] sdIds = ingredientBaseDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                ingredientBaseDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    Integer nameCount = ingredientBaseMapper.findIngredientBaseFullNameTotal(ingredientBaseDTO);

                    if(nameCount>0){
                        if(IsEmptyUtils.isEmpty(returnMsgName)){
                            returnMsgName.append(siteData.getName());
                        }else{
                            returnMsgName.append("、"+siteData.getName());
                        }
                    }
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        if(!IsEmptyUtils.isEmpty(returnMsgName)){
            returnMsgName.append("站点食材名称已存在");
            resultBean.setMsg(returnMsgName.toString());
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/14 19:05
     */
    @Override
    public ResultBean delIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(ingredientBaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(ingredientBaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("--" + dataSourceName + "--");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            //ingredientBaseDTO.setDeleteStatus("0");
            ingredientBaseMapper.deleteByPrimaryKey(ingredientBaseDTO.getId());
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
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/14 19:05
     */
    @Override
    public ResultBean findUpdIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception {
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

        ingredientBaseDTO.setScspImgPath("s");
        ingredientBaseDTO.setBasePrice(2.0);
        ingredientBaseDTO.setRatedTerm(1);
        ingredientBaseDTO.setRatedTermDW("d");
        ingredientBaseDTO.setInventoryLimit(1.0);
        ingredientBaseDTO.setCreateDate("c");
        ingredientBaseDTO.setMeteringName("n");
        ingredientBaseDTO.setUnit("u");
        ingredientBaseDTO.setDeleteStatus("1");
        ingredientBaseDTO.setNearPeriodValue(-1);
        ingredientBaseDTO.setNeedCleaning("n");
        ingredientBaseDTO.setCleaningTime("c");
        ingredientBaseDTO.setCleanWater("c");
        ingredientBaseDTO.setBoomType(-1);
        ingredientBaseDTO.setGgxh("g");
        ingredientBaseDTO.setGgxhdw("g");
        ingredientBaseDTO.setPurchasingStandard("p");
        ingredientBaseDTO.setNoMinalrating("n");
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getIngredientName())){
            ingredientBaseDTO.setIngredientName("n");
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getMainCategoryId())){
            ingredientBaseDTO.setMainCategoryId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSmallCategoryId())){
            ingredientBaseDTO.setSmallCategoryId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getWhouseId())){
            ingredientBaseDTO.setWhouseId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSuppId())){
            ingredientBaseDTO.setSuppId(-1);
        }
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getCheckStatus())){
            ingredientBaseDTO.setCheckStatus("s");
        }

        if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){

            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(ingredientBaseDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(ingredientBaseDTO.getSdId().toString()).toString(),SiteData.class);
            }
            ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = ingredientBaseMapper.findIngredientBase(ingredientBaseDTO);
                resultBean.setResult(ingredientBaseDTOResult);
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

    @Override
    public ResultBean findAllIngredientBase(IngredientBaseDTO ingredientBaseDTO) throws Exception {
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
        if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getId())){
            ingredientBaseDTO.setId(-1);
        }
        ingredientBaseDTO.setDeleteStatus("1");
        ingredientBaseDTO.setIngredientName("n");
        ingredientBaseDTO.setMeteringName("m");
        ingredientBaseDTO.setScspImgPath("s");
        ingredientBaseDTO.setLibraryId(-1);
        ingredientBaseDTO.setWarehouseDetailsId(-1);

        if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdIds())){
            String[] sdIds = ingredientBaseDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                ingredientBaseDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    ingredientBaseDTO.setSiteName(siteData.getName());
                    ingredientBaseDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = ingredientBaseMapper.findIngredientBaseList(ingredientBaseDTO);
                    mainLPLs.addAll(mainLPList);

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(ingredientBaseDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                ingredientBaseDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){

                    logger.info("--"+siteData.getDateSourceName()+"--");
                    ingredientBaseDTO.setPicturePrefix(siteData.getPicturePrefix());

                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    ingredientBaseDTO.setSiteName(siteData.getName());
                    ingredientBaseDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = ingredientBaseMapper.findIngredientBaseList(ingredientBaseDTO);
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
}
