package pl.lapme.adoption.model;

import pl.lapme.adoption.model.types.EventType;

public final class EventFactory {
    private EventFactory() {
    }
    public static ApplicationEvent loginSuccess(String username, String address) {
        return ApplicationEvent.builder().event("Login success. IP: " + address).msg("User: " + username).eventType(EventType.LOGIN).build();
    }

    public static ApplicationEvent loginFailed(String username, String password, String address) {
        return ApplicationEvent.builder().event("Login failed. IP: " + address).msg("User: " + username + " used password: " + password).eventType(EventType.LOGIN).build();
    }
}
