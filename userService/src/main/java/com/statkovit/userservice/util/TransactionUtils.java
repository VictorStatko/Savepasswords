package com.statkovit.userservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionUtils {
    private final PlatformTransactionManager platformTransactionManager;

    public <T> T executeInTransaction(Supplier<T> function) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);

        return transactionTemplate.execute(transactionStatus -> function.get());
    }

    public void executeInTransactionWithoutResult(Runnable runnable) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                runnable.run();
            }
        });
    }
}
