/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.dto;

import java.util.List;

/**
 *
 * @author DELL
 */
public class ListHoaDonThuocDAO {
    private String name;
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
    
}
