/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.dto;

import group2.client.entities.Casher;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DELL
 */
public class ListHoaDonThuocDAO {

    private String name;
    private String phone;
    private String address;
    private Date dob;
    private Boolean gender;
    private String sympton;
    private Casher casherId; 
    
    private List<HoaDonThuocDAO> listHDT;

    public List<HoaDonThuocDAO> getListHDT() {
        return listHDT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListHDT(List<HoaDonThuocDAO> listHDT) {
        this.listHDT = listHDT;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getSympton() {
        return sympton;
    }

    public void setSympton(String sympton) {
        this.sympton = sympton;
    }

    public Casher getCasherId() {
        return casherId;
    }

    public void setCasherId(Casher casherId) {
        this.casherId = casherId;
    }

}
