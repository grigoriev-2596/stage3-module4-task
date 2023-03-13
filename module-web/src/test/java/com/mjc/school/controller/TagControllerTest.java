package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.exception_handler.RestControllerExceptionHandler;
import com.mjc.school.controller.impl.TagRestController;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.impl.TagServiceImpl;
import com.mjc.school.service.query.TagSearchCriteriaParams;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest({TagRestController.class})
@ContextConfiguration(classes = {TagRestController.class, RestControllerExceptionHandler.class})
public class TagControllerTest {

    @MockBean
    private TagServiceImpl tagService;

    @Autowired
    private MockMvc mockMvc;

    private TagDtoResponse firstTestResponse;
    private TagDtoResponse secondTestResponse;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        firstTestResponse = new TagDtoResponse(1L, "weather");
        secondTestResponse = new TagDtoResponse(2L, "computer games");
    }

    @Test
    public void successCreateTagTest() {
        final TagDtoRequest request = new TagDtoRequest(null, firstTestResponse.name());
        final TagDtoResponse expected = firstTestResponse;

        Mockito.when(tagService.create(request)).thenReturn(expected);

        TagDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/tags")
                .then().log().all()
                .statusCode(201)
                .extract().as(TagDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulCreateNotValidTagTest() {
        final TagDtoRequest request = new TagDtoRequest(null, "weatheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeer");

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/tags")
                .then().log().all()
                .statusCode(400);
    }


    @Test
    public void successGetTagByIdTest() {
        final long id = secondTestResponse.id();
        final TagDtoResponse expected = secondTestResponse;

        Mockito.when(tagService.getById(id)).thenReturn(expected);

        TagDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/tags/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(TagDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulGetNonExistingTagByIdTest() {
        long id = firstTestResponse.id();

        Mockito.when(tagService.getById(id))
                .thenThrow(new NotFoundException(String.format(ErrorCode.TAG_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/tags/" + id)
                .then().log().all()
                .statusCode(404);

    }

    @Test
    public void successGetTagsTest() {
        List<TagDtoResponse> content = Arrays.asList(firstTestResponse, secondTestResponse);
        Page<TagDtoResponse> page = new PageImpl<>(content, PageRequest.of(0, 5), 10);

        Mockito
                .when(tagService.getTags(any(Pageable.class), any(TagSearchCriteriaParams.class)))
                .thenReturn(page);

        List<TagDtoResponse> actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/tags")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", TagDtoResponse.class);

        assertEquals(actual, content);
    }

    @Test
    public void successUpdateTagTest() {
        Long id = secondTestResponse.id();
        final TagDtoRequest request = new TagDtoRequest(id, secondTestResponse.name());
        final TagDtoResponse expected = secondTestResponse;

        Mockito.when(tagService.update(request)).thenReturn(expected);

        TagDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/tags/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(TagDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulUpdateNotValidTagTest() {
        Long id = secondTestResponse.id();
        final TagDtoRequest request = new TagDtoRequest(id, "we");

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/tags/" + id)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    public void unsuccessfulUpdateNonExistingTagTest() {
        final long id = secondTestResponse.id();
        final TagDtoRequest request = new TagDtoRequest(id, secondTestResponse.name());

        Mockito.when(tagService.update(request))
                .thenThrow(new NotFoundException(String.format(ErrorCode.TAG_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/tags/" + id)
                .then().log().all()
                .statusCode(404);
    }

    @Test
    public void successPatchTest() {
        final long id = secondTestResponse.id();
        final TagDtoResponse expected = secondTestResponse;

        Mockito.when(tagService.patch(eq(id), any(JsonPatch.class))).thenReturn(expected);

        TagDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/name\", \"value\" : \"" + secondTestResponse.name() + "\"}]")
                .when()
                .patch("/api/v1/tags/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(TagDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulPatchNonExistingTagTest() {
        final long id = secondTestResponse.id();

        Mockito.when(tagService.patch(eq(id), any(JsonPatch.class)))
                .thenThrow(new NotFoundException(String.format(ErrorCode.TAG_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/name\", \"value\" : \"" + secondTestResponse.name() + "\"}]")
                .when()
                .patch("/api/v1/tags/" + id)
                .then().log().all()
                .statusCode(404);
    }


}

