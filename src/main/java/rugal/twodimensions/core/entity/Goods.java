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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "goods", catalog = "two_dimension", schema = "")
@NamedQueries({
	@NamedQuery(name = "Goods.findAll", query = "SELECT g FROM Goods g")})
public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "gid", nullable = false)
	private Integer gid;
	@Size(max = 100)
	@Column(name = "name", length = 100)
	private String name;
	@Basic(optional = false)
	@NotNull
	@Column(name = "stock_price", nullable = false)
	private float stockPrice;
	@Basic(optional = false)
	@NotNull
	@Column(name = "sell_price", nullable = false)
	private float sellPrice;
	@Size(max = 10)
	@Column(name = "unit", length = 10)
	private String unit;
	@Column(name = "quantity")
	private Integer quantity;
	@OneToMany(mappedBy = "gid")
	private List<VenditionLog> venditionLogList;
	@JoinColumn(name = "vid", referencedColumnName = "vid")
	@ManyToOne
	private Vendor vid;
	@OneToMany(mappedBy = "gid")
	private List<StockLog> stockLogList;

	public Goods() {
	}

	public Goods(Integer gid) {
		this.gid = gid;
	}

	public Goods(Integer gid, float stockPrice, float sellPrice) {
		this.gid = gid;
		this.stockPrice = stockPrice;
		this.sellPrice = sellPrice;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(float stockPrice) {
		this.stockPrice = stockPrice;
	}

	public float getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(float sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean sell(int num) {
		boolean value = false;
		if (num > 0 && this.quantity >= num) {
			this.quantity -= num;
			value = true;
		}
		return value;
	}

	public void stock(int num) {
		if (num > 0) {
			this.quantity += num;
		}
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<VenditionLog> getVenditionLogList() {
		return venditionLogList;
	}

	public void setVenditionLogList(List<VenditionLog> venditionLogList) {
		this.venditionLogList = venditionLogList;
	}

	public Vendor getVid() {
		return vid;
	}

	public void setVid(Vendor vid) {
		this.vid = vid;
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
		hash += (gid != null ? gid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Goods)) {
			return false;
		}
		Goods other = (Goods) object;
		if ((this.gid == null && other.gid != null) || (this.gid != null && !this.gid.equals(other.gid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "rugal.twodimensions.core.entity.Goods[ gid=" + gid + " ]";
	}
}
