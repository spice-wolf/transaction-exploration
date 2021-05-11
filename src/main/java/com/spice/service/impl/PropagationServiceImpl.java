package com.spice.service.impl;

import com.spice.entity.po.UserOne;
import com.spice.entity.po.UserTwo;
import com.spice.service.PropagationService;
import com.spice.service.UserOneService;
import com.spice.service.UserTwoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author spice
 * @date 2021/05/11 20:59
 */
@Service
public class PropagationServiceImpl implements PropagationService {

    @Resource
    private UserOneService userOneService;

    @Resource
    private UserTwoService userTwoService;

    private final UserOne userGosling = new UserOne().setName("Gosling");
    private final UserTwo userLinus = new UserTwo().setName("Linus");
    private final UserTwo userDennis = new UserTwo().setName("Dennis");

    /**
     * Gosling : 被正常插入
     * Linus   : 被正常插入
     * Dennis  : 不会被插入，因为 addWithRequiredAndException 方法中抛出了一个异常，导致了事务回滚
     */
    @Override
    public void required() {
        userOneService.addWithRequired(userGosling);

        userTwoService.addWithRequired(userLinus);

        userTwoService.addWithRequiredAndException(userDennis);
    }

    /**
     * Gosling : 不会被插入，因为 addWithRequired 方法的事务加入到了 requiredTestWithTransaction 方法的事务中，
     *           而 addWithRequiredAndException 方法抛出了一个异常，导致 requiredTestWithTransaction 方法也抛出异常而进行了回滚
     * Linus   : 不会被插入，原因和 Gosling 的一样
     * Dennis  : 不会被插入，原因和 Gosling 的一样
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requiredWithTransaction() {
        userOneService.addWithRequired(userGosling);

        userTwoService.addWithRequired(userLinus);

        userTwoService.addWithRequiredAndException(userDennis);
    }

    /**
     * 请注意!!!
     * Gosling : 不会被插入，因为 addWithRequired 方法的事务加入到了 requiredTestWithTransactionAndExceptionCatch 方法的事务中，
     *           addWithRequiredAndException 方法抛出的异常被捕获了，但即使 requiredTestWithTransactionAndExceptionCatch 方法
     *           没有感知到异常，整个事务仍然会回滚
     * Linus   : 不会被插入，原因和 Gosling 的一样
     * Dennis  : 不会被插入，原因和 Gosling 的一样
     *
     * 由 requiredWithTransaction 方法和 requiredWithTransactionAndExceptionCatch 方法的测试结果可见，
     * 所有传播类型是 REQUIRED 的内部方法和它们的外部方法均处于同一个事务中，其中任何一个方法回滚了，整个事务都会被回滚。
     * 假如现在把 addWithRequiredAndException 方法的异常在它内部进行捕获，则三条数据都会被插入。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requiredWithTransactionAndExceptionCatch() {
        userOneService.addWithRequired(userGosling);

        userTwoService.addWithRequired(userLinus);

        try {
            userTwoService.addWithRequiredAndException(userDennis);
        } catch (RuntimeException e) {
            System.out.println("外部方法捕获了一个异常");
        }
    }
}
