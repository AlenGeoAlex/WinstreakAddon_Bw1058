package me.alen_alex.winstreakaddon.winstreakaddonbw1058.utils;

public enum PermissionData {

    NOTIFICATION_PERMISSION("wsaddon.notify"),
    USE_PERMISSION("wsaddon.use"),
    ADMIN_PERMISSION("wsaddon.admin");

    public final String permission;

    PermissionData(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}


