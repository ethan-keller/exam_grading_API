package nl.tudelft.sem10.userservice.repositories;

import nl.tudelft.sem10.userservice.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
