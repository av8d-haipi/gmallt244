package com.lxke.gmall.gmall_cart_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CartController {

    @RequestMapping("cartlist")
    public String cartList(HttpSession session){
        System.out.println("------------购物车");
        session.setAttribute("name","owz");  //拿到用户的会话 来进行判断是否登录
        return "One_JDshop";
    }
}
