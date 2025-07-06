package org.example.models;

import java.util.Date;
import static org.example.utils.ForegroundColors.*;

public class Task {
    private final int id;
    private final String description;
    private TaskStatus status;
    private final Date created_at;
    private Date updated_at;

    public Task(int id, String description, TaskStatus status) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.created_at = new Date();
        this.updated_at = null;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String status = this.status.toString();
        String updated_at = this.updated_at == null ? "Not Updated" : this.updated_at.toString();
        String color;
        switch (status) {
            case "DONE" -> color = ANSI_BLUE;
            case "IN_PROGRESS" -> color = ANSI_YELLOW;
            default -> color = ANSI_PURPLE;
        }

        if (status.equals("IN_PROGRESS")) status = "IN PROGRESS";
        return  ANSI_RESET + " > id: " + color + id +
                ANSI_RESET + "\n > description: " + color + description +
                ANSI_RESET + "\n > status: " + color + status +
                ANSI_RESET + "\n > created_at: " + color + created_at +
                ANSI_RESET + "\n > updated_at: " + color + updated_at +
                ANSI_RESET + "\n";
    }
}
