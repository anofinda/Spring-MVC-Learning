package com.example.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dongyudeng
 */
@Component
public class TaskService {
    final Logger logger= LoggerFactory.getLogger(getClass());

    @Scheduled(initialDelay = 60_000,fixedRate = 60_000)
    public void checkSystemStatus(){
        logger.info("Start check system status...");
    }

    @Scheduled(cron = "${task.report:0 15 2 * * *}")
    public void cronDailyReport() {
        logger.info("Start daily report task...");
    }
}
