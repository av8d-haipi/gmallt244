package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.PmsProductImage;
import com.lxkj.gmall.bean.PmsProductInfo;
import com.lxkj.gmall.bean.PmsProductSaleAttr;
import java.util.List;

public interface SpuService {
    /**
     * 指定 商品id 查询
     * @param spuId
     * @return
     */
    PmsProductInfo selectid(String spuId);

    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
