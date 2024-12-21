package io.ypolin.slideshow.event;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GlobalEvent {
    public static final String MSG_FORMAT = "EVENT_TYPE: %s. MSG:%s. PAYLOAD: %s";
    public enum EventType {
        IMAGE_ADDED("Image was registered in the app"),
        IMAGE_DELETED("Image was deleted"),
        PROOF_OF_PLAY("Image was played"),

        PAUSED("Slideshow was paused"),
        IMAGE_VOTED("Image was voted");
        private String message;

        EventType(String message) {
            this.message = message;
        }
    }

    private EventType type;
    private Map<String, Object> payload = new HashMap<>();

    public String getMessage(){
        return String.format(MSG_FORMAT,type.name(), type.message, payload);
    }


}
