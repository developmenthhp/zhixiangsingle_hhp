package cn.zhixiangsingle.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.file
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 19:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class FileUtil {
    private static Random random = new Random();
    private static SimpleDateFormat format = new SimpleDateFormat("/yyyy/MM/dd/HH");
    static
    {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }
    /**
     * 操作系统默认分隔符
     */
    public static String S = File.separator;

    /**
     * 将路径字符串标准化,去掉path首尾 /
     *
     * @param path
     * @return
     */
    public static String cleanPath(String path) {
        if (path == null) {
            return null;
        }
        path = path.trim();
        if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }

        path = path.replace("/", S);
        path = path.replace("\\", S);
        path = path.replace(S + S, S);

        if (path.startsWith(S) || path.startsWith(S)) {
            path = path.substring(1);
        }
        if (path.endsWith(S) || path.endsWith(S)) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
    public static String cleanRealPath(String path) {
        if (path == null) {
            return null;
        }
        path = path.trim();
        if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }

        path = path.replace("\\", "/");

        if (path.startsWith("/") || path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/") || path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
    /**
     * 获取文件存储路径
     *
     * @return FileUrl
     */
    private static String getFileUrl()
    {
        return format.format(new Date().getTime());
    }
    /**
     * 生成文件名称
     *
     * @return FileName
     */
    private static String genFileName()
    {
        return new Date().getTime() + String.valueOf(random.nextLong()).substring(1, 4);
    }
    /**
     * 根据文件后辍名, 生成文件存储目录
     *
     * @param suffixName
     * @return
     */
    public static String getFilePath(String suffixName)
    {
        return getFileUrl() + File.separatorChar + genFileName() + suffixName;
    }
    public static String getFileDri()
    {
        return getFileUrl() + File.separatorChar;
    }
    public static String getMyFilePath(String suffixName)
    {
        return genFileName() + suffixName;
    }
    public static String getFilePath(String suffixName,String prefix)
    {
        return getFileUrl() + File.separatorChar +prefix+ genFileName() + suffixName;
    }
    public static File createFilePath(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.isDirectory()){
            file.mkdirs();
        }
        return file;
    }
}
