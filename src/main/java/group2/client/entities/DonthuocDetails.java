/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

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
 * @author DELL
 */
@Entity
@Table(name = "donthuoc_details")
@NamedQueries({
    @NamedQuery(name = "DonthuocDetails.findAll", query = "SELECT d FROM DonthuocDetails d"),
    @NamedQuery(name = "DonthuocDetails.findById", query = "SELECT d FROM DonthuocDetails d WHERE d.id = :id"),
    @NamedQuery(name = "DonthuocDetails.findByQuantity", query = "SELECT d FROM DonthuocDetails d WHERE d.quantity = :quantity"),
    @NamedQuery(name = "DonthuocDetails.findByPrice", query = "SELECT d FROM DonthuocDetails d WHERE d.price = :price")})
public class DonthuocDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Integer price;
    @JoinColumn(name = "donthuoc_id", referencedColumnName = "id")
    @ManyToOne
    private Donthuoc donthuocId;
    @JoinColumn(name = "thuoc_id", referencedColumnName = "id")
    @ManyToOne
    private Thuoc thuocId;

    public DonthuocDetails() {
    }

    public DonthuocDetails(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Donthuoc getDonthuocId() {
        return donthuocId;
    }

    public void setDonthuocId(Donthuoc donthuocId) {
        this.donthuocId = donthuocId;
    }

    public Thuoc getThuocId() {
        return thuocId;
    }

    public void setThuocId(Thuoc thuocId) {
        this.thuocId = thuocId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DonthuocDetails)) {
            return false;
        }
        DonthuocDetails other = (DonthuocDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.DonthuocDetails[ id=" + id + " ]";
    }
    
}
