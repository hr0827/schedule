package com.biubiu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author 张海彪
 * @create 2018-02-11 上午3:36
 */
@Data
public class DelayScheduleRequest implements Serializable {

    private static final long serialVersionUID = 1211206324436640041L;

    @NotEmpty(message = "任务ID不能为空")
    private String taskId;

    @NotNull(message = "触发时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT-8")
    private Date triggerTime;

    @NotEmpty(message = "任务调度bean名称不能为空")
    private String beanName;

    private Map args;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        DelayScheduleRequest that = (DelayScheduleRequest) o;
        return taskId != null ? taskId.equals(that.taskId) : that.taskId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        return result;
    }

}
