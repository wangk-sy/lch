package com.wangk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangk.core.MyPageBean;
import com.wangk.mapper.LchMapper;
import com.wangk.model.LchHtmlInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName :LchService
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 10:45
 * @Version :1.0
 **/
public interface LchService extends IService<LchHtmlInfo> {
    int importData(MultipartFile file);
}
