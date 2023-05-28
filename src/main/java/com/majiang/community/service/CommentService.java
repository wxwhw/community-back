package com.majiang.community.service;

import cn.hutool.core.util.StrUtil;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.CommentDTO;
import com.majiang.community.dto.CommentPageDTO;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.ArticleMapper;
import com.majiang.community.mapper.CommentMapper;
import com.majiang.community.pojo.Comment;
import com.majiang.community.utils.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Author wxh
 * 2023/5/1 0:10
 */
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ArticleMapper articleMapper;

    public ResultInfo getCommentsByArticleId(Long articleId) {
        ResultInfo resultInfo = new ResultInfo();


        List<Comment> rootCommentList = null;
        HashMap<String, Object> map = new HashMap<>();
        Long total = null;
        try {
            rootCommentList = commentMapper.selectRootComByArticleId(articleId);
            total = commentMapper.selectTotalByArticleI(articleId);
            AssertUtil.isTrue(rootCommentList == null || total == null, "获取评论列表失败！");
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");

        }

        map.put("total", total);
        map.put("commentList", rootCommentList);
        resultInfo.setResult(map);
        resultInfo.setMsg("获取评论列表成功！");
        return resultInfo;
    }

    public ResultInfo getSubComByRootId(Long rootId) {

        ResultInfo resultInfo = new ResultInfo();
        List<Comment> subCommentList = commentMapper.selectSubComByRootId(rootId);

        resultInfo.setResult(subCommentList);
        return resultInfo;
    }

    public ResultInfo addComment(CommentDTO commentDTO) {


        ResultInfo resultInfo = new ResultInfo();

        commentDTO.setIsDelete((byte) 0);
        commentDTO.setCreateAt(new Date());
        commentDTO.setUpdateAt(new Date());

        try {
            precheck(commentDTO);
            AssertUtil.isTrue(commentMapper.insertComment(commentDTO) < 1 || articleMapper.updateCommentNum(commentDTO.getPostId()) < 1, "评论失败！");
            resultInfo.setMsg("评论成功！");
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }

        return resultInfo;
    }

    private void precheck(CommentDTO commentDTO) {

        AssertUtil.isTrue(commentDTO.getUserId() == null, "用户id不能为空！");
        AssertUtil.isTrue(commentDTO.getPostId() == null, "文章id不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(commentDTO.getContent()), "评论内容不能为空！");
    }

    public ResultInfo getAllComments(String nickname, String content, String startWith, String endWith, Integer pageNum, Integer pageSize) {

        ResultInfo resultInfo = new ResultInfo();

        Long total = commentMapper.selectTotal(nickname, content, startWith, endWith);
        Integer offset = (pageNum - 1) * pageSize;
        if (offset >= total) {
            offset = 0;
        }

        List<CommentPageDTO> commentList = null;
        try {
            commentList = commentMapper.selectAllComments(nickname, content, startWith, endWith, offset, pageSize);
            AssertUtil.isTrue(commentList == null, "评论列表为空！");
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("commentList", commentList);
        resultInfo.setResult(map);
        resultInfo.setMsg("获取评论列表成功！");
        return resultInfo;
    }

    public ResultInfo removeComment(Long id, String type) {

        ResultInfo resultInfo = new ResultInfo();

        try {

            AssertUtil.isTrue(!("Root").equals(type) && !("Sub").equals(type), "类型不正确");
            if (("Root").equals(type)) {
                AssertUtil.isTrue(commentMapper.deleteRootById(id) < 1, "删除失败~");
            } else if (("Sub").equals(type)) {
                AssertUtil.isTrue(commentMapper.deleteSubById(id) < 1 , "删除失败~");
            } else {
                throw new ParamsException("参数出错！");
            }

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
}
