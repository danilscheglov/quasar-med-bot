package dev.danilscheglov.quasarmedbot.model;

import dev.danilscheglov.quasarmedbot.model.enums.State;

public class UserState {

    private State state;
    private String name;

    public UserState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}