package kz.aibek.spring.rest_api_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kz.aibek.spring.rest_api_project.models.Measurement;


@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
}
