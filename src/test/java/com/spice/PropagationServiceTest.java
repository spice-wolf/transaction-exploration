package com.spice;

import com.spice.service.PropagationService;
import com.spice.service.UserOneService;
import com.spice.service.UserTwoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import javax.annotation.Resource;

/**
 * @author spice
 * @date 2021/05/11 15:55
 */
@SpringBootTest
public class PropagationServiceTest {

    @Resource
    private UserOneService userOneService;

    @Resource
    private UserTwoService userTwoService;

    @Resource
    private PropagationService propagationService;

    /**
     * 在每次测试之前都清空表中的数据
     */
    @BeforeEach
    public void startup() {
        userOneService.truncate();
        userTwoService.truncate();
    }

    @Test
    public void requiredTest() {
        Assertions.assertThrows(RuntimeException.class, () -> propagationService.required());
    }

    @Test
    public void requiredTestWithTransaction() {
        Assertions.assertThrows(RuntimeException.class, () -> propagationService.requiredWithTransaction());
    }

    @Test
    public void requiredTestWithTransactionAndExceptionCatch() {
        // 会抛 UnexpectedRollbackException 异常，异常信息是：Transaction rolled back because it has been marked as rollback-only
        // 这是因为在 required 类型中，内部方法抛出了异常，事务进行了回滚，但是外部方法捕获了这个异常而正常结束，导致了这个异常
        // 实际上，正常业务是不推荐这样做的，这里只是为了测试 required 而这样写的
        Assertions.assertThrows(UnexpectedRollbackException.class, () -> propagationService.requiredWithTransactionAndExceptionCatch());
    }
}
