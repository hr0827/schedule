package com.biubiu.util;

import com.biubiu.dto.DelayScheduleRequest;
import com.biubiu.service.ScheduleService;
import com.biubiu.model.DelayedElement;
import com.biubiu.utils.SpringContextHolder;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 张海彪
 * @create 2018-02-11 上午3:42
 */
public class DelayQueuedUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayQueuedUtils.class);

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private static final DelayQueue<DelayedElement> DELAY_QUEUE = new DelayQueue<>();

    /**
     * 添加待处理的延迟任务
     *
     * @param request 请求体
     * @param delay   延迟间隔
     */
    public static void process(DelayScheduleRequest request, long delay) {
        produce(request, delay);
    }

    /**
     * 取消延迟任务
     *
     * @param taskId 任务Id
     * @return 操作结果
     */
    public static boolean remove(String taskId) {
        if (Strings.isNullOrEmpty(taskId)) return false;
        return DELAY_QUEUE.remove(new DelayedElement(0, taskId));
    }

    private static void produce(DelayScheduleRequest request, long delay) {
        DELAY_QUEUE.add(new DelayedElement(delay, request));
    }

    static {
        EXECUTOR_SERVICE.execute(() -> {
            while (true) {
                try {
                    DelayedElement<DelayScheduleRequest> delayedElement = DELAY_QUEUE.take();
                    DelayScheduleRequest request = delayedElement.getT();
                    Object object = SpringContextHolder.getBean(request.getBeanName());
                    ScheduleService scheduleService;
                    if (object instanceof ScheduleService) {
                        scheduleService = (ScheduleService) object;
                        scheduleService.schedule(request.getArgs());
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        });
    }

}
