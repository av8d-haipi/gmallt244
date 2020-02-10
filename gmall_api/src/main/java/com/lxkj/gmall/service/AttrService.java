package com.lxkj.gmall.service;

import com.lxkj.gmall.bean.PmsBaseAttrInfo;

import java.util.List;

public interface AttrService {

    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);
}
