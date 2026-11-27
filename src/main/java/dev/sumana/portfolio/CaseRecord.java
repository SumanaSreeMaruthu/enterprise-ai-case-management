package dev.sumana.portfolio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class CaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String status = "OPEN";

    private String assignedTo = "";

    @jakarta.persistence.Column(length = 10000)
    private String notes = "";

    protected CaseRecord() {
    }

    public CaseRecord(String title, String notes) {
        this.title = title;
        this.notes = notes == null ? "" : notes;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getAssignedTo() { return assignedTo; }
    public String getNotes() { return notes; }
    public void setStatus(String status) { this.status = status; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
}
