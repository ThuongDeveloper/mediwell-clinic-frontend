/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "lichlamviec")
@NamedQueries({
    @NamedQuery(name = "Lichlamviec.findAll", query = "SELECT l FROM Lichlamviec l"),
    @NamedQuery(name = "Lichlamviec.findById", query = "SELECT l FROM Lichlamviec l WHERE l.id = :id"),
    @NamedQuery(name = "Lichlamviec.findByThu", query = "SELECT l FROM Lichlamviec l WHERE l.thu = :thu"),
    @NamedQuery(name = "Lichlamviec.findByDate", query = "SELECT l FROM Lichlamviec l WHERE l.date = :date"),
    @NamedQuery(name = "Lichlamviec.findByCreateAt", query = "SELECT l FROM Lichlamviec l WHERE l.createAt = :createAt"),
    @NamedQuery(name = "Lichlamviec.findByStarttime", query = "SELECT l FROM Lichlamviec l WHERE l.starttime = :starttime"),
    @NamedQuery(name = "Lichlamviec.findByEndtime", query = "SELECT l FROM Lichlamviec l WHERE l.endtime = :endtime")})
public class Lichlamviec implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 250)
    @Column(name = "thu")
    private String thu;
    @Column(name = "date")
//    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(name = "starttime")
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
//    @Temporal(TemporalType.TIME)
    private String starttime;
    @Column(name = "endtime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
//    @Temporal(TemporalType.TIME)
    private String endtime;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne
    private Doctor doctorId;

    public Lichlamviec() {
    }

    public Lichlamviec(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
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
        if (!(object instanceof Lichlamviec)) {
            return false;
        }
        Lichlamviec other = (Lichlamviec) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "group2.client.entities.Lichlamviec[ id=" + id + " ]";
    }
    
}
