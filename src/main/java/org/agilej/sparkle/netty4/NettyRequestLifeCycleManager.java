package org.agilej.sparkle.netty4;


import org.agilej.sparkle.core.ext.RequestLifeCycleManager;
import org.agilej.sparkle.mvc.ActionMethod;

public class NettyRequestLifeCycleManager implements RequestLifeCycleManager {

    @Override
    public boolean isResponseProcessedManually(ActionMethod actionMethod) {
        return false;
    }

}
