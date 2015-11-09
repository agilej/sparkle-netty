package org.agilej.sparkle.netty4;

import org.agilej.sparkle.core.ActionMethod;
import org.agilej.sparkle.engine.RequestLifeCycleManager;

public class NettyRequestLifeCycleManager implements RequestLifeCycleManager {

    @Override
    public boolean isResponseProcessedManually(ActionMethod actionMethod) {
        return false;
    }

}
