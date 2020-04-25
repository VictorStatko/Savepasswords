package com.statkovit.authorizationservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionUtils {

    private final JpaTransactionManager jpaTransactionManager;

    public <T> T executeInTransaction(Supplier<T> function) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(jpaTransactionManager);

        return transactionTemplate.execute(transactionStatus -> function.get());
    }

    public void executeInTransactionWithoutResult(Runnable runnable) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(jpaTransactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                runnable.run();
            }
        });
    }
}
