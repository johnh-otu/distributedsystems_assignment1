package org.example.utilities;

public class ServerMessage {
    public enum Type {
        COMPLETED,
        INVALID,
        ERROR,
        ROOM_LIST,
        GAME_STATE,
        SEND_HELP
    }
    
    private Type type;
    private String message_content;

    public ServerMessage(Type type, String message_content) {
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
