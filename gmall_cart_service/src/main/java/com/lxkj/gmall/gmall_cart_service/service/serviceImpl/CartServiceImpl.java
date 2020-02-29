package com.lxkj.gmall.gmall_cart_service.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lxkj.gmall.bean.OmsCartItem;
import com.lxkj.gmall.gmall_cart_service.mapper.CartMapper;
import com.lxkj.gmall.service.CartService;
import com.lxkj.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CartMapper cartMapper;


    /**
     * 修改商品选中的状态
     * @param omsCartItem
     */
    public void isCheckedxiu(OmsCartItem omsCartItem) {
        Example e = new Example(omsCartItem.getClass());
        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());
        cartMapper.updateByExampleSelective(omsCartItem,e);
        flushCartCache(omsCartItem.getMemberId());
    }

    /**
     * 从缓存中拿商品
     * @param userId
     * @return
     */
    public List<OmsCartItem> cartlist(String userId) {
        System.out.println("-------redis缓存");
        Jedis jedis = null;
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        try {
            jedis = redisUtil.getJedis();
            List<String> havls = jedis.hvals("user"+userId+"cart");

            for (String havl : havls){
                OmsCartItem omsCartItem = JSON.parseObject(havl,OmsCartItem.class);
                omsCartItemList.add(omsCartItem);
            }
            System.out.println("======="+omsCartItemList);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return omsCartItemList;
    }

    /**
     * 查询该用户所有的商品
     * @param memberId
     * @return
     */
    public List<OmsCartItem> seleallItem(String memberId){
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        flushCartCache(memberId);
        return cartMapper.select(omsCartItem);
    };

    /**
     * 同步缓存
     * @param memberId
     */
    public void flushCartCache(String memberId) {
        System.out.println("----同步redis缓存");
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItemList = cartMapper.select(omsCartItem);

        //同步缓存
        Jedis jedis = redisUtil.getJedis();

        Map<String,String> map = new HashMap<>();
        for(OmsCartItem cartItem : omsCartItemList){
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }
        jedis.del("user"+memberId+"cart");
        jedis.hmset("user"+memberId+"cart",map);
        jedis.close();
    }

    /**
     * 购物车已经添加过商品，进行数量更改，只会更改不为null的字段
     * @param omsCartItem
     * @return
     */
    public int updateQuantity(OmsCartItem omsCartItem) {
        System.out.println("---service:"+omsCartItem.getCount());
        return cartMapper.updateByPrimaryKey(omsCartItem);
    }

    /**
     * 查询购物车表是否添加过该商品
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
        System.out.println("添加商品---实现类");
        return  cartMapper.insert(omsCartItem);
    }
}
