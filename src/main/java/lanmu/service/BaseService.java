package lanmu.service;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import lanmu.entity.db.User;

public class BaseService {

    @Context
    protected SecurityContext securityContext;

    /**
     * 从上下文中直接获取自己的信息
     *
     * @return User
     */
    protected User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
