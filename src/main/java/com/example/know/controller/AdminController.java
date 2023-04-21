package com.example.know.controller;

import com.example.know.dao.SystemParameterDao;
import com.example.know.entity.*;
import com.example.know.enumeration.UrlType;
import com.example.know.service.*;
import com.example.know.util.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 管理人员接口
 *
 * @author bookWorm
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private BookService bookService;

    @Resource
    private AccessService accessService;

    @Resource
    private UserService userService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private RolePermissionService rolePermissionService;

    @Resource
    private BookPathService bookPathService;

    @Resource
    private BookTypeService bookTypeService;

    @Resource
    private RoleService roleService;

    @Resource
    private PlateService plateService;

    @Resource
    private PostService postService;

    @Resource
    private SystemParameterDao systemParameterDao;

    @GetMapping("/expectBookAndCount")
    public AjaxResult expectBookAndCount() {
        return AjaxResult.success(bookService.expectBookAndCount());
    }

    @GetMapping("/countNumberByTime")
    public AjaxResult countNumberByTime() {
        return AjaxResult.success(accessService.countNumberByTime());
    }

    @GetMapping("/getCount")
    public AjaxResult getCount() {
        long expectBookCount = bookService.getExpectBookCount();
        long accessCount = accessService.accessCount();
        Map map = new HashMap();
        map.put("expectBookCount", expectBookCount);
        map.put("accessCount", accessCount);
        return AjaxResult.success(map);
    }

    @PostMapping("/getUserList")
    public AjaxResult getUserList() {
        return AjaxResult.success(userService.getUserList());
    }

    @PostMapping("/setUserStatus")
    public AjaxResult setUserStatus(char flag, int userId) {
        int i = userService.setUserStatus(flag, userId);
        if (i == 1) {
            return AjaxResult.success("更改成功");
        }
        return AjaxResult.error("更改失败");
    }

    @PostMapping("/getUserRoleList")
    public AjaxResult getUserRoleList() {
        return AjaxResult.success(userRoleService.getAllUserRole());
    }

    @PostMapping("/getRoleList")
    public AjaxResult getRoleList() {
        return AjaxResult.success(roleService.getAllRole());
    }

    @PostMapping("/getPermissionList")
    public AjaxResult getPermissionList() {
        return AjaxResult.success(permissionService.getPermissionList());
    }

    @PostMapping("/bindUserRole")
    public AjaxResult bindUserRole(int userId, int roleId) {
        boolean b = userRoleService.bindUserRole(userId, roleId);
        if (b) {
            return AjaxResult.success("绑定成功");
        } else {
            return AjaxResult.error("绑定失败");
        }
    }

    @PostMapping("/removeUserRoleBind")
    public AjaxResult removeUserRoleBind(int userId, int roleId) {
        boolean b = userRoleService.removeUserRoleBind(userId, roleId);
        if (b) {
            return AjaxResult.success("移除成功");
        } else {
            return AjaxResult.error("移除失败");
        }
    }

    @PostMapping("/setRole")
    public AjaxResult setRole(Role role) {
        int i = roleService.updateRole(role);
        if (i == 1) {
            return AjaxResult.success("修改成功");
        } else {
            return AjaxResult.error("修改失败");
        }
    }

    @PostMapping("/deleteRole")
    public AjaxResult deleteRole(int roleId) {
        int i = roleService.deleteRole(roleId);
        if (i == 1) {
            return AjaxResult.success("修改成功");
        } else if (i == 0) {
            return AjaxResult.error("职位不存在");
        } else if (i == -1) {
            return AjaxResult.error("当前职位有用户绑定中，禁止删除");
        } else {
            return AjaxResult.error("修改失败");
        }
    }

    @PostMapping("/addRole")
    public AjaxResult addRole(Role role) {
        int i = roleService.addRole(role);
        if (i == 1) {
            return AjaxResult.success("添加成功");
        } else {
            return AjaxResult.error("添加失败");
        }
    }

    @PostMapping("/getPermissionByRoleId")
    public AjaxResult getPermission(int roleId) {
        return AjaxResult.success(rolePermissionService.getRoleAndPermission(roleId));
    }


    @PostMapping("/setRolePermission")
    public AjaxResult setRolePermission(int roleId, String permissionIds) {
        String[] rolePermissionIds = permissionIds.split(",");
        int[] rolePermissionId = new int[rolePermissionIds.length];
        for (int i = 0; i < rolePermissionId.length; i++) {
            rolePermissionId[i] = Integer.parseInt(rolePermissionIds[i]);
        }
        boolean b = rolePermissionService.setRolePermission(roleId, rolePermissionId);
        if (b) {
            return AjaxResult.success("修改成功");
        } else {
            return AjaxResult.error("修改失败");
        }
    }

    @PostMapping("setBook")
    public AjaxResult setBook(Book book, String bookPath) {
        if (bookService.updateBook(book) == 1 && bookPathService.updatePath(book.getBookId(), bookPath) == 1) {
            return AjaxResult.success("更改成功");
        }
        return AjaxResult.error();
    }

    @PostMapping("/getBookPath")
    public AjaxResult getBookPath(int bookId) {
        return AjaxResult.success(bookPathService.getPath(bookId));
    }

    @PostMapping("/updateBookType")
    public AjaxResult updateBookType(BookType bookType) {
        int i = bookTypeService.updateBookType(bookType);
        if (i == 1) {
            return AjaxResult.success("更改成功");
        }
        return AjaxResult.error("更改失败");
    }

    @PostMapping("/deleteBookType")
    public AjaxResult deleteBookType(int bookTypeId) {
        int i = bookTypeService.deleteBookType(bookTypeId);
        if (i == 1) {
            return AjaxResult.success("删除成功");
        } else if (i == -1) {
            return AjaxResult.error("类型正在被使用，禁止删除");
        }
        return AjaxResult.error("删除失败");
    }

    @PostMapping("/getPostList")
    public AjaxResult getPostList(String status, String orderBy, String postType, int startPage) {
        int postListLength = postService.getPostListNumber(status.charAt(0), Integer.parseInt(orderBy), postType.charAt(0), startPage);
        Map map = new HashMap();
        map.put("size", postListLength);
        map.put("postList", postService.getPostList(status.charAt(0), Integer.parseInt(orderBy), postType.charAt(0), startPage));
        return AjaxResult.success(map);
    }

    @PostMapping("/getPlateList")
    public AjaxResult getPlateList() {
        return plateService.getPlateByRanking();
    }

    @PostMapping("/getSystemParameter")
    public AjaxResult getSystemParameter() {
        return AjaxResult.success(systemParameterDao.selectList(null));
    }

    @PostMapping("/setSystemParameter")
    public AjaxResult setSystemParameter(int id, int number) {
        SystemParameter systemParameter = systemParameterDao.selectById(id);
        systemParameter.setNumber(number);
        int i = systemParameterDao.updateById(systemParameter);
        if (i == 1) {
            return AjaxResult.success("更改成功");
        } else {
            return AjaxResult.error("更改失败");
        }
    }

    @RequestMapping("/uploadPlateImg")
    public String uploadPlateImg(MultipartFile file, int plateId) {
        String oldFileName = file.getOriginalFilename();
        String filePath = "";
        filePath = UrlType.PLATEIMG.getUrl();
        String newName = UUID.randomUUID() + oldFileName;
        String newFileName = filePath + newName;
        File dest = new File(newFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            Plate plate = new Plate();
            plate.setPlateId(plateId);
            plate.setImgUrl(newName);
            plateService.updatePlate(plate);
        } catch (IOException ioException) {

        }
        return "../static/img/post/" + newName;
    }

    @PostMapping("/updatePlate")
    public AjaxResult updatePlate(Plate plate) {
        int i = plateService.updatePlate(plate);
        if (i == 1) {
            return AjaxResult.success("修改成功");
        } else {
            return AjaxResult.error("修改失败");
        }
    }

    @PostMapping("/updatePostStatus")
    public AjaxResult updatePostStatus(int postId, int status, String reasonType, String reasonContent) {
        if (status >= 0 && status <= 4) {
            postService.auditSucceeded(postId, status);
        } else if (status < 0) {
            postService.auditFailed(postId, Integer.parseInt(reasonType), reasonContent);
        } else if (status > 0) {
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @PostMapping("updatePostType")
    public AjaxResult updatePostType(int postId, int type) {
        if (postService.updatePostType(postId, type) == 1) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
