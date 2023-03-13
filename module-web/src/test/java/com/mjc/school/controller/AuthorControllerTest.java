package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.exception_handler.RestControllerExceptionHandler;
import com.mjc.school.controller.implementation.AuthorControllerImpl;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.implementation.AuthorServiceImpl;
import com.mjc.school.service.query.AuthorSearchCriteriaParams;
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

@WebMvcTest({AuthorControllerImpl.class})
@ContextConfiguration(classes = {AuthorControllerImpl.class, RestControllerExceptionHandler.class})
public class AuthorControllerTest {

    @MockBean
    private AuthorServiceImpl authorService;

    @Autowired
    private MockMvc mockMvc;

    private AuthorDtoResponse firstTestResponse;
    private AuthorDtoResponse secondTestResponse;



    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        firstTestResponse = new AuthorDtoResponse(1L, "Ivanov Testov", LocalDateTime.now(), LocalDateTime.now());
        secondTestResponse = new AuthorDtoResponse(2L, "Alesha Ivanov", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void successCreateAuthorTest() {
        final AuthorDtoRequest request = new AuthorDtoRequest(null, firstTestResponse.name());
        final AuthorDtoResponse expected = firstTestResponse;

        Mockito.when(authorService.create(request)).thenReturn(expected);

        AuthorDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/authors")
                .then().log().all()
                .statusCode(201)
                .extract().as(AuthorDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulCreateNotValidAuthorTest() {
        final AuthorDtoRequest request = new AuthorDtoRequest(null, "ab");

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/authors")
                .then().log().all()
                .statusCode(400);
    }


    @Test
    public void successGetAuthorByIdTest() {
        final long id = secondTestResponse.id();
        final AuthorDtoResponse expected = secondTestResponse;

        Mockito.when(authorService.getById(id)).thenReturn(expected);

        AuthorDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/authors/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(AuthorDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulGetNonExistingAuthorByIdTest() {
        long id = secondTestResponse.id();

        Mockito.when(authorService.getById(id))
                .thenThrow(new NotFoundException(String.format(ErrorCode.AUTHOR_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/authors/" + id)
                .then().log().all()
                .statusCode(404);

    }

    @Test
    public void successGetAuthorsTest() {
        List<AuthorDtoResponse> content = Arrays.asList(firstTestResponse, secondTestResponse);
        Page<AuthorDtoResponse> page = new PageImpl<>(content, PageRequest.of(0, 5), 10);

        Mockito
                .when(authorService.getAuthors(any(Pageable.class), any(AuthorSearchCriteriaParams.class)))
                .thenReturn(page);

        List<AuthorDtoResponse> actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/authors")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", AuthorDtoResponse.class);

        assertEquals(actual, content);
    }

    @Test
    public void successUpdateAuthorTest() {
        final AuthorDtoRequest request = new AuthorDtoRequest(secondTestResponse.id(), secondTestResponse.name());
        final AuthorDtoResponse expected = secondTestResponse;

        Mockito.when(authorService.update(request)).thenReturn(expected);

        AuthorDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/authors")
                .then().log().all()
                .statusCode(200)
                .extract().as(AuthorDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulUpdateNotValidAuthorTest() {
        final AuthorDtoRequest request = new AuthorDtoRequest(5L, "Ivaaaaaaaaaanov Peeeeeeeeeeeeeeeetr");

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/authors")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    public void unsuccessfulUpdateNonExistingAuthorTest() {
        final long id = secondTestResponse.id();
        final AuthorDtoRequest request = new AuthorDtoRequest(id, secondTestResponse.name());

        Mockito.when(authorService.update(request))
                .thenThrow(new NotFoundException(String.format(ErrorCode.AUTHOR_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/v1/authors")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    public void successPatchTest() {
        final long id = secondTestResponse.id();
        final AuthorDtoResponse expected = secondTestResponse;

        Mockito.when(authorService.patch(eq(id), any(JsonPatch.class))).thenReturn(expected);

        AuthorDtoResponse actual = RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/name\", \"value\" : \"" + secondTestResponse.name() + "\"}]")
                .when()
                .patch("/api/v1/authors/" + id)
                .then().log().all()
                .statusCode(200)
                .extract().as(AuthorDtoResponse.class);

        assertEquals(actual, expected);
    }

    @Test
    public void unsuccessfulPatchNonExistingAuthorTest() {
        final long id = secondTestResponse.id();

        Mockito.when(authorService.patch(eq(id), any(JsonPatch.class)))
                .thenThrow(new NotFoundException(String.format(ErrorCode.AUTHOR_DOES_NOT_EXIST.toString(), id)));

        RestAssuredMockMvc.given()
                .contentType("application/json-patch+json")
                .body("[{\"op\":\"replace\", \"path\" : \"/name\", \"value\" : \"" + secondTestResponse.name() + "\"}]")
                .when()
                .patch("/api/v1/authors/" + id)
                .then().log().all()
                .statusCode(404);
    }


}

