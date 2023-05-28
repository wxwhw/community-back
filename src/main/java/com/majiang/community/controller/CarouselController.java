package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.pojo.Carousel;
import com.majiang.community.service.CarouselService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author wxh
 * 2023/5/17 20:28
 */
@RestController
@RequestMapping("carousel")
public class CarouselController {

    @Resource
    private CarouselService carouselService;

    @PostMapping("add")
    public ResultInfo addCarousel(@RequestBody Carousel carousel){
        return carouselService.addCarousel(carousel);
    }

    @GetMapping("carouselPage")
    public ResultInfo getCarouselPage(@RequestParam(defaultValue = "") String auditState,
                                      @RequestParam(defaultValue = "") String type,
                                      @RequestParam(defaultValue = "") String title,
                                      Integer pageNum,
                                      Integer pageSize){
        return carouselService.getCarouselPage(auditState, type, title, pageNum, pageSize);
    }

    @PostMapping("audit")
    public ResultInfo changeAuditState(@RequestBody Carousel carousel){
        return carouselService.changeAuditState(carousel);
    }

    @PostMapping("/delete/{id}")
    public ResultInfo removeCarousel(@PathVariable Long id){
        return carouselService.removeCarousel(id);
    }

    @GetMapping("carouselList")
    public ResultInfo getCarouselList(){
        return carouselService.getCarouselList();
    }
}
