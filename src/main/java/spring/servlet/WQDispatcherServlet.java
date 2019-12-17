package spring.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 */
public class WQDispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        //1 加载配置文件
        doLoadConfig(config.getInitParameter("" ));
        //2 根据配置文件扫描所有的相关类
        doScanner();
        //3 初始化所有的相关类的实例，并且将其放入到IOS容器之中，也就是Map中
        doInstance();
        //4 自动实现依赖注入
        doAutowired();
        //5 初始化HandlerMapping
        initHandlerMapping();
        //6 等待请求（运行阶段）
        System.out.println("------- WQ MVC IS INIT-------");
    }

    private void initHandlerMapping() {
    }


    private void doAutowired() {
    }

    private void doInstance() {
    }

    private void doScanner() {
    }

    private void doLoadConfig(String initParameter) {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
//运行时阶段执行的方法

}
