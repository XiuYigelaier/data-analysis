package com.example.dataanalysisapiservice.mapper;

import com.example.dataanalysisapiservice.pojo.dto.ProjectClassificationDTO;
import com.example.dataanalysisapiservice.pojo.dto.ProjectClassification_ProjectDTO;
import com.example.dataanalysisapiservice.pojo.vo.ProjectClassificationVO;
import com.example.dataanalysisapiservice.pojo.vo.ProjectClassification_ProjectVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectClassificationMapper {

    ProjectClassificationMapper INSTANCE = Mappers.getMapper(ProjectClassificationMapper.class);

    // 单个 PO -> VO
    @Mapping(target = "projects", source = "projects")
    ProjectClassificationVO DTOtoVO(ProjectClassificationDTO dto);

    // 列表转换
    @Mapping(target = "projects", source = "projects")
    List<ProjectClassificationVO> DTOtoVOList(List<ProjectClassificationDTO> dtoList);

    ProjectClassification_ProjectVO toProjectVO(ProjectClassification_ProjectDTO projectDTO);

}
