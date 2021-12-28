package com.oop.evolution;

import com.opencsv.CSVWriter;

import java.io.FileWriter;


public class HandlerCSV {
    public static void addRecord(String newRecord) throws Exception {
        String csv = "./src/main/csv/data.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv, true));

        String[] record = newRecord.split(",");

        writer.writeNext(record);
        writer.close();
    }
}
