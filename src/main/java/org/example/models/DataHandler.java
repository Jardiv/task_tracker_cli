package org.example.models;

import static org.example.utils.ForegroundColors.ANSI_GREEN;
import static org.example.utils.ForegroundColors.ANSI_RED;
import static org.example.utils.ForegroundColors.ANSI_RESET;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Date;

import org.example.utils.FileHandler;


public class DataHandler {
    Gson gson = new GsonBuilder()
            .setDateFormat("MMM d, yyyy, h:mm:ss/a")
            .create();
    FileHandler fileHandler = new FileHandler();

    // Getters
    public ArrayList<Task> getTasks() {
        return getTasks("all");
    }

    public ArrayList<Task> getTasks(String option) {
        option = option.toUpperCase();
        ArrayList<String> tasksStr = fileHandler.getTasks();
        ArrayList<Task> tasks = new ArrayList<>();
        for (String taskStr : tasksStr) {
            Task task = gson.fromJson(taskStr, Task.class);
            if(task.getStatus().toString().equals(option) || option.equals("ALL")){
                tasks.add(task);
            }
        }
        return tasks;
    }
    public Task getTask(Object taskId){
        try {
            int id = taskId.getClass() == String.class ? Integer.parseInt((String) taskId) : (int) taskId;

            ArrayList<Task> tasks = getTasks();
            for (Task task : tasks) {
                if (task.getId() == id) {
                    return task;
                }
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public TaskStatus getStatus(String status) {
        try {
            return TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    private ArrayList<String> toStringArray(ArrayList<Task> tasks){
        ArrayList<String> tasksStr = new ArrayList<>();
        for (Task task : tasks) {
            tasksStr.add(gson.toJson(task));
        }
        return tasksStr;
    }

    // Setters / Adders
    public boolean addTask(String description) {
        if(description.isEmpty()){
            return false;
        }
        Task lastTask = gson.fromJson(fileHandler.getLastTask(), Task.class);
        int id = 1;
        if(lastTask != null){
            id = lastTask.getId() + 1;
        }
        Task task = new Task(id, description, TaskStatus.TODO);

        ArrayList<String> tasks = fileHandler.getTasks();
        tasks.add(gson.toJson(task));

        fileHandler.writeToFile(tasks);
        return true;
    }

    public String updateTask(String taskIdStr, String statusStr) {
        int taskId;

        if(statusStr.equalsIgnoreCase("in progress") || statusStr.equalsIgnoreCase("in-progress")){
            statusStr = "in_progress";
        }

        TaskStatus status = getStatus(statusStr);
        ArrayList<Task> tasks = getTasks();
        if(status == null) {
            return ANSI_RED + "Invalid status" + ANSI_RESET;
        }
        try{
            taskId = Integer.parseInt(taskIdStr);
        } catch (NumberFormatException e) {
            return ANSI_RED + "Invalid task id" + ANSI_RESET;
        }


        Task targetTask = null;
        for (Task task : tasks) {
            if(task.getId() == taskId){
                task.setStatus(status);
                task.setUpdated_at(new Date());
                targetTask = task;
                break;
            }
        }
        if (targetTask == null) return ANSI_RED + "Task not found" + ANSI_RESET;

        ArrayList<String> tasksStr = toStringArray(tasks);
        fileHandler.writeToFile(tasksStr);
        return ANSI_GREEN + "Task updated" + ANSI_RESET + "\n" + targetTask;
    }

    // Removers

    public void deleteTask(Task targetTask) {
        ArrayList<Task> tasks = getTasks();
        for(Task task : tasks){
            if(task.getId() == targetTask.getId()){
                tasks.remove(task);
                break;
            }
        }
        ArrayList<String> tasksStr = toStringArray(tasks);
        fileHandler.writeToFile(tasksStr);
    }

    public void deleteAllTasks(){
        ArrayList<Task> tasks = getTasks();
        tasks.clear();
        ArrayList<String> tasksStr = toStringArray(tasks);
        fileHandler.writeToFile(tasksStr);
    }

}
