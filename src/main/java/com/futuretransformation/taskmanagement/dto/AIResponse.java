package com.futuretransformation.taskmanagement.dto;

public class AIResponse {
    private String description;
    private String priority;
    private String estimatedEffort;

    public AIResponse(String description, String priority, String estimatedEffort) {
        this.description = description;
        this.priority = priority;
        this.estimatedEffort = estimatedEffort;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getEstimatedEffort() { return estimatedEffort; }
    public void setEstimatedEffort(String estimatedEffort) { this.estimatedEffort = estimatedEffort; }
}