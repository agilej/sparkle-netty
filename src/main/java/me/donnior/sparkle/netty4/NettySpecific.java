package me.donnior.sparkle.netty4;

import me.donnior.sparkle.core.argument.ArgumentResolverManager;
import me.donnior.sparkle.core.view.ViewRenderManager;
import me.donnior.sparkle.engine.RequestLifeCycleManager;
import me.donnior.sparkle.ext.EnvSpecific;
import me.donnior.sparkle.ext.VendorViewRenderProvider;

public class NettySpecific implements EnvSpecific {

    private final NettyParamResolversManager nprm = new NettyParamResolversManager();

    private RequestLifeCycleManager rlcm = new NettyRequestLifeCycleManager();

    @Override
    public ArgumentResolverManager getArgumentResolverManager() {
        return nprm;
    }
    
    @Override
    public RequestLifeCycleManager getLifeCycleManager() {
        return rlcm ;
    }

    @Override
    public VendorViewRenderProvider vendorViewRenderProvider() {
        return null;
    }
}
