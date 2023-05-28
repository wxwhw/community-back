package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Author wxh
 * 2023/5/18 20:34
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Resource
    private SearchService searchService;

    @GetMapping("article")
    public ResultInfo getSearchArticle(@RequestParam(required = true,defaultValue = "")String searchText,
                                       @RequestParam(required = true) Integer pageNum,
                                       @RequestParam(required = true) Integer pageSize){
        return searchService.searchArticle(searchText,pageNum,pageSize);
    }

    @GetMapping("user")
    public ResultInfo getSearchUser(@RequestParam(required = true,defaultValue = "")String searchText,
                                    @RequestParam(required = true) Integer pageNum,
                                    @RequestParam(required = true) Integer pageSize){
        return searchService.searchUser(searchText,pageNum,pageSize);
    }

    @GetMapping("type")
    public ResultInfo getSearchType(@RequestParam String type,@RequestParam String searchText){
        return searchService.getSearchType(type,searchText);
    }



}
