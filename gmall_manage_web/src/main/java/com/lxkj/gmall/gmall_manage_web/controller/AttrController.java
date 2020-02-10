package com.lxkj.gmall.gmall_manage_web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxkj.gmall.bean.PmsBaseAttrInfo;
import com.lxkj.gmall.service.AttrService;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class AttrController {

    @Reference
    private AttrService attrService;

    @RequestMapping(value = "/attrInfoList")
    public Object attrInfoList(String catalog3Id){
        return attrService.attrInfoList(catalog3Id);
    }

    @RequestMapping(value = "/saveAttrInfo")
    public Object saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String success = "";
        try {
            attrService.saveAttrInfo(pmsBaseAttrInfo);
            success="success";
        }catch (Exception e){
            success="error";
            e.printStackTrace();
        }
        return success;
    }

    @RequestMapping(value = "/getAttrValueList")
    public Object getAttrValueList(String attrId){
       // attrService.getAttrValueList(attrId);
        return null;
    }

}
