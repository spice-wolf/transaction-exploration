package com.spice.service;

import com.spice.entity.po.UserTwo;

/**
 * @author spice
 * @date 2021/05/11 15:54
 */
public interface UserTwoService {

    /**
     * 截断表
     */
    void truncate();

    /**
     * 新增UserTwo，并在方法内部抛出异常
     * propagation = required
     *
     * @param userTwo UserTwo
     */
    void addWithRequiredAndException(UserTwo userTwo);

    /**
     * 新增UserTwo，并在方法内抛出异常
     * propagation = requires_new
     *
     * @param userTwo UserTwo
     */
    void addWithRequiresNewAndException(UserTwo userTwo);

    /**
     * 新增UserTwo
     * propagation = nested
     *
     * @param userTwo UserTwo
     */
    void addWithNested(UserTwo userTwo);

    /**
     * 新增UserTwo，并在方法内抛出异常
     * propagation = nested
     *
     * @param userTwo UserTwo
     */
    void addWithNestedAndException(UserTwo userTwo);
}
