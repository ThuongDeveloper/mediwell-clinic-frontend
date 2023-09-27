package group2.client.dto;


import java.util.Date;
import java.util.List;

public class ListToaThuocDAO {

    private Date ngaytaikham;
    private String symptom;
    private int doctorId;
    private int taophieukhamId;
    private List<ToaThuocDAO> listTT;

    public ListToaThuocDAO() {
        // Hàm tạo mặc định, không cần truyền tham số
    }

    public Date getNgaytaikham() {
        return ngaytaikham;
    }

    public void setNgaytaikham(Date ngaytaikham) {
        this.ngaytaikham = ngaytaikham;
    }

    public int getTaophieukhamId() {
        return taophieukhamId;
    }

    public void setTaophieukhamId(int taophieukhamId) {
        this.taophieukhamId = taophieukhamId;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
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
