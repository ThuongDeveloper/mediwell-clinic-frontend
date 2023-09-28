/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

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
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "patient")
@JsonIgnoreProperties({"ratingCollection"})

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Patient.findAll", query = "SELECT p FROM Patient p"),
    @NamedQuery(name = "Patient.findById", query = "SELECT p FROM Patient p WHERE p.id = :id"),
    @NamedQuery(name = "Patient.findByPassword", query = "SELECT p FROM Patient p WHERE p.password = :password"),
    @NamedQuery(name = "Patient.findByName", query = "SELECT p FROM Patient p WHERE p.name = :name"),
    @NamedQuery(name = "Patient.findByEmail", query = "SELECT p FROM Patient p WHERE p.email = :email"),
    @NamedQuery(name = "Patient.findByAddress", query = "SELECT p FROM Patient p WHERE p.address = :address"),
    @NamedQuery(name = "Patient.findByPhone", query = "SELECT p FROM Patient p WHERE p.phone = :phone"),
    @NamedQuery(name = "Patient.findByImage", query = "SELECT p FROM Patient p WHERE p.image = :image"),
    @NamedQuery(name = "Patient.findByRole", query = "SELECT p FROM Patient p WHERE p.role = :role"),
    @NamedQuery(name = "Patient.findByGender", query = "SELECT p FROM Patient p WHERE p.gender = :gender")})
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 250)
    @Column(name = "password")
    private String password;
    @Size(max = 250)
    @Column(name = "name")
    private String name;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 250)
    @Column(name = "email")
    private String email;
    @Size(max = 250)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "phone")
    private String phone;
    @Size(max = 250)
    @Column(name = "image")
    private String image;
    @Size(max = 50)
    @Column(name = "role")
    private String role;
    @Column(name = "gender")
    
    private Boolean gender;
    @Column(name = "dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    @OneToMany(mappedBy = "patientId")
    private List<Rating> ratingList;
    @OneToMany(mappedBy = "patientId")
    private List<Appointment> appointmentList;

    public Patient() {
    }

    public Patient(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
    @XmlTransient
    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    @XmlTransient
    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
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
        if (!(object instanceof Patient)) {
            return false;
        }
        Patient other = (Patient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Patient[ id=" + id + " ]";
    }
    
}
