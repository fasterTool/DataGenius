package cn.fasterTool.common.web.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname TableDataInfo
 * @create 2023/5/1 19:05
 */
public class TableDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private long total;

    private List<T> rows;

    private int code;

    private String msg;

    public TableDataInfo() {
    }

    public TableDataInfo(List<T> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}