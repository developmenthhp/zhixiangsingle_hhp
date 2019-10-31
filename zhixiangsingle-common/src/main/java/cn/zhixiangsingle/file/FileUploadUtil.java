package cn.zhixiangsingle.file;

import cn.zhixiangsingle.entity.base.OpslabConfig;
import cn.zhixiangsingle.img.ImageConfig;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.file
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 19:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class FileUploadUtil {
    private static final Logger logger = LoggerFactory
            .getLogger(FileUploadUtil.class);
    public ResultBean uploadMyFile(MultipartFile file, String[] fileTypes) {
        ResultBean resultBean = new ResultBean();
        if (file.isEmpty()) {
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            return resultBean;
        }
        //获取上传邮件附件文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传邮件附件的文件名为--------->：" + fileName);
        String originalName = fileName;
        //String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        boolean typeFlag = true;
        for (String fileType : fileTypes) {
            if (fileType.equalsIgnoreCase(prefix)) {
                typeFlag = true;
                break;
            } else {
                typeFlag = false;
            }
        }
        if (!typeFlag) {
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.FILE_TYPE_NOT_SUPPORT.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.FILE_TYPE_NOT_SUPPORT.getMessage());
            return resultBean;
        }
        //文件存放目录
        String beforePath = FileUtil.getFileDri();
        String pathName = ImageConfig.SITE_ICON.getDirectory() + beforePath;
        //
        File fileDir = new File(pathName + "\\");
        String path = fileDir.getAbsolutePath();
        logger.info("上传邮件附件的文件目录--------->：" + path);
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = FileUtil.getMyFilePath(suffixName);
        logger.info("最终上传邮件附件名--------->：" + fileName + "---fileDir.getParentFile()" + fileDir.getParentFile() + "---beforePath" + beforePath);
        System.out.println(fileDir.getParentFile());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try {
            //在线项目使用
            //file.transferTo(new File(fileDir.getParentFile()+"/", fileName));
            //本地使用
            file.transferTo(new File(path, fileName));
            /*//在这里把每次上传的文件名存入redis 用分隔符隔开，rabbitmq消息读取完，删掉这些缓存
            //path+fileName
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            String key = existUser.getSdid()+"-MyEmailPhotoDeleteFlag"+"-"+existUser.getId();
            if(!IsEmptyUtils.isEmpty(redisService.get(key))){
                //在上传这一步只拼接文件路径值，在这个用户点击发送邮件的时候将存放在redis里的文件路径里的文件删除
                redisService.set(key,redisService.get(key)+","+path+"/"+fileName);
            }else{
                redisService.set(key,path+"/"+fileName);
            }
            System.out.println(redisService.get(key)+"...........................................................");*/
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            //resultBean.setMsg(WebMassage.EMAIL_OPTIONS_UPLOAD_SUCCESS);
            /*rb.setData(OpslabConfig.get("UPLOAD_PREFIX")+beforePath+fileName);*/
            resultBean.setResult(beforePath + fileName);
            resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
            resultBean.setMsg(path + "/" + fileName + "," + originalName);
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            e.printStackTrace();
        }
        return resultBean;
    }
    public boolean uploadBlobImgFile(MultipartFile file, String imgPath, String fileNamePath){
        try {
            File fileDir = new File(imgPath + "\\");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            OutputStream out = new FileOutputStream(fileNamePath);
            out.write(file.getBytes());
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
