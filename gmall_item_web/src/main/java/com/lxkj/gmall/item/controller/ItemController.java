package com.lxkj.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.PmsProductSaleAttr;
import com.lxkj.gmall.bean.PmsSkuInfo;
import com.lxkj.gmall.service.SkuService;
import com.lxkj.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    private SkuService skuService;

    @Reference
    private SpuService spuService;

    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable("skuId") String skuId, Map map){
        PmsSkuInfo skuInfo = skuService.skuById(skuId);
        //sku对象
        map.put("skuInfo",skuInfo);
        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrList = spuService.spuSaleAttrListCheckBySku(skuInfo.getProductId(),skuInfo.getId());
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrList);
        return "item";
    }
}
