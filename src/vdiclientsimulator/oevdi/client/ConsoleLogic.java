package vdiclientsimulator.oevdi.client;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import vdiclientsimulator.oevdi.client.LocalLogic.LocalContext;

public class ConsoleLogic {
    private LocalContext localCtx;
    private SpiceManager spiceMgr;
    private LocalLogic localLogic;

    public ConsoleLogic(LocalLogic localmodule, SpiceManager spiceMgr) {
        localCtx = localmodule.getlocalCtx();
        localLogic = localmodule;
        this.spiceMgr = spiceMgr;//new SpiceManager(LocalUtils.getInst());
    }

    public boolean callfunc(String fun, String funID, JSONObject params) {
        //LocalUtils.printLog("ConsoleLogic",fun+":"+ params.toString());
        //oLogger.getLogger().info("" + params.toString());
        LocalUtils.writeExternalLog("console", fun, params.toString());
        if (fun.equalsIgnoreCase(LocalUtils.console_req.reboot)) {
            //LocalUtils.reboot();
            return onReboot(fun, funID);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.shutdown)) {
            //LocalUtils.showdown();
            return onShutdown(fun, funID);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.ping)) {
            //LocalUtils.printLog("ConsoleLogic:","ping :" +  params.toString() );
            return onPing(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.changeName)) {
            return onChangeName(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.loginMode)) {
            //{'mode_id': mode_id}
            return onLoginMode(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.ChangeClientConfig)) {
            return onChangeClientConfig(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.upgrade_from_server)) {
            return onUpgradeFromServer();
//            return testUpgradeFromServer();
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.upgrade_client_force)) {
            return onUpgradeClientForce(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.refreshOrder)) {
            return onRefreshOrder(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.beginOrder)) {
            return onBeginOrder(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.console_req.endOrder)) {
            return onEndOrder(params);
        }

        return false;
    }

    private boolean onEndOrder(JSONObject params) {
        // TODO Auto-generated method stub
        localCtx._i_begin_order = 0;
        localCtx._i_end_order = 1;

        return true;
    }

    private boolean onBeginOrder(JSONObject params) {
        // TODO Auto-generated method stub
        localCtx._i_begin_order = 1;
        localCtx._i_end_order = 0;
        try {
            localCtx._i_refresh_order = params.getInt("start_number");

            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    private boolean onRefreshOrder(JSONObject params) {
        // TODO Auto-generated method stub
        //{"refresh_num": order}
        try {
            localCtx._i_refresh_order = params.getInt("refresh_num");
           
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public boolean testUpgradeFromServer() {
        //localCtx._i_need_update = 1;
        localCtx._i_force_update = 1;
        LocalUtils.printLog("ConsoleLogic", "onUpdateFromServer");

        if (localLogic != null) {
            localLogic.newTrdUpdateFromServer();
        }
        return true;
    }

    private boolean onUpgradeFromServer() {
        //localCtx._i_need_update = 1;
        localCtx._i_force_update = 1;
        LocalUtils.printLog("ConsoleLogic", "onUpdateFromServer");

        if (localLogic != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    localLogic.updateFromServer();
                }
            }).start();
        }

        return true;
    }

    private boolean onUpgradeClientForce(JSONObject params) {
        
        localCtx._i_force_update = 0;
        LocalUtils.printLog("ConsoleLogic", "upgradeclientforce:" + params.toString());
        return true;
    }

    private boolean onChangeClientConfig(JSONObject params) {
        // TODO Auto-generated method stub
        try {

            if (params.has("resolution"))//android system only have one resolution mode.
            {
                LocalUtils.printLog("consolelogic", "resolution:" + params.toString());
                String selDsp = params.getString("resolution");
            }

            if (params.has("shortcutKey")) //for spice:shift + f12
            {
                LocalUtils.printLog("consolelogic", "shortcutKey:" + params.toString());
            }

            if (params.has("fullscreen")) {
                LocalUtils.printLog("consolelogic", "fullscreen:" + params.toString());
            }

            if (params.has("self_service")) //for x86 toolbar ?
            {
                LocalUtils.printLog("consolelogic", "self_service:" + params.toString());
                String strVal = params.getString("self_service");
                {
                    LocalUtils.SavePersist(LocalUtils.vdi_req.get_toolbar_status, strVal);
                }
            }

            if (params.has("shutdown_with_vm")) //whether close vm after spice shutdown.
            {
                //LocalUtils.printLog("consolelogic","shutdown_with_vm:" + "not implement");
                String strParam = params.getString("shutdown_with_vm");
                LocalUtils.printLog("ConsoleLogic", "shutdown param:" + strParam);

                localCtx._s_shutdown_param = strParam;
                LocalUtils.SavePersist(LocalUtils.configs.vm_shutdown_param, strParam);
            }

            if (params.has("desktop_mode")) {
                localCtx._i_desktop_mode = params.getInt("desktop_mode");
            }

            if (params.has("wait_time")) {
                localCtx._i_desktop_wait = params.getInt("wait_time");
            }

            if (params.has("auto_start")) //auto run when dongle rebooting.
            {
                localCtx._i_auto_start = params.getBoolean("auto_start") ? 1 : 0;
            }

            if (params.has("server_ip")) {
                String strServerIp = params.getString("server_ip");
                if (!strServerIp.isEmpty()) {
                    localCtx._s_svr_ip = strServerIp;
                    LocalUtils.SavePersist(LocalUtils.configs.oevdiserverip, strServerIp);
                }
            }

            if (params.has("final_mode")) {
                localCtx._i_final_mode = params.getInt("final_mode");

            }

            if (params.has("mode_countdown")) {
                localCtx._i_mode_countdown = params.getInt("mode_countdown");
            }
           
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // if("desktop_mode".equalsIgnoreCase())
        LocalUtils.printLog("ConsleLogic", "ChangeClientConfig:" + params.toString());
        return false;
    }

    private boolean onLoginMode(JSONObject obj) {
     //   LocalUtils.exitSpiceBroadcast("onLoginMode");
        this.spiceMgr.clientLogout(true);
        this.spiceMgr.reset();
        localCtx._i_relogin = 1;
        return false;
    }

    private boolean onChangeName(JSONObject params) {
        // TODO Auto-generated method stub
        String strName = null;
        try {
            strName = params.getString("name");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (strName != null || !strName.isEmpty()) {
            localCtx._s_client_name = strName;
            LocalUtils.SavePersist_settings(LocalUtils.configs.oevdiclientname, localCtx._s_client_name);

            //re-register client
            localCtx._i_need_reg = 1;
            return true;
        }

        return false;
    }

    private boolean onPing(JSONObject params) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean onShutdown(String fun, String funID) {
        // TODO Auto-generated method stub
        JSONObject jo = new JSONObject();
        try {
            jo.put("client_mac", localCtx._s_client_mac);
            jo.put("replay_cmd_id", funID);
            jo.put("reply_body", "{}"); 
            int timeout = VdiConfigure.getInst().data.pullReplyTimeout;
            JSONObject res = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/pull", jo, timeout);
            if (res == null)
                return false;

            res = null;
            jo = null;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        LocalUtils.printLog("ConsoleLogic", "onShutdown：start shutdown...");

        byte[] vars = localCtx._s_shutdown_param.getBytes();
        boolean bt_t2v = (vars[0] == '1');//0
        boolean bp_t2v = (vars[2] == '1');

        /*
         char szModeStr[2] = { 0 };
        szModeStr[0] = shutdown_with_vm[1]; 
        evdi_set_cfg_int(CONFIG_SHUTDOWN_SETTING, "USERCONFIG", "TeacherCloseV2T", atoi(szModeStr));

        szModeStr[0] = shutdown_with_vm[0]; 
        evdi_set_cfg_int(CONFIG_SHUTDOWN_SETTING, "USERCONFIG", "TeacherCloseT2V", atoi(szModeStr));

        szModeStr[0] = shutdown_with_vm[3]; 
        evdi_set_cfg_int(CONFIG_SHUTDOWN_SETTING, "USERCONFIG", "PersonalCloseV2T", atoi(szModeStr));

        szModeStr[0] = shutdown_with_vm[2];
        evdi_set_cfg_int(CONFIG_SHUTDOWN_SETTING, "USERCONFIG", "PersonalCloseT2V", atoi(szModeStr));
         */
        
        boolean bVMPower = true;
        if (localCtx._i_login_type == 0 && bt_t2v)
            bVMPower = false;
        if (localCtx._i_login_type == 1 && bp_t2v)
            bVMPower = false;

        //spiceMgr.clientLogout(bshutdown);
        spiceMgr.powerAction(bVMPower);
        LocalUtils.os_shutdown("fun:" + fun);
        return false; //not need reply console again.
    }

    private boolean onReboot(String fun, String funID) {
        // TODO Auto-generated method stub
        //LocalUtils.requestUrl(url, jo, timeout);
        JSONObject jo = new JSONObject();

        try {
            jo.put("client_mac", localCtx._s_client_mac);
            jo.put("replay_cmd_id", funID);
            jo.put("reply_body", "{}");
            int timeout = VdiConfigure.getInst().data.pullReplyTimeout;
            JSONObject res = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/pull", jo, timeout);
            if (res == null)
                return false;

            jo = null;
            res = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        LocalUtils.printLog("ConsoleLogic", "onReboot() start reboot...");
        spiceMgr.clientLogout(false);
        LocalUtils.os_reboot("fun: " + fun);
        return false;
    }

}
