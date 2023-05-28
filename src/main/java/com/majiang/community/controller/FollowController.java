package com.majiang.community.controller;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.pojo.UserFollow;
import com.majiang.community.service.FollowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author wxh
 * 2023/5/4 23:04
 */
@RestController
@RequestMapping("follow")
public class FollowController {

    @Resource
    private FollowService followService;

    @PostMapping("user")
    public ResultInfo userFollow(@RequestBody UserFollow userFollow) {
        return followService.userFollow(userFollow);
    }

    @GetMapping("myfollowList")
    public ResultInfo getMyFollowList(@RequestParam(required = true) Long userId,
                                      @RequestParam(required = true) String followType,
                                      @RequestParam(required = false) Integer pageNum,
                                      @RequestParam(required = false) Integer pageSize) {

        return followService.getMyFollowList(userId, followType, pageNum, pageSize);
    }

    @GetMapping("fansList")
    public ResultInfo getFansList(@RequestParam(required = true) Long userId,
                                  @RequestParam(required = true) String followType,
                                  @RequestParam(required = false) Integer pageNum,
                                  @RequestParam(required = false) Integer pageSize){
        return followService.getFansList(userId, followType, pageNum, pageSize);
    }

    @GetMapping("checkUserIsFollow")
    public ResultInfo getUserIsFollow(@RequestParam(required = true) Long followed,@RequestParam(required = true) Long userId){
        return followService.getUserIsFollow(followed,userId);
    }

    @PostMapping("cancelFollow")
    public ResultInfo cancelFollow(@RequestBody UserFollow userFollow){
        return followService.cancelFollow(userFollow);
    }
}
