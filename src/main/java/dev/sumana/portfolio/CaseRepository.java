package dev.sumana.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<CaseRecord, Long> {
    java.util.List<CaseRecord> findByStatusIgnoreCase(String value);
}
