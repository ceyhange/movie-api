package com.gencay.movie.role;

public interface IRoleService {

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);
}
