package cn.zhixiangsingle.controller;

import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.imgPath.ImgPath;
import cn.zhixiangsingle.entity.base.job.JobData;
import cn.zhixiangsingle.entity.base.shifts.Shifts;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.business.dto.BusinessDTO;
import cn.zhixiangsingle.file.FileUploadUtil;
import cn.zhixiangsingle.img.Base64ImgUtil;
import cn.zhixiangsingle.json.JsonUtil;
import cn.zhixiangsingle.service.redis.RedisService;
import cn.zhixiangsingle.service.youTu.YouTuService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import cn.zhixiangsingle.web.responsive.WebMassage;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 13:57
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory
            .getLogger(IndexController.class);
    @Reference(version = "1.0.0")
    private YouTuService youTuService;
    @RequestMapping("/") public String transIndex() {
        return "home";
    }
    @RequestMapping("/index") public String index() {
        return "home";
    }
    @RequestMapping("/home") public String toHome() {
        return "home";
    }
    @RequestMapping("/login")
    public String toLogin() {
        return "login";
    }
    @RequestMapping("/toLogin")
    public String toTrueLogin() {
        return "toLogin";
    }
    @RequestMapping("/errorPage")
    public String toError(HttpServletRequest request) {
        return "error";
    }

    @PostMapping(value = "/uploadBase64Img")
    @ResponseBody
    public ResultBean uploadBase64Img(@RequestParam("img") String img) {
        ResultBean resultBean = new ResultBean();
        try {
            if(StringUtils.isEmpty(img)){
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            String imgPathStr = IndexController.class.getClassLoader().getResource("json/imgPathData.json").getPath();
            String jobJsonData = JsonUtil.readJsonFile(imgPathStr);
            JSONObject jobJob = JSON.parseObject(jobJsonData);
            ImgPath imgPathData = null;
            if(!IsEmptyUtils.isEmpty(jobJob.get(existUser.getSdId().toString()))){
                imgPathData = JSON.parseObject(jobJob.get(existUser.getSdId().toString()).toString(),ImgPath.class);
            }
            String filesPath = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss")
                    .format(new Date());
            String imgName = (int)((Math.random()*9+1)*100000000)+ ".png";
            // 生成文件路径
            String imgPath = imgPathData.getLocalPath()+imgPathData.getSinglePath() +"/"+ filesPath;
            File fileDir = new File(imgPath + "\\");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String fileNamePath = imgPath+"/"+imgName;
            img = img.split("base64,")[1];
            Base64ImgUtil base64ImgUtil = new Base64ImgUtil();
            if(base64ImgUtil.base64ChangeImage(img,fileNamePath)){
                //redisService.get(existUser.getSdId()+"_"+existUser.getId()+"_"+imgPathData.getSinglePath())
                resultBean.setSuccess(true);
                resultBean.setResult(imgPathData.getImgPath()+imgPathData.getSinglePath()+"/"+filesPath+"/"+imgName);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传base64位图片！异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
    @PostMapping(value = "/uploadFileImg")
    @ResponseBody
    public ResultBean uploadFileImg(@RequestParam("file")MultipartFile blobFile) {
        ResultBean resultBean = new ResultBean();
        try {
           // blobFile.getBytes();
            if(IsEmptyUtils.isEmpty(blobFile)){
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            String imgPathStr = IndexController.class.getClassLoader().getResource("json/imgPathData.json").getPath();
            String jobJsonData = JsonUtil.readJsonFile(imgPathStr);
            JSONObject jobJob = JSON.parseObject(jobJsonData);
            ImgPath imgPathData = null;
            if(!IsEmptyUtils.isEmpty(jobJob.get(existUser.getSdId().toString()))){
                imgPathData = JSON.parseObject(jobJob.get(existUser.getSdId().toString()).toString(),ImgPath.class);
            }
            String filesPath = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss")
                    .format(new Date());
            String imgName = UUID.randomUUID() + ".png";
            String imgPath = imgPathData.getLocalPath()+imgPathData.getSinglePath() +"/"+ filesPath;
            String fileNamePath = imgPath+"/"+imgName;
            FileUploadUtil fileUploadUtil = new FileUploadUtil();
            if(fileUploadUtil.uploadBlobImgFile(blobFile,imgPath,fileNamePath)){
                //redisService.get(existUser.getSdId()+"_"+existUser.getId()+"_"+imgPathData.getSinglePath())
                resultBean.setSuccess(true);
                resultBean.setResult(imgPathData.getSinglePath()+"/"+filesPath+"/"+imgName);
                resultBean.setObj(imgPathData.getImgPath());
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传blob类型图片异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
    @PostMapping(value = "/uploadOCRImg")
    @ResponseBody
    public ResultBean uploadOCRImg(@RequestParam("file")MultipartFile blobFile) {
        ResultBean resultBean = new ResultBean();
        try {
            if(IsEmptyUtils.isEmpty(blobFile)){
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            String imgPathStr = IndexController.class.getClassLoader().getResource("json/imgPathData.json").getPath();
            String jobJsonData = JsonUtil.readJsonFile(imgPathStr);
            JSONObject jobJob = JSON.parseObject(jobJsonData);
            ImgPath imgPathData = null;
            if(!IsEmptyUtils.isEmpty(jobJob.get(existUser.getSdId().toString()))){
                imgPathData = JSON.parseObject(jobJob.get(existUser.getSdId().toString()).toString(),ImgPath.class);
            }
            String filesPath = DateUtils.formatDate(new Date(),"yyyy/MM/dd/HH/mm/ss");
            String imgName = UUID.randomUUID() + ".png";
            String imgPath = imgPathData.getLocalPath()+imgPathData.getSinglePath() +"/"+ filesPath;
            String fileNamePath = imgPath+"/"+imgName;
            FileUploadUtil fileUploadUtil = new FileUploadUtil();
            if(fileUploadUtil.uploadBlobImgFile(blobFile,imgPath,fileNamePath)){
                Map<String,Object> businessDTO = youTuService.simpleCompanyOCR(imgPathData.getImgPath()+imgPathData.getSinglePath()+"/"+filesPath+"/"+imgName,existUser);
                resultBean.setSuccess(true);
                resultBean.setObj(imgPathData.getImgPath());
                resultBean.setExtraResult(imgPathData.getSinglePath()+"/"+filesPath+"/"+imgName);
                resultBean.setResult(businessDTO);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传blob类型图片并使用通用文字识别异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
