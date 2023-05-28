package com.majiang.community.service;

import cn.hutool.core.util.StrUtil;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.UserDTO;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.FollowMapper;
import com.majiang.community.pojo.Article;
import com.majiang.community.pojo.User;
import com.majiang.community.pojo.UserFollow;
import com.majiang.community.pojo.UserToken;
import com.majiang.community.utils.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Author wxh
 * 2023/5/4 23:07
 */
@Service
public class FollowService {

    @Resource
    private FollowMapper followMapper;


    public ResultInfo userFollow(UserFollow userFollow) {
        ResultInfo resultInfo = new ResultInfo();
        System.out.println(userFollow.getFollowType());
        try {

            checkParams(userFollow);
            /*设置默认值*/
            userFollow.setIsDelete((byte) 0);
            userFollow.setCreateAt(new Date());
            userFollow.setUpdateAt(new Date());

            UserFollow isFollow = null;
            isFollow = followMapper.selectIsExisted(userFollow);

            if (isFollow != null){
                AssertUtil.isTrue(followMapper.updateUserFollow(userFollow) < 1, "执行失败！");
            } else {
                AssertUtil.isTrue(followMapper.insertUserFollow(userFollow) < 1, "执行失败！");
            }


            if ("Article".equals(userFollow.getFollowType())) {
                resultInfo.setMsg("收藏成功！");
            } else if ("User".equals(userFollow.getFollowType())) {
                resultInfo.setMsg("关注成功！");
            } else {
                resultInfo.setMsg("未知错误~");
            }


        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统出错~");
        }
        return resultInfo;
    }

    void checkParams(UserFollow userFollow) {
        AssertUtil.isTrue(userFollow.getUserId() == null, "用户id不能为空~");
        AssertUtil.isTrue(userFollow.getFollowed() == null, "被关注主体id不能为空~");
        AssertUtil.isTrue(StrUtil.isBlank(userFollow.getFollowType()), "关注类型不能为空~");
        AssertUtil.isTrue(!"Article".equals(userFollow.getFollowType()) && !"User".equals(userFollow.getFollowType()), "关注类型不正确~只能为Article/User");
    }


    public ResultInfo getMyFollowList(Long userId, String followType, Integer pageNum, Integer pageSize) {

        ResultInfo resultInfo = new ResultInfo();

        AssertUtil.isTrue(userId == null, "用户id不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(followType), "关注类型不能为空！");
        AssertUtil.isTrue(!"Article".equals(followType) && !"User".equals(followType), "关注类型不正确~只能为Article/User");

        try {
            if(pageNum != null && pageSize != null){
                Integer offset = (pageNum - 1) * pageSize;
                if ("Article".equals(followType)) {

                        List<Article> articleList = followMapper.selectFavArticle(userId, offset, pageSize);
                        resultInfo.setResult(articleList);
                        resultInfo.setMsg("获取收藏文章列表成功~");

                } else if ("User".equals(followType)) {
                    List<UserDTO> userDTOList = followMapper.selectUserFollowList(userId,offset,pageSize);
                    resultInfo.setResult(userDTOList);
                    resultInfo.setMsg("获取关注用户成功~");
                } else {
                    throw new ParamsException("未知错误！");
                }
            }

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

    public ResultInfo getUserIsFollow(Long followed, Long userId) {

        ResultInfo resultInfo = new ResultInfo();
        HashMap<String, Object> map = new HashMap<>();
        try{
            UserFollow userFollow = null;
            userFollow = followMapper.selectUserIsFollowed(followed,userId);
            if(userFollow == null){
                map.put("isFollow",false);
            } else {
                map.put("isFollow",true);
            }

        }  catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }

        resultInfo.setMsg("获取关注状态成功！");
        resultInfo.setResult(map);
        return resultInfo;
    }


    public ResultInfo cancelFollow(UserFollow userFollow) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            checkParams(userFollow);

            if ("Article".equals(userFollow.getFollowType())){
                AssertUtil.isTrue(followMapper.removeUserFollow(userFollow.getFollowed(), userFollow.getUserId(), userFollow.getFollowType()) < 1,"取消收藏失败！");
                resultInfo.setMsg("取消收藏成功！");
            } else if ("User".equals(userFollow.getFollowType())) {
                AssertUtil.isTrue(followMapper.removeUserFollow(userFollow.getFollowed(), userFollow.getUserId(), userFollow.getFollowType()) < 1,"取消关注失败！");
                resultInfo.setMsg("取消关注成功！");
            } else {
                throw new ParamsException("未知错误！");
            }
        } catch (ParamsException p){
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(505);
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }

        return resultInfo;
    }

    public ResultInfo getFansList(Long userId, String followType, Integer pageNum, Integer pageSize) {

        ResultInfo resultInfo = new ResultInfo();

        AssertUtil.isTrue(userId == null, "用户id不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(followType), "关注类型不能为空！");
        AssertUtil.isTrue(!"User".equals(followType), "关注类型不正确~只能为User");

        try{
            if(pageNum != null && pageSize != null){

                Integer offset = (pageNum - 1) * pageSize;
                if ("User".equals(followType)) {
                    List<UserDTO> fansList = followMapper.selectUserFansList(userId,offset,pageSize);
                    AssertUtil.isTrue(fansList == null ,"获取粉丝列表失败~");
                    resultInfo.setResult(fansList);
                    resultInfo.setMsg("获取粉丝列表成功~");
                } else {
                    throw new ParamsException("未知错误！");
                }
            }
        } catch (ParamsException p){
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(505);
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！请重试！");
        }

        return resultInfo;
    }
}
