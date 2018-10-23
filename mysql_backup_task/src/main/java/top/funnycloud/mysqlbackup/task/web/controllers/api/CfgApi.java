package top.funnycloud.mysqlbackup.task.web.controllers.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.funnycloud.mysqlbackup.task.configuration.GlobleCfg;
import top.funnycloud.mysqlbackup.task.handle.BackupAndUpload;
import top.funnycloud.mysqlbackup.task.models.BackupTaskModel;
import top.funnycloud.mysqlbackup.task.web.config.CustomMillSecondsDateEditor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
   BackupTaskModel setBackupTaskModel(BackupTaskModel model,HttpServletRequest request) throws IOException {
        return globleCfg.setBackupTaskModel(model);
    }
    @RequestMapping(value = "removeBackupTaskModel", method = RequestMethod.POST)
    void removeBackupTaskModel(String id,HttpServletRequest request) throws IOException {
         globleCfg.removeBackupTaskModel(id);
    }
    @RequestMapping(value = "runBackupTaskModel", method = RequestMethod.POST)
    void runBackupTaskModel(String id,HttpServletRequest request) throws IOException {
        BackupTaskModel backupTaskModel = globleCfg.getBackupTaskModel(id);
        BackupAndUpload.dowork(backupTaskModel,true);
    }
}
