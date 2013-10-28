package demo;

import me.donnior.sparkle.route.RouteModule;
import me.donnior.sparkle.route.Router;

public class DemoRouteModuleOne implements RouteModule {
    

    @Override
    public void config(Router router) {
        
 
        router.match("/project/jsons").to("projects#jsons");
        
        router.match("/project/json").to("projects#json");
        
        router.match("/project/text").to("projects#text");
    
    }

}
