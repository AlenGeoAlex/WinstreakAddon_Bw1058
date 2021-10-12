package me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces;

import me.alen_alex.winstreakaddon.winstreakaddonbw1058.object.Winstreak;

import java.util.UUID;

public interface DataStorage {

    boolean initTables();

    boolean init();

    boolean isUserRegistered(UUID playerUUID);

    boolean registerUser(UUID playerUUID);

    Winstreak fetchUser(UUID playerUUID);

    void addStreak(UUID playerUUID);

    void closeConnection();

    void saveUserData(Winstreak obj);

    void resetStreak(UUID uuid);

    boolean setCurrent(UUID playerUUID,int currentStreak);

    boolean setHighest(UUID playerUUID, int highestStreak);

}
