package com.nh.boom;

public class Alert {
    private String title;
    private String description;
    private String severity;
    private boolean resolve;
    private boolean acknowledged;
    private String createdTimestampUsecs;
    private String entityId;

    public Alert() {
    }

    public Alert(String title, String description, String severity, boolean resolve, boolean acknowledged, String createdTimestampUsecs, String entityId) {
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.resolve = resolve;
        this.acknowledged = acknowledged;
        this.createdTimestampUsecs = createdTimestampUsecs;
        this.entityId = entityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public boolean isResolve() {
        return resolve;
    }

    public void setResolve(boolean resolve) {
        this.resolve = resolve;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public String getCreatedTimestampUsecs() {
        return createdTimestampUsecs;
    }

    public void setCreatedTimestampUsecs(String createdTimestampUsecs) {
        this.createdTimestampUsecs = createdTimestampUsecs;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", severity='" + severity + '\'' +
                ", resolve=" + resolve +
                ", acknowledged=" + acknowledged +
                ", createdTimestampUsecs='" + createdTimestampUsecs + '\'' +
                ", entityId='" + entityId + '\'' +
                '}';
    }
}
