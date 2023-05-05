package cn.fasterTool.common.datasource.constants;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 16:16
 * @Description:
 */
public interface BaseCRUDConstants {

    Integer DEFAULT_PAGE_NO = 1;

    Integer DEFAULT_PAGE_SIZE = 30;

    Integer PAGE_MIN_NUM = 1;

    Integer PAGE_SIZE_MAX_NUM = 2000;

    Integer PAGE_SIZE_MIN_NUM = 1;

    String DEFAULT_FIELD_ID_NAME = "id";
    String BRACKET_LEFT = "(";
    String BRACKET_RIGHT = ")";
    String UNDERLINE = "_";
    String EQUAL = "=";
    String NOT_EQUAL = "<>";
    String GREATER = ">";
    String GREATER_EQUAL = ">=";
    String LESS = "<";
    String LESS_EQUAL = "<=";
    String LIKE = "LIKE";

    String LEFT_JOIN = "LEFT JOIN";

    String RIGHT_JOIN = "RIGHT JOIN";

    String INNER_JOIN = "INNER JOIN";

    String AND_QUERY = "and";

    String OR_QUERY = "or";


    String ASC_SORT = "ASC";

    String DESC_SORT = "DESC";

    String COLUMN_IDENTIFIER = "`";

    String VALUE_IDENTIFIER = "'";

    String COMMA = ",";

    String BLANK_ = " ";

    String IN = "in";
    String NOT_IN = "not in";
    String ON = "on";


    String AS = "as";

    String POINT = ".";
}
