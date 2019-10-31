package cn.zhixiangsingle.img;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.img
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/11 13:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class Base64ImgUtil {
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:图片转BASE64
     * @author: hhp
     * @param:  * @param imagePath 图片位置包括完整图片名
     * @date: 2019/9/11 13:31
     */
    public String imageChangeBase64(String imagePath){
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imagePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param baseStr base64字符串
     * @param imagePath 生成的图片路径和名字
     * @date: 2019/9/11 13:33
     */
    public boolean base64ChangeImage(String baseStr,String imagePath){
        if (baseStr == null){
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(baseStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imagePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static void main(String[] args){
        Base64ImgUtil base64ImgUtil = new Base64ImgUtil();
        //加密
        String basestr = base64ImgUtil.imageChangeBase64("E://huangTao.JPG");
        //System.out.println(basestr);
        //解密
        // 生成文件名
        String filesPath = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss")
                .format(new Date());

        String imgName = (int)((Math.random()*9+1)*100000000)+ ".png";
        // 生成文件路径
        String imgPath = "/home/hhp_img/hhp_suZhouDaXue_img/" + filesPath;
        File fileDir = new File(imgPath + "\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String fileNamePath = imgPath+"/"+imgName;

        System.out.println((int)((Math.random()*9+1)*100000000)+",,,,,"+((new Random().nextInt(900000000) % (9 - 1 + 1))));
        //base64ImgUtil.base64ChangeImage(basestr,fileNamePath);
    }
}
