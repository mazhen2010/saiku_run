package org.saiku.service.user;

import org.saiku.database.JdbcUserDAO;
import org.saiku.database.dto.SaikuUser;
import org.saiku.service.ISessionService;
import org.saiku.service.datasource.DatasourceService;
import org.saiku.service.datasource.IDatasourceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by bugg on 01/05/14.
 */
public class UserService implements IUserManager, Serializable {

    private JdbcUserDAO uDAO;

    private IDatasourceManager iDatasourceManager;
    private DatasourceService datasourceService;
    private ISessionService sessionService;
    private List<String> adminRoles;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void setAdminRoles( List<String> adminRoles ) {
        this.adminRoles = adminRoles;
    }

    public void setJdbcUserDAO(JdbcUserDAO jdbcUserDAO) {
        this.uDAO = jdbcUserDAO;
    }

    public void setiDatasourceManager(IDatasourceManager repo) {
        this.iDatasourceManager = repo;
    }


    public void setSessionService(ISessionService sessionService){
        this.sessionService = sessionService;
    }

    public DatasourceService getDatasourceService() {
        return datasourceService;
    }

    public void setDatasourceService(DatasourceService datasourceService) {
        this.datasourceService = datasourceService;
    }

    public SaikuUser addUser(SaikuUser u) {
        uDAO.insert(u);
        uDAO.insertRole(u);
        iDatasourceManager.createUser(u.getUsername());
        return u;
    }

    public boolean deleteUser(SaikuUser u) {
        uDAO.deleteUser(u);
        iDatasourceManager.deleteFolder("homes/home:" + u.getUsername());
        return true;
    }

    public SaikuUser setUser(SaikuUser u) {
        return null;
    }

    public List<SaikuUser> getUsers() {
        Collection users = uDAO.findAllUsers();
        List<SaikuUser> l = new ArrayList<>();
        for (Object user : users) {
            l.add((SaikuUser) user);

        }
        return l;
    }

    public SaikuUser getUser(int id) {
        return uDAO.findByUserId(id);
    }

    public String[] getRoles(SaikuUser user) {
        return uDAO.getRoles(user);
    }

    public void addRole(SaikuUser u) {
        uDAO.insertRole(u);
    }

    public void removeRole(SaikuUser u) {
        uDAO.deleteRole(u);
    }

    public void removeUser(String username) {
        SaikuUser u = getUser(Integer.parseInt(username));

        uDAO.deleteUser(username);

        iDatasourceManager.deleteFolder("homes/" + u.getUsername());

    }

    public SaikuUser updateUser(SaikuUser u, boolean updatepassword) {
        SaikuUser user = uDAO.updateUser(u, updatepassword);
        uDAO.updateRoles(u);

        return user;

    }

    public boolean isAdmin() {
        List<String> roles = (List<String> ) sessionService.getAllSessionObjects().get("roles");

        if(roles!=null) {
            return !Collections.disjoint(roles, adminRoles);
        }
        else{
            return true;
        }

    }

    public void checkFolders(){

        String username = (String ) sessionService.getAllSessionObjects().get("username");

        boolean home = true;
        if(username != null) {
          home = datasourceService.hasHomeDirectory(username);
        }
        if(!home){
            datasourceService.createUserHome(username);
        }



    }

    public List<String> getAdminRoles(){
        return adminRoles;
    }

    public String getActiveUsername() {
        try {
            return (String) sessionService.getSession().get("username");
        } catch (Exception e) {
            log.error("Could not fetch username");
        }
        return null;
    }

    @Override
    public String getSessionId() {
        try {
            return (String) sessionService.getSession().get("sessionid");
        } catch (Exception e) {
            log.error("Could not get sessionid: "+e.getMessage());
        }
        return null;
    }
}
