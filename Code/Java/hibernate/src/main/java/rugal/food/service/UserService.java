/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.service;

import rugal.common.page.Pagination;
import rugal.core.entity.Authentication;
import rugal.food.entity.User;

/**
 *
 * @author Rugal Bernstein
 */
public interface UserService
{

    //     @Transactional(propagation = Propagation.REQUIRED)
    User deleteById(String id);

    User findById(String id);

    Pagination getPage(int pageNo, int pageSize);

    User logon(Authentication authentication);

    void logout();

    User save(User bean);
    
}
