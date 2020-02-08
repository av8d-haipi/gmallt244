package com.lxkj.gmall.gmall_user_service.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.lxkj.gmall.bean.UmsMemberReceiveAddress;
import com.lxkj.gmall.gmall_user_service.mapper.UmsMemberReceiveAddressMapper;
import com.lxkj.gmall.service.UmsMemberReceiveAddressService;
import javax.annotation.Resource;
import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {

    @Resource
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> getUserAll() {

        return umsMemberReceiveAddressMapper.getUserAll();
    }
}
