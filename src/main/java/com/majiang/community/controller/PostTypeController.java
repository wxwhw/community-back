package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.pojo.PostType;
import com.majiang.community.service.PostTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author wxh
 * 2023/4/4 13:32
 */
@RestController
@RequestMapping("postType")
public class PostTypeController {

    @Resource
    private PostTypeService postTypeService;

    @GetMapping("page")
    public ResultInfo findPage(@RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String scope,
                               @RequestParam(defaultValue = "") String auditState,
                               @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize){

        return postTypeService.findPage(name,scope,auditState,pageNum,pageSize);
    }

    @PostMapping("add")
    public ResultInfo addPostType(@RequestBody PostType postType){
        return postTypeService.addPostType(postType);
    }

    @PostMapping("audit")
    public ResultInfo audit(@RequestBody PostType postType){
        return postTypeService.auditPostType(postType);
    }

    @PostMapping("delete/{id}")
    public ResultInfo removePostType(@PathVariable Long id){
        return postTypeService.removePostType(id);
    }

    @GetMapping("{id}")
    public ResultInfo getPostTypeById(@PathVariable Long id){
        return postTypeService.getPostTypeById(id);
    }

    @GetMapping("allTypes")
    public ResultInfo getAllTypes(){
        return postTypeService.getAllTypes();
    }
}
