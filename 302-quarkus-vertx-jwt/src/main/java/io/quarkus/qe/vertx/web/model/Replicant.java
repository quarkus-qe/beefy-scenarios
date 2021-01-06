package io.quarkus.qe.vertx.web.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Replicant extends Person {
    private String model;
    private long liveSpanYears;
    private String[] specialAbilities;
    private boolean telepathy;
    private boolean fugitive;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getLiveSpanYears() {
        return liveSpanYears;
    }

    public void setLiveSpanYears(long liveSpanYears) {
        this.liveSpanYears = liveSpanYears;
    }

    public String[] getSpecialAbilities() {
        return specialAbilities;
    }

    public void setSpecialAbilities(String[] specialAbilities) {
        this.specialAbilities = specialAbilities;
    }

    public boolean isTelepathy() {
        return telepathy;
    }

    public void setTelepathy(boolean telepathy) {
        this.telepathy = telepathy;
    }

    public boolean isFugitive() {
        return fugitive;
    }

    public void setFugitive(boolean fugitive) {
        this.fugitive = fugitive;
    }
}
