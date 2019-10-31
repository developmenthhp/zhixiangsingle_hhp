package cn.zhixiangsingle.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.json
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/10 17:15
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class JSONUtils {
    /**
     * 将任意一个java对象转化为json字符串
     * <pre>
     *     Map<String,String> user = new Map<String,String>();
     *     user.add("name","cdj");
     *     <b>String json = JSONUtils.toJson(user);</b>
     * </pre>
     * 输出：<code>{"name":"cdj"}</code>
     * @param pojo
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String toJson(Object pojo) throws JsonGenerationException, JsonMappingException, IOException{
        ObjectMapper m = new ObjectMapper();
        m.getSerializationConfig().disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
        return m.writeValueAsString(pojo);

    }

    /**
     * 从文件中读取json数据将其转化为指定对象
     * <pre>
     * Map<String,User> result = JSONUtils.getObjFromFile(new File("/root/pathtofile/file")
     * 	   ,new TypeReference&lt;Map&lt;String,User&gt;&gt;() { });
     * </pre>
     * @param <T>
     * @param src
     * @param valueTypeRef
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjFromFile(File jsonFile, TypeReference<?> valueTypeRef) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T)objectMapper.readValue(jsonFile, valueTypeRef);
    }

    /**
     * 将json数据转化为指定对象
     * <pre>
     * Map<String,User> result = JSONUtils.getObjFromFile(new File("/root/pathtofile/file")
     * 	   ,new TypeReference&lt;Map&lt;String,User&gt;&gt;() { });
     * </pre>
     * @param <T>
     * @param src
     * @param valueTypeRef
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjFromFile(String json, TypeReference<?> valueTypeRef) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        return (T)objectMapper.readValue(json, valueTypeRef);
    }

    /**
     * 将对象序列化为json字符串并存储到文件中
     * @param jsonFile json文件
     * @param pojo 对象
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeObjToFile(File jsonFile,Object pojo) throws JsonGenerationException, JsonMappingException, FileNotFoundException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.defaultPrettyPrintingWriter();
        writer.writeValue(new FileOutputStream(jsonFile), pojo);
    }

    /**
     * 将一个json字符串转化为指定对象,注意json字符串不支持单引号，请使用\"
     * <pre>
     *     Map<String,User> result = JSONUtils.getObjFromString("{\"meng\" : { \"name\" : \"lan\"},}"
     *         , new TypeReference&lt;Map&lt;String,User&gt;&gt;() { });
     * </pre>
     * @param <T>
     * @param json
     * @param valueTypeRef
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjFromString(String json, TypeReference<?> valueTypeRef) throws JsonParseException, JsonMappingException, IOException{
        return (T)new ObjectMapper().readValue(json, valueTypeRef);
    }

    public static void main(String[] args){
        try {
            Map<String,String> map = JSONUtils.getObjFromString("{\"meng\" : \"cdjjjjjjj\"}", new TypeReference<Map<String,String>>() { });

        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
