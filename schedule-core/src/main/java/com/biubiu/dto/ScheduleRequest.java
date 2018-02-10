package com.biubiu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 张海彪
 * @create 2018-02-11 上午2:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest implements Serializable {

    private static final long serialVersionUID = -1950734458436524632L;

    @NotEmpty(message = "任务ID不能为空")
    private String taskId;

    @NotEmpty(message = "cron表达式不能为空")
    private String cron;

    @NotEmpty(message = "任务调度bean名称不能为空")
    private String beanName;

    private Map args;

}
