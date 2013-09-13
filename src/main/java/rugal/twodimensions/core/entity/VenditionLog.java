/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author rugal
 */
@Entity
@Table(name = "vendition_log", catalog = "two_dimension", schema = "")
@NamedQueries({
    @NamedQuery(name = "VenditionLog.findAll", query = "SELECT v FROM VenditionLog v")})
public class VenditionLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "vlid", nullable = false)
    private Integer vlid;
    @Column(name = "log_time")
    private Long logTime;
    @Column(name = "quantity")
    private Integer quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "actual_price", precision = 2)
    private Float actualPrice;
    @JoinColumn(name = "oid", referencedColumnName = "oid")
    @ManyToOne
    private Operator oid;
    @JoinColumn(name = "cid", referencedColumnName = "cid")
    @ManyToOne
    private Customer cid;
    @JoinColumn(name = "gid", referencedColumnName = "gid")
    @ManyToOne
    private Goods gid;

    public VenditionLog() {
    }

    public VenditionLog(Integer vlid) {
        this.vlid = vlid;
    }

    public Integer getVlid() {
        return vlid;
    }

    public void setVlid(Integer vlid) {
        this.vlid = vlid;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Operator getOid() {
        return oid;
    }

    public void setOid(Operator oid) {
        this.oid = oid;
    }

    public Customer getCid() {
        return cid;
    }

    public void setCid(Customer cid) {
        this.cid = cid;
    }

    public Goods getGid() {
        return gid;
    }

    public void setGid(Goods gid) {
        this.gid = gid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vlid != null ? vlid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VenditionLog)) {
            return false;
        }
        VenditionLog other = (VenditionLog) object;
        if ((this.vlid == null && other.vlid != null) || (this.vlid != null && !this.vlid.equals(other.vlid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rugal.twodimensions.core.entity.VenditionLog[ vlid=" + vlid + " ]";
    }
}
