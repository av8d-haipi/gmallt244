package com.lxkj.gmall.gmall_user_service.mapper;


import com.lxkj.gmall.bean.UmsMemberReceiveAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UmsMemberReceiveAddressMapper extends Mapper<UmsMemberReceiveAddress> {

    List<UmsMemberReceiveAddress> getUserAll();
}
