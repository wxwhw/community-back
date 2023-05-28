package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.CommentDTO;
import com.majiang.community.pojo.Comment;
import com.majiang.community.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Author wxh
 * 2023/5/1 0:06
 */
@RestController
@RequestMapping("comments")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("getRootComment")
    public ResultInfo getCommentsByArticleId(@RequestParam Long articleId){

        return commentService.getCommentsByArticleId(articleId);
    }

    @GetMapping("getSubComment")
    public ResultInfo getSubComByRootId(@RequestParam Long rootId){

        return commentService.getSubComByRootId(rootId);
    }

    @PostMapping("sendComment")
    public ResultInfo addComment(@RequestBody CommentDTO commentDTO){
        return commentService.addComment(commentDTO);
    }

    @GetMapping("getAllComments")
    public ResultInfo getAllComments(@RequestParam(defaultValue = "") String nickname,
                                     @RequestParam(defaultValue = "") String content,
                                     @RequestParam String startWith,
                                     @RequestParam String endWith,
                                     @RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize){

        return commentService.getAllComments(nickname,content,startWith,endWith,pageNum,pageSize);
    }

    @PostMapping("removeComment")
    public ResultInfo removeComment(@RequestParam Long id,@RequestParam String type){
        return commentService.removeComment(id,type);
    }
}
