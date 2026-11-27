package dev.sumana.portfolio.service.impl;

import dev.sumana.portfolio.CaseRecord;
import dev.sumana.portfolio.CaseService;
import dev.sumana.portfolio.service.CaseServiceOperations;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CaseServiceImpl implements CaseServiceOperations {

    private final CaseService delegate;

    public CaseServiceImpl(CaseService delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<CaseRecord> findAll() {
        return delegate.list();
    }

    @Override
    public CaseRecord findById(Long id) {
        return delegate.get(id);
    }
}
