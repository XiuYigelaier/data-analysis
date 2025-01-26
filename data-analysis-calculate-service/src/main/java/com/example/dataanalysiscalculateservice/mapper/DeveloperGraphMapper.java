package com.example.dataanalysiscalculateservice.mapper;

import com.example.dataanalysiscalculateservice.pojo.po.neo4j.DeveloperGraphPO;
import com.example.dataanalysiscalculateservice.pojo.vo.DeveloperGraphVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


public interface DeveloperGraphMapper {
    DeveloperGraphMapper INSTANCE = Mappers.getMapper(DeveloperGraphMapper.class);

    // 单个 PO -> VO
    DeveloperGraphVO toVO(DeveloperGraphPO po);

    // 列表转换
    List<DeveloperGraphVO> toVOList(List<DeveloperGraphPO> poList);

}