package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.AuthorEntity;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.mapstruct.Mapping;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class AuthorMapper {

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "news", ignore = true)
    public abstract AuthorEntity dtoRequestToModel(AuthorDtoRequest dto);

    public abstract AuthorDtoResponse modelToDtoResponse(AuthorEntity model);

    public abstract List<AuthorDtoResponse> listOfModelsToListOfResponses(List<AuthorEntity> modelList);
}
