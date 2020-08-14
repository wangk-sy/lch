package com.wangk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangk.core.MyPageBean;
import com.wangk.model.LchHtmlInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName :LchService
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 10:45
 * @Version :1.0
 **/
public interface LchService extends IService<LchHtmlInfo> {
    int importData(MultipartFile file);

    MyPageBean getUrlFromES(Integer page,Integer size,String name);
}
