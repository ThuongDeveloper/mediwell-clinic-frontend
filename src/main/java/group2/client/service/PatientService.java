package group2.client.service;

import group2.client.entities.Patient;
import group2.client.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Kiểm tra xem email có tồn tại trong hệ thống hay không
    public boolean emailExists(String email) {
        return patientRepository.existsByEmail(email);
    }
    public boolean updatePasswordByEmail(String email, String newPassword) {
        int updatedRows = patientRepository.updatePasswordByEmail(email, newPassword);
        return updatedRows > 0;
    }
}
