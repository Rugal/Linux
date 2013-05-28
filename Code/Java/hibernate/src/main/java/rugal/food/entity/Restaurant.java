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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(catalog = "food", schema = "")
@NamedQueries({
    @NamedQuery(name = "Restaurant.findAll", query = "SELECT r FROM Restaurant r")})
public class Restaurant implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer rid;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(nullable = false, length = 50)
    private String name;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="电话/传真格式无效, 应为 xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(nullable = false, length = 15)
    private String phone;

    @Size(max = 15)
    @Column(length = 15)
    private String telephone;

    @Size(max = 50)
    @Column(length = 50)
    private String address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rid")
    private List<Menu> menuList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rid")
    private List<OrderLog> orderLogList;

    public Restaurant()
    {
    }

    public Restaurant(Integer rid)
    {
        this.rid = rid;
    }

    public Restaurant(Integer rid, String name, String phone)
    {
        this.rid = rid;
        this.name = name;
        this.phone = phone;
    }

    public Integer getRid()
    {
        return rid;
    }

    public void setRid(Integer rid)
    {
        this.rid = rid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public List<Menu> getMenuList()
    {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList)
    {
        this.menuList = menuList;
    }

    public List<OrderLog> getOrderLogList()
    {
        return orderLogList;
    }

    public void setOrderLogList(List<OrderLog> orderLogList)
    {
        this.orderLogList = orderLogList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (rid != null ? rid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Restaurant)) {
            return false;
        }
        Restaurant other = (Restaurant) object;
        if ((this.rid == null && other.rid != null) || (this.rid != null && !this.rid.equals(other.rid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "rugal.foods.entity.Restaurant[ rid=" + rid + " ]";
    }
}
