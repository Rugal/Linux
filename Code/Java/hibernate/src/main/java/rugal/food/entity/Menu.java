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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(catalog = "food", schema = "")
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m")})
public class Menu implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer mid;

    @Size(max = 30)
    @Column(length = 30)
    private String name;

    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private float price;

    @Size(max = 50)
    @Column(length = 50)
    private String picture;

    @Size(max = 100)
    @Column(length = 100)
    private String depict;

    @JoinColumn(name = "rid", referencedColumnName = "rid", nullable = true)
    @ManyToOne(optional = false)
    private Restaurant rid;
    
    @Transient
    private Float duration;

    @Transient
    private Float quality;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mid")
    private List<OrderLog> orderLogList;

    public Menu()
    {
    }

    public Menu(Integer mid)
    {
        this.mid = mid;
    }

    public Menu(Integer mid, float price)
    {
        this.mid = mid;
        this.price = price;
    }

    public Integer getMid()
    {
        return mid;
    }

    public void setMid(Integer mid)
    {
        this.mid = mid;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public Restaurant getRid()
    {
        return rid;
    }

    public void setRid(Restaurant rid)
    {
        this.rid = rid;
    }

    public List<OrderLog> getOrderLogList()
    {
        return orderLogList;
    }

    public void setOrderLogList(List<OrderLog> orderLogList)
    {
        this.orderLogList = orderLogList;
    }

    public Float getDuration()
    {
        return duration;
    }

    public void setDuration(Float duration)
    {
        this.duration = duration;
    }

    public Float getQuality()
    {
        return quality;
    }

    public void setQuality(Float quality)
    {
        this.quality = quality;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDepict()
    {
        return depict;
    }

    public void setDepict(String depict)
    {
        this.depict = depict;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (mid != null ? mid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.mid == null && other.mid != null) || (this.mid != null && !this.mid.equals(other.mid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "rugal.foods.entity.Menu[ mid=" + mid + " ]";
    }
}
