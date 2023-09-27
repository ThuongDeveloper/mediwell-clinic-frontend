/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "donthuoc")
@NamedQueries({
    @NamedQuery(name = "Donthuoc.findAll", query = "SELECT d FROM Donthuoc d"),
    @NamedQuery(name = "Donthuoc.findById", query = "SELECT d FROM Donthuoc d WHERE d.id = :id"),
    @NamedQuery(name = "Donthuoc.findByTotalMoney", query = "SELECT d FROM Donthuoc d WHERE d.totalMoney = :totalMoney"),
    @NamedQuery(name = "Donthuoc.findByCreateAt", query = "SELECT d FROM Donthuoc d WHERE d.createAt = :createAt")})
public class Donthuoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
     @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @JoinColumn(name = "casher_id", referencedColumnName = "id")
    @ManyToOne
    private Casher casherId;
    @OneToMany(mappedBy = "donthuocId")
    private Collection<DonthuocDetails> donthuocDetailsCollection;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_money")
    private int totalMoney;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @JoinColumn(name = "phieukham_id", referencedColumnName = "id")
    @ManyToOne
    private Taophieukham phieukhamId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Donthuoc() {
    }

    public Donthuoc(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Casher getCasherId() {
        return casherId;
    }

    public void setCasherId(Casher casherId) {
        this.casherId = casherId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Taophieukham getPhieukhamId() {
        return phieukhamId;
    }

    public void setPhieukhamId(Taophieukham phieukhamId) {
        this.phieukhamId = phieukhamId;
    }

    public Collection<DonthuocDetails> getDonthuocDetailsCollection() {
        return donthuocDetailsCollection;
    }

    public void setDonthuocDetailsCollection(Collection<DonthuocDetails> donthuocDetailsCollection) {
        this.donthuocDetailsCollection = donthuocDetailsCollection;
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
        if (!(object instanceof Donthuoc)) {
            return false;
        }
        Donthuoc other = (Donthuoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Donthuoc[ id=" + id + " ]";
    }
    
}
