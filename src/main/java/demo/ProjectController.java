package demo;

import java.util.List;

import me.donnior.fava.util.FLists;
import me.donnior.sparkle.annotation.Controller;
import me.donnior.sparkle.annotation.Json;
import me.donnior.sparkle.annotation.Text;
import me.donnior.srape.AbstractFieldExposerModule;
import me.donnior.srape.FieldExposerModule;

@Controller("projects")
public class ProjectController {
    
    public FieldExposerModule jsons(){
        return new AbstractFieldExposerModule() {
            public void config() {
                expose(new int[]{1,2,3}).withName("ints");        //ints
                expose(new String[]{"one","two","three2"}).withName("strings");        //strings
            }
        };
    }
    
    @Json
    public List<String> json(){
        return FLists.create("one", "two", "three", "four");
    }
     
    @Text
    public String text(){
        return "{'msg':'Hello World'}";
    }
    
}
