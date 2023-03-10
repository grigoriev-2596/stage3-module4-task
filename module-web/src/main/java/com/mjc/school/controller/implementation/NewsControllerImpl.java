package com.mjc.school.controller.implementation;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.NewsController;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.query.NewsSearchCriteriaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/news")
public class NewsControllerImpl implements NewsController {

    private final NewsService newsService;


    @Autowired
    public NewsControllerImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public List<NewsDtoResponse> getNews(Pageable pageable, String title, String content, String authorName, List<String> tagNames, List<Long> tagIds) {
        NewsSearchCriteriaParams params = new NewsSearchCriteriaParams(title, content, authorName, tagNames, tagIds);
        return newsService.getNews(pageable, params).getContent();
    }

    @Override
    public NewsDtoResponse getById(Long id) {
        return newsService.getById(id);
    }

    @Override
    public NewsDtoResponse create(NewsDtoRequest createRequest) {
        return newsService.create(createRequest);
    }

    @Override
    public NewsDtoResponse update(Long id, NewsDtoRequest updateRequest) {
        return newsService.update(updateRequest);
    }

    @Override
    public Boolean deleteById(Long id) {
        return newsService.deleteById(id);
    }

    @Override
    public NewsDtoResponse patch(Long id, JsonPatch patch) {
        return newsService.patch(id, patch);
    }
}
