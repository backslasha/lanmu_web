package lanmu;

import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

import lanmu.provider.AuthRequestFilter;
import lanmu.provider.GsonProvider;
import lanmu.service.AccountService;

public class Application extends ResourceConfig {
    public Application() {
        // 注册逻辑处理的包名
        //packages("net.qiujuer.web.italker.push.service");
        packages(AccountService.class.getPackage().getName());

        // 注册我们的全局请求拦截器
        register(AuthRequestFilter.class);

        // 注册Json解析器
        register(GsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);

    }
}