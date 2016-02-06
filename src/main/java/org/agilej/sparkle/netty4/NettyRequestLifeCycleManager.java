package org.agilej.sparkle.netty4;

import org.agilej.sparkle.core.action.ActionMethod;
import org.agilej.sparkle.core.engine.RequestLifeCycleManager;

public class NettyRequestLifeCycleManager implements RequestLifeCycleManager {

    @Override
    public boolean isResponseProcessedManually(ActionMethod actionMethod) {
        return false;
    }

}
