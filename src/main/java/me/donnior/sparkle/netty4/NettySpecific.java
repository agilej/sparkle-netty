package me.donnior.sparkle.netty4;

import me.donnior.sparkle.core.resolver.ArgumentResolverManager;
import me.donnior.sparkle.core.view.ViewRenderManager;
import me.donnior.sparkle.engine.RequestLifeCycleManager;
import me.donnior.sparkle.ext.EnvSpecific;

public class NettySpecific implements EnvSpecific {

    private final NettyParamResolversManager nprm = new NettyParamResolversManager();
    private final ViewRenderManager vrr = new ViewRenderManager();
    private RequestLifeCycleManager rlcm = new NettyRequestLifeCycleManager();

    @Override
    public ArgumentResolverManager getArgumentResolverManager() {
        return nprm;
    }

    @Override
    public ViewRenderManager getViewRendersResovler() {
        return vrr;
    }
    
    @Override
    public RequestLifeCycleManager getLifeCycleManager() {
        return rlcm ;
    }

}
