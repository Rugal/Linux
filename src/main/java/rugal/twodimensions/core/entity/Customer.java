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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rugal
 */
@Entity
@Table(name = "customer", catalog = "two_dimension", schema = "")
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cid", nullable = false)
    private Integer cid;
    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "membership", nullable = false)
    private int membership;
    @Basic(optional = false)
    @NotNull
    @Column(name = "credit", nullable = false)
    private int credit;
    @Column(name = "birthday")
    private Long birthday;
    @OneToMany(mappedBy = "cid")
    private List<VenditionLog> venditionLogList;

    public Customer() {
    }

    public Customer(Integer cid) {
        this.cid = cid;
    }

    public Customer(Integer cid, int membership, int credit) {
        this.cid = cid;
        this.membership = membership;
        this.credit = credit;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMembership() {
        return membership;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void changeCredit(int num) {
        this.credit += num;
        if (this.credit < 0) {
            this.credit = 0;
        }
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public List<VenditionLog> getVenditionLogList() {
        return venditionLogList;
    }

    public void setVenditionLogList(List<VenditionLog> venditionLogList) {
        this.venditionLogList = venditionLogList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rugal.twodimensions.core.entity.Customer[ cid=" + cid + " ]";
    }
}
