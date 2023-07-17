/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "thuoc")
@NamedQueries({
    @NamedQuery(name = "Thuoc.findAll", query = "SELECT t FROM Thuoc t"),
    @NamedQuery(name = "Thuoc.findById", query = "SELECT t FROM Thuoc t WHERE t.id = :id"),
    @NamedQuery(name = "Thuoc.findByName", query = "SELECT t FROM Thuoc t WHERE t.name = :name"),
    @NamedQuery(name = "Thuoc.findByCompanyName", query = "SELECT t FROM Thuoc t WHERE t.companyName = :companyName"),
    @NamedQuery(name = "Thuoc.findByComposition", query = "SELECT t FROM Thuoc t WHERE t.composition = :composition"),
    @NamedQuery(name = "Thuoc.findByQuantity", query = "SELECT t FROM Thuoc t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "Thuoc.findByPrice", query = "SELECT t FROM Thuoc t WHERE t.price = :price"),
    @NamedQuery(name = "Thuoc.findByCreateAt", query = "SELECT t FROM Thuoc t WHERE t.createAt = :createAt")})
public class Thuoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "composition")
    private String composition;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Integer price;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @OneToMany(mappedBy = "thuocId")
    private List<DonthuocDetails> donthuocDetailsList;
    @JoinColumn(name = "typethuoc_id", referencedColumnName = "id")
    @ManyToOne
    private Typethuoc typethuocId;
    @OneToMany(mappedBy = "thuocId")
    private List<ToathuocDetails> toathuocDetailsList;

    public Thuoc() {
    }

    public Thuoc(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<DonthuocDetails> getDonthuocDetailsList() {
        return donthuocDetailsList;
    }

    public void setDonthuocDetailsList(List<DonthuocDetails> donthuocDetailsList) {
        this.donthuocDetailsList = donthuocDetailsList;
    }

    public Typethuoc getTypethuocId() {
        return typethuocId;
    }

    public void setTypethuocId(Typethuoc typethuocId) {
        this.typethuocId = typethuocId;
    }

    public List<ToathuocDetails> getToathuocDetailsList() {
        return toathuocDetailsList;
    }

    public void setToathuocDetailsList(List<ToathuocDetails> toathuocDetailsList) {
        this.toathuocDetailsList = toathuocDetailsList;
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
        if (!(object instanceof Thuoc)) {
            return false;
        }
        Thuoc other = (Thuoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Thuoc[ id=" + id + " ]";
    }
    
}
