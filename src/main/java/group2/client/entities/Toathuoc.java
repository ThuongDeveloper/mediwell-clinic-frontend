/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "toathuoc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Toathuoc.findAll", query = "SELECT t FROM Toathuoc t"),
    @NamedQuery(name = "Toathuoc.findById", query = "SELECT t FROM Toathuoc t WHERE t.id = :id"),
    @NamedQuery(name = "Toathuoc.findByCreateAt", query = "SELECT t FROM Toathuoc t WHERE t.createAt = :createAt"),
    @NamedQuery(name = "Toathuoc.findBySympton", query = "SELECT t FROM Toathuoc t WHERE t.sympton = :sympton"),
    @NamedQuery(name = "Toathuoc.findByNgaytaikham", query = "SELECT t FROM Toathuoc t WHERE t.ngaytaikham = :ngaytaikham")})
public class Toathuoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Size(max = 250)
    @Column(name = "sympton")
    private String sympton;
    @Column(name = "ngaytaikham")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngaytaikham;
    @OneToMany(mappedBy = "toathuocId")
    private List<Donthuoc> donthuocList;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne
    private Doctor doctorId;
    @JoinColumn(name = "taophieukham_id", referencedColumnName = "id")
    @ManyToOne
    private Taophieukham taophieukhamId;
   @OneToMany(mappedBy = "toathuocId")
   @JsonIgnore
    private Collection<ToathuocDetails> toathuocDetailsCollection;

    public Toathuoc() {
    }

    public Toathuoc(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getSympton() {
        return sympton;
    }

    public void setSympton(String sympton) {
        this.sympton = sympton;
    }

    public Date getNgaytaikham() {
        return ngaytaikham;
    }

    public void setNgaytaikham(Date ngaytaikham) {
        this.ngaytaikham = ngaytaikham;
    }

    @XmlTransient
    public List<Donthuoc> getDonthuocList() {
        return donthuocList;
    }

    public void setDonthuocList(List<Donthuoc> donthuocList) {
        this.donthuocList = donthuocList;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    public Taophieukham getTaophieukhamId() {
        return taophieukhamId;
    }

    public void setTaophieukhamId(Taophieukham taophieukhamId) {
        this.taophieukhamId = taophieukhamId;
    }

   
   public Collection<ToathuocDetails> getToathuocDetailsCollection() {
        return toathuocDetailsCollection;
    }

    public void setToathuocDetailsCollection(Collection<ToathuocDetails> toathuocDetailsCollection) {
        this.toathuocDetailsCollection = toathuocDetailsCollection;
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
        if (!(object instanceof Toathuoc)) {
            return false;
        }
        Toathuoc other = (Toathuoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Toathuoc[ id=" + id + " ]";
    }
    
}
