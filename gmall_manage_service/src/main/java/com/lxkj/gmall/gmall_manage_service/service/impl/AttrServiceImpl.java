package com.lxkj.gmall.gmall_manage_service.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxkj.gmall.bean.PmsBaseAttrInfo;
import com.lxkj.gmall.bean.PmsBaseAttrValue;
import com.lxkj.gmall.bean.PmsBaseSaleAttr;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsBaseAttrInfoMapper;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsBaseAttrValueMapper;
import com.lxkj.gmall.gmall_manage_service.mapper.PmsBaseSaleAttrMapper;
import com.lxkj.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo =new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
            List<PmsBaseAttrValue> pmsBaseAttrValues = new ArrayList<>();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String id = pmsBaseAttrInfo.getId();
        String err = "";
        if(StringUtils.isBlank(id)){
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
        }else{
            try {
                //id不空修改

                //属性修改
                Example example = new Example(PmsBaseAttrInfo.class);
                example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
                pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);

                //按照属性id删除所有属性值
                PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
                pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);

                //删除后，再把新的属性值插入
                List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsBaseAttrInfo.getAttrValueList();
                for(PmsBaseAttrValue pmsBaseAttrValue:pmsBaseAttrValueList){
                    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
                }
                err = "success";
            }catch (Exception e){
                err = "err";
                e.printStackTrace();
            }
        }
        return err;
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {
        String valueIdStr = StringUtils.join(valueIdSet,",");
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.getAttrValueListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }

}
