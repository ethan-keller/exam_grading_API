package nl.tudelft.sem10.userservice.repositories;

import nl.tudelft.sem10.userservice.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
