package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.TagEntity;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TagMapper {

    @Mapping(target = "news", ignore = true)
    public abstract TagEntity dtoRequestToModel(TagDtoRequest dto);

    public abstract TagDtoResponse modelToDtoResponse(TagEntity model);

    public abstract List<TagDtoResponse> listOfModelsToListOfResponses(List<TagEntity> modelList);
}
