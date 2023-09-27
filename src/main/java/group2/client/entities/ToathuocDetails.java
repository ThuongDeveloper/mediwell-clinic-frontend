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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "toathuoc_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ToathuocDetails.findAll", query = "SELECT t FROM ToathuocDetails t"),
    @NamedQuery(name = "ToathuocDetails.findById", query = "SELECT t FROM ToathuocDetails t WHERE t.id = :id"),
    @NamedQuery(name = "ToathuocDetails.findBySang", query = "SELECT t FROM ToathuocDetails t WHERE t.sang = :sang"),
    @NamedQuery(name = "ToathuocDetails.findByTrua", query = "SELECT t FROM ToathuocDetails t WHERE t.trua = :trua"),
    @NamedQuery(name = "ToathuocDetails.findByChieu", query = "SELECT t FROM ToathuocDetails t WHERE t.chieu = :chieu"),
    @NamedQuery(name = "ToathuocDetails.findByToi", query = "SELECT t FROM ToathuocDetails t WHERE t.toi = :toi"),
    @NamedQuery(name = "ToathuocDetails.findByQuantity", query = "SELECT t FROM ToathuocDetails t WHERE t.quantity = :quantity")})
public class ToathuocDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 250)
    @Column(name = "sang")
    private String sang;
    @Size(max = 250)
    @Column(name = "trua")
    private String trua;
    @Size(max = 250)
    @Column(name = "chieu")
    private String chieu;
    @Size(max = 250)
    @Column(name = "toi")
    private String toi;
    @Column(name = "quantity")
    private Integer quantity;
    @JoinColumn(name = "thuoc_id", referencedColumnName = "id")
    @ManyToOne
    private Thuoc thuocId;
    @JoinColumn(name = "toathuoc_id", referencedColumnName = "id")
    @ManyToOne
    private Toathuoc toathuocId;

    public ToathuocDetails() {
    }

    public ToathuocDetails(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSang() {
        return sang;
    }

    public void setSang(String sang) {
        this.sang = sang;
    }

    public String getTrua() {
        return trua;
    }

    public void setTrua(String trua) {
        this.trua = trua;
    }

    public String getChieu() {
        return chieu;
    }

    public void setChieu(String chieu) {
        this.chieu = chieu;
    }

    public String getToi() {
        return toi;
    }

    public void setToi(String toi) {
        this.toi = toi;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Thuoc getThuocId() {
        return thuocId;
    }

    public void setThuocId(Thuoc thuocId) {
        this.thuocId = thuocId;
    }

    public Toathuoc getToathuocId() {
        return toathuocId;
    }

    public void setToathuocId(Toathuoc toathuocId) {
        this.toathuocId = toathuocId;
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
        if (!(object instanceof ToathuocDetails)) {
            return false;
        }
        ToathuocDetails other = (ToathuocDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.ToathuocDetails[ id=" + id + " ]";
    }
    
}
