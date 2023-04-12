package com.example.know.timer;

import com.example.know.dao.ScheduleTaskDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author bookWorm
 */
@Component
public class ScheduleTask {
    private Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    private ScheduleTaskDao scheduleTaskDao;

    /**
     * 统计帖子热度
     * 热度规则 : access*1 + comment*3 + like*2 + collection*5
     * 0 15 10 ? * *
     */
    @Scheduled(cron = "0 15 10 ? * *")
    public void StatisticsOfPostPopularity() {
        try {
            scheduleTaskDao.calculateHeat();
        } catch (Exception e) {
            logger.error("统计热度失败" + e);
        }
        logger.info("统计热度成功");
    }
}
