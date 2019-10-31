package cn.zhixiangsingle.entity.base;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 18:39
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class IsEmptyUtils {
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        else if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        else if (obj instanceof Collection) return ((Collection<?>) obj).isEmpty();
        else if (obj instanceof Map) return ((Map<?, ?>) obj).isEmpty();
        else if (obj.getClass().isArray()) return Array.getLength(obj) == 0;

        return false;
    }
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
    public static void main(String args[]) {
        int[] code = {};
        System.out.println(isEmpty(code));
    }
}
