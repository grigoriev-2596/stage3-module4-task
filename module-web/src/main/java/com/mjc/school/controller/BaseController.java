package com.mjc.school.controller;

public interface BaseController<T, R, K> {

    R readById(K id);

    R create(T createRequest);

    R update(K id, T updateRequest);

    void deleteById(K id);
}
