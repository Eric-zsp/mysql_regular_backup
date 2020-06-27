package cn.joyconn.tools.mysqlbackup.task.web.controllers.api;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilder;

import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleCfg;
import cn.joyconn.tools.mysqlbackup.task.handle.BackupAndUpload;
import cn.joyconn.tools.mysqlbackup.task.models.BackupTaskModel;
import cn.joyconn.tools.mysqlbackup.task.models.PostBackupTaskModel;
import cn.joyconn.tools.mysqlbackup.task.utils.AESUtils;
import cn.joyconn.tools.mysqlbackup.task.web.config.CustomMillSecondsDateEditor;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Date;

@RequestMapping("/CfgApi")
@RestController
public class CfgApi {


    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Date.class,new CustomMillSecondsDateEditor());
    }

    @Autowired
    GlobleCfg globleCfg;
    @RequestMapping(value = "getBackupTaskModels", method = RequestMethod.GET)
    Collection<BackupTaskModel> getBackupTaskModels(HttpServletRequest request) throws IOException {
        return globleCfg.getBackupTaskModels();
    }
    @RequestMapping(value = "setBackupTaskModel", method = RequestMethod.POST)
    BackupTaskModel setBackupTaskModel(@RequestBody PostBackupTaskModel model,HttpServletRequest request) throws IOException {
        BackupTaskModel saveModel=null;
        if(Strings.isNotBlank(model.getPassword())){
            try{               
                model.setP_pwd( AESUtils.encryptStr(model.getPassword(),globleCfg.getDbEnKey()));                
            }catch (Exception ex){
            }
        }
        try{
            ObjectMapper objectMapper = new ObjectMapper();            
            if(model.getP_name()!=null){
                model.setP_name(URLDecoder.decode(model.getP_name(), "utf-8"));
            }
            saveModel=objectMapper.readValue(objectMapper.writeValueAsString(model), BackupTaskModel.class);           
        }catch (Exception ex){
        }       
        if(saveModel==null){
            return null;
        }
        return  globleCfg.setBackupTaskModel(saveModel);
    }
    @RequestMapping(value = "removeBackupTaskModel", method = RequestMethod.POST)
    void removeBackupTaskModel(String id,HttpServletRequest request) throws IOException {
         globleCfg.removeBackupTaskModel(id);
    }
    @RequestMapping(value = "runBackupTaskModel", method = RequestMethod.POST)
    void runBackupTaskModel(String id,HttpServletRequest request) throws IOException {
        BackupTaskModel backupTaskModel = globleCfg.getBackupTaskModel(id);
        BackupAndUpload.dowork(backupTaskModel);
    }
}
