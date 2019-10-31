package cn.zhixiangsingle.entity.base;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 18:42
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class PrintUtil {
    private static boolean DEBUG_FLAG = true;
    public static void out(Object content) {
        if (DEBUG_FLAG) {
            System.out.print(content);
        }
    }
    public static void outln(Object content) {
        if (DEBUG_FLAG) {
            System.out.println(content);
        }
    }
    public static void out(String TAG, Object content) {
        if (DEBUG_FLAG) {
            System.out.print("===" + TAG + ":" + content);
        }
    }
    public static void outln(String TAG, Object content) {
        if (DEBUG_FLAG) {
            System.out.println("===" + TAG + ":" + content);
        }
    }
}
