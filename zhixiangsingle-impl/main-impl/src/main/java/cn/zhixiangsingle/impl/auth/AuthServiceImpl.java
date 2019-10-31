package cn.zhixiangsingle.impl.auth;

import cn.zhixiangsingle.annotation.TargetDataSource;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.permission.Permission;
import cn.zhixiangsingle.entity.base.permission.vo.PermissionVO;
import cn.zhixiangsingle.entity.base.role.Role;
import cn.zhixiangsingle.entity.base.role.dto.RoleDTO;
import cn.zhixiangsingle.entity.base.role.vo.RoleVO;
import cn.zhixiangsingle.entity.base.rolePermission.RolePermissionKey;
import cn.zhixiangsingle.dao.permission.PermissionMapper;
import cn.zhixiangsingle.dao.rolePermission.RolePermissionMapper;
import cn.zhixiangsingle.dao.role.RoleMapper;
import cn.zhixiangsingle.service.auth.AuthService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.auth
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:53
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = AuthService.class)
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory
            .getLogger(AuthServiceImpl.class);
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param permission
     * @date: 2019/8/23 12:04
     */
    @Override
    public int addPermission(Permission permission) {
        return this.permissionMapper.insert(permission);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/23 12:04
     */
    @Override
    @TargetDataSource(name = "default")
    public List<Permission> permList() {
        Permission permission = new Permission();
        permission.setInsertTime(new Date());
        permission.setCode("c");
        permission.setUpdateTime(new Date());
        permission.setDescpt("d");
        permission.setIcon("i");
        permission.setIsType(-1);
        permission.setName("n");
        permission.setPage("p");
        permission.setPid(-1);
        permission.setZindex(-1);
        permission.setId(-1);
        return this.permissionMapper.findAll(permission);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param permission
     * @date: 2019/8/23 12:05
     */
    @Override
    @TargetDataSource(name = "default")
    public int updatePerm(Permission permission) {
        return this.permissionMapper.updateByPrimaryKeySelective(permission);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:05
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean getPermission(int id) {
        PermissionVO permission = new PermissionVO();
        permission.setZindex(-1);
        permission.setPid(-1);
        permission.setPage("p");
        permission.setName("n");
        permission.setIsType(-1);
        permission.setIcon("i");
        permission.setDescpt("d");
        permission.setCode("c");
        permission.setId(-1);
        permission.setUserId(id);
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(true);
        resultBean.setResult(this.permissionMapper.selectByPrimaryKey(permission));
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:05
     */
    @Override
    @TargetDataSource(name = "default")
    public String delPermission(int id) {
        Permission permission = new Permission();
        permission.setId(id);
        List<Permission> childPerm = this.permissionMapper.findChildPerm(permission);
        permission = null;
        if(!IsEmptyUtils.isEmpty(childPerm)){
            return IStatusMessage.SystemStatus.SUN_PERMISSION_NOT_DELETE.getMessage();
        }
        if(this.permissionMapper.deleteByPrimaryKey(id)>0){
            return IStatusMessage.SystemStatus.SUCCESS.getMessage();
        }else{
            return IStatusMessage.SystemStatus.SUCCESS.getMessage();
        }
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param roleDTO
     * @param page
     * @param limit
     * @date: 2019/8/23 12:11
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean roleList(RoleDTO roleDTO, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        if (null == page) {
            page = 1;
        }
        if (null == limit) {
            limit = 10;
        }
        PageHelper.startPage(page, limit);
        List<Map<String,Object>> roleList = this.roleMapper.findList(roleDTO);
        // 获取分页查询后的数据
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(roleList);
        resultBean.setSuccess(true);
        // 设置获取到的总记录数total：
        resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        resultBean.setResult(roleList);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        logger.debug("角色列表查询=roleList:" + roleList);
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param flag
     * @param userId
     * @date: 2019/8/23 12:11
     */
    @Override
    @TargetDataSource(name = "default")
    public List<PermissionVO> findPerms(Integer flag, Integer userId) {
        List<PermissionVO> permissionVOS = null;
        if(flag==1){
            PermissionVO permissionVO = new PermissionVO();
            permissionVO.setName("n");
            permissionVO.setId(0);
            permissionVOS = this.permissionMapper.getUserPerms(permissionVO);
            permissionVO = null;
        }else if(flag==0){
            Permission permission = new Permission();
            permission.setId(-1);
            permission.setName("n");
            permissionVOS = this.permissionMapper.findPerms(permission);
            permission = null;
        }
        return permissionVOS;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param role
     * @param permIds
     * @date: 2019/8/23 12:12
     */
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String addRole(Role role, String permIds) {
        this.roleMapper.insertSelective(role);
        int roleId=role.getId();
        String[] arrays=permIds.split(",");
        logger.debug("权限id =arrays="+arrays.toString());
        /*setRolePerms(roleId, arrays);*/
        List<String> strsToList1= Arrays.asList(arrays);
        int inserCount = this.rolePermissionMapper.insertBitch(roleId,strsToList1);
        return "ok";
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:12
     */
    @Override
    @TargetDataSource(name = "default")
    public RoleVO findRoleAndPerms(Integer id) {
        return this.roleMapper.findRoleAndPerms(id);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param role
     * @param permIds
     * @date: 2019/8/23 12:12
     */
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=80000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean updateRole(Role role, String permIds) {
        ResultBean resultBean = new ResultBean();
        int roleId=role.getId();
        String[] arrays=permIds.split(",");
        logger.debug("权限id =arrays="+arrays.toString());
        //1，更新角色表数据；
        int num=this.roleMapper.updateByPrimaryKeySelective(role);
        if(num<1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
        RolePermissionKey rpk = new RolePermissionKey();
        rpk.setRoleId(roleId);
        int delCount = this.rolePermissionMapper.deleteByPrimaryKey(rpk);
        rpk = null;
        List<String> strsToList1= Arrays.asList(arrays);
        int inserCount = this.rolePermissionMapper.insertBitch(roleId,strsToList1);
        if(!IsEmptyUtils.isEmpty(arrays)&&inserCount<1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/10/14 19:00
     */
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean delRole(int id) {
        ResultBean resultBean = new ResultBean();
        //1.删除角色对应的权限
        RolePermissionKey rpk = new RolePermissionKey();
        rpk.setRoleId(id);
        this.rolePermissionMapper.deleteByPrimaryKey(rpk);
        //2.删除角色
        int num=this.roleMapper.deleteByPrimaryKey(id);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param sdId
     * @date: 2019/8/23 12:14
     */
    @Override
    @TargetDataSource(name = "default")
    public List<Role> getRoles(Role role) {
        return this.roleMapper.getRoles(role);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param userId
     * @date: 2019/8/23 12:15
     */
    @Override
    @TargetDataSource(name = "default")
    public List<Role> getRoleByUser(Integer userId) {
        return this.roleMapper.getRoleByUserId(userId);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:15
     */
    @Override
    @TargetDataSource(name = "default")
    public List<Permission> findPermsByRoleId(Integer id) {
        PermissionVO permission = new PermissionVO();
        permission.setId(id);
        permission.setCode("c");
        return this.permissionMapper.findPermsByRole(permission);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:15
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean getUserPerms(Integer id) {
        ResultBean resultBean = new ResultBean();
        PermissionVO permissionVO = new PermissionVO();
        permissionVO.setName("n");
        permissionVO.setId(0);
        permissionVO.setIcon("i");
        permissionVO.setIsType(-1);
        permissionVO.setPage("p");
        permissionVO.setUserId(id);
        permissionVO.setPid(-1);
        List<PermissionVO> permissionVOS = this.permissionMapper.getUserPerms(permissionVO);
        permissionVO = null;
        resultBean.setSuccess(false);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        resultBean.setResult(permissionVOS);
        return resultBean;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param roleId
     * @param arrays
     * @date: 2019/8/23 12:16
     */
    /*@TargetDataSource(name = "default")
    private void setRolePerms(int roleId, String[] arrays) {
        for (String permid : arrays) {
            RolePermissionKey rpk=new RolePermissionKey();
            rpk.setRoleId(roleId);
            rpk.setPermitId(Integer.valueOf(permid));
            this.rolePermissionMapper.insert(rpk);
        }
    }*/
}
