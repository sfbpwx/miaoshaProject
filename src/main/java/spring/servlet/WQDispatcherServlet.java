package spring.servlet;

import org.springframework.validation.ObjectError;
import spring.annotation.WQAutowired;
import spring.annotation.WQContorller;
import spring.annotation.WQRequestMapping;
import spring.annotation.WQService;

import javax.management.ObjectInstance;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.*;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
public class WQDispatcherServlet extends HttpServlet {
    //所有的配置信息都加入到当前的properties
    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String,Object> ioc = new HashMap<>();

    private List<Handler> handlerMapping = new ArrayList<>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        //1 加载配置文件
        try {
            doLoadConfig(config.getInitParameter("contextConfigLocation" ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2 根据配置文件扫描所有的相关类
        doScanner(properties.getProperty("scanPackage"));
        //3 初始化所有的相关类的实例，并且将其放入到IOS容器之中，也就是Map中
        try {
            doInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //4 自动实现依赖注入
        try {
            doAutowired();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //5 初始化HandlerMapping
        initHandlerMapping();
        //6 等待请求（运行阶段）
        System.out.println("------- WQ MVC IS INIT-------");
    }
    private void doLoadConfig(String initParameter) throws Exception{
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(initParameter);
        try{
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
    }

    private void doScanner(String packageName) {
        //进行递归扫描
        URL url = this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        File sourceFile = new File(url.getFile());
        for(File file:sourceFile.listFiles()){
            //如果是文件夹。则重置文件路径，进一步进行扫描
            if(file.isDirectory()){
                doScanner(packageName+"."+file.getName());
                //如果不是文件夹，则替换.class的名称末尾，对文件进行转换
            }else{
                String className = packageName+"."+file.getName().replaceAll(".class","");
                //
                classNames.add(className);

            }
        }
    }

    private void doInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try{
            if(classNames.isEmpty()){return;}

        //如果不为空，利用反射机制将扫描进来的classNames进行初始化

        for(String className:classNames){
            Class<?> clazz = Class.forName(className);
            //进入bean的实例化截断，初始化IOC容器
            //未增加注解的不进行初始化
            //IOC容器规则
            //1、key默认用类名首字母小写


            if(clazz.isAnnotationPresent(WQContorller.class)){
                String beanName = lowerFirstCase(clazz.getSimpleName());

                ioc.put(beanName,clazz.newInstance());
            }else if(clazz.isAnnotationPresent(WQService.class)){
                //2、如果用户自定义名字，优先用用户的自定义名称
                WQService wqService = clazz.getAnnotation(WQService.class);
                String beanName = wqService.value();
                if(!"".equals(beanName.trim())){
                    beanName = lowerFirstCase(clazz.getSimpleName());
                }
                Object instance = clazz.newInstance();
                ioc.put(beanName,instance);
                Class<?>[] interfaces = clazz.getInterfaces();
                for(Class<?> i:interfaces){
                    //将接口类型作为key
                    ioc.put(i.getName(),instance);
                }
                //3、如果是接口，我们可以巧妙的用接口的类型作为key
            }else{
                continue;
            }
        }}catch (Exception e){

        }
    }

    private void doAutowired() throws IllegalAccessException {
        if(ioc.isEmpty()){return;}
        for(Map.Entry<String,Object> entry:ioc.entrySet()){
            //第一步获取到所有的字段（field）
            //不管public private 还是protected 都要强制注入
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for(Field field:fields){
                //不是所有的字段都要进行接下来的步骤
                if(!field.isAnnotationPresent(WQAutowired.class))continue;

                WQAutowired wqAutowired = field.getAnnotation(WQAutowired.class);
                String beanName = wqAutowired.value().trim();
                if("".equals(beanName)){
                    beanName = field.getType().getName();
                }
                //如果想访问私有和受保护的，定义强制授权访问
                field.setAccessible(true);

                field.set(entry.getValue(),ioc.get(beanName));
            }
        }
    }
    private void initHandlerMapping() {
        if(ioc.isEmpty())return;

        for(Map.Entry<String,Object> entry:ioc.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            //判断注解
            if(!clazz.isAnnotationPresent(WQContorller.class))continue;

            String baseUrl = "";
            if(clazz.isAnnotationPresent(WQRequestMapping.class)){
                WQRequestMapping requestMapping = clazz.getAnnotation(WQRequestMapping.class);
                baseUrl += requestMapping.value();
            }
//            Method[] methods = clazz.getMethods();
//            for(Method method:methods){
//                if(!method.isAnnotationPresent(WQRequestMapping.class))continue;
//
//                WQRequestMapping requestMapping = method.getAnnotation(WQRequestMapping.class);
//                String url = (baseUrl + requestMapping.value()).replaceAll("/+","/");
//                handlerMapping.put(url,method);
//                System.out.println("Mapping:-----"+url+"-----"+method);
//            }
            Method[] methods = clazz.getMethods();
            for(Method method:methods){
                if(!method.isAnnotationPresent(WQRequestMapping.class))continue;

                WQRequestMapping requestMapping = method.getAnnotation(WQRequestMapping.class);
                String regex = ("/"+requestMapping.value()).replaceAll("/+","/");
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.add(new Handler(pattern,entry.getValue(),method));
                System.out.println("mapping: "+regex+","+method);
            }
        }
    }
    //  首字母改小写
    private String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }









    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        String url = req.getRequestURI();
//        String contextPath = req.getContextPath();
//        url.replace(contextPath,"").replace("/+","/");
//
//        if(!handlerMapping.containsKey(url)){
//            resp.getWriter().write("404 not found");
//            return;
//        }
//        Method method = handlerMapping.get(url);

        //反射方法
        //需要两个参数 1、拿到这个method的instance，2、拿到实参（request中取值）
        //method.invoke(method)
    }
//运行时阶段执行的方法

    private class Handler{
        protected Object controller;
        protected Method method;
        protected Pattern pattern;
        protected Map<String,Integer> paramIndexMapping;

        protected Handler(Pattern pattern, ObjectError controller,Method method){
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;
            paramIndexMapping = new HashMap<>();
            putParamIndexMapping(method);
        }

        private void  putParamIndexMapping(Method method){

        }
    }

    private void doDispatch(HttpServletRequest request,HttpServletResponse response)throws Exception{
        try{
            Handler handler = getHandler(request);
            if(handler==null){
                response.getWriter().write("404 not found!!");
                return;
            }
            //获取方法的参数列表
            Class<?>[] paramTypes = handler.method.getParameterTypes();
            //保存所有需要自动赋值的参数值
            Object[] paramValues = new Object[paramTypes.length];
            //这是属于j2ee中的内容
            Map<String,String[]> params = request.getParameterMap();

            for(Map.Entry<String,String[]> param:params.entrySet()){
                String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","");
                //如果找到匹配对象则开始填充参数值
                if(!handler.paramIndexMapping.containsKey(param.getKey()))continue;
                int index = handler.paramIndexMapping.get(param.getKey());
                paramValues[index] = convert(paramTypes[index],value);
            }
        }
    }

    private Object convert(Class<?> paramType, String value) {
    }

    private Handler getHandler(HttpServletRequest request) throws Exception{
        if(handlerMapping.isEmpty())return null;

        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath,"").replace("/+","/");

        for(Handler handler:handlerMapping){
            try{
                Matcher matcher = handler.pattern.matcher(url);
                //如果没有匹配到，则进行下一个匹配
                if(!matcher.matches())continue;

                return handler;
            }catch (Exception e){
                throw e;
            }
        }
        return null;
    }
}
