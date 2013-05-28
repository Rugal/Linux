/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(catalog = "food", schema = "")
@NamedQueries({
    @NamedQuery(name = "Indent.findAll", query = "SELECT i FROM Indent i")})
public class Indent implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer iid;

    @Basic(optional = false)
    @NotNull
    @Column(name = "order_time", nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
    private Long orderTime;

    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false)
    @ManyToOne(optional = false)
    private User uid;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iid")
    private List<OrderLog> orderLogList;

    public Indent()
    {
    }

    public Indent(Integer iid)
    {
        this.iid = iid;
    }

    public Indent(Integer iid, Long orderTime)
    {
        this.iid = iid;
        this.orderTime = orderTime;
    }

    public Integer getIid()
    {
        return iid;
    }

    public void setIid(Integer iid)
    {
        this.iid = iid;
    }

    public Long getOrderTime()
    {
        return orderTime;
    }

    public void setOrderTime(Long orderTime)
    {
        this.orderTime = orderTime;
    }

    public User getUid()
    {
        return uid;
    }

    public void setUid(User uid)
    {
        this.uid = uid;
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
        hash += (iid != null ? iid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indent)) {
            return false;
        }
        Indent other = (Indent) object;
        if ((this.iid == null && other.iid != null) || (this.iid != null && !this.iid.equals(other.iid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "rugal.foods.entity.Indent[ iid=" + iid + " ]";
    }
}
