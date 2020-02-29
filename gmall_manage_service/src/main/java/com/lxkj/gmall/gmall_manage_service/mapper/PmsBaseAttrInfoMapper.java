package com.lxkj.gmall.gmall_manage_service.mapper;

import com.lxkj.gmall.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {

    List<PmsBaseAttrInfo> getAttrValueListByValueId(@Param("valueIdStr")String valueIdSet);
}
