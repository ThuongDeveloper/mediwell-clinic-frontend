package group2.client.dto;


import java.util.List;

public class ListToaThuocDAO {
    private String name;
    private String phone;
    private String address;
    private String symptom;
    private String description;
    private boolean state;
    private int doctorId;
    private List<ToaThuocDAO> listTT;

    public ListToaThuocDAO() {
        // Hàm tạo mặc định, không cần truyền tham số
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

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public List<ToaThuocDAO> getListTT() {
        return listTT;
    }

    public void setListTT(List<ToaThuocDAO> listTT) {
        this.listTT = listTT;
    }

}
