package com.biubiu.controller;

import com.biubiu.dto.DelayScheduleRequest;
import com.biubiu.dto.ScheduleRequest;
import com.biubiu.handler.DynamicSchedulingHandler;
import com.biubiu.service.ScheduleService;
import com.biubiu.model.Response;
import com.biubiu.util.DelayQueuedUtils;
import com.biubiu.util.SerializationUtils;
import com.biubiu.utils.DateUtils;
import com.biubiu.utils.SpringContextHolder;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;

/**
 * @author 张海彪
 * @create 2018-02-11 上午2:51
 */
@RestController
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {

    @Autowired
    private DynamicSchedulingHandler handler;

    /**
     * 添加定时任务
     *
     * @param request 请求实体
     * @return 响应信息实体
     */
    @PostMapping("/add")
    public Response schedule(@RequestBody @Valid ScheduleRequest request) {
        ScheduleService scheduleService = SpringContextHolder.getApplicationContext().getBean(request.getBeanName(), ScheduleService.class);
        Trigger trigger = new CronTrigger(request.getCron());
        try {
            handler.addTriggerTask(request.getTaskId(), new TriggerTask(() -> scheduleService.schedule(request.getArgs()), trigger));
            SerializationUtils.write(request, request.getTaskId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.fail();
        }
        return Response.success();
    }

    /**
     * 取消定时任务
     *
     * @param taskId 任务Id
     * @return 响应信息实体
     */
    @GetMapping("/cancel")
    public Response cancel(String taskId) {
        if (Strings.isNullOrEmpty(taskId)) {
            return Response.fail();
        }
        try {
            handler.cancelTriggerTask(taskId);
            SerializationUtils.delete(taskId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.fail();
        }
        return Response.success();
    }

    /**
     * 重制定时任务执行频率
     *
     * @param taskId 任务Id
     * @param cron   新的cron表达式
     * @return 响应信息实体
     */
    @GetMapping("/reset/{taskId}")
    public Response resetTaskCron(@PathVariable("taskId") String taskId, @RequestParam("cron") String cron) {
        if (Strings.isNullOrEmpty(taskId) || Strings.isNullOrEmpty(cron)) {
            return Response.fail();
        }
        try {
            SerializationUtils.delete(taskId);
            handler.resetTriggerTask(taskId, cron);
            ScheduleRequest request = ScheduleRequest.builder()
                    .taskId(taskId)
                    .cron(cron)
                    .beanName(null)
                    .args(null)
                    .build();
            SerializationUtils.write(request, taskId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.fail();
        }
        return Response.success();
    }

    /**
     * 添加延迟任务
     *
     * @param request 请求实体
     * @return 响应信息实体
     */
    @PostMapping("/delay/add")
    public Response delaySchedule(@RequestBody @Valid DelayScheduleRequest request) {
        long delay = compute(DateUtils.now(), request.getTriggerTime());
        try {
            DelayQueuedUtils.process(request, delay);
            SerializationUtils.write(request, request.getTaskId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.fail();
        }
        return Response.success();
    }

    /**
     * 取消延迟任务
     *
     * @param taskId 任务Id
     * @return 响应信息实体
     */
    @GetMapping("/delay/cancel")
    public Response cancelDelay(String taskId) {
        if (Strings.isNullOrEmpty(taskId)) {
            return Response.fail();
        }
        boolean cancel = DelayQueuedUtils.remove(taskId);
        if (cancel) {
            SerializationUtils.delete(taskId);
            return Response.success();
        } else {
            return Response.fail();
        }
    }

    private long compute(Date now, Date triggerTime) {
        Instant from = Instant.ofEpochMilli(now.getTime());
        Instant to = Instant.ofEpochMilli(triggerTime.getTime());
        long duration = from.minusMillis(to.toEpochMilli()).getEpochSecond();
        return duration < 0 ? 0 : duration;
    }

}
