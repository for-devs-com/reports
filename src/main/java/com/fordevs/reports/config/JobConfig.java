package com.fordevs.reports.config;

import com.fordevs.reports.model.InputTxtFile;
import com.fordevs.reports.writer.ConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


@Configuration
public class JobConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private FlatFileItemReader flatFileItemReader;
    @Autowired private ConsoleItemWriter consoleItemWriter;

    @Bean
    public Job chunkJob(){
        return jobBuilderFactory
                .get("chunkJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }

    @Bean
    public Step chunkStep(){
        return stepBuilderFactory.get("chunkStep")
                .<InputTxtFile, InputTxtFile>chunk(3)
                .reader(flatFileItemReader())
                .writer(writer())
                .build();
    }

    //@SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FlatFileItemReader<InputTxtFile> flatFileItemReader() {
        //Create reader instance
       FlatFileItemReader<InputTxtFile> flatFileItemReader = new FlatFileItemReader<InputTxtFile>();

        //Set input file location
        flatFileItemReader.setResource(new FileSystemResource("InputFiles/students.csv"));

        //Set number of lines to skips. Use it if file has header rows.
        flatFileItemReader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        flatFileItemReader.setLineMapper(new DefaultLineMapper<InputTxtFile>() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer("|") {
                    {
                        setNames("ID", "First Name", "Last Name", "Email");
                    }
                });
                //Set values in InputTxtFile class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<InputTxtFile>() {
                    {
                        setTargetType(InputTxtFile.class);
                    }
                });
            }
        });
        return flatFileItemReader;
    }

    @Bean
    public ConsoleItemWriter<InputTxtFile> writer()
    {
        return new ConsoleItemWriter<InputTxtFile>();
    }

}
