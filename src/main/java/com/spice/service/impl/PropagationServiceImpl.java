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
    private final UserTwo userDennis = new UserTwo().setName("Dennis");

    /**
     * Gosling : 被正常插入
     * Dennis  : 不会被插入，因为 addWithRequiredAndException 方法中抛出了一个异常，导致了事务回滚
     */
    @Override
    public void required() {
        userOneService.addWithRequired(userGosling);

        userTwoService.addWithRequiredAndException(userDennis);
    }

    /**
     * Gosling : 不会被插入，因为 addWithRequired 方法的事务加入到了 requiredWithTransaction 方法的事务中，
     *           而 addWithRequiredAndException 方法抛出了一个异常，导致 requiredWithTransaction 方法也抛出异常而进行了回滚
     * Dennis  : 不会被插入，原因和 Gosling 的一样
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requiredWithTransaction() {
        userOneService.addWithRequired(userGosling);

        userTwoService.addWithRequiredAndException(userDennis);
    }

    /**
     * 请注意!!!
     * Gosling : 不会被插入，因为 addWithRequired 方法的事务加入到了 requiredWithTransactionAndExceptionCatch 方法的事务中，
     *           addWithRequiredAndException 方法抛出的异常被捕获了，但即使 requiredWithTransactionAndExceptionCatch 方法
     *           没有感知到异常，整个事务仍然会回滚
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

        try {
            userTwoService.addWithRequiredAndException(userDennis);
        } catch (RuntimeException e) {
            System.out.println("外部方法捕获了一个异常");
        }
    }

    /**
     * Gosling : 被正常插入
     * Dennis  : 不会被插入，因为 addWithRequiresNewAndException 方法开启了自己的单独事务，并且内部抛出了异常，导致了事务回滚
     */
    @Override
    public void requiresNew() {
        userOneService.addWithRequiresNew(userGosling);

        userTwoService.addWithRequiresNewAndException(userDennis);
    }

    /**
     * Gosling : 被正常插入，因为即使 addWithRequiresNewAndException 方法抛出了异常，requiresNewWithTransaction 方法感知到异常并产生了回滚，
     *           但是因为 addWithRequiresNew 方法有自己单独的事务，并且已经提交了，所以 requiresNewWithTransaction 方法的回滚并不会影响 Gosling 的插入
     * Dennis  : 不会被插入，原因和{@link #requiresNew()}中的 Dennis 的一样
     *
     * 由此可见，传播类型是 REQUIRES_NEW 的内部方法会各自开启自己的事务，并且互不干扰
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requiresNewWithTransaction() {
        userOneService.addWithRequiresNew(userGosling);

        userTwoService.addWithRequiresNewAndException(userDennis);
    }

    /**
     * Gosling : 被正常插入，原因和{@link #requiresNewWithTransaction()}的 Gosling 的一样
     * Dennis  : 不会被插入，原因和{@link #requiresNew()}中的 Dennis 的一样
     * Linus   : 不会被插入，因为 addWithRequired 方法的事务被加入到了 requiresNewWithTransactionAndRequired 方法的事务中，
     *           而 addWithRequiresNewAndException 方法抛出了异常导致 requiresNewWithTransactionAndRequired 方法的事务进行了回滚
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requiresNewWithTransactionAndRequired() {
        userOneService.addWithRequiresNew(userGosling);

        userTwoService.addWithRequiresNewAndException(userDennis);

        UserOne userLinus = new UserOne().setName("Linus");
        userOneService.addWithRequired(userLinus);
    }

    /**
     * Gosling : 被正常插入，原因和{@link #requiresNewWithTransaction()}的 Gosling 的一样
     * Dennis  : 不会被插入，原因和{@link #requiresNew()}中的 Dennis 的一样
     * Linus   : 被正常插入，因为 addWithRequired 方法的事务被加入到了 requiresNewWithTransactionAndRequired 方法的事务中，
     *           addWithRequiresNewAndException 方法抛出了异常，但是被捕获了，requiresNewWithTransactionAndRequired 方法不会感知
     *           到这个异常，所以并不会进行回滚操作
     *
     * 由此可见，传播类型是 REQUIRES_NEW 的内部方法的事务和外部方法的事务互不干扰
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requiresNewWithTransactionAndRequiredAndExceptionCatch() {
        userOneService.addWithRequiresNew(userGosling);

        try {
            userTwoService.addWithRequiresNewAndException(userDennis);
        } catch (RuntimeException e) {
            System.out.println("外部方法捕获了一个异常");
        }

        UserOne userLinus = new UserOne().setName("Linus");
        userOneService.addWithRequired(userLinus);
    }

    /**
     * Gosling : 被正常插入，因为 nested 方法没有事务，所以 addWithNested 方法新建一个属于自己的事务，虽然 addWithNestedAndException 方法
     *           有抛出一个异常，但是并不影响 addWithNested 方法的事务
     * Dennis  : 不会被插入，因为 nested 方法没有事务，所以 addWithNestedAndException 方法新建一个属于自己的事务，然后内部抛了一个异常
     *           导致了事务回滚
     */
    @Override
    public void nested() {
        userOneService.addWithNested(userGosling);

        userTwoService.addWithNestedAndException(userDennis);
    }

    /**
     * Gosling : 被正常插入，因为 nestedWithExceptionThrows 方法没有事务，所以 addWithNested 方法新建一个属于自己的事务，
     *           虽然 nestedWithExceptionThrows 方法中抛出了一个异常，但是不影响 addWithNested 方法的独立事务
     * Linus   : 被正常插入，原因和 Gosling 的一样
     */
    @Override
    public void nestedWithExceptionThrows() {
        userOneService.addWithNested(userGosling);

        UserTwo userLinus = new UserTwo().setName("Linus");
        userTwoService.addWithNested(userLinus);
        throw new RuntimeException();
    }

    /**
     * Gosling : 不会被插入，因为 nestedWithTransaction 方法开启了事务，所以 addWithNested 方法也会开启一个该事务的子事务，
     *           addWithNestedAndException 方法抛出了异常，nestedWithTransaction 方法感知到异常并进行了回滚，所以子事务也会进行回滚
     * Dennis  : 不会被插入，因为 addWithNestedAndException 方法中抛出了异常，导致了自己的事务（指 addWithNestedAndException 方法的事务）进行了回滚，
     *           注意 Dennis 的原因和 Gosling 的原因是不一样的，可以接着看下面的{@link #nestedWithTransactionAndExceptionCatch()}方法
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void nestedWithTransaction() {
        userOneService.addWithNested(userGosling);

        userTwoService.addWithNestedAndException(userDennis);
    }

    /**
     * Gosling : 被正常插入，因为 nestedWithTransactionAndExceptionCatch 方法开启了事务，addWithNested 方法也会开启一个该事务的子事务，
     *           nestedWithTransactionAndExceptionCatch 方法捕获了异常，即没有感知到异常，事务正常提交了，因而 addWithNested 方法的
     *           事务也被正常提交了
     * Dennis  : 不会被插入，因为 addWithNestedAndException 方法抛出了异常，事务被回滚了，但是这个事务是嵌套事务中的子事务，并不会导致外部事务回滚
     *
     * 由此可见，嵌套事务中的外部事务回滚会导致它的所有子事务都回滚；嵌套事务中的子事务回滚不会导致它的外部事务回滚，这点和 required 类型不同！
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void nestedWithTransactionAndExceptionCatch() {
        userOneService.addWithNested(userGosling);

        try {
            userTwoService.addWithNestedAndException(userDennis);
        } catch (RuntimeException e) {
            System.out.println("外部方法捕获了一个异常");
        }
    }
}
