package cn.ly.common.datasource.service.query;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/30 14:12
 * @Description:
 */
public class GroupCondition {

    LinkedList<String> fields;


    public GroupCondition() {
        init();
    }

    public GroupCondition(String... fields) {
        init(fields);
    }

    private void init() {
        fields = new LinkedList<>();
    }

    private void init(String... fields) {
        this.fields = (LinkedList<String>) Arrays.asList(fields);
    }

    public static GroupCondition groupBy(String... fields) {
        return new GroupCondition(fields);
    }

    public GroupCondition field(String... filed) {
        this.fields.addAll(Arrays.asList(filed));
        return this;
    }

    public LinkedList<String> getFields() {
        return fields;
    }
}
