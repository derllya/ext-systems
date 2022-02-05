package edu.javacourse.city.domain;

public class PersonResponse {
    private boolean isRegistered;
    private boolean isTemporal;

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isTemporal() {
        return isTemporal;
    }

    public void setTemporal(boolean temporal) {
        isTemporal = temporal;
    }
}
