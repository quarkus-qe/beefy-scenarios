package io.quarkus.qe.quartz;

import java.time.Instant;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class ExecutionService {
    @Transactional(value = TxType.MANDATORY)
    public void addExecution(String owner) {
        ExecutionEntity entity = new ExecutionEntity();
        entity.seconds = Instant.now().getEpochSecond();
        entity.owner = owner;
        entity.persist();
    }
}
