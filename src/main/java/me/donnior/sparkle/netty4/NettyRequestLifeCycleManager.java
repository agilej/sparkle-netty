package me.donnior.sparkle.netty4;

import me.donnior.sparkle.core.ActionMethod;
import me.donnior.sparkle.engine.RequestLifeCycleManager;

public class NettyRequestLifeCycleManager implements RequestLifeCycleManager {

    @Override
    public boolean isResponseProcessedManually(ActionMethod actionMethod) {
        return false;
    }

}
