package cn.jedis;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 唐孟廷
 * @desc 主启动类
 * @date 2020/5/11 - 10:28
 */
public class Main {
    private static final Map<String, Integer> studentScopeMap = new HashMap();


    public static void getCaseFileInfo() {
        // 如果是文本文件也可以这么读  调用readAllLines 方法
        try {    // JDK1.8以后可以省略第二个参数，默认是UTF-8编码
            List<String> lines = Files.readAllLines(Paths.get("D:\\java_project\\2020\\05\\mmall\\src\\test\\resources\\student.txt"));
            for (String line : lines) {
                String[] datas = line.split("\t");
                String name = datas[0];
                Integer scope = Integer.parseInt(datas[1]);
                if (studentScopeMap.get(name) != null) {
                    studentScopeMap.put(name, studentScopeMap.get(name) + scope);
                } else {
                    studentScopeMap.put(name, scope);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        getCaseFileInfo();

        Jedis jedis = new Jedis("47.112.125.251", 6379);
        studentScopeMap.forEach((k, v) -> {
            System.out.println(String.format("name:%s,scope:%d", k, v));
            jedis.zadd("studentScopeSet", v, k);
        });

        // 排序显示前10的结果
        Set<String> res = jedis.zrange("studentScopeSet", 0, 9);
        res.forEach(System.out::println);

        /**
         * 运行结果:
         *  name:山中井野,scope:309618
         *  name:不知火舞,scope:308738
         *  name:明世隐,scope:316491
         *  name:曹超,scope:307388
         *  name:李白,scope:297547
         *  name:吕布,scope:308421
         *  name:向日葵,scope:308630
         *  name:韩信,scope:316751
         *  name:马超,scope:315481
         *  name:刘醒,scope:312441
         *  name:公孙离,scope:309607
         *  name:派大星,scope:308984
         *  name:项羽,scope:311488
         *  name:孙悟空,scope:304114
         *  name:娜可露露,scope:306024
         *  name:梁非凡,scope:311475
         *  李白
         *  孙悟空
         *  娜可露露
         *  曹超
         *  吕布
         *  向日葵
         *  不知火舞
         *  派大星
         *  公孙离
         *  山中井野
         */
    }
}
