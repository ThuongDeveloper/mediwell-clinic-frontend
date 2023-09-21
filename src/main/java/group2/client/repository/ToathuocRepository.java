package group2.client.repository;

import group2.client.entities.Toathuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToathuocRepository extends JpaRepository<Toathuoc, Integer> {
    // Các phương thức tùy chỉnh có thể được thêm ở đây nếu cần
}