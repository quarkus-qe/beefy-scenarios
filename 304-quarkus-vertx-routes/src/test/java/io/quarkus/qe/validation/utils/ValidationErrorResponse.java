package io.quarkus.qe.validation.utils;

public class ValidationErrorResponse {
    private String title;
    private String details;
    private int status;
    private ValidationError[] violations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ValidationError[] getViolations() {
        return violations;
    }

    public void setViolations(ValidationError[] violations) {
        this.violations = violations;
    }
}
