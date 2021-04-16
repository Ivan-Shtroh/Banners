package org.test.service;

import org.test.entity.Request;

import java.util.List;

public interface RequestService {

    String getBannerText(String requestName, String userAgent);

    Request getOne(Long id);

    void save(Request request);

    String getClientIP();

    List<Request> getAll();

}
