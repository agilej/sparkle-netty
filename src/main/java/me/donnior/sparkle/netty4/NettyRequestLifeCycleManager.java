package me.donnior.sparkle.netty4;

import me.donnior.sparkle.core.ActionMethodDefinition;
import me.donnior.sparkle.engine.RequestLifeCycleManager;

public class NettyRequestLifeCycleManager implements RequestLifeCycleManager {

    @Override
    public boolean isResponseProcessedManually(ActionMethodDefinition arg0) {
        return false;
    }

}
