package org.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.test.entity.Request;
import org.test.service.BannerService;
import org.test.service.CategoryService;
import org.test.service.RequestService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class RequestController {
    CategoryService categoryService;
    @Autowired
    public void setCategoryService(CategoryService categoryService){this.categoryService = categoryService;}

    BannerService bannerService;
    @Autowired
    public void setBannerService(BannerService bannerService){this.bannerService = bannerService;}

    RequestService requestService;
    @Autowired
    public void setRequestService(RequestService requestService){this.requestService = requestService;}

    /**
     * Метод для отображения текста баннера на экране
     * @param userAgent - принимает на вход Header User-Agent для идентификации клиента
     * @param requestName - Параметр определяет из какой категории будет показываться баннер
     * @return - Программа возвращает текст баннера или ответ, что баннера для отображения нет
     */
    @GetMapping("/content")
    public ResponseEntity<?> newRequest(@RequestHeader(value = "User-Agent")String userAgent,
                                        @RequestParam(value = "requestName") String requestName){
        String content = requestService.getBannerText(requestName, userAgent);
        if(content!=null){
            Request request = new Request();
            request.setDate(LocalDate.now());
            request.setUserAgent(userAgent);
            request.setBannerId(bannerService.getByContent(content));
            request.setIpAddress(requestService.getClientIP());
            requestService.save(request);
            return ResponseEntity.ok(content);
        }
        else return new ResponseEntity("no banner to display", HttpStatus.NO_CONTENT);




    }

    /**
     * Метот выдает список всех выполненных запросов на отображение банера
     * @param pageable - размер страницы по-умолчанию
     * @return - возвращает массив запросов
     */
    @GetMapping("/requests")
    public ResponseEntity<?> getRequests(@PageableDefault(page = 0,size = 15)Pageable pageable){
        List<Request> requests = requestService.getAll();

        return ResponseEntity.ok(requests);

    }
}
