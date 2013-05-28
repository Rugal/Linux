/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author Administrator
 */
@Entity
@Table(catalog = "food", schema = "")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")})
public class User implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(nullable = false, length = 20)
    private String uid;

    @Basic(optional = false)
    @NotNull
    @Column(name = "last_login", nullable = false)
    private Long lastLogin;

    @Basic(optional = false)
    @NotNull
    @Column(name = "activated", nullable = false,columnDefinition="bit")
    private boolean activated = false;

    @Basic(optional = false)
    @NotNull
    @Column(name = "online", nullable = false,columnDefinition="bit")
    private boolean online = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uid")
    private List<Indent> indentList;

    public User()
    {
    }

    public User(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public Long getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(Long lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    public List<Indent> getIndentList()
    {
        return indentList;
    }

    public void setIndentList(List<Indent> indentList)
    {
        this.indentList = indentList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "rugal.foods.entity.User[ uid=" + uid + " ]";
    }
}
