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
@Table(name = "type_doctor")
@NamedQueries({
    @NamedQuery(name = "TypeDoctor.findAll", query = "SELECT t FROM TypeDoctor t"),
    @NamedQuery(name = "TypeDoctor.findById", query = "SELECT t FROM TypeDoctor t WHERE t.id = :id"),
    @NamedQuery(name = "TypeDoctor.findByName", query = "SELECT t FROM TypeDoctor t WHERE t.name = :name")})
public class TypeDoctor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 150)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "typeDoctorId")
    private Collection<Doctor> doctorCollection;
    @OneToMany(mappedBy = "typeDoctorId")
    private Collection<Taophieukham> taophieukhamCollection;

    public TypeDoctor() {
    }

    public TypeDoctor(Integer id) {
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

    public Collection<Doctor> getDoctorCollection() {
        return doctorCollection;
    }

    public void setDoctorCollection(Collection<Doctor> doctorCollection) {
        this.doctorCollection = doctorCollection;
    }

    public Collection<Taophieukham> getTaophieukhamCollection() {
        return taophieukhamCollection;
    }

    public void setTaophieukhamCollection(Collection<Taophieukham> taophieukhamCollection) {
        this.taophieukhamCollection = taophieukhamCollection;
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
        if (!(object instanceof TypeDoctor)) {
            return false;
        }
        TypeDoctor other = (TypeDoctor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.TypeDoctor[ id=" + id + " ]";
    }
    
}
