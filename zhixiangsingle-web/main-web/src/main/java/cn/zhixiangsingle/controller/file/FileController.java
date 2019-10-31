package cn.zhixiangsingle.controller.file;

import cn.zhixiangsingle.entity.base.OpslabConfig;
import cn.zhixiangsingle.file.FileUtil;
import cn.zhixiangsingle.img.ImageConfig;
import cn.zhixiangsingle.service.redis.RedisService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.file
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 15:27
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LoggerFactory
            .getLogger(FileController.class);
    private static final String PREFIX = OpslabConfig.get("UPLOAD_PREFIX");
    @Reference(version = "1.0.0")
    private RedisService redisService;
    @RequestMapping(value = "/fileup", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean singleFileUpload(@RequestParam("photo")MultipartFile file){
        ResultBean rb = new ResultBean();
        if (file.isEmpty()) {
            rb.setResultCode(IStatusMessage.SystemStatus.FILE_IS_EMPTY.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.FILE_IS_EMPTY.getMessage());
            return rb;
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为--------->："+fileName);
        //文件存放目录
        String beforePath = FileUtil.getFileDri();
        String pathName = ImageConfig.SITE_ICON.getDirectory()+beforePath;
        //
        File fileDir = new File(pathName+"\\");
        String path = fileDir.getAbsolutePath();
        logger.info("最终上传邮件附件名--------->："+fileName+"---fileDir.getParentFile()"+fileDir.getParentFile()+"---beforePath"+beforePath);
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = FileUtil.getMyFilePath(suffixName);
        logger.info("最终名--------->："+fileName);
        System.out.println(fileDir.getParentFile());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            /*User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            String key = existUser.getSdid()+"-MyEmailPhotoDeleteFlag"+"-"+existUser.getId();
            redisService.set(key,path+"/"+fileName);
            System.out.println(redisService.get(key)+".................................................path+\"/\"+fileName......."+path+"/"+fileName);*/
            //在线项目使用
            file.transferTo(new File(fileDir.getParentFile()+"/", fileName));
            //本地使用
            //file.transferTo(new File(path, fileName));
            rb.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            /*rb.setData(OpslabConfig.get("UPLOAD_PREFIX")+beforePath+fileName);*/
            rb.setResult(beforePath+fileName);
            rb.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        } catch (Exception e) {
            rb.setResultCode(OpslabConfig.get("")+IStatusMessage.SystemStatus.ERROR.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            e.printStackTrace();
        }
        return rb;
    }
}
