package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.PmsProductInfo;

import java.util.List;

public interface SpuService {

    List<PmsProductInfo> spuList(String catalog3Id);
}
