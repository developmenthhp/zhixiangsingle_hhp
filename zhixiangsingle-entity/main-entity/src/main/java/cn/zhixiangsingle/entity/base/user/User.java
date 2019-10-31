package cn.zhixiangsingle.entity.base.user;

import cn.zhixiangsingle.entity.base.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.user
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:10
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="user")
public class User extends BaseEntity {
    @NotBlank(message = "用户名不能为空")
    @Length(min=5, max=20, message="用户名长度必须在5-20之间")
    @Pattern(regexp = "^[a-zA-Z_]\\w{4,19}$", message = "用户名必须以字母下划线开头，可由字母数字下划线组成")
    private String userName;
    /* 邮箱 */
    private String email;
    @NotBlank(message = "密码不能为空")
    @Length(min=6, max=40, message="密码长度必须在6-40之间")
    private String password;
    // 创建时间
    /*@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;
    /* 登录标识 */
    private String token;
    private Integer insertUid;
    private Date updateTime;
    private Boolean job;
    //工种(类似权限采用二进制筛选)
    private Integer profession;
    private Boolean zx;
    private Integer sdId;
    private Integer qsType;
    private Integer classes;
    private String accessCode;
}
