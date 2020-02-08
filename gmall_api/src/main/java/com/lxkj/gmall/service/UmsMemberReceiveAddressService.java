package com.lxkj.gmall.service;
import com.lxkj.gmall.bean.UmsMemberReceiveAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UmsMemberReceiveAddressService {

    public List<UmsMemberReceiveAddress> getUserAll();

}
