package io.quarkus.qe.quartz;

import java.time.Instant;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

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
