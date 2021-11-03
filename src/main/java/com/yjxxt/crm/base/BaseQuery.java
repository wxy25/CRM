package com.yjxxt.crm.base;

//分页   一页十条 从page ---》limit
public class BaseQuery {
    //开头
    private Integer page=1;
    //结尾
    private Integer limit=10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
