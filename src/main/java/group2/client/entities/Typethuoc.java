/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import java.io.Serializable;
import java.util.Collection;
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
 * @author DELL
 */
@Entity
@Table(name = "typethuoc")
@NamedQueries({
    @NamedQuery(name = "Typethuoc.findAll", query = "SELECT t FROM Typethuoc t"),
    @NamedQuery(name = "Typethuoc.findById", query = "SELECT t FROM Typethuoc t WHERE t.id = :id"),
    @NamedQuery(name = "Typethuoc.findByName", query = "SELECT t FROM Typethuoc t WHERE t.name = :name")})
public class Typethuoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 250)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "typethuocId")
    private Collection<Thuoc> thuocCollection;

    public Typethuoc() {
    }

    public Typethuoc(Integer id) {
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

    public Collection<Thuoc> getThuocCollection() {
        return thuocCollection;
    }

    public void setThuocCollection(Collection<Thuoc> thuocCollection) {
        this.thuocCollection = thuocCollection;
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
        if (!(object instanceof Typethuoc)) {
            return false;
        }
        Typethuoc other = (Typethuoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Typethuoc[ id=" + id + " ]";
    }
    
}