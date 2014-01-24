package me.donnior.sparkle.netty;

import me.donnior.sparkle.core.resolver.AbstractParamResolversManager;
import me.donnior.sparkle.core.resolver.SimpleArgumentResolver;


public class NettyParamResolversManager extends AbstractParamResolversManager {
    
    public NettyParamResolversManager() {
        registerArgumentResolver(new SimpleArgumentResolver());
    }
    
}
