package com.idogfooding.backbone.network;

import java.util.List;

/**
 * BasePagedResult
 *
 * @author Charles
 */
public class BasePagedResult<E> extends BaseEntity {

    private int totalRow;
    private boolean hasNextPage = false;
    private List<E> list;

    public BasePagedResult(int totalRow, boolean hasNextPage, List<E> list) {
        this.totalRow = totalRow;
        this.hasNextPage = hasNextPage;
        this.list = list;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }
}
