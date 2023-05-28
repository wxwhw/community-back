package com.majiang.community.service;

import com.majiang.community.base.ResultInfo;
import com.majiang.community.dto.ArticleTypeDTO;
import com.majiang.community.dto.TagDTO;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.TagMapper;
import com.majiang.community.pojo.Tag;
import com.majiang.community.utils.AssertUtil;
import org.apache.ibatis.annotations.Select;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Author wxh
 * 2023/4/3 14:03
 */
@Service
public class TagService {

    @Resource
    private TagMapper tagMapper;

    public ResultInfo changeAuditState(Long id, String newState,Date updateAt){

        ResultInfo resultInfo = new ResultInfo();
        try{
            AssertUtil.isTrue(tagMapper.updateAudit(id,newState,updateAt) < 1,"审核失败！");
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

    public ResultInfo insertTag(Tag tag){

        ResultInfo resultInfo = new ResultInfo();
        tag.setCreatorId(34L);
        tag.setAuditState("PENDING");
        tag.setIsDelete((byte)0);
        tag.setCreateAt(new Date());
        tag.setUpdateAt(new Date());
        tag.setRefCount(0L);

        try{
            AssertUtil.isTrue(tagMapper.selectTagByName(tag.getName())!= null,"标签已存在！");
            AssertUtil.isTrue(tagMapper.insertTag(tag) < 1, "添加失败！");
            resultInfo.setMsg("添加成功！");
        } catch (ParamsException p){
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }
        return resultInfo;
    }

    public ResultInfo removeTag(Long id) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            AssertUtil.isTrue(tagMapper.deleteById(id) < 1,"删除失败！");
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

    public ResultInfo getTagGroup(){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setResult(tagMapper.getGroups());
        return resultInfo;
    }

    public ResultInfo getHotTags() {

        ResultInfo resultInfo = new ResultInfo();

        List<TagDTO> tagList = null;

        try{
            tagList = tagMapper.selectHotTags();
            AssertUtil.isTrue(tagList == null,"获取热门标签失败！");
        } catch (ParamsException p) {
            resultInfo.setCode(505);
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }
        resultInfo.setMsg("获取热门标签成功！");
        resultInfo.setResult(tagList);
        return resultInfo;
    }

    public ResultInfo getTagById(Long id) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            TagDTO tag = null;
            tag = tagMapper.selectTagById(id);
            resultInfo.setMsg("查询成功！");
            resultInfo.setResult(tag);
        }  catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("执行失败！");
        }

        return resultInfo;
    }
}
