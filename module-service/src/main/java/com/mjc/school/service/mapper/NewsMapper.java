package com.mjc.school.service.mapper;

import com.mjc.school.repository.impl.AuthorRepositoryImpl;
import com.mjc.school.repository.impl.TagRepositoryImpl;
import com.mjc.school.repository.model.NewsEntity;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", uses = {AuthorMapper.class, TagMapper.class})
public abstract class NewsMapper {

    @Autowired
    protected TagRepositoryImpl tagRepository;

    @Autowired
    protected AuthorRepositoryImpl authorRepository;

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "author", expression = "java( authorRepository.getById(dto.authorId()).get() )")
    @Mapping(target = "tags", expression = "java( dto.tagIds().stream().map(id -> tagRepository.getById(id).get()).toList() )")
    public abstract NewsEntity dtoRequestToModel(NewsDtoRequest dto);

    public abstract NewsDtoResponse modelToDtoResponse(NewsEntity model);

    public abstract List<NewsDtoResponse> listOfModelsToListOfResponses(List<NewsEntity> modelList);
}
