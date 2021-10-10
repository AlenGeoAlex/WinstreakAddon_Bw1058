package me.alen_alex.winstreakaddon.winstreakaddonbw1058.data.database;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T> {
    void accept(T t) throws SQLException;
}
