package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 移动端-客户体验预约套餐查询管理页面
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询套餐列表
     */
    @RequestMapping(value = "/getSetmeal", method = RequestMethod.POST)
    public Result findPage() {
        List<Setmeal> setmealList = setmealService.findAll();
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmealList);
    }


    /**
     * 根据套餐id查询套餐数据（套餐数据+检查组数据+检查项数据）
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(Integer id) {
        Setmeal setmeal = setmealService.findById(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

}
