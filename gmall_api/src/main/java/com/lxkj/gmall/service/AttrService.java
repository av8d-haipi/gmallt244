package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.PmsBaseAttrInfo;
import com.lxkj.gmall.bean.PmsBaseAttrValue;
import com.lxkj.gmall.bean.PmsBaseSaleAttr;

import java.util.List;

public interface AttrService {

    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();
}
