package cn.fasterTool.common.datasource.service.query;

import cn.fasterTool.common.datasource.constants.BaseCRUDConstants;

/**
 * 分页条件
 *
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 10:36
 * @Description:
 */
public class PageCondition {


    private Integer page = 1;

    private Integer pageSize = 30;

    public PageCondition() {
    }

    public static PageCondition getDefaultCondition() {
        return new PageCondition(BaseCRUDConstants.DEFAULT_PAGE_NO, BaseCRUDConstants.DEFAULT_PAGE_SIZE);
    }


    public static void checkAndSet(PageCondition pageCondition) {
        if (pageCondition == null) {
            pageCondition = getDefaultCondition();
        }

        pageCondition.checkAndSetPage();
        pageCondition.checkAndSetPageSize();

    }


    public void checkAndSetPage() {
        if (page == null || page < BaseCRUDConstants.PAGE_MIN_NUM)
            page = BaseCRUDConstants.PAGE_MIN_NUM;
    }

    public void checkAndSetPageSize() {
        if (pageSize == null || pageSize < BaseCRUDConstants.PAGE_SIZE_MIN_NUM || pageSize > BaseCRUDConstants.PAGE_SIZE_MAX_NUM)
            pageSize = BaseCRUDConstants.DEFAULT_PAGE_SIZE;
    }


    public PageCondition(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public long getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
