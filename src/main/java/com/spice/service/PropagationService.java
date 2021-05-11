package com.spice.service;

/**
 * Spring事务的7种传播类型：
 * 1.PROPAGATION_REQUIRED : 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择
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
}
