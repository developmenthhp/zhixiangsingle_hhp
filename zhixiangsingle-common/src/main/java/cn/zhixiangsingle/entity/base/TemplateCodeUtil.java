package cn.zhixiangsingle.entity.base;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/30 14:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class TemplateCodeUtil {
    protected static final Logger logger = LoggerFactory.getLogger(TemplateCodeUtil.class);

    // 使用多线程安全的Map来缓存BeanCopier，由于读操作远大于写，所以性能影响可以忽略
    public static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 通过cglib BeanCopier形式,使用cglib拷贝，同属性同类型拷贝
     *
     * @param source 源转换的对象
     * @param target 目标转换的对象
     */
    public static void copyProperties(Object source, Object target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier = null;
        if (beanCopierMap.containsKey(beanKey)) {
            copier = beanCopierMap.get(beanKey);
        } else {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.putIfAbsent(beanKey, copier);// putIfAbsent已经实现原子操作了。
        }
        copier.copy(source, target, null);
    }

    /**
     * 通过cglib BeanCopier形式,使用cglib拷贝，同属性同类型拷贝
     *
     * @param source 源转换的对象
     * @param target 目标转换的对象
     * @param action 支持Lambda函数给拷贝完的对象一些自定义操作
     * @return
     */
    public static <O,T> T copyProperties(O source, T target, Consumer<T> action) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier = null;
        if (beanCopierMap.containsKey(beanKey)) {
            copier = beanCopierMap.get(beanKey);
        } else {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.putIfAbsent(beanKey, copier);// putIfAbsent已经实现原子操作了。
        }
        copier.copy(source, target, null);
        action.accept(target);
        return target;
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    /**
     * transalte常规反射拷贝，同名同类型拷贝,推荐使用  transTo(O o, Class<T> clazz)
     * ×通过常规反射形式 DTO对象转换为实体对象。如命名不规范或其他原因导致失败。
     * @param t 源转换的对象
     * @param e 目标转换的对象
     *
     */
    public static <T, O> void transalte(T t, O e) {
        Method[] tms = t.getClass().getDeclaredMethods();
        Method[] tes = e.getClass().getDeclaredMethods();
        for (Method m1 : tms) {
            if (m1.getName().startsWith("get")) {
                String mNameSubfix = m1.getName().substring(3);
                String forName = "set" + mNameSubfix;
                for (Method m2 : tes) {
                    if (m2.getName().equals(forName)) {
                        // 如果类型一致，或者m2的参数类型是m1的返回类型的父类或接口
                        boolean canContinue = m2.getParameterTypes()[0].isAssignableFrom(m1.getReturnType());
                        if (canContinue) {
                            try {
                                m2.invoke(e, m1.invoke(t));
                                break;
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                                // log未配置会报 WARN No appenders could be found for
                                // logger log4j:WARN Please initialize the log4j
                                // system properly.
                                logger.debug("DTO 2 Entity转换失败");
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }

        }
        // logger.debug("转换完成");

    }

    /**
     * 对象属性拷贝，忽略源转换对象的 null值
     * @param t 源转换的对象
     * @param e 目标转换的对象
     *
     */
    public static <T, O> void copyPropertiesIgnoreNull(T t, O e) {
        final BeanWrapper bw = new BeanWrapperImpl(t);
        PropertyDescriptor[] pds = bw.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = bw.getPropertyValue(pd.getName());
            if (srcValue != null) emptyNames.add(pd.getName());
        }
        partialCopy(t, e, emptyNames.toArray());
    }

    /**
     * 对象属性拷贝，同属性同类型拷贝，忽略源转换对象不符合自定义规则的属性
     * @param t 源转换的对象
     * @param e 目标转换的对象
     * @param action  lambda传入的是t的属性名和属性值，返回true和e对象有该属性则拷贝该值
     */
    public static <T, O> void copyPropertiesIgnoreCustom(T t, O e, BiPredicate<String, Object> action) {
        final BeanWrapper bw = new BeanWrapperImpl(t);
        PropertyDescriptor[] pds = bw.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = bw.getPropertyValue(pd.getName());
            // 自定义条件的成立与否，返回true则拷贝，反之不拷贝，满足同属性同类型。
            if (action.test(pd.getName(), srcValue)) emptyNames.add(pd.getName());
        }
        partialCopy(t, e, emptyNames.toArray());
    }


    /** 同类型字段部分拷贝
     * @param t 源数据对象
     * @param e 接收对象
     * @param key 要拷贝的字段名数组
     */
    public static <T> void partialCopy(T t , T e, Object... key) {

        BeanMap t1 = BeanMap.create(t);
        BeanMap e1 = BeanMap.create(e);
        int i = key.length;
        while (i-- > 0) {
            e1.replace(key[i], t1.get(key[i]));
        }
    }


    /**
     * 对象集合转换，两个对象的属性名字需要一样
     */
    public static <T, O> List<T> transTo(List<O> fromList, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> toList = new ArrayList<>();
        for (O e : fromList) {
            T entity = clazz.newInstance();
            BeanUtils.copyProperties(e, entity);
            toList.add(entity);
        }
        return toList;
    }


    /**
     * 对象集合转换，两个对象的属性名字需要一样，并可自定义设置一些参数
     */
    public static <T, O> List<T> transTo(List<O> fromList, Class<T> clazz, OnTransListener<T, O> onTransListener) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> toList = new ArrayList<>();
        for (O e : fromList) {
            T entity = clazz.newInstance();
            BeanUtils.copyProperties(e, entity);
            if (onTransListener != null) {
                onTransListener.doSomeThing(entity, e);
            }
            toList.add(entity);
        }
        return toList;
    }

    /**
     * 使用BeanUtils，对象集合转换，两个对象的属性名字需要一样，并可自定义设置一些参数
     *
     * @param fromList 源数据List
     * @param clazz 需要转换成的clazz
     * @param action 支持lambda表达式自定义设置一些参数
     * @return
     */
    public static <T, O> List<T> transToCustom(List<O> fromList, Class<T> clazz, BiConsumer<O, T> action) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> toList = new ArrayList<>();
        for (O e : fromList) {
            T entity = clazz.newInstance();
            BeanUtils.copyProperties(e, entity);
            action.accept(e, entity);
            toList.add(entity);
        }
        return toList;
    }

    /**
     * 使用BeanUtils，对象转换，E转为t对象
     */
    public static <T, O> T transTo(O e, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        T t = clazz.newInstance();
        BeanUtils.copyProperties(e, t);
        return t;
    }

    /**
     * 接口常量转为指定类型的List
     */
    public static <T> List<T> interfaceTransToVal(Class<?> clazz, Class<T> toClazz) throws IllegalAccessException {
        List<T> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            T t = (T) field.get(clazz);
            if (t.getClass() == toClazz) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * json 转为对象
     */
    public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
//        ObjectMapper objectMapper = new ObjectMapper();
        T t = null;
        try {
            t = JSONObject.parseObject(jsonStr, clazz);
//            t = objectMapper.readValue(jsonByte, clazz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * json 转为Array
     */
    public static <T> List<T> jsonToArray(String jsonStr, Class<T> clazz) {
        if (jsonStr == null || jsonStr.length() == 0) {
            return null;
        } else {
//        ObjectMapper objectMapper = new ObjectMapper();
            List<T> arr = null;
            try {
                arr = JSON.parseArray(jsonStr, clazz);
//                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
//                arr = objectMapper.readValue(jsonStr, javaType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return arr;
        }
    }

    /**
     * Map 转为对象，字段格式要一致
     */
    public static <T> T mapTrasnToObject(Map<String, Object> map, Class<T> clazz) throws IOException {
        byte[] jsonBytes = JSON.toJSONBytes(map, SerializerFeature.WriteNullStringAsEmpty);
        T t = JSON.parseObject(jsonBytes, clazz,Feature.IgnoreNotMatch);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonStr = objectMapper.writeValueAsString(map);
//        T t = objectMapper.readValue(jsonStr, clazz);
        return t;
    }

    /**
     * object(非基本类型和List类型)转为 Map
     * 若类型不正确返回的map为EmptyMap，其不可添加元素
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> objectTrasnToMap(Object obj) {
        if (Objects.isNull(obj)) {
            return Collections.EMPTY_MAP;
        } else {
            Map<String, Object> map = BeanMap.create(obj);
            return map.size() == 1 && map.containsKey("empty") ? Collections.EMPTY_MAP : new HashMap<>(map);
        }
    }

    /**
     * 集合根据某个关键字进行分组
     */
    public static <T> Map<String, List<T>> groupBy(List<T> tList, StringKey<T> stringKey) {
        Map<String, List<T>> map = new HashMap<>();
        for (T t : tList) {
            if (map.containsKey(stringKey.key(t))) {
                map.get(stringKey.key(t)).add(t);

            } else {
                List<T> list = new ArrayList<>();
                list.add(t);
                map.put(stringKey.key(t), list);
            }
        }
        return map;
    }



    /**
     * 把对象转为json,并输出到日志中
     */
    public static void logObject(String tag, Object object) {
        try { // 保留空值数据为 ""
            String json = JSON.toJSONString(object, SerializerFeature.WriteNullStringAsEmpty);
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(object);
            logger.info("{}{}", tag, json);
        } catch (JSONException e) {
            e.printStackTrace();
            logger.info("exception = {}", e);
        }
    }


    /**
     * 集合分组的关键词
     */
    public interface StringKey<T> {
        String key(T t);
    }


    /**
     * 编写一些额外逻辑
     */
    public interface OnTransListener<T, O> {
        void doSomeThing(T t, O e);
    }
}
