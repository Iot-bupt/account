//package cn.edu.bupt.config;
//
//import com.baidu.disconf.client.DisconfMgrBeanSecond;
//import com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.baidu.disconf.client.DisconfMgrBean;
//import org.springframework.context.annotation.ImportResource;
//import org.springframework.util.PropertyPlaceholderHelper;
//
//import java.util.List;
//
///**
// * Created by CZX on 2018/6/6.
// */
//@Configuration
////@ImportResource(locations={"classpath:disconf.xml"})
//public class DisconfConfig {
//
//    @Bean
//    public DisconfMgrBean getDisconfMgrBean(){
//        DisconfMgrBean disconfMgrBean = new DisconfMgrBean();
//        disconfMgrBean.setScanPackage("cn.edu.bupt");
//        return disconfMgrBean;
//    }
//
//    @Bean
//    public DisconfMgrBeanSecond getDisconfMgrBeanSecond(){
//        DisconfMgrBeanSecond disconfMgrBeanSecond = new DisconfMgrBeanSecond();
//        return disconfMgrBeanSecond;
//    }
//
//    @Bean
//    public ReloadablePropertiesFactoryBean getReloadablePropertiesFactoryBean(){
//        ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean = new ReloadablePropertiesFactoryBean();
//        reloadablePropertiesFactoryBean.setLocation("classpath:/2.properties");
//        return reloadablePropertiesFactoryBean;
//    }
//
//    @Bean
//    public ReloadingPropertyPlaceholderConfigurer getReloadingPropertyPlaceholderConfigurer() {
//        ReloadingPropertyPlaceholderConfigurer reloadingPropertyPlaceholderConfigurer = new ReloadingPropertyPlaceholderConfigurer();
//        reloadingPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
//        reloadingPropertyPlaceholderConfigurer.setIgnoreResourceNotFound(true);
//        try {
//            reloadingPropertyPlaceholderConfigurer.setProperties(getReloadablePropertiesFactoryBean().getObject());
//            return reloadingPropertyPlaceholderConfigurer;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}
