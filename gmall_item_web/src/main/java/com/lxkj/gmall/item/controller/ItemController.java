package com.lxkj.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lxkj.gmall.bean.PmsProductSaleAttr;
import com.lxkj.gmall.bean.PmsSkuAttrValue;
import com.lxkj.gmall.bean.PmsSkuInfo;
import com.lxkj.gmall.bean.PmsSkuSaleAttrValue;
import com.lxkj.gmall.service.SkuService;
import com.lxkj.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    private SkuService skuService;

    @Reference
    private SpuService spuService;

    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable("skuId") String skuId, Map map, HttpServletRequest request){
        String remoteAddr = request.getRemoteAddr();

        //request.getHeader(""); //nginx负载均衡

        PmsSkuInfo skuInfo = skuService.getSkuById(skuId,remoteAddr);
        //sku对象
        map.put("skuInfo",skuInfo);
        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrList = spuService.spuSaleAttrListCheckBySku(skuInfo.getProductId(),skuInfo.getId());
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrList);

        //查询当前sku的spu的其他的sku的集合的hash表
        Map<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos=skuService.getSkuSaleAttrValueListBySpu(skuInfo.getProductId());
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {

            String k = "";
            String v = pmsSkuInfo.getId();
            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = pmsSkuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValues) {
                k+=pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";//239,245
            }
            skuSaleAttrHash.put(k,v);
        }
        //将sku的销售属性hash表放到页面
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
        return "item";
    }
}
