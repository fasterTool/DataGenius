package cn.ly.archive.study.test.config.datasource;

import cn.ly.common.datasource.service.query.AbstractQueryTree;
import cn.ly.common.datasource.service.query.IQueryTree;
import cn.ly.common.datasource.service.query.build.AbstractTreeBuilder;
import cn.ly.common.datasource.service.query.build.ITreeBuilder;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname OtherTreeBuilder
 * @create 2023/5/3 21:20
 */
public class OtherTreeBuilder extends AbstractTreeBuilder implements ITreeBuilder {

    public OtherTreeBuilder(IQueryTree queryTree) {
        super.queryTree = (AbstractQueryTree) queryTree;
    }

    public OtherTreeBuilder(IQueryTree queryTree, Class clazz) {
        super.queryTree = (AbstractQueryTree) queryTree;
        super.clazz = clazz;
    }


    @Override
    public AbstractTreeBuilder build() {
        return build(false);
    }

    @Override
    public AbstractTreeBuilder rebuild() {
        return build(true);
    }

    @Override
    protected void generatePageBuilder(boolean needRefresh) {
        if (pageBuilder != null && !needRefresh) return;
        if (!this.queryTree.isUsePage()) return;
        queryTree.checkPageCondition();
        pageBuilder = String.format("limit %s offset %s", queryTree.getPageCondition().getPageSize(), (queryTree.getPageCondition().getPage() - 1) * queryTree.getPageCondition().getPageSize());
    }
}
