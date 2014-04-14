package me.donnior.sparkle.netty;

import me.donnior.sparkle.core.resolver.ParamResolversManager;
import me.donnior.sparkle.core.view.ViewRendersResovler;
import me.donnior.sparkle.engine.RequestLifeCycleManager;
import me.donnior.sparkle.ext.EnvSpecific;

public class NettySpecific implements EnvSpecific {

    private final NettyParamResolversManager nprm = new NettyParamResolversManager();
    private final ViewRendersResovler vrr = new ViewRendersResovler();
    private RequestLifeCycleManager rlcm = new NettyRequestLifeCycleManager();

    @Override
    public ParamResolversManager getParamsResolverManager() {
        return nprm;
    }

    @Override
    public ViewRendersResovler getViewRendersResovler() {
        return vrr;
    }
    
    @Override
    public RequestLifeCycleManager getLifeCycleManager() {
        return rlcm ;
    }

}
