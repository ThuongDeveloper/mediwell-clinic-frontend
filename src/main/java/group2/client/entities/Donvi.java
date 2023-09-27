package group2.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "donvi")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Donvi.findAll", query = "SELECT t FROM Donvi t"),
        @NamedQuery(name = "Donvi.findById", query = "SELECT t FROM Donvi t WHERE t.id = :id"),
        @NamedQuery(name = "Donvi.findByName", query = "SELECT t FROM Donvi t WHERE t.name = :name")})
public class Donvi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "donviId")
    @JsonIgnore
    private Collection<Thuoc> thuocCollection;

    public Donvi() {
    }

    public Donvi(Integer id) {
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

    @XmlTransient
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
        if (!(object instanceof Donvi)) {
            return false;
        }
        Donvi other = (Donvi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "groub2.backend.entities.Donvi[ id=" + id + " ]";
    }

}
