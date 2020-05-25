package com.wangk.controller.lch;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangk.core.MyPageBean;
import com.wangk.core.Result;
import com.wangk.core.ResultGenerator;
import com.wangk.model.LchHtmlInfo;
import com.wangk.service.LchService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName :LchController
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 9:55
 * @Version :1.0
 **/
@RestController
@RequestMapping("/lch")
public class LchController {

    @Autowired
    LchService service;

    @GetMapping("")
    public Result getAllUrl() {
        List<LchHtmlInfo> lchHtmlInfos = service.list();
        return ResultGenerator.getSuccessInfo(lchHtmlInfos);
    }

    @RequestMapping("/page")
//    @RequiresRoles("admin")
    public Result getAllUrl(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        IPage<LchHtmlInfo> iPage = new Page<>(page, size);
        IPage<LchHtmlInfo> pageInfo = service.page(iPage);
        MyPageBean pageBean = new MyPageBean();
        pageBean.setData(pageInfo.getRecords());
        pageBean.setTotal(pageInfo.getTotal());
        return ResultGenerator.getSuccessInfo(pageBean);
    }

    @RequestMapping("/import")
    public Result importData(MultipartFile file) {
        int result = service.importData(file);
        return ResultGenerator.getSuccessInfo(result);
    }

    @PostMapping("/")
    public Result insert(@RequestBody LchHtmlInfo lchHtmlInfo){
        boolean save = service.save(lchHtmlInfo);
        if (save){
            return ResultGenerator.getSuccessInfo();
        }
        return  ResultGenerator.getFailureInfo("添加失败");
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        if (service.removeById(id)) {
            return ResultGenerator.getSuccessInfo();
        }
        return ResultGenerator.getFailureInfo("删除失败");
    }
}
