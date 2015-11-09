package org.agilej.sparkle.netty4;

import org.agilej.sparkle.core.argument.*;


public class NettyParamResolversManager extends AbstractArgumentResolverManager {
    
    public NettyParamResolversManager() {
        registerArgumentResolver(new SimpleArgumentResolver());
        registerArgumentResolver(new WebRequestArgumentResolver());
        registerArgumentResolver(new PathVariableArgumentResolver());
        registerArgumentResolver(new ParamsArgumentResolver());
    }
    
}
