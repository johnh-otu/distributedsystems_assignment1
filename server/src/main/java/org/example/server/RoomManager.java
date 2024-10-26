package org.example.server;

import java.util.HashMap;

public class RoomManager {
    
    private HashMap<String, Room> roomMap;
    
    public RoomManager() {
        roomMap = new HashMap<>();
    }

    public synchronized boolean addRoom(Room room, String id) {
        if (roomMap.containsKey(id)) {
            return false;
        }
        roomMap.put(id, room);
        return true;
    }
    public synchronized boolean delRoom(String id) {
        if (!roomMap.containsKey(id)) {
            return false;
        }
        roomMap.remove(id);
        return true;
    }
    public synchronized HashMap<String, Room> getMap() {
        return new HashMap<>(roomMap);
    }
    public synchronized boolean hasId(String id) {
        return roomMap.containsKey(id);
    }
    public synchronized Room getRoom(String id) {
        return roomMap.get(id);
    }


}
