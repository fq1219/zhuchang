package com.zhuchang.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResult<T> {

    /**状态*/
    public int code = 0;
    /**状态信息*/
    public String msg = "";
    /**数据总数*/
    public long count;

    public List<T> data;

    PageResult(long count, List<T> data){
        this.count = count;
        this.data = data;
    }

    PageResult( int code, String msg, long count, List<T> data){
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public PageResult(Page<T> pageList) {
        this.count = pageList.getTotal();
        this.data = pageList.getRecords();
    }
}
