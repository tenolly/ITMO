package client.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.common.base.Predicate;

import client.managers.execptions.RecursionFoundException;
import shared.handlers.Enviroment;

/**
* Stores and provides interaction with the CollectionHandler and CommandHandler
*/
public class ClientEnviroment extends Enviroment {
    private ArrayList<Scanner> scanners = new ArrayList<Scanner>();

    public ClientEnviroment() {}

    public ArrayList<String> getListOfLines(String filename, Predicate<String> recursionEntryPoint) throws RecursionFoundException, IOException {
        return getListOfLines(filename, new ArrayList<String>(), new ArrayList<String>(), recursionEntryPoint);
    }

    public ArrayList<String> getListOfLines(String filename, ArrayList<String> linesAlreadyRead, ArrayList<String> filesAlreadyRead, Predicate<String> recursionEntryPoint) throws RecursionFoundException, IOException {
        if (filesAlreadyRead.contains(filename)) throw new RecursionFoundException("Recursion found");
        
        filesAlreadyRead.add(new File(filename).getAbsolutePath());
        
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (recursionEntryPoint.apply(line)) {
                String[] parts = line.trim().split("\\s+", 2);
                linesAlreadyRead = getListOfLines(parts[1], linesAlreadyRead, filesAlreadyRead, recursionEntryPoint);
            } else {
                linesAlreadyRead.add(line);
            }
        }
        reader.close();

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
