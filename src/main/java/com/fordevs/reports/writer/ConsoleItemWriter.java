package com.fordevs.reports.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;

import java.util.List;

public class ConsoleItemWriter<InputFile> implements ItemWriter<InputFile> {
    @Override
    public void write(List<? extends InputFile> items) throws Exception, ParseException {
        System.out.println("Chunking...");
        for (InputFile item : items) {
            System.out.println(item);
        }
    }
}