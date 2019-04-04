package com.example.android.ffxivhelper;

public class ResultObject {
    public final String characterName;
    public final String characterID;

    public ResultObject(String name, String ID) {
        characterName = name;
        characterID = ID;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getCharacterID() {
        return characterID;
    }
}
