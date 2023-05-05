package cn.fasterTool.common.datasource.service.query.build;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/30 20:02
 * @Description:
 */
public interface ITreeBuilder {

    public AbstractTreeBuilder build();

    public AbstractTreeBuilder rebuild();

    public String generatePageTotalSql();

    public String generatePageInfoSql();

    public String generateNoPageSql();
}
