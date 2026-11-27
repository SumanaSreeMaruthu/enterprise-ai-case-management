package dev.sumana.portfolio;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/cases")
public class ApiController {

    public record CreateRequest(@NotBlank String title, String notes) {
    }

    public record UpdateRequest(@NotBlank String status, String assignedTo) {
    }

    private final CaseRepository repository;
    private final GeminiService gemini;

    public ApiController(CaseRepository repository, GeminiService gemini) {
        this.repository = repository;
        this.gemini = gemini;
    }

    @GetMapping
    public List<CaseRecord> list() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CaseRecord create(@Valid @RequestBody CreateRequest request) {
        return repository.save(new CaseRecord(request.title(), request.notes()));
    }

    @GetMapping("/{id}")
    public CaseRecord get(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found"));
    }

    @PatchMapping("/{id}")
    public CaseRecord update(@PathVariable Long id, @Valid @RequestBody UpdateRequest request) {
        CaseRecord record = get(id);
        record.setStatus(request.status());
        record.setAssignedTo(request.assignedTo() == null ? "" : request.assignedTo());
        return repository.save(record);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found");
        }
        repository.deleteById(id);
    }

    @PostMapping("/{id}/ai-summary")
    public Map<String, String> summarize(@PathVariable Long id) {
        return Map.of("summary", gemini.summarize(get(id).getNotes()));
    }
}
