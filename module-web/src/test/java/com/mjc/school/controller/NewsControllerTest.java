package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.exception_handler.RestControllerExceptionHandler;
import com.mjc.school.controller.implementation.NewsControllerImpl;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.implementation.NewsServiceImpl;
import com.mjc.school.service.query.NewsSearchCriteriaParams;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest({NewsControllerImpl.class})
@ContextConfiguration(classes = {NewsControllerImpl.class, RestControllerExceptionHandler.class})
public class NewsControllerTest {

    @MockBean
    private NewsServiceImpl newsService;

    @Autowired
    private MockMvc mockMvc;

    private NewsDtoResponse firstTestResponse;
    private NewsDtoResponse secondTestResponse;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        LocalDateTime now = LocalDateTime.now();
        AuthorDtoResponse author = new AuthorDtoResponse(1L, "Alesha Popovich", now, now);
        List<TagDtoResponse> gamesTags = List.of(new TagDtoResponse(1L, "computer games"), new TagDtoResponse(2L, "game dev"));
        List<TagDtoResponse> weatherTags = List.of(new TagDtoResponse(1L, "weather"), new TagDtoResponse(2L, "weather in Minsk"));

        firstTestResponse = new NewsDtoResponse(1L, "Atomic heart", "Atomic Heart is a single-player first-person shooter.",
                now, now, author, gamesTags);
        secondTestResponse = new NewsDtoResponse(2L, "Minsk weather", "The air quality is generally acceptable for most individuals.",
                now, now, author, weatherTags);
    }

    @Test
    public void successCreateNewsTest() {
        final NewsDtoRequest request = new NewsDtoRequest(null, firstTestResponse.title(), firstTestResponse.content(), 1L, List.of(1L, 2L));
        final NewsDtoResponse expected = firstTestResponse;

        Mockito.when(newsService.create(request)).thenReturn(expected);

        NewsDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/news")
                .then().log().all()
                .statusCode(201)
                .extract().as(NewsDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulCreateNotValidNewsTest() {
        String notValidTitle = "titl";
        final NewsDtoRequest request = new NewsDtoRequest(null, notValidTitle, secondTestResponse.content(), 1L, List.of(1L, 2L));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/news")
                .then().log().all()
                .statusCode(400);
    }


    @Test
    public void successGetNewsByIdTest() {
        final long id = secondTestResponse.id();
        final NewsDtoResponse expected = secondTestResponse;

        Mockito.when(newsService.getById(id)).thenReturn(expected);

        NewsDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/news/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(NewsDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulGetNonExistingNewsByIdTest() {
        long id = firstTestResponse.id();

        Mockito.when(newsService.getById(id))
                .thenThrow(new NotFoundException(String.format(ErrorCode.NEWS_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/news/" + id)
                .then().log().all()
                .statusCode(404);

    }

    @Test
    public void successGetNewsTest() {
        List<NewsDtoResponse> content = Arrays.asList(firstTestResponse, secondTestResponse);
        Page<NewsDtoResponse> page = new PageImpl<>(content, PageRequest.of(0, 5), 10);

        Mockito
                .when(newsService.getNews(any(Pageable.class), any(NewsSearchCriteriaParams.class)))
                .thenReturn(page);

        List<NewsDtoResponse> actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/news")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", NewsDtoResponse.class);

        assertEquals(actual, content);
    }

    @Test
    public void successUpdateNewsTest() {
        final long id = secondTestResponse.id();
        final NewsDtoRequest request = new NewsDtoRequest(id, secondTestResponse.title(), secondTestResponse.content(), 1L, List.of(1L, 2L));
        final NewsDtoResponse expected = secondTestResponse;

        Mockito.when(newsService.update(request)).thenReturn(expected);

        NewsDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/news/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(NewsDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulUpdateNotValidNewsTest() {
        long id = secondTestResponse.id();
        String notValidContent = "cont";
        final NewsDtoRequest request = new NewsDtoRequest(id, firstTestResponse.title(), notValidContent, 1L, List.of(1L, 2L));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/news/" + id)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    public void unsuccessfulUpdateWithNonExistingNewsTest() {
        final long id = secondTestResponse.id();
        final NewsDtoRequest request = new NewsDtoRequest(id, secondTestResponse.title(), secondTestResponse.content(), 1L, List.of(1L, 2L));

        Mockito.when(newsService.update(request))
                .thenThrow(new NotFoundException(String.format(ErrorCode.NEWS_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/news/" + id)
                .then().log().all()
                .statusCode(404);
    }

    @Test
    public void successPatchTest() {
        final long id = secondTestResponse.id();
        final NewsDtoResponse expected = secondTestResponse;

        Mockito.when(newsService.patch(eq(id), any(JsonPatch.class))).thenReturn(expected);

        NewsDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/title\", \"value\" : \"" + secondTestResponse.title() + "\"}]")
                .when()
                .patch("/news/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(NewsDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulPatchNonExistingNewsTest() {
        final long id = secondTestResponse.id();

        Mockito.when(newsService.patch(eq(id), any(JsonPatch.class)))
                .thenThrow(new NotFoundException(String.format(ErrorCode.NEWS_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/content\", \"value\" : \"" + secondTestResponse.content() + "\"}]")
                .when()
                .patch("/newss/" + id)
                .then().log().all()
                .statusCode(404);
    }


}

