package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.mapper.TagMapper;
import com.majiang.community.pojo.Tag;
import com.majiang.community.pojo.User;
import com.majiang.community.service.TagService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author wxh
 * 2023/4/3 13:51
 */
@RestController
@RequestMapping("tag")
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private TagMapper tagMapper;

    @GetMapping("/findAll")
    public void findAllTags(){

    }

    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam(defaultValue = "") String name,
                                        @RequestParam(defaultValue = "") String group,
                                        @RequestParam(defaultValue = "") String auditState,
                                        @RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {


        Long total = tagMapper.selectTotal(name,group,auditState);
        Integer i = (pageNum -1) * pageSize;
        if(i >= total){
            i = 0;
        }
        List<Tag> allTags = tagMapper.selectPage(name,group,auditState,i,pageSize);


        Map<String, Object> res = new HashMap<>();

        res.put("allTags",allTags);
        res.put("total",total);

        return res;
    }

    @PostMapping("/update")
    public ResultInfo changeAuditState(@RequestBody Tag tag){

        Long id = tag.getId();
        String newState = tag.getAuditState();
        tag.setUpdateAt(new Date());
        return tagService.changeAuditState(id,newState,tag.getUpdateAt());
    }

    @PostMapping("/add")
    public ResultInfo addTag(@RequestBody Tag tag){
        return tagService.insertTag(tag);
    }

    @PostMapping("/delete/{id}")
    public ResultInfo removeTag(@PathVariable Long id){
        return tagService.removeTag(id);
    }

    @GetMapping("/allTags")
    public ResultInfo getAllTagsByGroup(){
        return tagService.getTagGroup();
    }

    @GetMapping("/hottag")
    public ResultInfo getHotTags(){
        return tagService.getHotTags();
    }

    @GetMapping("{id}")
    public ResultInfo getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }
}
