package com.spice.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author spice
 * @date 2021/05/11 15:49
 */
@Data
@TableName("user_one")
public class UserOne {

    /**
     * 主键
     * 设置主键的生成方式是；手动输入（方便测试）
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    public static final String ID = "id";
    public static final String NAME = "name";
}
