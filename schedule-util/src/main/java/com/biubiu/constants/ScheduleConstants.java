package com.biubiu.constants;

/**
 * @author 张海彪
 * @create 2018-02-11 上午4:14
 */
public enum ScheduleConstants {
    TEST(BeanNameConstants.TEST);

    ScheduleConstants(String beanName) {
        this.beanName = beanName;
    }

    private String beanName;

    public String getBeanName() {
        return beanName;
    }
}
