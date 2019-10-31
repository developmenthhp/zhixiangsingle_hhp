package cn.zhixiangsingle.web.request;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.file.FileUtil;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.request
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 16:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class DownloadUrlFile {
    public static final String ext = ".JPG,.PNG";
    public static String download(String urlString, String localPath, String imgUrl) throws Exception {
        String filename2="";
        String suffixName = urlString.substring(urlString.lastIndexOf("."));
        if(validateFileExtName(suffixName)){
            filename2 = FileUtil.getFilePath(suffixName);
        }else{
            throw new Exception("请上传jpg或png格式的照片");
        }
        String path=filename2.substring(filename2.indexOf("/"),filename2.lastIndexOf("/"));
        filename2=filename2.substring(filename2.lastIndexOf("/")+1,filename2.length());
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(localPath+path);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+File.separatorChar+filename2);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return imgUrl+path+File.separatorChar+filename2;
    }
    private static boolean validateFileExtName(String extName){
        if(!IsEmptyUtils.isEmpty(extName)){
            String[] exts = ext.split(",");
            for (String extStr : exts){
                if (extStr.equalsIgnoreCase(extName.toUpperCase())){
                    return true;
                }
            }
        }
        return false;
    }
}
