package me.donnior.sparkle.netty4;

import me.donnior.sparkle.core.argument.*;


public class NettyParamResolversManager extends AbstractArgumentResolverManager {
    
    public NettyParamResolversManager() {
        registerArgumentResolver(new SimpleArgumentResolver());
        registerArgumentResolver(new WebRequestArgumentResolver());
        registerArgumentResolver(new PathVariableArgumentResolver());
        registerArgumentResolver(new ParamsArgumentResolver());
    }
    
}
