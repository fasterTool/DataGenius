package cn.fasterTool.common.datasource.service.impl;

import cn.fasterTool.common.datasource.service.IBaseService;
import cn.fasterTool.common.datasource.mapper.CommonBaseMapper;
import cn.fasterTool.common.datasource.service.builder.CRUDGeneratorFactory;
import cn.fasterTool.common.datasource.service.query.IQueryTree;
import cn.fasterTool.common.datasource.service.query.build.ITreeBuilder;
import cn.fasterTool.common.web.domain.TableDataInfo;
import com.baidu.fsg.uid.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/25 17:00
 * @Description:
 */
@Transactional
public abstract class AbstractBaseServiceImpl<T> implements IBaseService<T> {

    private final static Logger logger = LoggerFactory.getLogger(AbstractBaseServiceImpl.class);

    final static int BATCH_INSERT_MAX_NUMBER = 300;

    private Class<T> type;

    private Class getTClass() {
        return type;
    }

    @Autowired
    private CommonBaseMapper commonBaseMapper;

    @Autowired
    private UidGenerator uidGenerator;

    @Value("${spring.datasource.name:mysql}")
    private String dataSourceType;


    private T getInstance() {
        return (T) getInstance(getTClass());
    }

    private <V> V getInstance(Class<V> clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            V instance = (V) constructor.newInstance();
            return instance;
        } catch (NoSuchMethodException e) {
            logger.error("there has missing no parameters constructor");
            throw new RuntimeException("there has missing no parameters constructor", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractBaseServiceImpl() {
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        for (Type typeArgument : typeArguments) {
            if (typeArgument instanceof Class) {
                this.type = (Class<T>) typeArgument;
                break;
            }
        }
    }


    @Override
    public int add(T t) {
        return add(t, null);
    }

    @Override
    public int add(T t, String... ignoreFields) {
        String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSql(t, ignoreFields);
        return commonBaseMapper.insertMethod(insertSql);
    }


    @Override
    public int addWithUid(T t) {
        String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSqlUseUid(t,uidGenerator);
        return commonBaseMapper.insertMethod(insertSql);
    }

    @Override
    public int addWithUid(T t, String... ignoreFields) {
        String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSqlUseUid(t,uidGenerator, ignoreFields);
        return commonBaseMapper.insertMethod(insertSql);
    }

    @Override
    public int addExcludeId(T t) {
        String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSqlExcludeId(t);
        return commonBaseMapper.insertMethod(insertSql);
    }

    @Override
    public int addExcludeId(T t, String... ignoreFields) {
        String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSqlExcludeId(t, ignoreFields);
        return commonBaseMapper.insertMethod(insertSql);
    }

    @Override
    public int batchAdd(List<T> t) {
        return batchAdd(t, null);
    }

    @Override
    public int batchAdd(List<T> t, String... ignoreFields) {
        return batchAdd(t, BATCH_INSERT_MAX_NUMBER, ignoreFields);
    }

    @Override
    @Transactional
    public int batchAdd(List<T> t, int batchNum, String... ignoreFields) {
        int index = 0;
        while (index < t.size() - batchNum) {
            String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSql(t.subList(index, index += batchNum), ignoreFields);
            commonBaseMapper.insertMethod(insertSql);
        }

        String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSql(t.subList(index, t.size()), ignoreFields);
        commonBaseMapper.insertMethod(insertSql);

        return t.size();
    }

    @Override
    public int batchAddWithUid(List<T> t, String... ignoreFields) {
        return batchAddWithUid(t, BATCH_INSERT_MAX_NUMBER, ignoreFields);
    }

    @Override
    @Transactional
    public int batchAddWithUid(List<T> t, int batchNum, String... ignoreFields) {
        int index = 0;
        while (index < t.size() - batchNum) {
            String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSqlUseUid(t.subList(index, index += batchNum),uidGenerator, ignoreFields);
            commonBaseMapper.insertMethod(insertSql);
        }

        String insertSql = CRUDGeneratorFactory.builder(dataSourceType).insertSqlUseUid(t.subList(index, t.size()),uidGenerator, ignoreFields);
        commonBaseMapper.insertMethod(insertSql);

        return t.size();
    }

    @Override
    public int updateNotNull(T t) {
        String sql = CRUDGeneratorFactory.builder(dataSourceType).updateSqlNotNullSql(t);
        return commonBaseMapper.updateMethod(sql);
    }

    @Override
    public int updateAll(T t) {
        String sql = CRUDGeneratorFactory.builder(dataSourceType).updateSqlAllSql(t);
        return commonBaseMapper.updateMethod(sql);
    }

    @Override
    public T findById(Object id) {
        return findById(id, null);
    }

    @Override
    public T findById(Object id, String... ignoreFields) {
        String sql = CRUDGeneratorFactory.builder(dataSourceType).findByIdSql(id, getTClass(), ignoreFields);
        Map byId = commonBaseMapper.findById(sql);
        return byId == null ? null : (T) explainMap(byId);
    }

    @Override
    public int deleteById(Object id) {
        String sql = CRUDGeneratorFactory.builder(dataSourceType).deleteByIdSql(id, getTClass());
        int index = commonBaseMapper.delete(sql);
        return index;
    }

    @Override
    public int deleteByIds(List<Object> ids) {
        String sql = CRUDGeneratorFactory.builder(dataSourceType).deleteByIdSql(ids, getTClass());
        int index = commonBaseMapper.delete(sql);
        return index;
    }


    @Override
    public List<T> search(IQueryTree queryTree) {
        return search(queryTree, getTClass());
    }

    @Override
    public <R> List<R> search(IQueryTree queryTree, Class<R> clazz) {
        String sql = CRUDGeneratorFactory.builder(dataSourceType).querySql(queryTree, getTClass());
        List<Map> list = commonBaseMapper.query(sql);

        if (list == null) return new ArrayList<>();

        List<R> result = new ArrayList<>(list.size());
        list.forEach(item -> result.add(explainMap((Map<String, Object>) item, clazz)));

        return result;
    }

    @Override
    public <T> TableDataInfo searchPageInfo(IQueryTree queryTree) {
        return searchPageInfo(queryTree, getTClass());
    }

    @Override
    public <R> TableDataInfo searchPageInfo(IQueryTree queryTree, Class<R> resultType) {
        ITreeBuilder treeBuilder = CRUDGeneratorFactory.builder(dataSourceType).queryBuilder(queryTree, getTClass());
        String totalSql = treeBuilder.generatePageTotalSql();

        int total = commonBaseMapper.count(totalSql);
        if (total == 0) return new TableDataInfo();

        String pageSql = treeBuilder.generatePageInfoSql();
        List<Map> list = commonBaseMapper.query(pageSql);

        List<R> result = new ArrayList<>(list.size());
        list.forEach(item -> result.add(explainMap((Map<String, Object>) item, resultType)));

        return new TableDataInfo(result, total);
    }


    public T explainMap(Map<String, Object> obj) {
        return (T) explainMap(obj, getTClass());
    }

    public <V> V explainMap(Map<String, Object> obj, Class<V> clazz) {
        if (obj == null) return null;

        V instance = getInstance(clazz);

        for (Map.Entry<String, Object> item : obj.entrySet()) {
            String fieldName = item.getKey();
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(instance, convertAttributeValue(field.getType().getName(), item.getValue()));
            } catch (NoSuchFieldException e) {
                logger.warn("can't found the field={{}},reason{{}}", fieldName, e);
            } catch (IllegalAccessException e) {
                logger.warn("can't setObject value field={{}},reason{{}}", fieldName, e);
                throw new RuntimeException(e);
            }
        }
        return (V) instance;
    }

    private static Object convertAttributeValue(String type, Object value) {
        if ("long".equals(type) || "java.lang.Long".equals(type)) {
            return Long.parseLong(String.valueOf(value));
        } else if ("double".equals(type) || "java.lang.Double".equals(type)) {
            return Double.parseDouble(String.valueOf(value));
        } else if ("String".equals(type) || "java.lang.String".equals(type)) {
            return String.valueOf(value);
        } else if ("int".equals(type) || "java.lang.Integer".equals(type)) {
            return Integer.parseInt(String.valueOf(value));
        } else if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
            return Boolean.parseBoolean(String.valueOf(value));
        } else if ("java.sql.Timestamp".equals(type)) {
            if (value instanceof Long) {
                return new Timestamp(Long.parseLong(String.valueOf(value)));
            }
            if (value instanceof LocalDateTime) {
                return Timestamp.valueOf((LocalDateTime) value);
            }
        }
        return value;
    }

}
