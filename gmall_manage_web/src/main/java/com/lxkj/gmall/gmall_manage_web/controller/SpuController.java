package com.lxkj.gmall.gmall_manage_web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.PmsProductImage;
import com.lxkj.gmall.bean.PmsProductInfo;
import com.lxkj.gmall.bean.PmsProductSaleAttr;
import com.lxkj.gmall.bean.PmsSkuInfo;
import com.lxkj.gmall.gmall_manage_web.util.PmsUploadUtil;
import com.lxkj.gmall.service.SpuService;
import org.csource.common.MyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class SpuController {

    @Reference
    private SpuService spuService;

    @RequestMapping(value = "/spuImageList")
    public Object spuImageList(String spuId){
        List<PmsProductImage> pmsProductImages=spuService.spuImageList(spuId);
        return null;
    }

    @RequestMapping(value = "/spuSaleAttrList")
    public Object spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrList=spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrList;
    }

    @RequestMapping(value = "/spuList")
    public Object spuList(String catalog3Id){
        System.out.println(catalog3Id);
        System.out.println(spuService.spuList(catalog3Id).size());
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping(value = "/saveSpuInfo")
    public Object saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping(value = "/fileUpload")
    public Object fileUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException, MyException {
        //将图片或者音视频上传到分布式的文件的文件存储系统
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        //将图片的储存路径返回给页面
        System.out.println(imgUrl);
        return imgUrl;
    }


}
