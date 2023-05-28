package com.majiang.community.service;

import cn.hutool.core.util.StrUtil;
import com.majiang.community.base.ResultInfo;
import com.majiang.community.exceptions.ParamsException;
import com.majiang.community.mapper.CarouselMappper;
import com.majiang.community.pojo.Carousel;
import com.majiang.community.utils.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Author wxh
 * 2023/5/17 20:34
 */
@Service
public class CarouselService {

    @Resource
    private CarouselMappper carouselMappper;

    public ResultInfo addCarousel(Carousel carousel) {

        ResultInfo resultInfo = new ResultInfo();
        preCheck(carousel);

        carousel.setCreateAt(new Date());
        carousel.setUpdateAt(new Date());
        carousel.setIsDelete((byte) 0);
        carousel.setAuditState("PENDING");

        try{
            AssertUtil.isTrue(carouselMappper.insertCarousel(carousel) < 1, "新增轮播图失败！");
        } catch (ParamsException p){
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(505);
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }
        resultInfo.setMsg("添加成功！");
        return resultInfo;
    }

    public void preCheck(Carousel carousel){
        AssertUtil.isTrue(StrUtil.isBlank(carousel.getType()),"轮播图类型不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(carousel.getImgUrl()),"图片地址不能为空！");
        AssertUtil.isTrue(StrUtil.isBlank(carousel.getActUrl()),"跳转链接不能为空！");
        AssertUtil.isTrue(carousel.getStartAt() == null, "生效时间不能为空！");
        AssertUtil.isTrue(carousel.getEndAt() == null, "失效时间不能为空！");
        AssertUtil.isTrue(carousel.getCreator() == null, "创建人编号不能为空！");
    }

    public ResultInfo getCarouselPage(String auditState,String Type,String title,Integer pageNum,Integer pageSize){
        ResultInfo resultInfo = new ResultInfo();

        HashMap<Object, Object> map = new HashMap<>();
        try{
            if(pageNum != null && pageSize != null){
                Integer offset = (pageNum - 1) * pageSize;
                List<Carousel> carouselList = null;
                List<String> typeList = null;
                Long total = carouselMappper.selectTotal(auditState, Type, title);
                typeList = carouselMappper.selectTypeList();
                carouselList = carouselMappper.selectCarouselPage(auditState, Type, title, offset, pageSize);
                map.put("typeList",typeList);
                map.put("carouselList",carouselList);
                map.put("total",total);
            } else {
                throw new ParamsException("页号和页大小不能为空！");
            }
        } catch (ParamsException p){
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(505);
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("系统错误！");
        }
        resultInfo.setMsg("获取轮播图列表成功！");
        resultInfo.setResult(map);
        return resultInfo;
    }

    public ResultInfo changeAuditState(Carousel carousel) {

        ResultInfo resultInfo = new ResultInfo();

        Long id = carousel.getId();
        String newState = carousel.getAuditState();


        try{
            AssertUtil.isTrue(id == null || StrUtil.isBlank(newState),"参数错误！");
            AssertUtil.isTrue(carouselMappper.updateAuditState(id,newState) < 1,"审核失败！");
        } catch (ParamsException p) {
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(505);
            resultInfo.setMsg("审核异常！");
        }
        resultInfo.setMsg("审核完成！");
        return resultInfo;
    }

    public ResultInfo removeCarousel(Long id) {

        ResultInfo resultInfo = new ResultInfo();
        try {
            AssertUtil.isTrue(carouselMappper.deleteById(id) < 1,"删除失败！");
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

    public ResultInfo getCarouselList() {

        ResultInfo resultInfo = new ResultInfo();

        try {
            List<Carousel> carouselList = null;
            carouselList = carouselMappper.selectCarouselList();
            AssertUtil.isTrue(carouselList == null,"轮播图列表为空！");
            resultInfo.setMsg("获取轮播图列表成功！");
            resultInfo.setResult(carouselList);
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
}
