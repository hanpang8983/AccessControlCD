package com.github.soukie.model.RABCPolicy;

import com.github.soukie.model.ModelValues;
import com.github.soukie.model.RABCPolicy.subjects.*;
import com.github.soukie.model.database.DatabaseOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/19.
 */
public class RBACManagement {
    private DatabaseOperation databaseOperation;

    public RBACManagement() {
        this.databaseOperation = new DatabaseOperation(new Date().getTime());
        try {
            databaseOperation.initDatabaseConnection(ModelValues.DATABASE_MYSQL_PROPERTIES_FILE_PATH);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public RBACManagement(DatabaseOperation databaseOperation) {
        this.databaseOperation = databaseOperation;
    }

    /**
     * The method to add user record to database.
     *
     * @param user: User
     * @return 0: added failed; 1: added succeed; 2: the role had already existed
     */
    public int addUser(User user) {
        if (databaseOperation.queryUser(user.userName) != null) {
            return 2;
        }
        return databaseOperation.addUser(user.userName, user.password, user.userInfo, user.createdTime);
    }

    /**
     * The method to delete user by user name, and delete URA record related to user
     *
     * @param userName: user name
     * @return 0: deleted failed; 1: deleted succeed
     */
    public int deleteUser(String userName) {
        int deleteResult = databaseOperation.deleteUser(userName);
        if (deleteResult == 1) {
            databaseOperation.deleteURAByUserName(userName);
        }
        return deleteResult;
    }

    /**
     * The method to add role record to database.
     *
     * @param role: role
     * @return 0: added failed; 1: added succeed; 2: the role had already existed
     */
    public int addRole(Role role) {
        if (databaseOperation.queryRole(role.roleName) != null) {
            return 2;
        }
        return databaseOperation.addRole(role.roleName, role.roleInfo, role.createdTime);
    }

    /**
     * The method to delete role by role name and delete URA, PRA, RRA related to role.
     *
     * @param roleName: role name
     * @return 0: deleted failed; 1: deleted succeed
     */
    public int deleteRole(String roleName) {
        int deleteResult = databaseOperation.deleteRole(roleName);
        if (deleteResult == 1) {
            databaseOperation.deleteURAByRoleName(roleName);
            databaseOperation.deleteRRAByFatherRoleName(roleName);
            databaseOperation.deleteRRAByChildrenRoleName(roleName);
            databaseOperation.deletePRAByRoleName(roleName);
        }

        return deleteResult;
    }

    /**
     * The method to add permission record into database.
     *
     * @param permission: permission
     * @return 0: added failed; 1: added succeed; 2: the permission had already existed
     */
    public int addPermission(Permission permission) {
        if (databaseOperation.queryPermission(permission.permissionName) != null) {
            return 2;
        }
        return databaseOperation.addPermission(permission.permissionName, permission.permissionInfo, permission.createdTime);
    }

    /**
     * The method to delete permission by permission name, and delete PRA record related to permission.
     *
     * @param permissionName: permission name
     * @return 0: deleted failed; 1: deleted succeed
     */
    public int deletePermission(String permissionName) {
        int deleteResult = databaseOperation.deletePermission(permissionName);
        if (deleteResult == 1) {
            databaseOperation.deletePRAByPermissionName(permissionName);
        }
        return deleteResult;
    }

    /**
     * The method to add URA record into database.
     *
     * @param user: user
     * @param role: role
     * @return 0: added failed; 1: added succeed; 2: the URA record had already existed
     */
    public int createURA(User user, Role role) {
        if (databaseOperation.queryURA(user.userName, role.roleName) != null) {
            return 2;
        }
        user.addRoleName(role.roleName);
        return databaseOperation.addURA(user.userName, role.roleName);
    }

    public int deleteURA(User user, Role role) {
        int deleteResult = databaseOperation.deleteURA(user.userName, role.roleName);
        if (deleteResult == 1) {
            user.removeRoleName(role.roleName);
        }
        return deleteResult;
    }

    /**
     * The method to add PRA record into database.
     *
     * @param role:       role
     * @param permission: permission
     * @return 0: added failed; 1: added succeed; 2: the PRA record had already existed
     */
    public int createPRA(Role role, Permission permission) {
        if (databaseOperation.queryPRA(role.roleName, permission.permissionName) != null) {
            return 2;
        }
        role.addPermissionName(permission.permissionName);
        return databaseOperation.addPRA(role.roleName, permission.permissionName);
    }

    public int deletePRA(Role role, Permission permission) {
        int deleteResult = databaseOperation.deletePRA(role.roleName, permission.permissionName);
        if (deleteResult == 1) {
            role.removePermissionName(permission.permissionName);
        }
        return deleteResult;
    }

    /**
     * The method to add RRA record into database.
     *
     * @param fatherRole:   father role
     * @param childrenRole: children role
     * @return 0: added failed; 1: added succeed; 2: the RRA record had already existed
     */
    public int createRRA(Role fatherRole, Role childrenRole) {
        if (databaseOperation.queryRRA(fatherRole.roleName, childrenRole.roleName) != null) {
            return 2;
        }
        fatherRole.addChildrenRoleName(childrenRole.roleName);
        childrenRole.setFatherRoleName(fatherRole.roleName);
        return databaseOperation.addRRA(fatherRole.roleName, childrenRole.roleName);
    }

    public int deleteRRA(Role fatherRole, Role childrenRole) {
        int deleteResult = databaseOperation.deleteRRA(fatherRole.roleName, childrenRole.roleName);
        if (deleteResult == 1) {
            fatherRole.removeChildrenRoleName(childrenRole.roleName);
            childrenRole.setFatherRoleName(ModelValues.ADMIN_ROLE_NAME);
        }
        return deleteResult;
    }

    public ArrayList<String> queryRoleNamesOfUser(User user) {
        return user.getRolenames();
    }

    public ArrayList<String> queryRoleNamesOfUser(String userName) {
        return databaseOperation.queryURAByUserName(userName).stream().map(URA::getRoleName).
                collect(Collectors.toCollection(ArrayList<String>::new));
    }

    /**
     * The method to query first layer's permission of role.
     *
     * @param role: Role
     * @return ArrayList<String>
     */
    public ArrayList<String> queryDirectPermissionNamesOfRole(Role role) {
        return role.getChildrenRoleNames();
    }

    /**
     * The method to query all permissions of role recursively.
     *
     * @param roleName role name
     * @return ArrayList<String>
     */
    public ArrayList<String> queryPermissionNamesOfRoles(String roleName) {
        RRA rra = databaseOperation.queryRRAByChildrenRoleName(roleName);
        ArrayList<String> permissionNamesOfRoles = databaseOperation.queryPRAByRoleName(roleName).stream().
                map(PRA::getPermissionName).collect(Collectors.toCollection(ArrayList<String>::new));
        if (rra != null) {
            permissionNamesOfRoles.addAll(databaseOperation.queryPRAByRoleName(rra.getFatherRoleName()).
                    stream().map(PRA::getPermissionName).collect(Collectors.toCollection(ArrayList<String>::new)));
        }
        return permissionNamesOfRoles;
    }

    /**
     * The method to query all permissions of user recursively
     * @param userName: user name
     * @return ArrayList<String>
     */
    public ArrayList<String> queryPermissionNamesOfUser(String userName) {
        ArrayList<String> roleNamesOfUser = databaseOperation.queryURAByUserName(userName).stream().
                map(URA::getRoleName).collect(Collectors.toCollection(ArrayList<String>::new));
        ArrayList<String> permissionNamesOfUser = new ArrayList<>();
        for (String roleName :
                roleNamesOfUser) {
            permissionNamesOfUser.addAll(queryPermissionNamesOfRoles(roleName));
        }

        return permissionNamesOfUser;
    }

    /**
     * The method to query all users and init theirs' roles.
     * @return ArrayList<User>
     */
    public ArrayList<User> queryAllUsers() {
        ArrayList<User> allUsers = databaseOperation.queryAllUser();
        for (User user :
                allUsers) {
            user.addRoleNames(databaseOperation.queryURAByUserName(user.userName).stream().
                    map(URA::getRoleName).collect(Collectors.toCollection(ArrayList<String>::new)));
        }

        return allUsers;
    }

    /**
     * The method to query all roles and init theirs' father role and children roles.
     * @return ArrayList<Role>
     */
    public ArrayList<Role> queryAllRoles() {
        ArrayList<Role> allRoles = databaseOperation.queryAllRole();
        for (Role role :
                allRoles) {
            RRA rra = databaseOperation.queryRRAByChildrenRoleName(role.roleName);
            if (rra != null) {
                role.setFatherRoleName(rra.getFatherRoleName());
            }
            role.addChildrenRoleNames(databaseOperation.queryRRAByFatherRoleName(role.roleName).stream().
                    map(RRA::getChildrenRoleName).collect(Collectors.toCollection(ArrayList<String>::new)));
        }

        return allRoles;
    }

    /**
     * The method to query all permissions.
     * @return ArrayList<Permission>
     */
    public ArrayList<Permission> queryAllPermissions() {
        return databaseOperation.queryAllPermission();
    }
}
