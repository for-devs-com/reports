package com.fordevs.reports.config;
import com.fordevs.reports.model.InputFile;
import com.fordevs.reports.writer.ConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;


@Configuration
public class JobConfig {

    @Autowired JobBuilderFactory jobBuilderFactory;
    @Autowired StepBuilderFactory stepBuilderFactory;

    @Value("input/txtstudents.txt")
    private Resource[] inputResources;

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
                .<InputFile, InputFile>chunk(3)
                .reader(itemReader() )
                .writer(consoleItemWriter())
                .build();
    }


    @Bean
    public FlatFileItemReader<InputFile> itemReader(){
        //Create reader instance
        FlatFileItemReader<InputFile> itemReader =
                new FlatFileItemReader<>();

        //Set input file location
        itemReader.setResource(new FileSystemResource("input/txtstudents.txt"));

        //Set number of lines to skips. Use it if file has header rows.
        itemReader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        itemReader.setLineMapper(new DefaultLineMapper<>() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer("|") {
                    {
                        setNames("ID", "First Name", "Last Name", "Email");
                    }
                });
                //Set values in InputFile class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(InputFile.class);
                    }
                });
            }
        });
        return itemReader;
    }

    // Multiple flat files reader
    @Bean
    public MultiResourceItemReader<InputFile> multiResourceItemReader()
    {
        MultiResourceItemReader<InputFile> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(itemReader());
        return resourceItemReader;
    }

    @Bean
    public ConsoleItemWriter<InputFile> consoleItemWriter(){
        return new ConsoleItemWriter<>();
    }

}
