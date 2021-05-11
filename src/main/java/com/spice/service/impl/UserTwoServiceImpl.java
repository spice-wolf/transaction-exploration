package com.spice.service.impl;

import com.spice.dao.UserTwoMapper;
import com.spice.entity.po.UserTwo;
import com.spice.service.UserTwoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author spice
 * @date 2021/05/11 15:54
 */
@Service
public class UserTwoServiceImpl implements UserTwoService {

    @Resource
    private UserTwoMapper userTwoMapper;

    @Override
    public void truncate() {
        userTwoMapper.truncate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addWithRequiredAndException(UserTwo userTwo) {
        userTwoMapper.insert(userTwo);
        throw new RuntimeException();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void addWithRequiresNewAndException(UserTwo userTwo) {
        userTwoMapper.insert(userTwo);
        throw new RuntimeException();
    }
}
