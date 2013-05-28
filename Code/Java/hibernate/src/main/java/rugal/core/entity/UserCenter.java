/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.core.entity;

import rugal.food.entity.User;

/**
 *
 * @author Rugal Bernstein
 */
public class UserCenter
{

    private String state;

    private String info;

    private Data data;

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public Data getData()
    {
        return data;
    }

    public void setData(Data data)
    {
        this.data = data;
    }

    public User encapsulate()
    {
        User u = new User();
        u.setUid(data.getUsername());
        return u;
    }
}
