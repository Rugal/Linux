/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.core.service;

import rugal.core.entity.Authentication;
import rugal.food.entity.User;

/**
 *
 * @author Rugal Bernstein
 */
public interface AuthenticationService
{

    User authenticate(Authentication authentication);
    
}
