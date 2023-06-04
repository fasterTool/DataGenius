# DataGenius 一款基于mybaits对持久化层解耦的高级解决方案
> DataGenius 是一款基于 mybaits 框架的智能数据库框架，能够兼容多种数据库，提供高效快速的增删改查基本代码生成功能，能够满足大部分业务场景的需求。DataGenius 具有智能化、易用化、高效化的特点，能够显著提高企业开发效率，降低开发成本。同时，DataGenius 还支持多种编程语言和开发框架，能够为不同领域的业务场景提供更加专业的解决方案。在未来，DataGenius 将不断发展和创新，成为数字化时代的重要贡献力量。
<br>


## data-common

> + data-common 主要是提供一个工具，根据数据库实体，在service层，对数据库持久化层操作提供crud相关接口。
>   - 目前第一版主要是在mybatis框架基础上进行集成，并整合了百度uidGenerator分布式id唯一生成方式进行主键生成
>   - 提供最为基础的增删改查相关操作，包括单个及批量操作
>   - 对于复杂的sql包括几种连接、group by、having、sort等方式提供了QueryTree的支持，基本涵盖了绝大部分的使用场景
>   - 暂不支持with 、 临时表等复杂sql，这个也是缺陷，后续有计划进行补齐
>   - 目前第一版仅支持pg、mysql两种数据库类型，对于需要扩展其他数据库，可以通过继承增强的方式进行兼容

## 最新版本
https://mvnrepository.com/artifact/io.github.fasterTool/data-genius-common-datasource
```xml
  <!-- https://mvnrepository.com/artifact/io.github.fasterTool/data-genius-common-datasource -->
<dependency>
    <groupId>io.github.fasterTool</groupId>
    <artifactId>data-genius-common-datasource</artifactId>
    <version>0.0.1</version>
</dependency>

```

## 快速开始

### application.yml 配置
 
+ 配置spring.datasource.name 来指定数据库类型
+ 配置mybatis配置项（这里主要是如果需要DataSource类的uidGenerator作为分布式主键生成器使用，自增等业务忽略）

```
spring: 
  datasource:
    # 通过name来判断使用哪个数据库，默认mysql，暂时只支持mysql和pg（postgresql）两种，可以增强自己实现
    name: other
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: .....
    username: ......
    password: ......
    type: com.mysql.cj.jdbc.MysqlDataSource 
    
    ......
    
# 当我们需要uidGenerator作为分布式主键时需要配置classpath:/mybatis/mapper/*.xml
mybatis:
  mapper-locations: classpath:/mybatis/mapper/*.xml 
```
### springboot启动类配置
 启动类配置

```java

/**
 * 
 *  <ul>
 *      <li>配置mapperScan
 *          <ul>
 *              <li>datasouce必备引入包：cn.fasterTool.common.datasource.mapper</li>
 *              <li>百度uidGenerator引入包：com.baidu.fsg.uid.worker.dao</li>
 *          </ul>
 *      </li> 
 *      <li>增强jdbc配置类（无需增强请忽略）：cn.ly.study.spring.config</li>
 *  </ul>
 *  
 */
@SpringBootApplication
@MapperScan(basePackages = {"cn.fasterTool.common.datasource.mapper", "com.baidu.fsg.uid.worker.dao"})
@Configuration("cn.ly.study.spring.config")
@EnableOpenApi
public class SpringBootApplication { 
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }
}
```  
### 百度uidGenerator配置
+ sql：https://github.com/fasterTool/DataGenius/blob/master/sql/init.sql
+ config配置：https://github.com/fasterTool/DataGenius/blob/master/springboot-archive-demo/src/main/java/cn/ly/archive/study/test/config/UidConfig.java


### 增强数据库类型配置
当mysql及postgresql无法满足业务需求需要增强扩展其他数据库时使用：
+ 教程：[2.1.3 对DataSource类进行扩展使用示例](#2.1.3-对datasource类进行扩展使用示例)
+ 示例：[https://github.com/fasterTool/DataGenius/tree/master/springboot-archive-demo/src/main/java/cn/ly/archive/study/test/config/datasource](config/datasource)

<br/>
<hr/>
<br/>

### 一、模块总览： 
 
|父模块名| 子模块名 | 层级 | 模块功能说明 |
|---| --- | --- | --- |
|data-common | - | 1 | 数据库相关模块 |
| - |data-common-datasource | 1 | 基于mybatis数据库增强 | 
| - |data-common-web | 1 | web统一 |
|springboot-archive-test | | 1 | 数据库增强测试类 |
 

### 二、模块说明：

#### 2.1 data-common-datasource 基于mybatis数据库增强模块
> + 基于mybatis，忽略持久化层的相关细节，快速开发业务系统，从而达到降本增效的效果
> + 集成了百度uidGenerator作为分布式主键id，可以适配分布式场景
> + 可以自行扩展增强

##### 2.1.1 引用data-common进行业务代码撰写示例：（以user对象举例）

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

4. 继承service后可以直接使用方法：
```java

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
```

##### 2.1.2 data-common高级搜索之queryTree介绍：

> + 使用searchPageInfo进行分页检索时，为了更加灵活支持大部分业务场景的sql语句，我们使用QueryTree对查询的树级结构进行构建，其结构如下述代码所示；
> + 对于子查询等，mysql等数据库并非做得较好，我个人也比较支持将子查询变成了几种连接查询操作，故使用子查询时需要进行sql的优化变成连接查询操作

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

##### 2.1.3 对DataSource类进行扩展使用示例

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
