package com.lxkj.gmall.gmall_redisson_test.redissontest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.PmsSearchSkuInfo;
import com.lxkj.gmall.bean.PmsSkuInfo;
import com.lxkj.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    @Reference
    private SkuService skuService; //查询mysql
    @Autowired
    private JestClient jestClient;

    @RequestMapping(value = "110")
    public String index() throws IOException {
        //查询mysql
        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();

        pmsSkuInfoList = skuService.getAllSku();
        //转化为es的数据结构
        List<PmsSearchSkuInfo> searchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo searchSkuInfo = new PmsSearchSkuInfo();

            BeanUtils.copyProperties(pmsSkuInfo,searchSkuInfo);

            searchSkuInfos.add(searchSkuInfo);
        }
        //导入es
        for (PmsSearchSkuInfo searchSkuInfo : searchSkuInfos) {
            Index put = new Index.Builder(searchSkuInfo).index("gmall0105").type("PmsSkuInfo").id(searchSkuInfo.getId()).build();
            jestClient.execute(put);
        }

        return "success";
    }
}
