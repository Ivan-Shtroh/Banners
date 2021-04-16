package org.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.entity.Banner;
import org.test.entity.Category;
import org.test.entity.Request;
import org.test.repository.BannerRepository;
import org.test.repository.CategoryRepository;
import org.test.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
public class RequestServiceImpl implements RequestService {

    HttpServletRequest httpServletRequest;

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    RequestRepository requestRepository;

    @Autowired
    public void setRequestRepository(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    BannerRepository bannerRepository;

    @Autowired
    public void setBannerRepository(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    /**
     * Метод обределяет какой баннер показать пользователю: не удаленный баннер с максимальной ценой, который пользователю сегодня
     * еще не показывали на этом устройстве
     * @param requestName - поиск баннеров осуществляется по категории
     * @param userAgent - User-Agent проверяется устройство пользователя на котором должен показываться баннер
     * @return возвращается текст баннера
     */
    @Override
    public String getBannerText(String requestName, String userAgent, String ip) {
        Category category = categoryRepository.findByRequestName(requestName);
        List<Banner> banners = bannerRepository.findAllByCategoryId(category);
        banners.removeIf(Banner::isDeleted);
        List<Request> requests = requestRepository.findAll();
        LocalDate date = LocalDate.now();
        String content;
        List<BigDecimal> prices = new ArrayList<>();
        List<Banner> bannerList = new ArrayList<>();
        BigDecimal maxPrice = new BigDecimal("0.0");

        for (Banner banner : banners) {
            prices.add(banner.getPrice());
        }
        for (BigDecimal price : prices) {

            if (price.compareTo(maxPrice) >= 0) maxPrice = price;
        }
        for (Banner banner : banners) {
            if (banner.getPrice().compareTo(maxPrice) == 0) bannerList.add(banner);

        }


        for (Request request : requests) {
            Banner banner = bannerList.get(new Random().nextInt(bannerList.size()));
            if (request.getDate().equals(date)) {
                if (request.getBannerId().getContent().equals(banner.getContent())) {
                    if (userAgent.equals(request.getUserAgent())) {
                        if (ip.equals(request.getIpAddress())){
                                bannerList.remove(banner);
                        }
                    }
                }
            }
        }


        content = bannerList.get(new Random().nextInt(bannerList.size())).getContent();
        return content;
    }

    @Override
    public Request getOne(Long id) {
        return requestRepository.findById(id).get();
    }

    @Override
    public void save(Request request) {
        requestRepository.save(request);
    }

    /**
     * Определяется ip=адрес для записи в таблицу request
     * @return возвращается ip-адрес
     */
    @Override
    public String getClientIP() {
        String ipAddress = "";
        if (httpServletRequest != null) {
            ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null || "".equals(ipAddress)) {
                ipAddress = httpServletRequest.getRemoteAddr();
            }
        }
        return ipAddress;
    }

    @Override
    public List<Request> getAll() {
        return requestRepository.findAll();
    }
}
