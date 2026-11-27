package dev.sumana.portfolio;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cases")
public class ApiController {

    public record CreateRequest(@NotBlank String title, String notes) {}\n    public record UpdateRequest(@NotBlank String status, String assignedTo) {}

    private final CaseService service;

    public ApiController(CaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<CaseRecord> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public CaseRecord get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CaseRecord create(@Valid @RequestBody CreateRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}")
    public CaseRecord update(@PathVariable Long id, @Valid @RequestBody UpdateRequest request) {
        return service.update(id, request.status(), request.assignedTo());
    }

    @PostMapping("/{id}/ai-summary")
    public Map<String, String> summarize(@PathVariable Long id) {
        return Map.of("summary", service.summarize(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
