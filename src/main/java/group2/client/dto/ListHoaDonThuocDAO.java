/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.dto;

import group2.client.entities.Casher;
import java.util.List;

/**
 *
 * @author DELL
 */
public class ListHoaDonThuocDAO {

    private String name;
    private String phone;
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

    public Casher getCasherId() {
        return casherId;
    }

    public void setCasherId(Casher casherId) {
        this.casherId = casherId;
    }

}
