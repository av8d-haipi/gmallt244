package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.OmsCartItem;
import tk.mybatis.mapper.common.BaseMapper;

public interface CartService{


    /**
     * 同步缓存
     * @param memberId
     */
    void flushCartCache(String memberId);

    /**
     * 添加过商品，再次添加就 更新数量
     * @param omsCartItemCha
     * @return
     */
    int updateQuantity(OmsCartItem omsCartItemCha);

    /**
     * 查询购物车表是否添加过改商品
     * @param memberId
     * @param productSkuId
     * @return
     */
    OmsCartItem selCartSkuId(String memberId, String productSkuId);

    /*
    添加商品到购物车
     */
    int  addCart(OmsCartItem omsCartItem);


}
