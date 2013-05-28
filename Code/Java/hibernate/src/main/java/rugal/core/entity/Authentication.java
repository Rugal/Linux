/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.core.entity;

/**
 *
 * @author Rugal Bernstein
 */
public class Authentication
{

    private String id;

    private String password;

    public Authentication(String id, String password)
    {
        this.id = id;
        this.password = password;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isInvalide()
    {
        if (id != null && password != null) {
            return true;
        } else {
            return false;
        }
    }
}
