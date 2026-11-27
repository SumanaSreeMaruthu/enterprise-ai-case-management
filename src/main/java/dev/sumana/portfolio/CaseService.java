package dev.sumana.portfolio;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class CaseService {

    private final CaseRepository repository;
    private final GeminiService gemini;

    public CaseService(CaseRepository repository, GeminiService gemini) {
        this.repository = repository;
        this.gemini = gemini;
    }

    public List<CaseRecord> list() {
        return repository.findAll();
    }

    public CaseRecord get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found"));
    }

    @Transactional
    public CaseRecord create(ApiController.CreateRequest request) {
        return repository.save(new CaseRecord(request.title(), request.notes()));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        }
        repository.deleteById(id);
    }

    @Transactional
    public CaseRecord update(Long id, String status, String assignedTo) {
        CaseRecord record = get(id);
        if (!java.util.Set.of("OPEN", "IN_PROGRESS", "UNDER_REVIEW", "CLOSED").contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported status");
        }
        record.setStatus(status);
        record.setAssignedTo(assignedTo == null ? "" : assignedTo);
        return repository.save(record);
    }

    public String summarize(Long id) {
        return gemini.summarize(get(id).getNotes());
    }
    public List<CaseRecord> search(String value) {
        if (value == null || value.isBlank()) {
            return repository.findAll();
        }
        return repository.findByStatusIgnoreCase(value.trim());
    }
}
