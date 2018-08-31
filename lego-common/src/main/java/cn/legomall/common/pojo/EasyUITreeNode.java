package cn.legomall.common.pojo;

import java.io.Serializable;

/**
 * EasyUITree初始化需要的数据
 * @ClassName EasyUITreeNode
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/27 16:45
 * @Version 1.0
 **/
public class EasyUITreeNode implements Serializable {
    private Long id;
    private  String text;
    private  String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
