package com.biubiu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;

/**
 * @author 张海彪
 * @create 2018-02-11 上午4:17
 */
public class SerializationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationUtils.class);

    /**
     * 序列化文件
     *
     * @param object   序列化实体
     * @param filename 序列化文件全路径名
     */
    public static boolean write(Object object, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(object);
            oos.flush();
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * 反序列化文件
     *
     * @param filename 反序列化文件全路径名
     * @return 反序列化实体
     */
    public static Object read(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * 删除序列化文件
     *
     * @param filename 序列化文件全路径名
     * @return 处理结果
     */
    public static boolean delete(String filename) {
        File file = new File(filename);
        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
