package com.idogfooding.backbone.network;

import java.util.List;

/**
 * PageResult
 * 分页数据,对于自动加载的数据,真正关心的只有是否还有下一页和列表
 *
 * @author Charles
 */
public class PageResult<T> extends BaseEntity {

    private List<T> list;
    private int pageNumber;                // page number
    private int pageSize;                // result amount of this page
    private int totalPage;                // total page
    private int totalRow;                // total row
    private boolean hasNextPage;

    /**
     * Constructor.
     *
     * @param list       the list of paginate result
     */
    public PageResult(List<T> list) {
        this.list = list;
        this.pageNumber = 1;
        this.pageSize = 1;
        this.totalPage = 1;
        this.totalRow = 1;
        this.hasNextPage = false;
    }

    public PageResult(List<T> list, boolean hasNextPage) {
        this.list = list;
        this.hasNextPage = hasNextPage;
    }

    public PageResult(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
        this.hasNextPage = pageNumber != totalPage;
    }

    /**
     * Return list of this page.
     */
    public List<T> getList() {
        return list;
    }

    /**
     * Return page number.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Return page size.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Return total page.
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * Return total row.
     */
    public int getTotalRow() {
        return totalRow;
    }

    public boolean isFirstPage() {
        return pageNumber == 1;
    }

    public boolean isLastPage() {
        return pageNumber == totalPage;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }
}
