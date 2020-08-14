package com.wangk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangk.core.Result;
import com.wangk.core.ResultGenerator;
import com.wangk.model.User;
import com.wangk.service.UserService;
import com.wangk.utils.FastDFSClient;
import com.wangk.utils.JwtUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @ClassName :UserController
 * @Description :TODO
 * @Author :16388
 * @Date :2020/6/5 9:49
 * @Version :1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    FastDFSClient fastDFSClient;
    @Autowired
    UserService userService;

    @Value("${fdfs.http.secret_key}")
    String fastdfsToken;
    @Value("${fdfs.reqHost}")
    String IP;
    @GetMapping("/")
    public Result findAll() {
        List<User> list = userService.list();
        return ResultGenerator.getSuccessInfo(list);
    }

    @PostMapping("/")
    public Result addUser(@RequestBody User user){
        userService.save(user);
        return  ResultGenerator.getSuccessInfo();
    }
    @PutMapping("/")
    public Result update(@RequestBody User user){
        userService.updateById(user);
        return ResultGenerator.getSuccessInfo();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        userService.removeById(id);
        return ResultGenerator.getSuccessInfo();
    }

    @GetMapping("/info")
    public Result getCurrentUser(){
        Object principal = SecurityUtils.getSubject().getPrincipal();
        String username = JwtUtils.getClaim(principal.toString(), "username");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        return ResultGenerator.getSuccessInfo(user);
    }

    @PostMapping("/userface")
    public Result userface(User user, MultipartFile file){
        String path="";
        try {
            path = fastDFSClient.uploadFile(file);
            user.setUserface(path);
            userService.update(user,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.getSuccessInfo();
    }

    @GetMapping("/viewImg")
    public void viewImg(HttpServletResponse response, HttpServletRequest request, String filePath){
        OutputStream os=null;
        InputStream in=null;
        try {
            os = new BufferedOutputStream(response.getOutputStream());
            byte[] download = fastDFSClient.download(filePath);
            in = new ByteArrayInputStream(download);
            Thumbnails.of(in).scale(1f).outputQuality(1f).outputFormat("jpg").toOutputStream(os);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
