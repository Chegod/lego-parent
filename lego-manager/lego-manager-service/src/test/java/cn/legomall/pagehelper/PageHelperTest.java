package cn.legomall.pagehelper;

import cn.legomall.mapper.TbItemMapper;
import cn.legomall.pojo.TbItem;
import cn.legomall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * 分页插件测试
 * @ClassName PageHelperTest
 * @Description TODO
 * @Author eooy
 * @Date 2018/4/25 23:18
 * @Version 1.0
 **/
public class PageHelperTest {
    @Test
    public  void testPageHelper(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        //设置分页信息,第几页(不是起始页):1,每页显示条数:10
        PageHelper.startPage(1,10);
        //PageHelper.startPage()之后的第一个select会进行分页查询
        TbItemExample example = new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(tbItems);
        System.out.println(tbItemPageInfo.getTotal());//934,总记录数
        System.out.println(tbItemPageInfo.getPages());//94,总页数
        System.out.println(tbItemPageInfo.getPageNum());//1,当前页
        System.out.println(tbItemPageInfo.getPageSize());//10,每页条数
    }
}
