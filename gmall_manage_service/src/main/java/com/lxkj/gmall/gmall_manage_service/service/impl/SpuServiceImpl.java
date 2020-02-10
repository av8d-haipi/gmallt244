package com.lxkj.gmall.gmall_manage_service.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxkj.gmall.bean.PmsProductInfo;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsProductInfoMapper;
import com.lxkj.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(pmsProductInfo);
    }
}
