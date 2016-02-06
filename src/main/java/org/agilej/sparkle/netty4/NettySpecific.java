package org.agilej.sparkle.netty4;

import org.agilej.sparkle.core.argument.ArgumentResolverManager;
import org.agilej.sparkle.core.engine.RequestLifeCycleManager;
import org.agilej.sparkle.core.ext.EnvSpecific;
import org.agilej.sparkle.core.ext.VendorViewRenderProvider;

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
