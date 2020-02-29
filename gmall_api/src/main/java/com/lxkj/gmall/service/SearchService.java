package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.PmsSearchParam;
import com.lxkj.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {

    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
