package org.example.utilities;

public class ClientMessage {
    public enum Type {
        LIST_ROOMS,
        JOIN_ROOM,
        LEAVE_ROOM,
        MAKE_ROOM,
        PLAY_MOVE,
        QUIT
    }
    
    private Type type;
    private String message_content;

    public ClientMessage(Type type, String message_content) {
        this.type = type;
        this. message_content = message_content;
    }

    public Type getType() {
        return type;
    }
    public String getContent() {
        return message_content;
    }
}
