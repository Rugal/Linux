/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author rugal
 */
@Entity
@Table(name = "operator", catalog = "two_dimension", schema = "")
@NamedQueries({
    @NamedQuery(name = "Operator.findAll", query = "SELECT o FROM Operator o")})
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "oid", nullable = false)
    private Integer oid;
    @Size(max = 40)
    @Column(name = "name", length = 40)
    private String name;
    @Size(max = 50)
    @Column(name = "password", length = 50)
    private String password;
    @OneToMany(mappedBy = "oid")
    private List<VenditionLog> venditionLogList;
    @OneToMany(mappedBy = "oid")
    private List<StockLog> stockLogList;

    public Operator() {
    }

    public Operator(Integer oid) {
        this.oid = oid;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<VenditionLog> getVenditionLogList() {
        return venditionLogList;
    }

    public void setVenditionLogList(List<VenditionLog> venditionLogList) {
        this.venditionLogList = venditionLogList;
    }

    public List<StockLog> getStockLogList() {
        return stockLogList;
    }

    public void setStockLogList(List<StockLog> stockLogList) {
        this.stockLogList = stockLogList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oid != null ? oid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operator)) {
            return false;
        }
        Operator other = (Operator) object;
        if ((this.oid == null && other.oid != null) || (this.oid != null && !this.oid.equals(other.oid)) || (this.password != null && !this.password.equals(other.password))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rugal.twodimensions.core.entity.Operator[ oid=" + oid + " ]";
    }
}
