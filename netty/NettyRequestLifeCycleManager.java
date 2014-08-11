package me.donnior.sparkle.netty;

import me.donnior.sparkle.core.ActionMethodDefinition;
import me.donnior.sparkle.engine.RequestLifeCycleManager;

public class NettyRequestLifeCycleManager implements RequestLifeCycleManager {

    @Override
    public boolean isResponseProcessedManually(ActionMethodDefinition arg0) {
        return false;
    }

}
