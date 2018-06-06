package cn.edu.bupt.config;

import com.baidu.disconf.client.DisconfMgrBeanSecond;
import com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean;
import com.baidu.disconf.client.addons.properties.ReloadingPropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baidu.disconf.client.DisconfMgrBean;

import java.util.List;

/**
 * Created by CZX on 2018/6/6.
 */
@Configuration
public class DisconfConfig {

    @Bean
    public DisconfMgrBean getDisconfMgrBean(){
        DisconfMgrBean disconfMgrBean = new DisconfMgrBean();
        disconfMgrBean.setScanPackage("cn.edu.bupt");
        return disconfMgrBean;
    }

    @Bean
    public DisconfMgrBeanSecond getDisconfMgrBeanSecond(){
        DisconfMgrBeanSecond disconfMgrBeanSecond = new DisconfMgrBeanSecond();
        return disconfMgrBeanSecond;
    }

    @Bean
    public ReloadablePropertiesFactoryBean getReloadablePropertiesFactoryBean(){
        ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean = new ReloadablePropertiesFactoryBean();
        reloadablePropertiesFactoryBean.setLocation("redis.properties");
        return reloadablePropertiesFactoryBean;
    }

//    @Bean
//    public ReloadingPropertyPlaceholderConfigurer getReloadingPropertyPlaceholderConfigurer(){
//        ReloadingPropertyPlaceholderConfigurer reloadingPropertyPlaceholderConfigurer = new ReloadingPropertyPlaceholderConfigurer();
//        reloadingPropertyPlaceholderConfigurer.setIgnoreResourceNotFound(true);
//        reloadingPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
//        List<>
//        reloadingPropertyPlaceholderConfigurer.setProperties(getReloadablePropertiesFactoryBean());
//    }
}
