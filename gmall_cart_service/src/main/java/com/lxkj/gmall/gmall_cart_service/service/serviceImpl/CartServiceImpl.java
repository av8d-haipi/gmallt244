package com.lxkj.gmall.gmall_cart_service.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lxkj.gmall.bean.OmsCartItem;
import com.lxkj.gmall.gmall_cart_service.mapper.CartMapper;
import com.lxkj.gmall.gmall_cart_web.cartutil.RedisUtil;
import com.lxkj.gmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    RedisUtil redisUtil;


    /**
     * 同步缓存
     * @param memberId
     */
    public void flushCartCache(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItemList = cartMapper.select(omsCartItem);

        //同步缓存
        Jedis jedis = redisUtil.hashCode();

        Map<String,String> map = new HashMap<>();
        for(OmsCartItem cartItem : omsCartItemList){
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }
        jedis.hmset("user"+memberId+"cart",map);
    }

    /**
     * 购物车已经添加过商品，进行数量更改，只会更改不为null的字段
     * @param omsCartItemCha
     * @return
     */
    public int updateQuantity(OmsCartItem omsCartItemCha) {
        return cartMapper.updateByPrimaryKeySelective(omsCartItemCha);
    }

    /**
     * 查询购物车表是否添加过改商品
     * @param memberId
     * @param productSkuId
     * @return
     */
    public OmsCartItem selCartSkuId(String memberId, String productSkuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(productSkuId);
        return cartMapper.selectOne(omsCartItem);
    }

    /*
         添加商品到购物车
         */
    public int addCart(OmsCartItem omsCartItem) {
        return  cartMapper.insert(omsCartItem);
    }
}
