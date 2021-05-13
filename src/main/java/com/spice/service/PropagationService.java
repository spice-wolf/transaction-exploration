package com.spice.service;

/**
 * Spring事务的7种传播类型：
 * 1.PROPAGATION_REQUIRED     : 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择
 * 2.PROPAGATION_REQUIRES_NEW : 新建一个独立事务，如果当前存在事务，把当前事务挂起
 * 3.PROPAGATION_NESTED       : 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则新建一个事务
 *
 * 独立事务：不依赖于其他任何事务（当然也就不依赖于外部事务），它拥有自己的隔离范围、锁等等。当独立事务开始执行时，外部事务会被挂起；
 *         独立事务执行结束时，外部事务继续执行
 * 嵌套事务：它是已经存在事务的一个真正的子事务。嵌套事务开始执行时，它将取得一个 savepoint。如果这个嵌套事务失败，将回滚到此 savepoint。
 *         潜套事务是外部事务的一部分，只有外部事务结束后它才会被提交
 *
 * @author spice
 * @date 2021/05/11 20:56
 */
public interface PropagationService {

    /**
     * 普通地测试 required 类型
     */
    void required();

    /**
     * 测试 required 类型，并且外部方法加上了事务
     */
    void requiredWithTransaction();

    /**
     * 测试 required 类型，并且外部方法加上了事务，而且捕获了内部方法产生的异常
     */
    void requiredWithTransactionAndExceptionCatch();

    /**
     * 普通地测试 requires_new 类型
     */
    void requiresNew();

    /**
     * 测试 requires_new 类型，并且外部方法加上了事务
     */
    void requiresNewWithTransaction();

    /**
     * 测试 requires_new 类型，并且外部方法加上了事务，同时加入了一个 required 类型
     */
    void requiresNewWithTransactionAndRequired();

    /**
     * 测试 requires_new 类型，并且外部方法加上了事务，同时加入了一个 required 类型，而且捕获了内部方法产生的异常
     */
    void requiresNewWithTransactionAndRequiredAndExceptionCatch();

    /**
     * 普通地测试 nested 类型
     */
    void nested();

    /**
     * 测试 nested 类型，并且外部方法中抛出了一个异常
     */
    void nestedWithExceptionThrows();

    /**
     * 测试 nested 类型，并且外部方法加上了事务
     */
    void nestedWithTransaction();

    /**
     * 测试 nested 类型，并且外部方法加上了事务，而且捕获了内部方法产生的异常
     */
    void nestedWithTransactionAndExceptionCatch();
}
