package com.majiang.community.service;

import cn.hutool.core.util.StrUtil;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.ArticleDTO;
import com.majiang.community.dto.ArticleTypeDTO;
import com.majiang.community.dto.RelatedArticleDTO;
import com.majiang.community.dto.UserDTO;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.ArticleMapper;
import com.majiang.community.pojo.Article;
import com.majiang.community.pojo.UserFollow;
import com.majiang.community.pojo.UserLike;
import com.majiang.community.utils.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Author wxh
 * 2023/4/28 16:12
 */
@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    public ResultInfo publish(Article article) {

        ResultInfo resultInfo = new ResultInfo();

        /*设置默认值*/
        article.setAuditState("PENDING");
        article.setApprovals(0L);
        article.setIsDelete((byte) 0);
        article.setCollections(0L);
        article.setComments(0L);
        article.setCreateAt(new Date());
        article.setUpdateAt(new Date());
        article.setMarrow((byte) 0);
        article.setOfficial((byte) 0);
        article.setSort(0);
        article.setViews(0L);
        article.setTop((byte) 0);

        if(StrUtil.isBlank(article.getCoverUrl())){
          article.setCoverUrl("");
        }

        try {
            precheck(article);
            AssertUtil.isTrue(articleMapper.insertArticle(article) < 1, "发布失败！");
            AssertUtil.isTrue(articleMapper.insertTagInArticle(article) < 1, "插入标签失败！");
            resultInfo.setMsg("发布成功！");
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;
    }

    private void precheck(Article article) {

        AssertUtil.isTrue(StrUtil.isBlank(article.getTitle()), "文章标题不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(article.getMarkdownContent()), "文章内容不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(article.getHtmlContent()), "编辑器解析失败！");
        AssertUtil.isTrue(article.getTypeId() == null, "请选择社区！");
        AssertUtil.isTrue(article.getTagIds() == null || article.getTagIds().length < 1, "请选择标签！");
        AssertUtil.isTrue(article.getUserId() == null, "无法获取用户信息！");

    }

    public ResultInfo getArticlePage(Long typeId, String auditState, Byte official, Byte top, Byte marrow, Long authorId, String title, Integer pageNum, Integer pageSize) {
        ResultInfo resultInfo = new ResultInfo();
        Long totalArticle = articleMapper.selectTotal(typeId, auditState, official, top, marrow, authorId, title);
        List<ArticleDTO> articleList = null;
        if (pageNum != null && pageSize != null) {
            Integer i = (pageNum - 1) * pageSize;
            if (i >= totalArticle) {
                i = 0;
            }
            articleList = articleMapper.selectPage(typeId, auditState, official, top, marrow, authorId, title, i, pageSize);
            if (articleList != null) {
                for (ArticleDTO article : articleList) {
                    UserDTO userDTO = articleMapper.selectUserById(article.getUserId());
                    ArticleTypeDTO articleTypeDTO = articleMapper.selectArticleTypeById(article.getTypeId());
                    article.setUser(userDTO);
                    article.setArticleType(articleTypeDTO);
                }
            }

        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", totalArticle);
        data.put("articleList", articleList);
        resultInfo.setResult(data);
        return resultInfo;
    }

    public ResultInfo getArticleById(Long id) {
        ResultInfo resultInfo = new ResultInfo();

        ArticleDTO articleDTO = null;
        String msg404 = "你访问的页面不存在哦~";
        try {
            articleDTO = articleMapper.selectArticleById(id);
            if (articleDTO != null) {

                if (!articleDTO.getAuditState().equals("PASS")) {
                    throw new ParamsException(404, "您访问的页面不存在！");
                }
                /*添加浏览量*/
                articleMapper.updateViews(articleDTO.getId());

                UserDTO userDTO = articleMapper.selectUserById(articleDTO.getUserId());
                ArticleTypeDTO articleTypeDTO = articleMapper.selectArticleTypeById(articleDTO.getTypeId());
                articleDTO.setArticleType(articleTypeDTO);
                articleDTO.setUser(userDTO);
            } else {
                AssertUtil.isTrue(articleDTO == null, "文章不存在！");
            }

        } catch (ParamsException p) {
            if (p.getMsg().equals(msg404)) {
                resultInfo.setCode(404);
                resultInfo.setMsg(msg404);
            } else {
                resultInfo.setCode(505);
                resultInfo.setMsg(p.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("articleDetail", articleDTO);
        resultInfo.setResult(data);

        return resultInfo;
    }

    public ResultInfo addApprovals(UserLike userLike) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            userLike.setCreateAt(new Date());
            userLike.setUpdateAt(new Date());
            userLike.setIsDelete((byte) 0);

            AssertUtil.isTrue(userLike.getArticleId() == null, "获取不到文章ID！");
            AssertUtil.isTrue(userLike.getUserId() == null, "获取不到用户ID！");
            AssertUtil.isTrue(articleMapper.insertApproval(userLike) < 1 || articleMapper.updateApprovalNum(userLike.getArticleId()) < 1, "点赞失败！");
            HashMap<String, Object> map = new HashMap<>();
            map.put("liked",true);
            resultInfo.setMsg("点赞成功👍");
            resultInfo.setResult(map);
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;
    }

    public ResultInfo auditArticle(ArticleDTO articleDTO) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            int count = articleMapper.updateAudit(articleDTO.getId(), articleDTO.getAuditState(), articleDTO.getMarrow(), articleDTO.getOfficial(), articleDTO.getTop(), new Date());
            AssertUtil.isTrue(count < 1, "更新失败~");
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg(e.getMessage());
        }
        resultInfo.setMsg("更新成功~");
        return resultInfo;

    }

    public ResultInfo deleteArticle(Long id) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            AssertUtil.isTrue(articleMapper.deleteById(id) < 1, "删除失败~");
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg(e.getMessage());
        }
        resultInfo.setMsg("删除成功~");

        return resultInfo;
    }

    public ResultInfo getRelatedArticleList(Long articleId) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            List<RelatedArticleDTO> relatedArticleList = null;
            relatedArticleList = articleMapper.selectRelatedArticleList(articleId);
            AssertUtil.isTrue(relatedArticleList == null, "相关文章列表为空！");
            resultInfo.setMsg("获取相关文章列表成功！");
            resultInfo.setResult(relatedArticleList);
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg(e.getMessage());
        }

        return resultInfo;
    }

    public ResultInfo getIfLiked(Long articleId, Long userId) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            AssertUtil.isTrue(articleId == null || userId == null,"参数错误！");
            UserLike userLike = null;
            userLike = articleMapper.selectUserLiked(articleId,userId);
            if(userLike != null){
                resultInfo.setResult(true);
                resultInfo.setMsg("已点赞！");
            } else {
                resultInfo.setResult(false);
                resultInfo.setMsg("未点赞！");
            }
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg(e.getMessage());
        }

        return resultInfo;
    }

    public ResultInfo cancelApprovals(UserLike userLike) {
        ResultInfo resultInfo = new ResultInfo();

        try {

            AssertUtil.isTrue(userLike.getArticleId() == null, "获取不到文章ID！");

            AssertUtil.isTrue(userLike.getUserId() == null, "获取不到用户ID！");
            AssertUtil.isTrue(articleMapper.deleteUserLike(userLike) < 1 || articleMapper.deleteApproval(userLike.getArticleId()) < 1, "点赞失败！");
            HashMap<String, Object> map = new HashMap<>();
            map.put("liked",false);
            resultInfo.setMsg("取消点赞成功！");
            resultInfo.setResult(map);
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;
    }

    public ResultInfo getArticleByTagId(Long tagId, Integer pageNum, Integer pageSize) {

        ResultInfo resultInfo = new ResultInfo();
        Long totalArticle = articleMapper.selectTotalByTagId(tagId);
        List<ArticleDTO> articleList = null;
        if (pageNum != null && pageSize != null) {
            Integer offset = (pageNum - 1) * pageSize;
            if (offset >= totalArticle) {
                offset = 0;
            }
            articleList = articleMapper.selectArticleByTagId(tagId, offset, pageSize);
            if (articleList != null) {
                for (ArticleDTO article : articleList) {
                    UserDTO userDTO = articleMapper.selectUserById(article.getUserId());
                    ArticleTypeDTO articleTypeDTO = articleMapper.selectArticleTypeById(article.getTypeId());
                    article.setUser(userDTO);
                    article.setArticleType(articleTypeDTO);
                }
            }

        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", totalArticle);
        data.put("articleList", articleList);
        resultInfo.setResult(data);
        return resultInfo;
    }

    public ResultInfo getIfCollected(Long followed, Long userId) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            AssertUtil.isTrue(followed == null || userId == null,"参数错误！");
            UserFollow userCollect = null;
            userCollect = articleMapper.selectUserColloected(followed,userId);
            if(userCollect != null){
                resultInfo.setResult(true);
                resultInfo.setMsg("已收藏！");
            } else {
                resultInfo.setResult(false);
                resultInfo.setMsg("未收藏！");
            }
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg(e.getMessage());
        }

        return resultInfo;
    }

    public ResultInfo addCollections(UserFollow userFollow) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            userFollow.setCreateAt(new Date());
            userFollow.setUpdateAt(new Date());
            userFollow.setIsDelete((byte) 0);
            userFollow.setFollowType("Article");

            AssertUtil.isTrue(userFollow.getFollowed() == null, "获取不到文章ID！");
            AssertUtil.isTrue(userFollow.getUserId() == null, "获取不到用户ID！");
            AssertUtil.isTrue(articleMapper.insertCollect(userFollow) < 1 || articleMapper.updateCollections(userFollow.getFollowed()) < 1, "收藏失败！");
            HashMap<String, Object> map = new HashMap<>();
            map.put("collected",true);
            resultInfo.setMsg("收藏成功⭐");
            resultInfo.setResult(map);
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;
    }

    public ResultInfo cancelCollect(UserFollow userFollow) {

        ResultInfo resultInfo = new ResultInfo();

        try {

            AssertUtil.isTrue(userFollow.getFollowed() == null, "获取不到文章ID！");
            AssertUtil.isTrue(userFollow.getUserId() == null, "获取不到用户ID！");
            userFollow.setFollowType("Article");
            AssertUtil.isTrue(articleMapper.deleteUserFollowed(userFollow) < 1 || articleMapper.deleteCollections(userFollow.getFollowed()) < 1, "取消收藏失败！");
            HashMap<String, Object> map = new HashMap<>();
            map.put("collected",false);
            resultInfo.setMsg("取消收藏成功！");
            resultInfo.setResult(map);
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;
    }

    public ResultInfo getColArticleList(Long userId, Integer pageNum, Integer pageSize) {

        ResultInfo resultInfo = new ResultInfo();
        Long totalArticle = articleMapper.selectTotalColArticle(userId);
        List<ArticleDTO> articleList = null;
        if (pageNum != null && pageSize != null) {
            Integer offset = (pageNum - 1) * pageSize;
            if (offset >= totalArticle) {
                offset = 0;
            }
            articleList = articleMapper.selectColArticle(userId, offset, pageSize);
            if (articleList != null) {
                for (ArticleDTO article : articleList) {
                    UserDTO userDTO = articleMapper.selectUserById(article.getUserId());
                    ArticleTypeDTO articleTypeDTO = articleMapper.selectArticleTypeById(article.getTypeId());
                    article.setUser(userDTO);
                    article.setArticleType(articleTypeDTO);
                }
            }

        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", totalArticle);
        data.put("articleList", articleList);
        resultInfo.setResult(data);
        return resultInfo;
    }
}
