package com.majiang.community.service;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.*;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.ArticleMapper;
import com.majiang.community.mapper.PostTypeMapper;
import com.majiang.community.mapper.TagMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author wxh
 * 2023/5/18 20:37
 */
@Service
public class SearchService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PostTypeMapper postTypeMapper;

    @Resource
    private TagMapper tagMapper;

    public ResultInfo searchArticle(String searchText, Integer pageNum, Integer pageSize) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            List<ArticleDTO> articleList = null;
            if (pageNum != null && pageSize != null) {
                Integer offset = (pageNum - 1) * pageSize;

                articleList = articleMapper.selectSearchArticle(searchText, offset, pageSize);
                if (articleList != null) {
                    for (ArticleDTO article : articleList) {
                        UserDTO userDTO = articleMapper.selectUserById(article.getUserId());
                        ArticleTypeDTO articleTypeDTO = articleMapper.selectArticleTypeById(article.getTypeId());
                        article.setUser(userDTO);
                        article.setArticleType(articleTypeDTO);
                    }
                    resultInfo.setMsg("查找文章成功！");
                    resultInfo.setResult(articleList);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }
        return resultInfo;
    }

    public ResultInfo searchUser(String searchText, Integer pageNum, Integer pageSize) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            if(pageNum !=null && pageSize != null){
                Integer offset = (pageNum - 1) * pageSize;
                List<SearchUserDTO> searchUserList = null;
                searchUserList = userMapper.selectSearchUser(searchText, offset, pageSize);
                resultInfo.setMsg("查找用户列表成功！");
                resultInfo.setResult(searchUserList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }
        return resultInfo;
    }


    public ResultInfo getSearchType(String type, String searchText) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            AssertUtil.isTrue(!"community".equals(type) && !"tag".equals(type),"参数错误！");
            AssertUtil.isTrue(StrUtil.isBlank(searchText),"搜索参数不能为空！");
            if("community".equals(type)){
                List<ArticleTypeDTO> community = postTypeMapper.selectArticleTypeByBlurName(searchText);
                resultInfo.setResult(community);
            } else if ("tag".equals(type)) {
                List<TagDTO> tag = tagMapper.selectTagByBlurName(searchText);
                resultInfo.setResult(tag);
            } else{
                throw new ParamsException("未知错误！");
            }
        } catch (ParamsException p){
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }
        return resultInfo;
    }
}
