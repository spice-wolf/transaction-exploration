package com.spice.service.impl;

import com.spice.dao.UserOneMapper;
import com.spice.entity.po.UserOne;
import com.spice.service.UserOneService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author spice
 * @date 2021/05/11 15:54
 */
@Service
public class UserOneServiceImpl implements UserOneService {

    @Resource
    private UserOneMapper userOneMapper;

    @Override
    public void truncate() {
        userOneMapper.truncate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addWithRequired(UserOne userOne) {
        userOneMapper.insert(userOne);
    }
}
