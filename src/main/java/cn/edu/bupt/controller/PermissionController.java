package cn.edu.bupt.controller;

import cn.edu.bupt.dao.Role.RoleService;
import cn.edu.bupt.dao.permission.PermissionRepository;
import cn.edu.bupt.dao.permission.PermissionService;
import cn.edu.bupt.entity.Permission;
import cn.edu.bupt.entity.Role;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.exception.IOTException;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/permission", params = { "role_id"},method = RequestMethod.POST)
    public void saveRolePermissionRelation(@RequestParam int role_id,
                                           @RequestBody String permission_ids) throws IOTException {

        JsonObject PermissionJson = (JsonObject) new JsonParser().parse(permission_ids);
        String[] permissionIds = PermissionJson.get("id").getAsString().split(",");
        for (String permission_id:permissionIds){
            Integer id = Integer.parseInt(permission_id);
            permissionService.saveRolePermissionRelation(role_id,id);
        }
    }

    @ApiOperation(value = "删除一个role下的permission")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/permission",params = {  "role_id"  }, method = RequestMethod.DELETE)
    public void deleteRolePermissionRelation(@RequestParam int role_id,
                               @RequestBody String permission_ids) throws IOTException {
        JsonObject PermissionJson = (JsonObject) new JsonParser().parse(permission_ids);
        String[] permissionIds = PermissionJson.get("id").getAsString().split(",");
        for (String permission_id:permissionIds){
            Integer id = Integer.parseInt(permission_id);
            permissionService.deleteARelation(role_id,id);
        }
    }

    @ApiOperation(value = "根据ID获取role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/role",params = {  "role_id"  }, method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getRole(@RequestParam int role_id) throws IOTException {
        return roleService.findRoleById(role_id).toString();
    }

    @ApiOperation(value = "获取所有role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/roles", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getRoles() throws IOTException {
        return roleService.findAllRoles().toString();
    }

    @ApiOperation(value = "创建一个role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Role saveRole(@RequestBody String roleInfo) throws IOTException {
        Role role = JSON.parseObject(roleInfo,Role.class);
        roleService.saveRole(role);
        return role;
    }

    @ApiOperation(value = "删除一个role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/role", method = RequestMethod.DELETE)
    public void deleteRole(@RequestParam Integer roleId) throws IOTException {
        roleService.deleteRoleById(roleId);
    }

    @ApiOperation(value = "更新一个role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
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
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/permissions",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getAllPermissions() throws IOTException {
        return permissionService.findAllPermissions().toString();
    }

    @ApiOperation(value = "获取一个role下分配的permission")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/rolePermission", params = {  "role_id"  },method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getPermissionsByRoleId(@RequestParam int role_id) throws IOTException {
        return permissionService.findAllPermissionsByRoleId(role_id).toString();
    }

    @ApiOperation(value = "获取一个role下还未分配的permission")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/roleNotOwnedPermission", params = {  "role_id"  },method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getNotOwnedPermissionsByRoleId(@RequestParam int role_id) throws IOTException {
        return permissionService.findAllNotOwnedPermissionsByRoleId(role_id).toString();
    }

    @ApiOperation(value = "为一个user分配role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/user/role", params = { "role_id","user_id"},method = RequestMethod.POST)
    public void saveRoleUserRelation(@RequestParam Integer role_id,
                                     @RequestParam Integer user_id) throws IOTException {
        roleService.saveRoleUserRelation(role_id,user_id);
    }

    @ApiOperation(value = "为一个user删除role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/user/role", params = { "role_id","user_id"},method = RequestMethod.DELETE)
    public void deleteRoleUserRelation(@RequestParam Integer role_id,
                                     @RequestParam Integer user_id) throws IOTException {
        roleService.deleteRoleUserRelation(role_id,user_id);
    }

    @ApiOperation(value = "获取一个用户下的所有extra role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/roles", params = { "user_id"}, method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getUserExtraRoles(@RequestParam Integer user_id) throws IOTException {
        return roleService.findExtraRolesByUserId(user_id).toString();
    }

    @ApiOperation(value = "获取一个用户下的所有未拥有的extra role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/notOwnedRoles", params = { "user_id"}, method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getUserNotOwnedExtraRoles(@RequestParam Integer user_id) throws IOTException {
        return roleService.findNotOwnedExtraRolesByUserId(user_id).toString();
    }

    @ApiOperation(value = "获取一个用户下的所有role")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/UserRoles", params = { "user_id"}, method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getUserRoles(@RequestParam Integer user_id) throws IOTException {
        return roleService.findAllRolesByUserId(user_id).toString();
    }
}
