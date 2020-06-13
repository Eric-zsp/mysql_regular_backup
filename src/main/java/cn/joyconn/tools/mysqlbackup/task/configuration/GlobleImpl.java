package cn.joyconn.tools.mysqlbackup.task.configuration;

import org.springframework.stereotype.Service;

import cn.joyconn.tools.mysqlbackup.task.utils.FrameSpringBeanUtil;


@Service
public class GlobleImpl {

    public static GlobleCfg getGlobleCfgStatic() {
        
        GlobleCfg GlobleCfg = FrameSpringBeanUtil.getBean(GlobleCfg.class);
        return GlobleCfg;
    }

}
