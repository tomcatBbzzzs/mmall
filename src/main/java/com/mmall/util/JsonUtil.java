package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author 唐孟廷
 * @desc json工具类
 * @date 2020/5/12 - 17:10
 */
@Slf4j
public class JsonUtil {
    /**
     * 读写核心类
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 很重要, 一些配置初始化
     */
    static {
        // 对象的所有字段全部列入序列化,这里JsonSerialize.Inclusion提供了几种方案:
        //      ALWAYS      所有字段都进行序列化
        //      NON_NULL    非空字段进行序列化
        //      NON_DEFAULT 值和默认值不同的进行序列化
        //      NON_EMPTY   值为null或空内容的进行序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        // 取消默认转换timestamps形式, json序列化会将date转换为timestamp
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        // 所有的日期格式都统一为一下的样式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 忽略 再json字符串中存在, 但是再java对象中不存在对应属性的情况. 防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将实例转换为字符串
     *
     * @param bean 具体实例
     * @param <T>  实例类型
     * @return 返回序列化后的字符串结果
     */
    public static <T> String beanToString(T bean) {
        if (bean == null) {
            return null;
        }
        try {
            return bean instanceof String ? (String) bean : objectMapper.writeValueAsString(bean);
        } catch (Exception e) {
            log.warn("序列化bean异常", e);
            return null;
        }
    }

    /**
     * 将实例转换为字符串,并且格式化输出
     *
     * @param bean 具体实例
     * @param <T>  实例类型
     * @return 返回序列化后的字符串结果
     */
    public static <T> String beanToStringPretty(T bean) {
        if (bean == null) {
            return null;
        }
        try {
            return bean instanceof String ? (String) bean : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
        } catch (Exception e) {
            log.warn("序列化bean异常", e);
            return null;
        }
    }


    /**
     * 将json字符串转换为指定的实体类实例
     *
     * @param json      json
     * @param beanClass 返回具体类类型
     * @param <T>       实例类型
     * @return 返回json转换为指定实体类后的实例
     */
    public static <T> T stringToBean(String json, Class<T> beanClass) {
        if (StringUtils.isEmpty(json) || beanClass == null) {
            return null;
        }

        try {
            return beanClass.equals(String.class) ? (T) json : (T) objectMapper.readValue(json, beanClass);
        } catch (IOException e) {
            log.warn("json转换为bean异常", e);
            return null;
        }
    }


    /**
     * 将json转换为指定类型的实例,支持泛型,比如 List<User>
     *
     * @param json          json
     * @param typeReference 返回具体类类型
     * @param <T>           实例类型
     * @return 返回json转换为指定实体类后的实例
     */
    public static <T> T stringToBean(String json, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(json) || typeReference == null) {
            return null;
        }

        try {
            return (T) (typeReference.getType().equals(String.class) ? json : objectMapper.readValue(json, typeReference));
        } catch (Exception e) {
            log.warn("json转换为bean异常", e);
            return null;
        }
    }


    /**
     * 将json转换为指定类型的实例,支持泛型,比如 List<User>
     *
     * @param json            json
     * @param collectionClass 集合类型,比如List.class, Set.class
     * @param elementClass    具体泛型类,比如User.class
     * @param <T>             返回泛型
     * @return 返回json转换为指定实体类后的实例
     */
    public static <T> T stringToBean(String json, Class<? extends Collection> collectionClass, Class<?>... elementClass) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);

        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.warn("json转换为bean异常", e);
            return null;
        }


    }

    public static void main(String[] args) {
        User user1 = new User();
        user1.setUsername("Rosen");
        user1.setPassword("1234");
        user1.setEmail("123456789@163.com");
        user1.setCreateTime(new Date());
        user1.setRole(Const.Role.ROLE_ADMIN);


        User user2 = new User();
        user2.setUsername("Geely");
        user2.setPassword("123456");
        user2.setUpdateTime(new Date());
        user2.setEmail("123456789@163.com");
        user2.setRole(Const.Role.ROLE_CUSTOMER);

        List<User> userList = Lists.newArrayList(user1, user2);
        String json = beanToStringPretty(userList);

        System.out.println(json);

        List<User> transferUserList1 = stringToBean(json, new TypeReference<List<User>>() {
        });

        transferUserList1.forEach(System.out::println);

        List<User> transferUserList2 = stringToBean(json, List.class, User.class);
        transferUserList2.forEach(System.out::println);

    }


}
