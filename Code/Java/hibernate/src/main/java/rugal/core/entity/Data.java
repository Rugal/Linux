/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.core.entity;

/**
 *
 * @author Rugal Bernstein
 */
public class Data
{

    private int uid;

    private String username;

    private String email;

    private String avatar;

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uid)
    {
        this.uid = uid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }
}