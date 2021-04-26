package io.quarkus.qe.vertx.web.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BladeRunner extends Person {
    private int VoightKampffTestAmount;
    private int retirements;
    private double dailyRate;

    public int getVoightKampffTestAmount() {
        return VoightKampffTestAmount;
    }

    public void setVoightKampffTestAmount(int voightKampffTestAmount) {
        VoightKampffTestAmount = voightKampffTestAmount;
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
