/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.dto;

/**
 *
 * @author DELL
 */
public class HoaDonThuocDAO {
    private int thuocID;
    private int price;
    private int quantity;

    public HoaDonThuocDAO(int thuocID, int price, int quantity) {
        this.thuocID = thuocID;
        this.price = price;
        this.quantity = quantity;
    }

    public int getThuocID() {
        return thuocID;
    }

    public void setThuocID(int thuocID) {
        this.thuocID = thuocID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
}
