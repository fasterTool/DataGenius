package cn.fasterTool.common.datasource.service.builder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/28 17:51
 * @Description:
 */
public class CRUDGeneratorFactory {


    private static ConcurrentHashMap<String, ISqlGenerator> sqlGenerateMap = new ConcurrentHashMap<>();

    static {
        sqlGenerateMap.put("mysql", new DefaultCrudGeneratorBuilder());
        sqlGenerateMap.put("pg", new PostGreSqlCRUDGeneratorBuilder());
    }

    public static ISqlGenerator builder() {
        return builder(DataType.MYSQL.type);
    }

    public static ISqlGenerator addSqlGenerator(String key, ISqlGenerator sqlGenerator) {
        return sqlGenerateMap.put(key, sqlGenerator);
    }

    public static ISqlGenerator builder(String type) {
        return sqlGenerateMap.get(type);
    }


    public enum DataType {
        MYSQL("mysql"),
        POST_GRE_SQL("pg");

        private String type;

        DataType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }


}
