# springboot-utils 工具类集合
<hr/>

<br>

## 模块总览：
<hr/>
 
|父模块名| 子模块名 | 层级 | 模块功能说明 |
|---| --- | --- | --- |
|data-common | - | 1 | 数据库相关模块 |
| - |data-common-datasource | 1 | 基于mybatis数据库增强 |
| - |data-common-swagger | 1 | swagger相关 |
| - |data-common-web | 1 | web统一 |
|springboot-archive-test | | 1 | 数据库增强测试类 |


## 模块说明：<hr/>

### data-common-datasource 基于mybatis数据库增强模块
> 基于mybatis，忽略持久化层的相关细节，快速开发业务系统，从而达到降本增效的效果
> 集成了百度uidGenerator作为分布式主键id，可以适配分布式场景
> 实现了自动化增强处理

### 直接使用示例：（以user对象举例）

1.**实体**：
>这里注意一般性要求指明表名:
>   * @Table(tableName = "db_user")，
>   * @Id注解
>   * @Column 若字段非驼峰变成下划线的命名规则，声明数据库表属性名
```java
@Data
@Table(tableName = "db_user")
public class User implements Serializable {

    @Id
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "添加时间") 
    @Column(column = "add_time")
    private Timestamp addTime;

    @ApiModelProperty(value = "上次修改时间")
    private Timestamp lastModifyTime;

    @ApiModelProperty(value = "添加用户")
    private Long addUser;

    @ApiModelProperty(value = "上次修改用户")
    private Long lastModifyUser;


}

```

2.**server层**
```java
/**
 * 继承IBaseService接口
 */
public interface IUserService extends IBaseService<User> {
}

/**
 * 继承AbstractBaseServiceImpl实现类并实现该接口
 */
public class UserServiceImpl extends AbstractBaseServiceImpl<User> implements IUserService {
}

```
3.注明yaml的DataSource的name
```yaml
spring: 
  datasource: 
    name: mysql
```

### queryTree介绍：

```java
import javax.annotation.Resource;

public class Test{
    @Resource
    private IUserService userService;

    public void queryTree(){
        
        // 1. 构建QueryTree
        IQueryTree queryTree = new QueryTree<User>()
                 // 默认使用searchPageInfo的返回体声明的字段
                .resultMap()
                 // 不使用from指定表，则使用User表名
                .from()
                .join(JoinCondition.innerJoin())
                .query(QueryCondition.where())
                .group(GroupCondition.groupBy("",""))
                .having(QueryCondition.where())
                .sort(SortCondition.sort(tableName,columnName,sortType))
                .page(pageNo,pageSize);
    
       // 2. 进行查询,指定返回体
       TableDataInfo<UserVo> searchPageInfo = userService.searchPageInfo(queryTree, UserVo.class);

       TableDataInfo<User> searchPageInfo = userService.searchPageInfo(queryTree);

      
    }
        

}
```

### 增强使用示例：（以user对象举例）

1. 继承CRUDGeneratorBuilder

```java
public class OtherCRUDGeneratorBuilder extends CRUDGeneratorBuilder {
    public String type() {
        return "other";
    }

    /**
     * 这里主要是构建了treeBuilder，使用2步骤构建的builder
     */
    @Override
    protected ITreeBuilder treeBuilder(IQueryTree queryTree) {
        return new OtherTreeBuilder(queryTree);
    }

    @Override
    protected ITreeBuilder treeBuilder(IQueryTree queryTree, Class clazz) {
        return new OtherTreeBuilder(queryTree, clazz);
    }
}
```

2. 实现ITreeBuilder接口
> + 两个构造方法需要实现
> + builder方法若是和父类没有区别直接声明即可
> + 重写generate*()方法，对于差异性的部分进行重构：
>   - select --> generateResultParam(needRefresh);
>   - from -->  generateFromBuilder(needRefresh);
>   - {join} on -->  generateJoinBuilder(needRefresh);
>   - where --> generateQueryBuilder(needRefresh);
>   - group by --> generateGroupBuilder(needRefresh);
>   - having  --> generateHavingBuilder(needRefresh);
>   - order by --> generateSortBuilder(needRefresh);
>   - limit  --> generatePageBuilder(needRefresh);
>
> 注：对于内连接统一使用 inner join
     

```java

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

```

3. 注册CRUDGeneratorBuilder

数据库声明name使用最新的那个type
```yaml
spring: 
  datasource: 
    name: other
```

这里使用的是实现CommandLineRunner接口，也可以通过其他方式写入这个即可
```java
@Component
public class InitCommandLineRunner implements CommandLineRunner {


    public void run(String... args) throws Exception {
        OtherCRUDGeneratorBuilder otherCRUDGeneratorBuilder = new OtherCRUDGeneratorBuilder();
        CRUDGeneratorFactory.addSqlGenerator(otherCRUDGeneratorBuilder.type(), otherCRUDGeneratorBuilder);
    }
}
```