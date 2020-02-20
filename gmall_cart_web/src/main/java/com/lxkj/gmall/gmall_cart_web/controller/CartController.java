package com.lxkj.gmall.gmall_cart_web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lxkj.gmall.bean.OmsCartItem;
import com.lxkj.gmall.bean.PmsProductInfo;
import com.lxkj.gmall.bean.PmsSkuInfo;
import com.lxkj.gmall.gmall_cart_web.cartutil.CookieUtil;
import com.lxkj.gmall.service.CartService;
import com.lxkj.gmall.service.SkuService;
import com.lxkj.gmall.service.SpuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.Name;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;
    @Reference
    CartService cartService;


    /**
     * 添加商品
     * @return
     */
    @RequestMapping("addcart")
    public String addCart(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        System.out.println("--------------添加商品");
        List<OmsCartItem> listCartItem = new ArrayList<>();

        PmsSkuInfo pmsSkuInfo = skuService.skuById("11");
        PmsProductInfo pmsProductInfo = spuService.selectid(pmsSkuInfo.getProductId());
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductSkuId(pmsSkuInfo.getId());
        omsCartItem.setMemberId("1");
        omsCartItem.setQuantity(1);
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setSp1("");
        omsCartItem.setSp2("");
        omsCartItem.setSp3("");
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductName(pmsProductInfo.getProductName());
        omsCartItem.setProductSubTitle(pmsProductInfo.getDescription());
        omsCartItem.setProductSkuCode("11111");
        omsCartItem.setMemberNickname("");
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductBrand("");
        omsCartItem.setProductSn("");
        omsCartItem.setProductAttr("");

        session.setAttribute("name","");  //拿到用户的会话 来进行判断是否登录

        //判断用户是否登录
        if(StringUtils.isBlank((CharSequence) session.getAttribute("name"))){
            //未登录
            //Cookie 里面原有的购物车数据
            String cartlistCk = CookieUtil.getCookieValue(request,"cartlistcookie",true);
            //判断Cookie中是否为空
            if(StringUtils.isBlank(cartlistCk)){
                //为空
                listCartItem.add(omsCartItem);
            }else{
                //不为空
                listCartItem = JSON.parseArray(cartlistCk,OmsCartItem.class);
                //判断添加的购物车数据在cookie中是否存在
                if(if_cart_exist(listCartItem,omsCartItem)){
                    //Cookie中存在已经有的商品，就更新数量
                    for(OmsCartItem cartItem : listCartItem){
                        if(cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                            cartItem.setQuantity(cartItem.getQuantity()+omsCartItem.getQuantity());
                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));
                        }
                    }
                }else{
                    listCartItem.add(omsCartItem);
                }
            }
            CookieUtil.setCookie(request,response,"cartlistcookie",JSON.toJSONString(listCartItem),60*60*72,true);
            System.out.println("-----浏览器cookie");
        }else{
            //已经登录
            //判断该用户是否添加过该商品
            OmsCartItem omsCartItemCha =  cartService.selCartSkuId("1","11");

            if(omsCartItemCha == null){
                //没有添加过
                if(cartService.addCart(omsCartItem) <= 0 ){
                    return "One_JDshop";
                }
            }else{
                //添加过
                omsCartItemCha.setQuantity(omsCartItem.getQuantity()+1);
                if(cartService.updateQuantity(omsCartItemCha) <= 0){
                    return "One_JDshop";
                }
            }

        }
        return "success";
    }

    /**
     * 购物车页面
     * @param session
     * @return
     */
    @RequestMapping("cartlist")
    public String cartList(HttpSession session){
        System.out.println("------------购物车");
        session.setAttribute("name","owz");  //拿到用户的会话 来进行判断是否登录
        return "One_JDshop";
    }

    public boolean if_cart_exist(List<OmsCartItem> omsCartItems,OmsCartItem omsCartItem){
        boolean isok = false;
        for(OmsCartItem cartItem : omsCartItems){
            String productid = cartItem.getProductId();
            if(productid.equals(omsCartItem.getProductId())){
                isok = true;
            }
        }
        return isok;
    }
}
