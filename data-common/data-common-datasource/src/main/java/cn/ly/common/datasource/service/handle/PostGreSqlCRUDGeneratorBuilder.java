package cn.ly.common.datasource.service.handle;

import cn.ly.common.datasource.service.query.IQueryTree;
import cn.ly.common.datasource.service.query.build.ITreeBuilder;
import cn.ly.common.datasource.service.query.build.PgTreeBuilder;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname PostgreSqlCRUDGeneratorBuilder
 * @create 2023/5/3 20:31
 */
public class PostGreSqlCRUDGeneratorBuilder extends CRUDGeneratorBuilder {

    @Override
    protected String type() {
        return CRUDGeneratorFactory.DataType.POST_GRE_SQL.getType();
    }

    @Override
    public ITreeBuilder treeBuilder(IQueryTree queryTree) {
        return new PgTreeBuilder(queryTree);
    }

    @Override
    public ITreeBuilder treeBuilder(IQueryTree queryTree, Class clazz) {
        return new PgTreeBuilder(queryTree, clazz);
    }
}
