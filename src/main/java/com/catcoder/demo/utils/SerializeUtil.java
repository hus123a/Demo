package com.catcoder.demo.utils;


import java.io.*;

/**
 * @ProjectName: demo
 * @Package: com.catcoder.demo.utils
 * @ClassName: SerializeUtil
 * @Author: DELL
 * @Description: 序列化工具类
 * @Date: 2020/6/9 16:57
 * @Version: 1.0
 */
public class SerializeUtil {
    /**
     * 序列化对象
     * @param obj
     * @return
     */
    public static byte[] serialize(Object obj){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(obj);
            byte[] byteArray = baos.toByteArray();
            return byteArray;

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     * @param byteArray
     * @return
     */
    public static Object unSerialize(byte[] byteArray){
        ByteArrayInputStream bais = null;
        try {
            //反序列化为对象
            bais = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
