package com.example.dataanalysiscollectionservice.mapper;
import com.example.dataanalysiscollectionservice.pojo.po.neo4j.DeveloperCollectionGraphPO;
import com.example.dataanalysiscollectionservice.pojo.vo.neo4j.DeveloperCollectionGraphVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeveloperGraphMapper {
    DeveloperGraphMapper INSTANCE = Mappers.getMapper(DeveloperGraphMapper.class);

    // 单个 PO -> VO
    DeveloperCollectionGraphVO toVO(DeveloperCollectionGraphPO po);

    // 列表转换
    List<DeveloperCollectionGraphVO> toVOList(List<DeveloperCollectionGraphPO> poList);

}
