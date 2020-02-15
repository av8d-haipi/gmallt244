package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.OmsCartItem;
import tk.mybatis.mapper.common.BaseMapper;

public interface CartService{

    /*
    添加商品到购物车
     */
    int  addCart(OmsCartItem omsCartItem);

}
