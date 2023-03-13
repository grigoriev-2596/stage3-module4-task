package com.mjc.school.controller.implementation;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.NewsController;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.query.NewsSearchCriteriaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsControllerImpl implements NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsControllerImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Override
    public List<NewsDtoResponse> getNews(
            @PageableDefault(size = 2)
            @SortDefault(sort = "title", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            String title,

            @RequestParam(required = false)
            String content,

            @RequestParam(required = false)
            String authorName,

            @RequestParam(required = false)
            List<String> tagNames,

            @RequestParam(required = false)
            List<Long> tagIds) {
        NewsSearchCriteriaParams params = new NewsSearchCriteriaParams(title, content, authorName, tagNames, tagIds);
        return newsService.getNews(pageable, params).getContent();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @Override
    public List<NewsDtoResponse> readAll() {
        return newsService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id:\\d+}")
    @Override
    public NewsDtoResponse readById(@PathVariable Long id) {
        return newsService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Override
    public NewsDtoResponse create(@Valid @RequestBody NewsDtoRequest createRequest) {
        return newsService.create(createRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Override
    public NewsDtoResponse update(@Valid @RequestBody NewsDtoRequest updateRequest) {
        return newsService.update(updateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id:\\d+}")
    @Override
    public boolean deleteById(@PathVariable Long id) {
        return newsService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public NewsDtoResponse patch(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return newsService.patch(id, patch);
    }
}
