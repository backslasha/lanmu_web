package lanmu;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

import lanmu.service.AccountService;

public class Application extends ResourceConfig {
    public Application() {
        // 注册逻辑处理的包名
        //packages("net.qiujuer.web.italker.push.service");
        packages(AccountService.class.getPackage().getName());

        // 注册Json解析器
        register(JacksonJsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);

    }
}