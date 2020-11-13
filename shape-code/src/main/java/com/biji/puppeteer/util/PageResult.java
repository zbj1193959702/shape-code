package com.biji.puppeteer.util;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {
    private List<T> records;
    private int totalRecordCount;
    private int pageSize;
    private int pageIndex;

    public static final Integer PAGE_INDEX_1 = 1;
    public static final Integer PAGE_SIZE_10 = 10;
    public static final Integer PAGE_SIZE_20 = 20;

    public int getPageCount() {
        if (this.pageSize > 0)
            return (this.totalRecordCount - 1) / this.pageSize + 1;
        else
            return 0;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }
}
