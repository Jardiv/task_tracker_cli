package org.example;

import org.example.models.DataHandler;
import org.example.models.Task;
import static org.example.utils.ForegroundColors.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    final static DataHandler dataHandler = new DataHandler();
    final static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Task CLI");
        label:
        while(true){
            String commandStr = getInput(ANSI_YELLOW +"\ntask-cli: " + ANSI_RESET).trim();
            String command = commandStr.split(" ")[0].toLowerCase();
            commandStr = commandStr.replaceFirst(command, "");
            commandStr = commandStr.trim();
            clearScreen();
            switch (command) {
                case "0":
                case "exit": {
                    break label;
                }
                case "9":
                case "help": {
                    help();
                    break;
                }
                case "1":
                case "add":{
                    addTask(commandStr);
                    break;
                }
                case "2":
                case "list": {
                    list(commandStr);
                    break;
                }
                case "3":
                case "set":
                case "update": {
                    String [] commandStrs = commandStr.split(" ", 2);
                    setStatus(commandStrs);
                    break;
                }
                case "4":
                case "delete": {
                    delete(commandStr);
                    break;
                }
                default:{
                    System.out.println(ANSI_RED + "Invalid command" + ANSI_RESET);
                    System.out.println("Type '" + ANSI_YELLOW + "help" + ANSI_RESET + "' for commands");
                    break;
                }
            }
        }
    }

    static void help() {
        final String primary = ANSI_CYAN;
        final String reset = ANSI_RESET;
        final String sample = ANSI_YELLOW;
        final String numcol = ANSI_CYAN;
        final String commands =
            "> " + numcol + 9 + reset +" '" + primary + "help" + reset + "'\n" +
            "> " + numcol + 0 + reset +" '" + primary + "exit" + reset + "'\n" +
            "> " + numcol + 1 + reset +" '" + primary + "add (description)" + reset + "' ex. '" +
                    sample + "add buy milk"+ reset + "'\n" +
            "> " + numcol + 2 + reset +" '" + primary + "list" + reset + "'\n" +
            "> " + numcol + 2 + reset +" '" + primary + "list (status)" + reset +
            "> " + numcol + 3 + reset +" '" + primary + "set (task-id) (status)" + reset + "'\n" +
                    sample + "    status: todo, in progress, done" + reset + "\n" +
            "> " + numcol + 4 + reset +" '" + primary + "delete (task-id)" + reset + "'\n" +
            "> " + numcol + 4 + reset +" '" + primary + "delete all" + reset + "'\n";

        System.out.println("Enter the number or name of the command");
        System.out.println(ANSI_GREEN + "Commands" + ANSI_RESET + ": ");
        System.out.println(commands);
    }

    static void addTask(String description) {
        if (description.isEmpty()) {
            System.out.println(ANSI_RED + "Invalid description" + ANSI_RESET);
            System.out.println("command format: add (description)");
            return;
        }
        boolean isAdded = dataHandler.addTask(description);
        if(isAdded){
            System.out.println(ANSI_GREEN + "Task added" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Task unable to add" + ANSI_RESET);
        }
    }

    static void list(String status) {
        status = status.toLowerCase();
        if (status.contains("in progress") || status.contains("in-progress")) {
            status = "in_progress";
        }
        if(dataHandler.getStatus(status) == null && !status.isEmpty() && !status.equals("all")){
            System.out.println(ANSI_RED + "Invalid status" + ANSI_RESET);
            System.out.println("status: todo, in progress, done");
            return;
        }

        if(status.isEmpty()) {
            status = "all";
        }

        ArrayList<Task> tasks = dataHandler.getTasks(status);
        status = status.equals("in_progress") ? "In Progress" : status;
        status = status.toUpperCase().charAt(0) + status.substring(1);
        System.out.println(ANSI_GREEN + status + " tasks: " + ANSI_RESET);
        if(tasks.isEmpty()){
            String msg = status.equals("All") ? "No tasks found" : "No " + status + " tasks found";
            System.out.println(ANSI_PURPLE + msg + ANSI_RESET);
        } else {
            for (Task task : tasks) {
                System.out.println(task.toString());
            }
        }
    }

    static void setStatus(String [] commandStr) {
        if(commandStr.length != 2) {
            System.out.println(ANSI_RED + "Invalid command" + ANSI_RESET);
            System.out.println("command format: set (task-id) (status)");
            System.out.println("status: todo, in-progress, done");
            return;
        }
        String updateMsg = dataHandler.updateTask(commandStr[0], commandStr[1].toUpperCase());
        System.out.println(updateMsg);
    }

    static void delete(String id){
        if (id.isEmpty()) {
            System.out.println(ANSI_RED + "Invalid command format" + ANSI_RESET);
            System.out.println("command format: delete (task-id)");
            return;
        }

        String deleteMsg;
        if(id.equalsIgnoreCase("all")){
            boolean confirm = confirmDelete(ANSI_YELLOW + "Are you sure you want to"+ ANSI_RED +" delete all tasks" + ANSI_YELLOW + "?");
            if (!confirm) { return; }

            dataHandler.deleteAllTasks();
            deleteMsg = ANSI_GREEN + "All tasks deleted" + ANSI_RESET;
        } else {
            Task task = dataHandler.getTask(id);
            if (task == null) {
                System.out.println(ANSI_RED + "Invalid task id" + ANSI_RESET);
                return;
            }

            boolean confirm = confirmDelete(ANSI_YELLOW + "Are you sure you want to delete this task? \n" +
                    ANSI_RESET + task);
            if (!confirm) {
                return;
            }

            dataHandler.deleteTask(task);
            deleteMsg = ANSI_GREEN + "Task deleted" + ANSI_RESET;
        }
        System.out.println(deleteMsg);
    }

    static boolean confirmDelete(String prompt) {
        String confirm = getInput(ANSI_YELLOW + prompt + "\n" +
                "("+ANSI_RED + "YES" + ANSI_RESET + "/NO): ");
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled");
            return false;
        }
        return true;
    }


    // Helper functions
    public static String getInput(Object prompt) {
        System.out.print(prompt);
        return scan.nextLine();
    }

    public static void clearScreen() {

        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            System.out.println("Unable to clear screen: " + ex.getMessage());
        }

    }


}