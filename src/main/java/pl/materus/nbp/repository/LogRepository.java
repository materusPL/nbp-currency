package pl.materus.nbp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.materus.nbp.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}