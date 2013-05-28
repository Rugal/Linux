/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.core.service.impl;

import rugal.core.service.AuthenticationService;
import java.util.Date;
import org.springframework.stereotype.Service;
import rugal.common.util.HttpSender;
import rugal.core.entity.Authentication;
import rugal.core.entity.UserCenter;
import rugal.food.entity.User;

/**
 *
 * @author Rugal Bernstein
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService
{

    private final String URL = "http://user.zjut.com/api.php?app=member&action=login&username=:username&password=:password";

    private String generateURL(Authentication authentication)
    {
        String requestURL = URL.toString();
        requestURL = requestURL.replaceAll(":username", authentication.getId());
        requestURL = requestURL.replaceAll(":password", authentication.getPassword());
        return requestURL;
    }

    @Override
    public User authenticate(Authentication authentication)
    {
        String requestURL = generateURL(authentication);
        HttpSender sender = new HttpSender(requestURL);
        UserCenter uc = sender.execute();
        if (uc.getState().equals("success")) {
            return uc.encapsulate();
        }
        return null;
    }
}
