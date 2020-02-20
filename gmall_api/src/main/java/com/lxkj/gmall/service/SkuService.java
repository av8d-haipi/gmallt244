package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.PmsSkuInfo;

public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo skuById(String skuId);
}