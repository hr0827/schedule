package com.biubiu.service;

import com.biubiu.constants.BeanNameConstants;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 张海彪
 * @create 2018-02-11 上午4:13
 */
@Component(BeanNameConstants.TEST)
public class TestScheduleServiceImpl implements ScheduleService {

    @Override
    public void schedule(Map args) {
        doSchedule(args);
    }

    private void doSchedule(Map args) {
        System.out.println("test......");
    }

}
