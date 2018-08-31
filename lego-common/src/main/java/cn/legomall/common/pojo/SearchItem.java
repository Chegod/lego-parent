package cn.legomall.common.pojo;

import java.io.Serializable;

/**
 * 封装从tb_item表和tb_item_cat表中查询的商品数据
 * @ClassName SearchItem
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/9 14:44
 * @Version 1.0
 **/
public class SearchItem implements Serializable {

    private String id;//商品id
    private String title;//商品标题
    private String sell_point;//商品买点
    private Long price;//商品价格
    private String image;//商品的图片
    private String category_name;//改商品类别的名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String[] getImages() {
        if (image != null && !"".equals(image)) {
            String[] strings = image.split(",");
            return strings;
        }
        return null;
    }
}
