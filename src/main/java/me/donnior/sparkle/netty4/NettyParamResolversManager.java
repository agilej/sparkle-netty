package me.donnior.sparkle.netty4;

import me.donnior.sparkle.core.resolver.AbstractArgumentResolverManager;
import me.donnior.sparkle.core.resolver.PathVariableArgumentResolver;
import me.donnior.sparkle.core.resolver.SimpleArgumentResolver;
import me.donnior.sparkle.core.resolver.WebRequestArgumentResolver;


public class NettyParamResolversManager extends AbstractArgumentResolverManager {
    
    public NettyParamResolversManager() {
        registerArgumentResolver(new WebRequestArgumentResolver());
        registerArgumentResolver(new SimpleArgumentResolver());
        registerArgumentResolver(new PathVariableArgumentResolver());
    }
    
}
