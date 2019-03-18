package com.idogfooding.backbone.network;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * PageResult
 * 分页数据,对于自动加载的数据,真正关心的只有是否还有下一页和列表
 *
 * @author Charles
 */
public class PageResult<T> extends BaseEntity {

    private List<T> list;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private int totalRow;
    @JsonAdapter(BooleanTypeAdapter.class)
    private boolean lastPage;

    public PageResult(List<T> list) {
        this.list = list;
        this.pageNumber = 1;
        this.pageSize = 10;
        this.totalPage = 1;
        this.totalRow = 1;
        this.lastPage = true;
    }

    public PageResult(List<T> list, boolean lastPage) {
        this.list = list;
        this.lastPage = lastPage;
    }

    public PageResult(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
        this.lastPage = pageNumber != totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
}
