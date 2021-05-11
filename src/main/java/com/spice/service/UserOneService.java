package com.spice.service;

import com.spice.entity.po.UserOne;

/**
 * @author spice
 * @date 2021/05/11 15:54
 */
public interface UserOneService {

    /**
     * 截断表
     */
    void truncate();

    /**
     * 新增UserOne
     * propagation = required
     *
     * @param userOne UserOne
     */
    void addWithRequired(UserOne userOne);

    /**
     * 新增UserOne
     * propagation = requires_new
     *
     * @param userOne UserOne
     */
    void addWithRequiresNew(UserOne userOne);
}
