package cn.legomall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**在结果集中封装了total和rows,作为参数传递给easyUIdatagrid进行分页
 * @ClassName EasyUIDataGridResult
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/26 15:20
 * @Version 1.0
 **/
public class EasyUIDataGridResult implements Serializable {
    private Integer total;
    private List<?> rows;

    public EasyUIDataGridResult() {
        this.total = total;
        this.rows = rows;
    }
    public EasyUIDataGridResult(Long total, List<?> rows) {
        this.total = total.intValue();
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
