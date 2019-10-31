package cn.zhixiangsingle.impl;

import cn.zhixiangsingle.component.RedisServiceImpl;
import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.config.SpringBeanFactoryUtils;
import cn.zhixiangsingle.dao.fromWallAlert.FromWallAlertMapper;
import cn.zhixiangsingle.dao.fromWallBase.FromWallBaseMapper;
import cn.zhixiangsingle.dao.gasAlarm.GasAlarmMapper;
import cn.zhixiangsingle.dao.gasBase.GasBaseMapper;
import cn.zhixiangsingle.dao.gasSwitchAlert.GasSwitchAlertMapper;
import cn.zhixiangsingle.dao.gasSwitchBase.GasSwitchBaseMapper;
import cn.zhixiangsingle.dao.ratplateAlert.RatplateAlertMapper;
import cn.zhixiangsingle.dao.ratplateBase.RatplateBaseMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.dao.slipperyAlert.SlipperyAlertMapper;
import cn.zhixiangsingle.dao.slipperyBase.SlipperyBaseMapper;
import cn.zhixiangsingle.dao.temperature.TemperatureMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.TemplateCodeUtil;
import cn.zhixiangsingle.entity.base.haiKang.HaiKang;
import cn.zhixiangsingle.entity.base.leCheng.LeCheng;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.fromWall.fromWallAlert.dto.FromWallAlertDTO;
import cn.zhixiangsingle.entity.fromWall.fromWallBase.dto.FromWallBaseDTO;
import cn.zhixiangsingle.entity.gas.GasBase.dto.GasBaseDTO;
import cn.zhixiangsingle.entity.gas.gasAlarm.dto.GasAlarmDTO;
import cn.zhixiangsingle.entity.gasSwitch.gasSwitchAlert.dto.GasSwitchAlertDTO;
import cn.zhixiangsingle.entity.gasSwitch.gasSwitchBase.dto.GasSwitchBaseDTO;
import cn.zhixiangsingle.entity.ratplate.ratplateAlert.dto.RatplateAlertDTO;
import cn.zhixiangsingle.entity.ratplate.ratplateBase.dto.RatplateBaseDTO;
import cn.zhixiangsingle.entity.slippery.slipperyAlert.dto.SlipperyAlertDTO;
import cn.zhixiangsingle.entity.slippery.slipperyBase.dto.SlipperyBaseDTO;
import cn.zhixiangsingle.service.MqttGateway;
import cn.zhixiangsingle.web.request.DownloadUrlFile;
import cn.zhixiangsingle.web.service.deviceManager.impl.DeviceManagerServiceImpl;
import cn.zhixiangsingle.service.MqttReceiveService;
import cn.zhixiangsingle.web.responsive.ResultBean;
import cn.zhixiangsingle.web.service.haiKangManager.impl.HaiKangManagerServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 11:08
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class MqttReceiveServiceImpl implements MqttReceiveService {
    private static final Logger logger = LoggerFactory
            .getLogger(MqttReceiveServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private GasBaseMapper gasBaseMapper;
    @Autowired
    private GasAlarmMapper gasAlarmMapper;
    @Autowired
    private RatplateBaseMapper ratplateBaseMapper;
    @Autowired
    private RatplateAlertMapper ratplateAlertMapper;
    @Autowired
    private TemperatureMapper temperatureMapper;
    @Autowired
    private FromWallBaseMapper fromWallBaseMapper;
    @Autowired
    private FromWallAlertMapper fromWallAlertMapper;
    @Autowired
    private SlipperyBaseMapper slipperyBaseMapper;
    @Autowired
    private SlipperyAlertMapper slipperyAlertMapper;
    @Autowired
    private GasSwitchBaseMapper gasSwitchBaseMapper;
    @Autowired
    private GasSwitchAlertMapper gasSwitchAlertMapper;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private MqttGateway mqttGateway;
    @Bean(name = "gasBaseMapper")
    public GasBaseMapper getGasBaseMapper(){
        return gasBaseMapper;
    }
    @Bean(name = "gasAlarmMapper")
    public GasAlarmMapper getGasAlarmMapper(){
        return gasAlarmMapper;
    }
    @Bean(name = "ratplateBaseMapper")
    public RatplateBaseMapper getRatplateBaseMapper(){
        return ratplateBaseMapper;
    }
    @Bean(name = "ratplateAlertMapper")
    public RatplateAlertMapper getRatplateAlertMapper(){
        return ratplateAlertMapper;
    }
    @Bean(name = "fromWallBaseMapper")
    public FromWallBaseMapper getFromWallBaseMapper(){
        return fromWallBaseMapper;
    }
    @Bean(name = "fromWallAlertMapper")
    public FromWallAlertMapper getFromWallAlertMapper(){
        return fromWallAlertMapper;
    }
    @Bean(name = "slipperyBaseMapper")
    public SlipperyBaseMapper getSlipperyBaseMapper(){
        return slipperyBaseMapper;
    }
    @Bean(name = "slipperyAlertMapper")
    public SlipperyAlertMapper getSlipperyAlertMapper(){
        return slipperyAlertMapper;
    }
    @Bean(name = "gasSwitchBaseMapper")
    public GasSwitchBaseMapper getGasSwitchBaseMapper(){
        return gasSwitchBaseMapper;
    }
    @Bean(name = "gasSwitchAlertMapper")
    public GasSwitchAlertMapper getGasSwitchAlertMapper(){
        return gasSwitchAlertMapper;
    }
    @Bean(name = "redisServiceImpl")
    public RedisServiceImpl getRedisServiceImpl(){
        return redisService;
    }
    @Bean(name = "mqttGateway1")
    public MqttGateway getMqttGateway(){
        return mqttGateway;
    }
    public MqttReceiveServiceImpl(){
        GasBaseMapper gasBaseMapper2 = SpringBeanFactoryUtils.getBean("gasBaseMapper",GasBaseMapper.class);
        this.gasBaseMapper = gasBaseMapper2;
        GasAlarmMapper gasAlarmMapper2 = SpringBeanFactoryUtils.getBean("gasAlarmMapper",GasAlarmMapper.class);
        this.gasAlarmMapper = gasAlarmMapper2;

        RatplateBaseMapper ratplateBaseMapper2 = SpringBeanFactoryUtils.getBean("ratplateBaseMapper",RatplateBaseMapper.class);
        this.ratplateBaseMapper = ratplateBaseMapper2;
        RatplateAlertMapper ratplateAlertMapper2 = SpringBeanFactoryUtils.getBean("ratplateAlertMapper",RatplateAlertMapper.class);
        this.ratplateAlertMapper = ratplateAlertMapper2;

        FromWallBaseMapper fromWallBaseMapper2 = SpringBeanFactoryUtils.getBean("fromWallBaseMapper",FromWallBaseMapper.class);
        this.fromWallBaseMapper = fromWallBaseMapper2;
        FromWallAlertMapper fromWallAlertMapper2 = SpringBeanFactoryUtils.getBean("fromWallAlertMapper",FromWallAlertMapper.class);
        this.fromWallAlertMapper = fromWallAlertMapper2;

        SlipperyBaseMapper slipperyBaseMapper2 = SpringBeanFactoryUtils.getBean("slipperyBaseMapper",SlipperyBaseMapper.class);
        this.slipperyBaseMapper = slipperyBaseMapper2;
        SlipperyAlertMapper slipperyAlertMapper2 = SpringBeanFactoryUtils.getBean("slipperyAlertMapper",SlipperyAlertMapper.class);
        this.slipperyAlertMapper = slipperyAlertMapper2;

        GasSwitchBaseMapper gasSwitchBaseMapper2 = SpringBeanFactoryUtils.getBean("gasSwitchBaseMapper",GasSwitchBaseMapper.class);
        this.gasSwitchBaseMapper = gasSwitchBaseMapper2;
        GasSwitchAlertMapper gasSwitchAlertMapper2 = SpringBeanFactoryUtils.getBean("gasSwitchAlertMapper",GasSwitchAlertMapper.class);
        this.gasSwitchAlertMapper = gasSwitchAlertMapper2;

        RedisServiceImpl redisServiceImpl = SpringBeanFactoryUtils.getBean("redisServiceImpl",RedisServiceImpl.class);
        this.redisService = redisServiceImpl;
        MqttGateway mqttGateway2 = SpringBeanFactoryUtils.getBean("mqttGateway",MqttGateway.class);
        this.mqttGateway = mqttGateway2;
    }

    @Override
    @Async("defaultThreadPool") //此处方法实现被加入到线程池中执行。当前的方法立刻返回，上级调用即可结束
    public ResultBean handlerMqttMessage(String topic, String data) {
        logger.info("开始耗时等待...............");
        logger.info("this topic is:"+topic+",this data = "+data);
        try {
            StringBuffer stringBufferJson = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                stringBufferJson.append(line);
            }
            JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(topic))){
                siteData = JSON.parseObject(jobj.get(topic).toString(),SiteData.class);
            }
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
               String lcOrHK = siteData.getLcOrHK();
               String localPath = siteData.getLocalPath()+siteData.getSinglePath();
               String imgPath = siteData.getImgPath();

                HaiKang haiKang = siteData.getHaiKang();
                LeCheng leCheng = siteData.getLeCheng();
                Map<String,String> staticParams = new HashMap<>();
                staticParams.put("host",haiKang.getHkHost());
                staticParams.put("appKey",haiKang.getAppKey());
                staticParams.put("appSecret",haiKang.getAppSecret());

                staticParams.put("lcHost",leCheng.getLcHost());
                staticParams.put("lcPort",leCheng.getLcPort());
                staticParams.put("lcAppId",leCheng.getAppId());
                staticParams.put("lcSecret", leCheng.getSecret());
                staticParams.put("lcPhone",leCheng.getPhone());

                if(!IsEmptyUtils.isEmpty(data)){
                    String[] datas = data.split(":");
                    String derivesNum = datas[0];
                    String type = datas[1];
                    String value = datas[2];
                    String currTime = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
                    switch (type){
                        case "tempecc":
                            /*String[] values = value.split(";");
                            TemperatureDTO temperatureDTO = new TemperatureDTO();
                            temperatureDTO.setHumidityVal(values[1].replace("00", "")+"%RH");
                            temperatureDTO.setStat();
                            temperatureDTO.setTemperatureArea();
                            temperatureDTO.setTemperatureSensor(derivesNum);
                            temperatureDTO.setTemperatureVal(values[0].replace("00", "")+"℃");
                            temperatureDTO.setTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
                            temperatureDTO.setSdId(siteData.getSdId());*/
                            break;
                        case "gascc":
                            GasBaseDTO gasBaseDTO = new GasBaseDTO();
                            gasBaseDTO.setId(-1);
                            gasBaseDTO.setGasSensor(derivesNum);
                            gasBaseDTO.setGasArea("g");
                            gasBaseDTO.setSdId(-1);
                            GasBaseDTO resultGaseBase = null;
                            List<Map<String,Object>> gasBaseDTOList = gasBaseMapper.findGasBaseList(gasBaseDTO);
                            if(IsEmptyUtils.isEmpty(gasBaseDTOList)){
                                resultGaseBase = TemplateCodeUtil.mapTrasnToObject(gasBaseDTOList.get(0),GasBaseDTO.class);
                                resultGaseBase.setGasVal("1000>");
                                resultGaseBase.setGasYhVal("异常");
                                resultGaseBase.setTime(currTime);
                                gasBaseMapper.updateByPrimaryKey(resultGaseBase);

                                GasAlarmDTO gasAlarmDTO = new GasAlarmDTO();
                                gasAlarmDTO.setAlarmArea(resultGaseBase.getGasArea());
                                gasAlarmDTO.setAlarmTime(currTime);
                                gasAlarmDTO.setDescriptionInfo(resultGaseBase.getGasArea()+"煤气燃气值可能发生泄漏 ,请立刻进行检查。时间:"+currTime);
                                gasAlarmDTO.setHandleStatus("1");
                                gasAlarmDTO.setSdId(resultGaseBase.getSdId());
                                gasAlarmMapper.insertSelective(gasAlarmDTO);
                            }
                            break;
                        case "ratplatecc":
                            RatplateBaseDTO ratplateBaseDTO = new RatplateBaseDTO();
                            ratplateBaseDTO.setRatplateArea("r");
                            ratplateBaseDTO.setRatplateRultionsTime("r");
                            ratplateBaseDTO.setRatplateSensor(derivesNum);
                            ratplateBaseDTO.setSdId(-1);
                            List<Map<String,Object>> ratplateBaseDTOS = ratplateBaseMapper.findRatplateBaseList(ratplateBaseDTO);
                            if(!IsEmptyUtils.isEmpty(ratplateBaseDTOS)){
                                RatplateBaseDTO resultRatplateBase = TemplateCodeUtil.mapTrasnToObject(ratplateBaseDTOS.get(0),RatplateBaseDTO.class);
                                RatplateAlertDTO ratplateAlertDTO = new RatplateAlertDTO();
                                ratplateAlertDTO.setRatplateArea(resultRatplateBase.getRatplateArea());
                                ratplateAlertDTO.setRatplateDescription("声波传感检测到挡鼠板被拿起又或被移位,请及时处理..");
                                ratplateAlertDTO.setStartTime(currTime);
                                ratplateAlertDTO.setRatplateStatus("1");
                                ratplateAlertDTO.setRatplateSensor(derivesNum);
                                if(!IsEmptyUtils.isEmpty(resultRatplateBase.getRatplateRultionsTime())){
                                    //开启抓拍
                                    Object resultData = getJsonObject(lcOrHK,siteData.getSdId(),resultRatplateBase.getRatplateRultionsTime()
                                    ,localPath,imgPath,staticParams);

                                    ratplateAlertDTO.setRatplateRultionsTime(resultData.toString());
                                }
                                ratplateAlertDTO.setSdId(resultRatplateBase.getSdId());
                                ratplateAlertMapper.insertSelective(ratplateAlertDTO);
                            }
                            break;
                        case "fromwallcc":
                            FromWallBaseDTO fromWallBaseDTO = new FromWallBaseDTO();
                            fromWallBaseDTO.setFromwallArea("f");
                            fromWallBaseDTO.setFromwallSensor(derivesNum);
                            fromWallBaseDTO.setSdId(-1);
                            fromWallBaseDTO.setMaxImumDistance("m");
                            List<Map<String,Object>> fromWallBases = fromWallBaseMapper.findFromWallBaseList(fromWallBaseDTO);
                            if(!IsEmptyUtils.isEmpty(fromWallBases)){
                                FromWallBaseDTO fromWallBase = TemplateCodeUtil.mapTrasnToObject(fromWallBases.get(0),FromWallBaseDTO.class);
                                FromWallAlertDTO fromWallAlertDTO = new FromWallAlertDTO();
                                fromWallAlertDTO.setFromwallArea(fromWallBase.getFromwallArea());
                                fromWallAlertDTO.setFromwallSensor(derivesNum);
                                fromWallAlertDTO.setRatplateDescription("物品存放距离墙壁过于近,不符合相关存放规定.["+value+"]");
                                fromWallAlertDTO.setRatplateStartTime(currTime);
                                fromWallAlertDTO.setRatplateStatus("1");
                                if(!IsEmptyUtils.isEmpty(fromWallBase.getMaxImumDistance())){
                                    //开启抓拍
                                    Object resultData = getJsonObject(lcOrHK,siteData.getSdId(),fromWallBase.getMaxImumDistance()
                                            ,localPath,imgPath,staticParams);

                                    fromWallAlertDTO.setRatplateRultionsTime(resultData.toString());
                                }
                                fromWallAlertDTO.setSdId(fromWallBase.getSdId());
                                fromWallAlertMapper.insertSelective(fromWallAlertDTO);
                            }
                            break;
                        case "slipperycc":
                            SlipperyBaseDTO slipperyBaseDTO = new SlipperyBaseDTO();
                            slipperyBaseDTO.setSlipperyArea("s");
                            slipperyBaseDTO.setSlipperySensor(derivesNum);
                            slipperyBaseDTO.setSdId(-1);
                            slipperyBaseDTO.setSlipperyWater("s");
                            List<Map<String,Object>> slipperyBases = slipperyBaseMapper.findSlipperyBaseList(slipperyBaseDTO);
                            if(!IsEmptyUtils.isEmpty(slipperyBases)){
                                SlipperyBaseDTO slipperyBase = TemplateCodeUtil.mapTrasnToObject(slipperyBases.get(0),SlipperyBaseDTO.class);
                                SlipperyAlertDTO slipperyAlertDTO = new SlipperyAlertDTO();
                                slipperyAlertDTO.setRatplateArea(slipperyBase.getSlipperyArea());
                                slipperyAlertDTO.setRatplateSensor(derivesNum);
                                slipperyAlertDTO.setRatplateRultionsTime(slipperyBase.getSlipperyWater());
                                slipperyAlertDTO.setAlertTime(currTime);
                                slipperyAlertDTO.setRatplateDescription(slipperyBase.getSlipperyArea()+"地面积水时间3分钟,超过规定范围,时间:"+currTime);
                                slipperyAlertDTO.setRatplateStatus("1");
                                slipperyAlertDTO.setSdId(slipperyBase.getSdId());
                                slipperyAlertMapper.insertSelective(slipperyAlertDTO);
                            }
                            break;
                        case "cleancc":
                            redisService.set("sensor_"+derivesNum+siteData.getSdId(), value);
                            break;
                        case "switchcc":
                            GasSwitchBaseDTO gasSwitchBaseDTO = new GasSwitchBaseDTO();
                            gasSwitchBaseDTO.setArea("a");
                            gasSwitchBaseDTO.setSensor(derivesNum);
                            gasSwitchBaseDTO.setSdId(-1);
                            gasSwitchBaseDTO.setId(-1);
                            gasSwitchBaseDTO.setStatus(-1);
                            gasSwitchBaseDTO.setLcSequence("l");
                            List<Map<String,Object>> gasSwitchBases = gasSwitchBaseMapper.findGasSwitchBaseList(gasSwitchBaseDTO);
                            if(!IsEmptyUtils.isEmpty(gasSwitchBases)){
                                GasSwitchBaseDTO gasSwitchBase = TemplateCodeUtil.mapTrasnToObject(gasSwitchBases.get(0),GasSwitchBaseDTO.class);
                                Integer status = gasSwitchBase.getStatus();
                                //开启
                                if(value.equals("0")){
                                    if(status==1){
                                        GasSwitchBaseDTO updGasSwitchBase = new GasSwitchBaseDTO();
                                        updGasSwitchBase.setStatus(0);
                                        updGasSwitchBase.setId(gasSwitchBase.getId());
                                        updGasSwitchBase.setSdId(gasSwitchBase.getSdId());
                                        gasSwitchBaseMapper.updateByPrimaryKey(updGasSwitchBase);
                                        GasSwitchAlertDTO gasSwitchAlertDTO = new GasSwitchAlertDTO();
                                        gasSwitchAlertDTO.setArea(gasSwitchBase.getArea());
                                        gasSwitchAlertDTO.setDescription("正常开关");
                                        gasSwitchAlertDTO.setSensor(derivesNum);
                                        gasSwitchAlertDTO.setStartTime(currTime);
                                        gasSwitchAlertDTO.setStatus(4);
                                        if(!IsEmptyUtils.isEmpty(gasSwitchBase.getLcSequence())){
                                            //开启抓拍
                                            Object resultData = getJsonObject(lcOrHK,siteData.getSdId(),gasSwitchBase.getLcSequence()
                                                    ,localPath,imgPath,staticParams);
                                            gasSwitchAlertDTO.setOpenUrlImg(resultData.toString());
                                        }
                                        gasSwitchAlertMapper.insertSelective(gasSwitchAlertDTO);

                                    }
                                }else{
                                    mqttGateway.sendToMqtt("light_off","test001B");
                                    if(status==0){
                                        GasSwitchBaseDTO updGasSwitchBase = new GasSwitchBaseDTO();
                                        updGasSwitchBase.setStatus(1);
                                        updGasSwitchBase.setId(gasSwitchBase.getId());
                                        updGasSwitchBase.setSdId(gasSwitchBase.getSdId());
                                        gasSwitchBaseMapper.updateByPrimaryKey(updGasSwitchBase);
                                        GasSwitchAlertDTO gasSwitchAlertDTO = new GasSwitchAlertDTO();
                                        gasSwitchAlertDTO.setStatus(4);
                                        gasSwitchAlertDTO.setSensor(derivesNum);
                                        gasSwitchAlertDTO.setSdId(gasSwitchBase.getSdId());
                                        Integer maxId = gasSwitchAlertMapper.findMaxId(gasSwitchAlertDTO);
                                        GasSwitchAlertDTO gasSwitchAlertUpd = new GasSwitchAlertDTO();
                                        gasSwitchAlertUpd.setId(maxId);
                                        gasSwitchAlertUpd.setEndTime(currTime);
                                        gasSwitchAlertUpd.setSdId(gasSwitchBase.getSdId());
                                        if(!IsEmptyUtils.isEmpty(gasSwitchBase.getLcSequence())){
                                            //开启抓拍
                                            Object resultData = getJsonObject(lcOrHK,siteData.getSdId(),gasSwitchBase.getLcSequence()
                                                    ,localPath,imgPath,staticParams);
                                            gasSwitchAlertUpd.setOffUrlImg(resultData.toString());
                                        }
                                        gasSwitchAlertMapper.updateByPrimaryKey(gasSwitchAlertUpd);
                                    }
                                }
                            }
                            break;
                        default:
                            logger.info("no type equals..............");
                            break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            logger.info("耗时等待结束，线程结束............");
        }
        return null;
    }
    private Object getJsonObject(String type,Object sdId,Object deviceId,String localPath,String imgPath,Map<String,String> staticParams){
        Object resultStr = "";
        String key = sdId+"_lcToken";
        if(!IsEmptyUtils.isEmpty(type)){
            if(type.equals("lc")){
                DeviceManagerServiceImpl device =new DeviceManagerServiceImpl();
                HashMap<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("channelId", "0");
                paramsMap.put("token", redisService.get(sdId+"_lcToken"));
                paramsMap.put("deviceId", deviceId);
                paramsMap.put("encrypt", false);
                paramsMap.put("lcHost",staticParams.get("lcHost"));
                paramsMap.put("lcPort",staticParams.get("lcPort"));
                paramsMap.put("lcAppId",staticParams.get("lcAppId"));
                paramsMap.put("lcSecret", staticParams.get("lcSecret"));
                paramsMap.put("lcPhone",staticParams.get("lcPhone"));

                JSONObject json = device.setDeviceSnap(paramsMap);
                JSONObject jsonResult = json.getJSONObject("result");
                logger.info(jsonResult+"..............leCheng catch picture");
                if("TK1002".equals(jsonResult.getString("code"))){
                    logger.info(jsonResult+"");
                }else{
                    JSONObject jsondata=jsonResult.getJSONObject("data");
                    if(jsondata!=null){
                        String strurl=jsondata.getString("url");
                        if(!IsEmptyUtils.isEmpty(strurl)){
                            resultStr = strurl;
                        }
                    }else{
                        logger.info("-----------离墙距离触发拍照失败");
                    }
                }

            }else if(type.equals("hk")){
                HaiKangManagerServiceImpl haiKangManagerService = new HaiKangManagerServiceImpl();
                HashMap<String, String> map3=new HashMap<String, String>();
                map3.put("accessToken", redisService.get(sdId+"_hkToken").toString());
                map3.put("deviceSerial", deviceId.toString());
                map3.put("channelNo", "1");
                map3.put("host",staticParams.get("host"));
                map3.put("appKey",staticParams.get("appKey"));
                map3.put("appSecret",staticParams.get("appSecret"));

                JSONObject json3=haiKangManagerService.deviceCapture(map3);
                JSONObject jsondata3= json3.getJSONObject("data");
                logger.info("海康抓拍info......"+json3);
                if("10002".equals(json3.getString("code"))){
                    logger.info(json3+"");
                }else{
                    JSONObject jsondata=json3.getJSONObject("data");
                    if(jsondata!=null){
                        String strurl=jsondata.getString("picUrl");
                        if(strurl!=null){
                            //海康图片链接下载自己服务器中并保持到数据库
                            String url;
                            try {
                                url = DownloadUrlFile.download(strurl,localPath,imgPath);
                                resultStr = url;
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }else{
                            logger.info("-----离墙距离触发后拍照失败1");
                        }
                    }else{
                        logger.info("-----离墙距离触发后拍照失败1");
                    }
                }
            }
        }
        return resultStr;
    }
}
