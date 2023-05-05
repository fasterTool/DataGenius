package cn.ly.common.datasource.service;
 
import cn.ly.common.datasource.service.query.IQueryTree;
import cn.ly.common.web.domain.TableDataInfo;

import java.util.List;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/25 17:00
 * @Description:
 */
public interface IBaseService<T> {


    public int add(T t);

    int add(T t, String... ignoreFields);

    int addWithUid(T t);

    int addWithUid(T t, String... ignoreFields);

    int addExcludeId(T t);

    int addExcludeId(T t, String... ignoreFields);

    int batchAdd(List<T> t);

    int batchAdd(List<T> t, String... ignoreFields);

    int batchAdd(List<T> t, int batchNum, String... ignoreFields);

    public int updateNotNull(T t);

    public int updateAll(T t);

    public T findById(Object id);

    public T findById(Object id, String... ignoreFields);


    int deleteById(Object id);

    int deleteByIds(List<Object> ids);

    List<T> search(IQueryTree queryTree);

    <R> List<R> search(IQueryTree queryTree, Class<R> clazz);

    <R> TableDataInfo searchPageInfo(IQueryTree queryTree);

    <R> TableDataInfo searchPageInfo(IQueryTree queryTree, Class<R> resultType);
}
