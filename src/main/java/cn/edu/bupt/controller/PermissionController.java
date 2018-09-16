package cn.edu.bupt.controller;

import cn.edu.bupt.dao.Role.RoleService;
import cn.edu.bupt.dao.permission.PermissionRepository;
import cn.edu.bupt.dao.permission.PermissionService;
import cn.edu.bupt.entity.Permission;
import cn.edu.bupt.entity.Role;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.exception.IOTException;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by CZX on 2018/9/3.
 */
@RestController
@RequestMapping("/api/v1/account")
public class PermissionController extends BaseController{

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "为一个role增加permission")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/permission", params = { "role_id"},method = RequestMethod.POST)
    public void saveRolePermissionRelation(@RequestParam int role_id,
                                           @RequestBody List<Integer> permission_ids) throws IOTException {

        for (Integer permission_id:permission_ids){
            permissionService.saveRolePermissionRelation(role_id,permission_id);
        }
    }

    @ApiOperation(value = "删除一个role下的permission")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/permission",params = {  "role_id"  }, method = RequestMethod.DELETE)
    public void deleteRolePermissionRelation(@RequestParam int role_id,
                               @RequestBody List<Integer> permission_ids) throws IOTException {
        for (Integer permission_id:permission_ids){
            permissionService.deleteARelation(role_id,permission_id);
        }
    }

    @ApiOperation(value = "根据ID获取role")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/role",params = {  "role_id"  }, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public Role getRole(@RequestParam int role_id) throws IOTException {
        return roleService.findRoleById(role_id);
    }

    @ApiOperation(value = "获取所有role")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/roles", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public List<Role> getRoles() throws IOTException {
        return roleService.findAllRoles();
    }

    @ApiOperation(value = "创建一个role")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public void saveRole(@RequestBody String roleInfo) throws IOTException {
        Role role = JSON.parseObject(roleInfo,Role.class);
        roleService.saveRole(role);
    }

    @ApiOperation(value = "删除一个role")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/role", method = RequestMethod.DELETE)
    public void deleteRole(@RequestParam Integer roleId) throws IOTException {
        roleService.deleteRoleById(roleId);
    }

    @ApiOperation(value = "更新一个role")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/role", method = RequestMethod.PUT)
    public void updateRole(@RequestBody String roleInfo) throws IOTException {
        try {
            Role role = JSON.parseObject(roleInfo,Role.class);
            roleService.updateRole(role);
        }catch (Exception e){
            handleException(e);
        }
    }

    @ApiOperation(value = "获取所有permission")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/permission",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public List<Permission> getAllPermissions() throws IOTException {
        return permissionService.findAllPermissions();
    }

    @ApiOperation(value = "获取一个role下分配的permission")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/rolePermission", params = {  "role_id"  },method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public List<Permission> getPermissionsByRoleId(@RequestParam int role_id) throws IOTException {
        return permissionService.findAllPermissionsByRoleId(role_id);
    }

    @ApiOperation(value = "为一个user分配role")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/permission", params = { "role_id","user_id"},method = RequestMethod.POST)
    public void saveRoleUserRelation(@RequestParam Integer role_id,
                                     @RequestParam Integer user_id) throws IOTException {
        roleService.saveRoleUserRelation(role_id,user_id);
    }

    @ApiOperation(value = "获取一个用户下的所有extra role")
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/roles", params = { "user_id"}, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public List<Role> getUserExtraRoles(@RequestParam Integer user_id) throws IOTException {
        return roleService.findExtraRolesByUserId(user_id);
    }
}
