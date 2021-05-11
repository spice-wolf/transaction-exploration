package com.spice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spice.entity.po.UserTwo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spice
 * @date 2021/05/11 15:53
 */
@Mapper
public interface UserTwoMapper extends BaseMapper<UserTwo> {

    /**
     * 截断表
     */
    void truncate();
}
