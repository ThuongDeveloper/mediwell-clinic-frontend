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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hokim
 */
@Entity
@Table(name = "casher")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Casher.findAll", query = "SELECT c FROM Casher c"),
    @NamedQuery(name = "Casher.findById", query = "SELECT c FROM Casher c WHERE c.id = :id"),
    @NamedQuery(name = "Casher.findByName", query = "SELECT c FROM Casher c WHERE c.name = :name"),
    @NamedQuery(name = "Casher.findByUsername", query = "SELECT c FROM Casher c WHERE c.username = :username"),
    @NamedQuery(name = "Casher.findByPassword", query = "SELECT c FROM Casher c WHERE c.password = :password"),
    @NamedQuery(name = "Casher.findByEmail", query = "SELECT c FROM Casher c WHERE c.email = :email"),
    @NamedQuery(name = "Casher.findByAddress", query = "SELECT c FROM Casher c WHERE c.address = :address"),
    @NamedQuery(name = "Casher.findByRole", query = "SELECT c FROM Casher c WHERE c.role = :role"),
    @NamedQuery(name = "Casher.findByCreateAt", query = "SELECT c FROM Casher c WHERE c.createAt = :createAt")})
public class Casher implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 250)
    @Column(name = "name")
    private String name;
    @Size(max = 250)
    @Column(name = "username")
    private String username;
    @Size(max = 250)
    @Column(name = "password")
    private String password;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 250)
    @Column(name = "email")
    private String email;
    @Size(max = 250)
    @Column(name = "address")
    private String address;
    @Size(max = 50)
    @Column(name = "role")
    private String role;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @OneToMany(mappedBy = "casherId")
    private List<Donthuoc> donthuocList;
    @OneToMany(mappedBy = "casherId")
    private List<Taophieukham> taophieukhamList;

    public Casher() {
    }

    public Casher(Integer id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
    
    public List<Donthuoc> getDonthuocList() {
        return donthuocList;
    }
    public void setDonthuocList(List<Donthuoc> donthuocList) {
        this.donthuocList = donthuocList;
    }
    public List<Taophieukham> getTaophieukhamList() {
        return taophieukhamList;
    }
    public void setTaophieukhamList(List<Taophieukham> taophieukhamList) {
        this.taophieukhamList = taophieukhamList;
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
        if (!(object instanceof Casher)) {
            return false;
        }
        Casher other = (Casher) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Casher[ id=" + id + " ]";
    }
    
}
