/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "taophieukham")
@NamedQueries({
    @NamedQuery(name = "Taophieukham.findAll", query = "SELECT t FROM Taophieukham t"),
    @NamedQuery(name = "Taophieukham.findById", query = "SELECT t FROM Taophieukham t WHERE t.id = :id"),
    @NamedQuery(name = "Taophieukham.findBySothutu", query = "SELECT t FROM Taophieukham t WHERE t.sothutu = :sothutu"),
    @NamedQuery(name = "Taophieukham.findByName", query = "SELECT t FROM Taophieukham t WHERE t.name = :name"),
    @NamedQuery(name = "Taophieukham.findByPhone", query = "SELECT t FROM Taophieukham t WHERE t.phone = :phone"),
    @NamedQuery(name = "Taophieukham.findByAddress", query = "SELECT t FROM Taophieukham t WHERE t.address = :address"),
    @NamedQuery(name = "Taophieukham.findByTotalMoney", query = "SELECT t FROM Taophieukham t WHERE t.totalMoney = :totalMoney"),
    @NamedQuery(name = "Taophieukham.findByPatientId", query = "SELECT t FROM Taophieukham t WHERE t.patientId = :patientId"),
    @NamedQuery(name = "Taophieukham.findByCreateAt", query = "SELECT t FROM Taophieukham t WHERE t.createAt = :createAt")})
public class Taophieukham implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "sothutu")
    private Integer sothutu;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "total_money")
    private Integer totalMoney;
    @Column(name = "patient_id")
    private Integer patientId;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
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

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
