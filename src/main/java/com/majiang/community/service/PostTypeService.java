package com.majiang.community.service;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.ArticleTypeDTO;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.PostTypeMapper;
import com.majiang.community.pojo.PostType;
import com.majiang.community.utils.AssertUtil;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author wxh
 * 2023/4/4 13:41
 */
@Service
public class PostTypeService {

    @Resource
    private PostTypeMapper postTypeMapper;

    public ResultInfo findPage(String name, String scope, String auditState, Integer pageNum, Integer pageSize) {

        ResultInfo resultInfo = new ResultInfo();
        HashMap<String, Object> res = new HashMap<>();
        try {
            Long total = postTypeMapper.selectTotal(name, scope, auditState);
            // 解决分页问题
            Integer i = (pageNum - 1) * pageSize;
            if (i > total) {
                i = 0;
            }
            List<PostType> allPostTypes = postTypeMapper.selectPage(name, scope, auditState, i, pageSize);
            AssertUtil.isTrue(total == null || allPostTypes == null, "数据查找失败！");
            resultInfo.setMsg("数据查找成功！");
            res.put("allPostTypes", allPostTypes);
            res.put("total", total);
            resultInfo.setResult(res);

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

    public ResultInfo addPostType(PostType postType) {

        ResultInfo resultInfo = new ResultInfo();

        postType.setIsDelete((byte) 0);
        postType.setCreateAt(new Date());
        postType.setUpdateAt(new Date());
        postType.setAuditState("PENDING");
        postType.setCreatorId(16L);
        postType.setRefCount(0L);

        try {
            checkAddParams(postType);
            AssertUtil.isTrue(postTypeMapper.insertPostType(postType) < 1, "新增失败！");
            resultInfo.setMsg("新增成功！");
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

    private void checkAddParams(PostType postType) {
        AssertUtil.isTrue(StrUtil.isBlank(postType.getName()), "分类名称不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(postType.getScope()), "作用域不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(postType.getDescription()), "分类描述不能为空！");
        AssertUtil.isTrue(postTypeMapper.selectPostTypeByName(postType.getName()) != null, "分类已存在！");
    }

    public ResultInfo auditPostType(PostType postType) {

        ResultInfo resultInfo = new ResultInfo();
        try {
            AssertUtil.isTrue(postTypeMapper.updateAudit(postType.getId(), postType.getAuditState(), new Date()) < 1, "审核失败！");
            resultInfo.setMsg("审核完成！");
        } catch (ParamsException p) {
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            resultInfo.setCode(505);
            resultInfo.setMsg("审核异常！");
        }
        return resultInfo;

    }

    public ResultInfo removePostType(Long id) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            AssertUtil.isTrue(postTypeMapper.deleteById(id) < 1,"删除失败！");
            resultInfo.setMsg("删除成功！");
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

    public ResultInfo getAllTypes() {

        ResultInfo resultInfo = new ResultInfo();
        try {
            List<PostType> allTypes = postTypeMapper.selectAllTypes();
            AssertUtil.isTrue(allTypes == null,"查询失败！");
            resultInfo.setMsg("查询成功！");
            resultInfo.setResult(allTypes);
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

    public ResultInfo getPostTypeById(Long id) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            ArticleTypeDTO community = null;
            community = postTypeMapper.selectPostTypeById(id);
            resultInfo.setMsg("查询成功！");
            resultInfo.setResult(community);
        }  catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }
        return resultInfo;
    }
}
