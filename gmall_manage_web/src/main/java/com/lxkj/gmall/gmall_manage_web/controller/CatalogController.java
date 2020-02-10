package com.lxkj.gmall.gmall_manage_web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.PmsBaseCatalog1;
import com.lxkj.gmall.bean.PmsBaseCatalog2;
import com.lxkj.gmall.bean.PmsBaseCatalog3;
import com.lxkj.gmall.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@CrossOrigin    //用于跨域前端
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @RequestMapping("/getCatalog1")
    @ResponseBody
    public Object getCatalog1(){
        System.out.println("==========111");
        List<PmsBaseCatalog1> catalog1s = catalogService.getCatalog1();
        return catalog1s;
    }

    @RequestMapping("/getCatalog2")
    @ResponseBody
    public Object getCatalog2(String catalog1Id){
        System.out.println("==========222");
        List<PmsBaseCatalog2> catalog2s = catalogService.getCatalog2(catalog1Id);
        return catalog2s;
    }

    @RequestMapping("/getCatalog3")
    @ResponseBody
    public Object getCatalog3(String catalog2Id){
        System.out.println("==========333");
        List<PmsBaseCatalog3> catalog3s = catalogService.getCatalog3(catalog2Id);
        return catalog3s;
    }
}
