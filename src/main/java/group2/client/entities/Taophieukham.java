/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Collection;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "taophieukham")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taophieukham.findAll", query = "SELECT t FROM Taophieukham t"),
    @NamedQuery(name = "Taophieukham.findById", query = "SELECT t FROM Taophieukham t WHERE t.id = :id"),
    @NamedQuery(name = "Taophieukham.findBySothutu", query = "SELECT t FROM Taophieukham t WHERE t.sothutu = :sothutu"),
    @NamedQuery(name = "Taophieukham.findByName", query = "SELECT t FROM Taophieukham t WHERE t.name = :name"),
    @NamedQuery(name = "Taophieukham.findByPhone", query = "SELECT t FROM Taophieukham t WHERE t.phone = :phone"),
    @NamedQuery(name = "Taophieukham.findByAddress", query = "SELECT t FROM Taophieukham t WHERE t.address = :address"),
    @NamedQuery(name = "Taophieukham.findByTotalMoney", query = "SELECT t FROM Taophieukham t WHERE t.totalMoney = :totalMoney"),
    @NamedQuery(name = "Taophieukham.findByCreateAt", query = "SELECT t FROM Taophieukham t WHERE t.createAt = :createAt"),
    @NamedQuery(name = "Taophieukham.findByGender", query = "SELECT t FROM Taophieukham t WHERE t.gender = :gender"),
    @NamedQuery(name = "Taophieukham.findByDob", query = "SELECT t FROM Taophieukham t WHERE t.dob = :dob"),
    @NamedQuery(name = "Taophieukham.findBySympton", query = "SELECT t FROM Taophieukham t WHERE t.sympton = :sympton")})
public class Taophieukham implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sothutu")
    @NotNull(message = "No. cannot be left blank!!!")
    private Integer sothutu;
    @Size(max = 250)
    @Column(name = "name")
    @NotBlank(message = "Name cannot be left blank!!!")
    @Length(min = 3, max = 50, message = "Name must be from 3 to 50 characters")
    private String name;
    @Column(name = "phone")
    @NotBlank(message = "Phone cannot be left blank!!!")
    @Length(min = 10, max = 30, message = "Phone must be from 10 to 30 numbers")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone")

    private String phone;
    @Column(name = "address")
    @NotBlank(message = "Address cannot be left blank!!!")
    @Length(min = 10, max = 150, message = "Address must be from 10 to 150 characters")
    private String address;

    @Column(name = "total_money")
    private Integer totalMoney;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(name = "gender")
    private Boolean gender;
    @Column(name = "dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Size(max = 250)
    @Column(name = "sympton")
    private String sympton;
    @OneToMany(mappedBy = "taophieukhamId")
    @JsonIgnore
    private Collection<Toathuoc> toathuocCollection;

    @JoinColumn(name = "casher_id", referencedColumnName = "id")
    @ManyToOne
    private Casher casherId;

    @JoinColumn(name = "type_doctor_id", referencedColumnName = "id")
    @ManyToOne
    private TypeDoctor typeDoctorId;

    public Taophieukham() {
    }

    public Taophieukham(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSothutu() {
        return sothutu;
    }

    public void setSothutu(Integer sothutu) {
        this.sothutu = sothutu;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSympton() {
        return sympton;
    }

    public void setSympton(String sympton) {
        this.sympton = sympton;
    }

    @XmlTransient
    public Collection<Toathuoc> getToathuocCollection() {
        return toathuocCollection;
    }

    public void setToathuocCollection(Collection<Toathuoc> toathuocCollection) {
        this.toathuocCollection = toathuocCollection;
    }

    public Casher getCasherId() {
        return casherId;
    }

    public void setCasherId(Casher casherId) {
        this.casherId = casherId;
    }

    public TypeDoctor getTypeDoctorId() {
        return typeDoctorId;
    }

    public void setTypeDoctorId(TypeDoctor typeDoctorId) {
        this.typeDoctorId = typeDoctorId;
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
        if (!(object instanceof Taophieukham)) {
            return false;
        }
        Taophieukham other = (Taophieukham) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Taophieukham[ id=" + id + " ]";
    }

}
