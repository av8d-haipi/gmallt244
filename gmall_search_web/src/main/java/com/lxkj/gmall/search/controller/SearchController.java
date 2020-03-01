package com.lxkj.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.*;
import com.lxkj.gmall.service.AttrService;
import com.lxkj.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {


    @Reference
    private SearchService searchService;

    @Reference
    private AttrService attrService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {
        //调用搜索服务，放回搜收结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);
        System.out.println(pmsSearchSkuInfos.size() + "==");

        //抽取检索结果包含的频台属性集合
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }
        //根据valueid将属性列表查询出来
        List<PmsBaseAttrInfo> pmsBaseAttrValues = attrService.getAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrValues);

        //对平台属性集合进一步处理，去掉当前条件中valueId所在属性组
        String[] delValueIds = pmsSearchParam.getValueId();
        if (delValueIds != null) {
            //面包屑attrValueSelectedList
            //pmsSearchParam
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            //如果valueId参数不为空,说明当前请求中包含属性的参数，每一个属性参数，都会生成一个面包屑
            for (String delValueId : delValueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrValues.iterator();//平台属性集合
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                //生成面包屑的参数
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParamPorCrumb(pmsSearchParam,delValueId));
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> skuAttrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsSkuAttrValue : skuAttrValueList) {
                        String id = pmsSkuAttrValue.getId();
                        if (delValueId.equals(id)) {
                            //查询面包屑的属性值名称
                            pmsSearchCrumb.setValueName(pmsSkuAttrValue.getValueName());
                            //删除该属性值所在的属性组
                            iterator.remove();
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
            modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        }

        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);
        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNoneBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }


        return "list";
    }

    private String getUrlParamPorCrumb(PmsSearchParam pmsSearchParam,String delValueId) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] skuAttrValueList = pmsSearchParam.getValueId();
        String urlParam = "";
        if (StringUtils.isNoneBlank(keyword)) {
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }
        if (StringUtils.isNoneBlank(catalog3Id)) {
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }
        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                if(!pmsSkuAttrValue.equals(delValueId)){
                    urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
                }
            }
        }
        return urlParam;
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] skuAttrValueList = pmsSearchParam.getValueId();
        String urlParam = "";

        if (StringUtils.isNoneBlank(keyword)) {
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }
        if (StringUtils.isNoneBlank(catalog3Id)) {
            if (StringUtils.isNoneBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }
        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
            }
        }

        return urlParam;
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
