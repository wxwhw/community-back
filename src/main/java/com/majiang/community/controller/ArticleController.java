package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.ArticleDTO;
import com.majiang.community.pojo.Article;
import com.majiang.community.pojo.UserFollow;
import com.majiang.community.pojo.UserLike;
import com.majiang.community.service.ArticleService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author wxh
 * 2023/4/28 16:08
 */
@RestController
@RequestMapping("article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("publish")
    public ResultInfo publish(@RequestBody Article article) {
        return articleService.publish(article);
    }


    @GetMapping("allArticles")
    public ResultInfo getAll(@RequestParam(name = "typeId", required = false) Long typeId,
                             @RequestParam(name = "auditState", required = false, defaultValue = "") String auditState,
                             @RequestParam(name = "official", required = false) Byte official,
                             @RequestParam(name = "top", required = false) Byte top,
                             @RequestParam(name = "marrow", required = false) Byte marrow,
                             @RequestParam(name = "authorId", required = false) Long authorId,
                             @RequestParam(name = "title", required = false, defaultValue = "") String title,
                             @RequestParam(name = "pageNum", required = false) Integer pageNum,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return articleService.getArticlePage(typeId, auditState, official, top, marrow, authorId, title, pageNum, pageSize);
    }

    @GetMapping("{id}")
    public ResultInfo getArticleById(@PathVariable("id") Long id) {
        return articleService.getArticleById(id);
    }

    @PostMapping("like")
    public ResultInfo addApprovals(@RequestBody UserLike userLike) {
        return articleService.addApprovals(userLike);
    }

    @PostMapping("cancelLike")
    public ResultInfo cancelApprovals(@RequestBody UserLike userLike) {
        return articleService.cancelApprovals(userLike);
    }

    @PostMapping("collect")
    public ResultInfo addCollections(@RequestBody UserFollow userFollow) {
        return articleService.addCollections(userFollow);
    }

    @PostMapping("cancelCollect")
    public ResultInfo cancelCollect(@RequestBody UserFollow userFollow) {
        return articleService.cancelCollect(userFollow);
    }

    @PostMapping("audit")
    public ResultInfo auditArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.auditArticle(articleDTO);
    }

    @PostMapping("delete")
    public ResultInfo deleteArticle(@RequestParam Long id) {
        return articleService.deleteArticle(id);
    }

    @GetMapping("relatedArticleList")
    public ResultInfo getRelatedArticleList(@RequestParam Long articleId) {
        return articleService.getRelatedArticleList(articleId);
    }

    @GetMapping("getIfLiked")
    public ResultInfo getIfLiked(@RequestParam Long articleId, @RequestParam Long userId) {
        return articleService.getIfLiked(articleId, userId);
    }

    @GetMapping("getIfCollected")
    public ResultInfo getIfCollected(@RequestParam Long followed, @RequestParam Long userId) {
        return articleService.getIfCollected(followed, userId);
    }

    @GetMapping("getArticleByTagId")
    public ResultInfo getArticleByTagId(@RequestParam Long tagId,
                                        @RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
        return articleService.getArticleByTagId(tagId, pageNum, pageSize);
    }

    @GetMapping("colArticle")
    public ResultInfo getColArticleList(@RequestParam Long userId,
                                        @RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
        return articleService.getColArticleList(userId,pageNum,pageSize);
    }

}
