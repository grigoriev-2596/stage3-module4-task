package com.mjc.school.service.mapper;

import com.mjc.school.repository.impl.NewsRepositoryImpl;
import com.mjc.school.repository.model.CommentEntity;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    protected NewsRepositoryImpl newsRepository;

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "news", expression = "java( newsRepository.getById(source.newsId()).get() )")
    public abstract CommentEntity dtoRequestToModel(CommentDtoRequest source);

    @Mapping(target = "newsId", source = "news.id")
    public abstract CommentDtoResponse modelToDtoResponse(CommentEntity source);

    public abstract List<CommentDtoResponse> listOfModelsToListOfResponses(List<CommentEntity> source);
}
