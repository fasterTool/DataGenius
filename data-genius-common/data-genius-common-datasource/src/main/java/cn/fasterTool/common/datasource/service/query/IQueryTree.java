package cn.fasterTool.common.datasource.service.query;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 14:31
 * @Description:
 */
public interface IQueryTree {

    IQueryTree resultMap(String... column);

    IQueryTree usePageInfo(boolean usePage);

    IQueryTree from(String formSql);

    IQueryTree query(QueryCondition queryCondition);

    IQueryTree join(JoinCondition... joinCondition);

    IQueryTree sort(SortCondition... sortCondition);

    IQueryTree group(GroupCondition groupCondition);

    IQueryTree having(QueryCondition queryCondition);

    IQueryTree page(Integer page, Integer pageSize);


}
