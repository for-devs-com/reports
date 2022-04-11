package com.fordevs.reports.writer;

import java.util.List;

import com.fordevs.reports.model.InputTxtFile;
import org.springframework.batch.item.ItemWriter;

public class ConsoleItemWriter<I> implements ItemWriter<InputTxtFile> {
    @Override
    public void write(List<? extends InputTxtFile> items) throws Exception {
        System.out.println("Inside Item Writer");
        items.stream().forEach(System.out::println);
    }
}