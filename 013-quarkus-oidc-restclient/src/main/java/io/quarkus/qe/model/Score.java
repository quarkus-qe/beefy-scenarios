package io.quarkus.qe.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Score {
    private int teamA;
    private int teamB;

    public Score(){}

    public Score(int scoreTeamA, int scoreTeamB) {
        this.teamA = scoreTeamA;
        this.teamB = scoreTeamB;
    }

    public int getTeamA() {
        return teamA;
    }

    public void setTeamA(int teamA) {
        this.teamA = teamA;
    }

    public int getTeamB() {
        return teamB;
    }

    public void setTeamB(int teamB) {
        this.teamB = teamB;
    }

    @Override
    public String toString() {
        return "Score{" +
                "teamA=" + teamA +
                ", teamB=" + teamB +
                '}';
    }
}
