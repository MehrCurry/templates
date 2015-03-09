package de.gzockoll.prototype.templates.ui.util;

import com.vaadin.ui.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorHandler {

    public static void handleError(Exception e, String message) {
        log.error(message + e);
        Notification.show(message + e.getCause().getMessage(), Notification.Type.ERROR_MESSAGE);
    }

    public static void handleError(Exception e) {
        log.error("An error occured: " + e);
        Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
    }
}
