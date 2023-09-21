package group2.client.dto;



import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import lombok.Getter;

public class RatingDAO {
    private int doctorId;
    private int patientId;
    private Double rating;
    private String comment;
    public RatingDAO() {
    }

    public int getDoctor_id() {
        return doctorId;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctorId = doctor_id;
    }

    public int getPatient_id() {
        return patientId;
    }

    public void setPatient_id(int patient_id) {
        this.patientId = patient_id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

