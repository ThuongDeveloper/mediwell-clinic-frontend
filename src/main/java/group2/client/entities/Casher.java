package group2.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import group2.client.exception.UniqueEmail;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.Length;

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
    @NamedQuery(name = "Casher.findByPhone", query = "SELECT c FROM Casher c WHERE c.phone = :phone"),
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
    @NotBlank(message = "Name cannot be left blank!!!")
    @Length(min = 3, max = 50, message = "Name must be from 3 to 50 characters")
    private String name;
    @Size(max = 250)
    @Column(name = "phone")
    @NotBlank(message = "Phone cannot be left blank!!!")
    @Pattern(regexp = "^[0-9]*$", message = "Phone must contain only numbers")
    private String phone;
    @Column(name = "password")
    @NotBlank(message = "Password cannot be left blank!!!")
    @Length(min = 8, max = 100, message = "Password must be from 8 to 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$", message = "Invalid password")
    private String password;
    @Size(max = 250)
    @Column(name = "email")
    @NotBlank(message = "Email cannot be left blank!!!")
    @Email
    private String email;
    @Size(max = 250)
    @Column(name = "address")
    @NotBlank(message = "Address cannot be left blank!!!")
    @Length(min = 10, max = 150, message = "Address must be from 10 to 150 characters")
    private String address;
    @Size(max = 50)
    @Column(name = "role")
    private String role;
    @Column(name = "age")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Age cannot be null")
    private Date age;
    @Column(name = "gender")
    @NotNull(message = "Gender cannot be null")
    private Boolean gender;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @OneToMany(mappedBy = "casherId")
    @JsonIgnore
    private Collection<Donthuoc> donthuocCollection;
    @OneToMany(mappedBy = "casherId")
    @JsonIgnore
    private Collection<Taophieukham> taophieukhamCollection;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getAge() {
        return age;
    }

    public void setAge(Date age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
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

//    public List<Donthuoc> getDonthuocList() {
//        return donthuocList;
//    }
//    public void setDonthuocList(List<Donthuoc> donthuocList) {
//        this.donthuocList = donthuocList;
//    }
//    public List<Taophieukham> getTaophieukhamList() {
//        return taophieukhamList;
//    }
//    public void setTaophieukhamList(List<Taophieukham> taophieukhamList) {
//        this.taophieukhamList = taophieukhamList;
//    }
    public Collection<Donthuoc> getDonthuocCollection() {
        return donthuocCollection;
    }

    public void setDonthuocCollection(Collection<Donthuoc> donthuocCollection) {
        this.donthuocCollection = donthuocCollection;
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
