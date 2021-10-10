package me.alen_alex.winstreakaddon.winstreakaddonbw1058.interfaces;

import de.leonhard.storage.Yaml;

public interface YamlFiles {

    void init();

    public void loadFile();

    public Yaml getFile();

    public void reloadFile();

    public String getFileVersion();


}
