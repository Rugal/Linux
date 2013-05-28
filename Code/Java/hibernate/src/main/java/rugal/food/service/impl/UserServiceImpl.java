/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.service.impl;

import rugal.food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.core.entity.Authentication;
import rugal.core.service.AuthenticationService;
import rugal.food.dao.UserDao;
import rugal.food.entity.User;

/**
 *
 * @author Rugal Bernstein
 */
@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationService authenticationService;

    public void setAuthenticationService(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    @Override
    public User save(User bean)
    {
        return userDao.save(bean);
    }

//     @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User deleteById(String id)
    {
        return userDao.deleteById(id);
    }

    @Override
    public User findById(String id)
    {
        return userDao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize)
    {
        return userDao.getPage(pageNo, pageSize);
    }

    @Override
    public User logon(Authentication authentication)
    {
        //check id&pwd
        if (authentication.isInvalide()) {
            User u = authenticationService.authenticate(authentication);
            User innerUser = userDao.findById(u.getUid());
            if (innerUser == null) {
                userDao.save(u);
                return u;
            }
            return innerUser;
        }
        return null;
    }

    @Override
    public void logout()
    {
    }
}
