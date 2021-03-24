package com.example.elsearch.controller;

import com.example.elsearch.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: controller
 * @Description: TODO
 * @author: 大佬
 * @Date: 2021/3/24 22:14
 * @Version: 1.0
 **/
@Controller
public class controller {
    @Autowired
    private ContentService contentService;
    @RequestMapping("/login")
    public  String index(){

        return "index";
    }

    @GetMapping("/parse/{keyword}")
    @ResponseBody
    public  Boolean parseXml(@PathVariable("keyword") String keyword) throws IOException {
       return contentService.parseContent(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    @ResponseBody
    public List<Map<String,Object>> SearchXml(@PathVariable("keyword") String keyword,
                               @PathVariable("pageNo") int pageNo,
                               @PathVariable("pageSize") int pageSize) throws IOException {
        List<Map<String, Object>> list = contentService.searchPage(keyword, pageNo, pageSize);
        return list;
    }
}
