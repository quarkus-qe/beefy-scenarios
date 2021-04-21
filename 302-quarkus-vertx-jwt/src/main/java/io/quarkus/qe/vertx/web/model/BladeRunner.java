package io.quarkus.qe.vertx.web.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BladeRunner extends Person {
    private int voightKampffTestAmount;
    private int retirements;
    private double dailyRate;

    public int getVoightKampffTestAmount() {
        return voightKampffTestAmount;
    }

    public void setVoightKampffTestAmount(int voightKampffTestAmount) {
        this.voightKampffTestAmount = voightKampffTestAmount;
    }

    public int getRetirements() {
        return retirements;
    }

    public void setRetirements(int retirements) {
        this.retirements = retirements;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }
}
