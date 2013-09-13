package rugal.common.security.userdetails;

import rugal.common.security.*;

/**
 * @author Luke Taylor
 * @version $Id: AccountStatusUserDetailsChecker.java 3558 2009-04-15 07:39:21Z
 * ltaylor $
 */
public class AccountStatusUserDetailsChecker implements UserDetailsChecker
{

    @Override
    public void check(UserDetails user) throws AccountStatusException
    {
        if (!user.isAccountNonLocked())
        {
            throw new LockedException();
        }

        if (!user.isEnabled())
        {
            throw new DisabledException("User is disabled", user);
        }

        if (!user.isAccountNonExpired())
        {
            throw new AccountExpiredException("User account has expired", user);
        }

        if (!user.isCredentialsNonExpired())
        {
            throw new CredentialsExpiredException(
                    "User credentials have expired", user);
        }
    }
}