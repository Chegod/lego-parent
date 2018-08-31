package cn.legomall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 封装search.jsp所需要的数据
 * @ClassName SearchResult
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 22:34
 * @Version 1.0
 **/
public class SearchResult implements Serializable {
    private List<SearchItem> itemList;//查询到的商品
    private int totalPages;//总页数
    private int recourdCount;//查询到多少条记录

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRecourdCount() {
        return recourdCount;
    }

    public void setRecourdCount(int recourdCount) {
        this.recourdCount = recourdCount;
    }
}

