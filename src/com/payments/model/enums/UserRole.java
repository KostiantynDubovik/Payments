package com.payments.model.enums;

/**
 * @author Kostiantyn Dubovik
 */
public enum UserRole {

    ADMIN(1), USER(2), GUEST(3);

    final int roleId;

    UserRole (int roleId){
        this.roleId = roleId;
    }

    public static UserRole chooseRole(int roleId) {
        UserRole result = GUEST;
        for (UserRole role : UserRole.values()) {
            if (role.roleId==roleId){
                result = role;
            }
        }
        return result;
    }

    public int getRoleId(){
        return roleId;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
