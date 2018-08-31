package cn.legomall.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 * @ClassName GlobalExceptionResolver
 * @Description TODO
 * @Author eooy
 * @Date 2018/5/12 15:27
 * @Version 1.0
 **/
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    //使用指定类初始化日志对象
    private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        //打印堆栈追踪信息到控制台
        ex.printStackTrace();
        //写日志文件
        logger.error("系统发生异常", ex);
        //发邮件、发短信
        //Jmail：可以查找相关的资料
        //需要在购买短信。调用第三方接口即可。
        //展示错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "系统发生异常，请稍后重试");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
