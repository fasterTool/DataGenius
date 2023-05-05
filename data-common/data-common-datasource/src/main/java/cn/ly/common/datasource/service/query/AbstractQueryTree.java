package cn.ly.common.datasource.service.query;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 查询树结构--目前还差 where子查询的逻辑没有处理，这里建议直接使用inner join处理
 *
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 10:35
 * @Description:
 */
public abstract class AbstractQueryTree implements IQueryTree {

    private boolean usePage = true;

    List<String> resultFields;

    String fromTable;

    List<JoinCondition> joinConditions;


    QueryCondition having;
    QueryCondition queryCondition;

    GroupCondition groupCondition;

    LinkedList<SortCondition> sortConditions;

    PageCondition pageCondition;


    AbstractQueryTree() {
    }

    public AbstractQueryTree(Integer page, Integer pageSize) {
        init(page, pageSize);
    }

    @Override
    public AbstractQueryTree resultMap(String... column) {
        if (column == null || column.length == 0) return this;
        this.resultFields = Arrays.asList(column);
        return this;
    }

    @Override
    public AbstractQueryTree usePageInfo(boolean usePage) {
        this.usePage = usePage;
        return this;
    }

    @Override
    public AbstractQueryTree from(String formSql) {
        this.fromTable = formSql;
        return this;
    }

    @Override
    public AbstractQueryTree page(Integer page, Integer pageSize) {
        init(page, pageSize);
        return this;
    }

    @Override
    public AbstractQueryTree query(QueryCondition queryCondition) {
        this.queryCondition = queryCondition;
        return this;
    }

    @Override
    public AbstractQueryTree join(JoinCondition... joinCondition) {
        initJoin();
        joinConditions.addAll(Arrays.asList(joinCondition));
        return this;
    }

    @Override
    public AbstractQueryTree sort(SortCondition... sortCondition) {
        initSort();
        sortConditions.addAll(Arrays.asList(sortCondition));
        return this;
    }

    @Override
    public AbstractQueryTree group(GroupCondition groupCondition) {
        this.groupCondition = groupCondition;
        return this;
    }

    @Override
    public AbstractQueryTree having(QueryCondition queryCondition) {
        this.having = queryCondition;
        return this;
    }

    private void init(Integer page, Integer pageSize) {
        this.pageCondition = new PageCondition(page, pageSize);
    }


    private void initJoin() {
        if (joinConditions == null) {
            synchronized (this) {
                if (joinConditions == null) {
                    joinConditions = new LinkedList<>();
                }
            }
        }
    }

    private void initSort() {
        if (sortConditions == null) {
            synchronized (this) {
                if (sortConditions == null) {
                    sortConditions = new LinkedList<>();
                }
            }
        }
    }


    public boolean isUsePage() {
        return usePage;
    }

    public List<String> getResultFields() {
        return resultFields;
    }

    public String getFromTable() {
        return fromTable;
    }

    public List<JoinCondition> getJoinConditions() {
        return joinConditions;
    }

    public QueryCondition getQueryCondition() {
        return queryCondition;
    }

    public GroupCondition getGroupCondition() {
        return groupCondition;
    }

    public LinkedList<SortCondition> getSortConditions() {
        return sortConditions;
    }

    public PageCondition getPageCondition() {
        return pageCondition;
    }

    public QueryCondition getHaving() {
        return having;
    }

    public void checkPageCondition() {
        if (this.pageCondition == null) this.pageCondition = new PageCondition();
        PageCondition.checkAndSet(pageCondition);
    }
}
