package client.managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.common.base.Predicate;

import utils.database.User;
import utils.handlers.CommandHandler;
import utils.handlers.Enviroment;

/**
* Stores and provides interaction with the CollectionHandler and CommandHandler
*/
public class ClientEnviroment extends Enviroment {
    private User user;
    
    private CommandHandler commandHandler;
    private ArrayList<Scanner> scanners = new ArrayList<Scanner>();

    public ClientEnviroment(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getListOfLines(String filename, ArrayList<String> linesAlreadyRead, ArrayList<String> filesAlreadyRead, Predicate<String> recursionEntryPoint) {
        if (filename == null) {
            linesAlreadyRead.add("You must type filename");
            return linesAlreadyRead;
        } else if (filesAlreadyRead.contains(filename)) {
            System.out.println("\nRecursion found, skipped this file: " + filename);
            return linesAlreadyRead;
        };
        filesAlreadyRead.add(filename);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (recursionEntryPoint.apply(line)) {
                    linesAlreadyRead = getListOfLines(reader.readLine(), linesAlreadyRead, filesAlreadyRead, recursionEntryPoint);
                } else {
                    linesAlreadyRead.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Can't read file: " + filename);
        }

        return linesAlreadyRead;
    }

    public void addScanner(Scanner scanner) {
        scanners.add(scanner);
    }

    public Scanner getScanner() {
        return scanners.get(scanners.size() - 1);
    }

    public int getScannersStackSize() {
        return scanners.size();
    }

    public void popScanner() {
        this.scanners.remove(scanners.size() - 1);
    }
}
