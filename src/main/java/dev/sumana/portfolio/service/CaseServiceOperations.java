package dev.sumana.portfolio.service;

import dev.sumana.portfolio.CaseRecord;
import java.util.List;

public interface CaseServiceOperations {
    List<CaseRecord> findAll();
    CaseRecord findById(Long id);
}
