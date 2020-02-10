package com.lxkj.gmall.gmall_manage_service.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxkj.gmall.bean.PmsBaseAttrInfo;
import com.lxkj.gmall.bean.PmsBaseAttrValue;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsBaseAttrInfoMapper;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsBaseAttrValueMapper;
import com.lxkj.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo =new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        return pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String err = "";
        try {
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
            for(PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrValues){
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
            err = "success";
        }catch (Exception e){
            err = "err";
            e.printStackTrace();
        }
        return err;
    }
}
