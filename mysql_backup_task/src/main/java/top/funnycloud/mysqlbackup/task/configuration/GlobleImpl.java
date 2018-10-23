package top.funnycloud.mysqlbackup.task.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobleImpl {
    @Autowired
    GlobleCfg globleCfg;
   private  static GlobleCfg globleCfgStatic;

    public static GlobleCfg getGlobleCfgStatic() {
        return globleCfgStatic;
    }

    public void initAutoWired(){
        if(globleCfg!=null) {
            globleCfgStatic = globleCfg;
        }
    }
}
