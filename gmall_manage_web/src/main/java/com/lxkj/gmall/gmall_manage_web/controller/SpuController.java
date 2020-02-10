package com.lxkj.gmall.gmall_manage_web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.PmsProductInfo;
import com.lxkj.gmall.service.SpuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class SpuController {

    @Reference
    private SpuService spuService;

    @RequestMapping(value = "/spuList")
    public Object spuList(String catalog3Id){
        System.out.println(catalog3Id);
        System.out.println(spuService.spuList(catalog3Id).size());
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping(value = "/saveSpuInfo")
    public Object saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        return null;
    }

    @RequestMapping(value = "/fileUpload")
    public Object fileUpload(@RequestParam("file") MultipartFile multipartFile){
//        System.out.println(catalog3Id);
//        System.out.println(spuService.spuList(catalog3Id).size());
        //将图片的储存路径返回给页面
        String imgUrl = "";
        return null;
    }
}
