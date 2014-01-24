package me.donnior.sparkle.netty;

import me.donnior.sparkle.core.resolver.ParamResolversManager;
import me.donnior.sparkle.core.view.ViewRendersResovler;
import me.donnior.sparkle.ext.EnvSpecific;

public class NettySpecific implements EnvSpecific {

    @Override
    public ParamResolversManager getParamsResolverManager() {
        return new NettyParamResolversManager();
    }

    @Override
    public ViewRendersResovler getViewRendersResovler() {
        return new ViewRendersResovler();
    }

}
