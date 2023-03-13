package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.exception_handler.RestControllerExceptionHandler;
import com.mjc.school.controller.impl.CommentControllerImpl;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.impl.CommentServiceImpl;
import com.mjc.school.service.query.CommentSearchCriteriaParams;
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

@WebMvcTest({CommentControllerImpl.class})
@ContextConfiguration(classes = {CommentControllerImpl.class, RestControllerExceptionHandler.class})
public class CommentControllerTest {

    @MockBean
    private CommentServiceImpl commentService;

    @Autowired
    private MockMvc mockMvc;

    private CommentDtoResponse firstTestResponse;
    private CommentDtoResponse secondTestResponse;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        LocalDateTime now = LocalDateTime.now();
        firstTestResponse = new CommentDtoResponse(1L, "weather", 1L, now, now);
        secondTestResponse = new CommentDtoResponse(2L, "computer games", 2L, now, now);
    }

    @Test
    public void successCreateCommentTest() {
        final CommentDtoRequest request = new CommentDtoRequest(null, firstTestResponse.content(), firstTestResponse.newsId());
        final CommentDtoResponse expected = firstTestResponse;

        Mockito.when(commentService.create(request)).thenReturn(expected);

        CommentDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/comments")
                .then().log().all()
                .statusCode(201)
                .extract().as(CommentDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulCreateNotValidCommentTest() {
        final CommentDtoRequest request = new CommentDtoRequest(null, "cont", secondTestResponse.newsId());

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/comments")
                .then().log().all()
                .statusCode(400);
    }


    @Test
    public void successGetCommentByIdTest() {
        final long id = secondTestResponse.id();
        final CommentDtoResponse expected = secondTestResponse;

        Mockito.when(commentService.getById(id)).thenReturn(expected);

        CommentDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/comments/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(CommentDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulGetNonExistingCommentByIdTest() {
        long id = firstTestResponse.id();

        Mockito.when(commentService.getById(id))
                .thenThrow(new NotFoundException(String.format(ErrorCode.COMMENT_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/comments/" + id)
                .then().log().all()
                .statusCode(404);

    }

    @Test
    public void successGetCommentsTest() {
        List<CommentDtoResponse> content = Arrays.asList(firstTestResponse, secondTestResponse);
        Page<CommentDtoResponse> page = new PageImpl<>(content, PageRequest.of(0, 5), 10);

        Mockito
                .when(commentService.getComments(any(Pageable.class), any(CommentSearchCriteriaParams.class)))
                .thenReturn(page);

        List<CommentDtoResponse> actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/comments")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", CommentDtoResponse.class);

        assertEquals(actual, content);
    }

    @Test
    public void successUpdateCommentTest() {
        final CommentDtoRequest request = new CommentDtoRequest(secondTestResponse.id(), secondTestResponse.content(), secondTestResponse.newsId());
        final CommentDtoResponse expected = secondTestResponse;

        Mockito.when(commentService.update(request)).thenReturn(expected);

        CommentDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/comments")
                .then().log().all()
                .statusCode(200)
                .extract().as(CommentDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulUpdateNotValidCommentTest() {
        final CommentDtoRequest request = new CommentDtoRequest(secondTestResponse.id(), "cont", secondTestResponse.newsId());

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/comments")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    public void unsuccessfulUpdateWithNonExistingCommentTest() {
        final long id = secondTestResponse.id();
        final CommentDtoRequest request = new CommentDtoRequest(id, secondTestResponse.content(), secondTestResponse.newsId());

        Mockito.when(commentService.update(request))
                .thenThrow(new NotFoundException(String.format(ErrorCode.COMMENT_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/comments")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    public void successPatchTest() {
        final long id = secondTestResponse.id();
        final CommentDtoResponse expected = secondTestResponse;

        Mockito.when(commentService.patch(eq(id), any(JsonPatch.class))).thenReturn(expected);

        CommentDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/content\", \"value\" : \"" + secondTestResponse.content() + "\"}]")
                .when()
                .patch("/api/v1/comments/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(CommentDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulPatchNonExistingCommentTest() {
        final long id = secondTestResponse.id();

        Mockito.when(commentService.patch(eq(id), any(JsonPatch.class)))
                .thenThrow(new NotFoundException(String.format(ErrorCode.COMMENT_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/content\", \"value\" : \"" + secondTestResponse.content() + "\"}]")
                .when()
                .patch("/api/v1/comments/" + id)
                .then().log().all()
                .statusCode(404);
    }


}

