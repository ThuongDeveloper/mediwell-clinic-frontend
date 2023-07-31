/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.dto;

import javax.persistence.Column;

/**
 *
 * @author dochi
 */
public class ThuocDAO {

    private Integer id;
    private String name;
    private String companyName;
    private String composition;
    private Integer quantity;
    private Integer price;
    private Integer typethuocId;

    public ThuocDAO() {
    }

    public ThuocDAO(Integer id, String name, String companyName, String composition, Integer quantity, Integer price, Integer typethuocId) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
        this.composition = composition;
        this.quantity = quantity;
        this.price = price;
        this.typethuocId = typethuocId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTypethuocId() {
        return typethuocId;
    }

    public void setTypethuocId(Integer typethuocId) {
        this.typethuocId = typethuocId;
    }

}
