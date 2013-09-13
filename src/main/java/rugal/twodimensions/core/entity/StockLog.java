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
@Table(name = "stock_log", catalog = "two_dimension", schema = "")
@NamedQueries({
    @NamedQuery(name = "StockLog.findAll", query = "SELECT s FROM StockLog s")})
public class StockLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "slid", nullable = false)
    private Integer slid;
    @Column(name = "log_time")
    private Long logTime;
    @Column(name = "quantity")
    private Integer quantity;
    @JoinColumn(name = "oid", referencedColumnName = "oid")
    @ManyToOne
    private Operator oid;
    @JoinColumn(name = "gid", referencedColumnName = "gid")
    @ManyToOne
    private Goods gid;
    @JoinColumn(name = "vid", referencedColumnName = "vid")
    @ManyToOne
    private Vendor vid;

    public StockLog() {
    }

    public StockLog(Integer slid) {
        this.slid = slid;
    }

    public Integer getSlid() {
        return slid;
    }

    public void setSlid(Integer slid) {
        this.slid = slid;
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

    public Operator getOid() {
        return oid;
    }

    public void setOid(Operator oid) {
        this.oid = oid;
    }

    public Goods getGid() {
        return gid;
    }

    public void setGid(Goods gid) {
        this.gid = gid;
    }

    public Vendor getVid() {
        return vid;
    }

    public void setVid(Vendor vid) {
        this.vid = vid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (slid != null ? slid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockLog)) {
            return false;
        }
        StockLog other = (StockLog) object;
        if ((this.slid == null && other.slid != null) || (this.slid != null && !this.slid.equals(other.slid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rugal.twodimensions.core.entity.StockLog[ slid=" + slid + " ]";
    }
    
}
