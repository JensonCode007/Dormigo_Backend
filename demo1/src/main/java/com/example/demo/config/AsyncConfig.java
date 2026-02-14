package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework. context.annotation.Bean;
import org.springframework.context.annotation. Configuration;
import org.springframework. scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org. springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java. util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);          // Minimum threads
        executor.setMaxPoolSize(10);          // Maximum threads
        executor.setQueueCapacity(100);       // Queue size
        executor.setThreadNamePrefix("Async-");
        executor.initialize();

        log.info("✅ Async executor configured:  5-10 threads");
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutor();
    }
}