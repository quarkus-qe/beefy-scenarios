package io.quarkus.qe.validation.utils;

public class ValidationErrorResponse {
    private String title;
    private String detail;
    private int status;
    private ValidationError[] violations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
