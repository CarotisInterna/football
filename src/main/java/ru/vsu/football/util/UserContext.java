package ru.vsu.football.util;

import ru.vsu.football.domain.UserRoleId;


public class UserContext {
    private static UserRoleId role;

    public static void setRole(UserRoleId userRoleId) {
        role = userRoleId;
    }

    public static UserRoleId getUserRole() {
        return role;
    }

    public static boolean isManager() {
        return UserRoleId.MANAGER.equals(role);
    }

    public static boolean isAdmin() {
        return UserRoleId.ADMIN.equals(role);
    }
}
