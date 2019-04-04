package com.example.android.ffxivhelper;

public class CollectibleObject {
    private final String collectibleID;
    private final String collectibleName;
    private final int collectibleType;

    public CollectibleObject(String ID, int type) {
        collectibleType = 0; //TODO: support multiple types
        collectibleID = ID;
        collectibleName = "Dummy Mount";
    }

    public String getCollectibleID() {
        return collectibleID;
    }

    public String getCollectibleName() {
        return collectibleName;
    }
}
