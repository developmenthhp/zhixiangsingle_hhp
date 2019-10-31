package cn.zhixiangsingle.web.responsive;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.responsive
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 19:06
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ResultBean  implements Serializable {
    /** 成功，失败标识 */
    private boolean success;
    /** 提示信息 */
    private String msg;
    /** 查询总条数 */
    private long total;
    /**总页数*/
    private long totalPage;
    /** 结果数据对象 */
    private Object result;
    /** 其他参数 */
    private Object resultCode;
    /** 结果数据对象集合 */
    private List<?> rows;
    private Object obj;
    private Object extraResult;
    public ResultBean() {

    }
}
