package vdiclientsimulator.oevdi.client;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vdiclientsimulator.oevdi.client.LocalUtils.NetInfo;
import vdiclientsimulator.oevdi.client.LocalUtils.SystemInfo;
import vdiclientsimulator.oevdi.netupdate.FtpUpdateHelper;
import vdiclientsimulator.oevdi.oem.OemManager;

//import org.apache.http.client.methods.HttpGet;

public class LocalLogic {
    //private Context client;
    public enum listenerEvent {
        SVR_REGISTERED,
        SVR_REGISTER_FAILED,
        OEM_UPDATED,
        SVR_CHANGED,
        LOCAL_IP_CHANGED,
        TRY_LOAD_RES
    };

    interface LocalLogicListener {
        void onEvent(listenerEvent id);
    }

    public class LocalContext {
        public int _i_net_connected;
        public int _i_svr_connected;
        public String _s_svr_ip;
        public String _s_pre_svr_ip; //previously enabled ip.

        public String _s_rabbitmq_ip;
        public int _i_svr_port;
        public String _s_client_uuid; //uuid
        public String _s_client_mac;//fake mac
        public String _s_client_login_name; //host returned after registered by mac.
        public String _s_client_name;
        public int _i_begin_order;
        public int _i_end_order;
        public int _i_refresh_order;
        public String _s_client_ip;
        public String _s_client_mask;
        public String _s_config_psw;
        public int _i_client_id;
        public int _i_client_type; //0:linux 1: arm 2:window 3:android
        public int _i_desktop_mode;
        public int _i_desktop_wait;
        public int _i_need_update;
        public int _i_force_update;

        public int _i_reg_succ;
        public int _i_reg_err;
        public int _i_need_reg;
        public int _i_relogin;
        public int _i_login_type;
        public int _i_final_mode;
        public int _i_mode_countdown;

        public String _s_os_name;
        public String _s_svr_base_URL;
        public int _i_auto_start;
        //		public String 	_s_vm_shutdown_param;
        public String _s_cpu_info;
        public String _s_meminfo;
        public String _s_client_version;
        public String _s_console_version;
        public int _i_client_version_code;
        public String _s_personal_name;
        public String _s_personal_pwd;
        public String _s_client_trace;
        //public String 	_s_source_id;
        public String _s_cookie;
        public String _s_shutdown_param;
        public LocalLogicListener _ctx_listener;
        public volatile String _s_apk_md5;

        //10.2.81.36:8090
        public LocalContext() {
            _i_net_connected = 1;
            _i_svr_connected = 1;
            _s_svr_ip = ""; //remote host ip
            _s_pre_svr_ip = "";
            _i_svr_port = 8090;
            _s_client_uuid = "";
            _s_client_mac = "";
            _s_client_login_name = "";
            _s_client_name = "android client v0.1"; //default name
            _s_apk_md5 = "";

            _i_begin_order = 0;
            _i_end_order = 1;
            _i_refresh_order = 0;
            _i_desktop_mode = 0;
            _i_desktop_wait = 5;
            _s_client_ip = "";
            _i_client_type = 0;
            _s_client_mask = "";
            _s_config_psw = "oe1234";//default password.
            _i_client_id = -1;
            _i_need_update = 0;
            _i_force_update = 0;

            _i_reg_succ = 0;
            _i_reg_err = 0;
            _i_need_reg = 1; //１times is needed at least...
            _i_relogin = 0;
            _i_login_type = 0; //0:login teaching desktop, 1:login personal desktop;
            _i_auto_start = 0;
            _i_final_mode = 1;
            _i_mode_countdown = 5;

//			_s_vm_shutdown_param="1000";
            _s_os_name = "android";
            _s_cpu_info = "unknown";
            _s_meminfo = "unknown";
            _s_client_version = "unknown";
            _i_client_version_code = 0;
            _s_console_version = "unknown";
            _s_personal_name = "";
            _s_personal_pwd = "";
            _s_client_trace = "";
            //_s_source_id = "";
            _s_cookie = "";
            _s_shutdown_param = "1000"; //default:t_v2t ,p_v2t = true;
            _ctx_listener = null;
        }

        public LocalContext(LocalContext localCtx) {
            _i_net_connected = localCtx._i_net_connected;
            _i_svr_connected = localCtx._i_svr_connected;
            _s_svr_ip = localCtx._s_svr_ip;
            _s_pre_svr_ip = localCtx._s_pre_svr_ip;
            _i_svr_port = localCtx._i_svr_port;
            _s_client_uuid = localCtx._s_client_uuid;
            _s_client_mac = localCtx._s_client_mac;
            _s_client_login_name = localCtx._s_client_login_name;
            _s_client_name = localCtx._s_client_name;
            _i_begin_order = localCtx._i_begin_order;
            _i_end_order = localCtx._i_end_order;
            _i_desktop_mode = localCtx._i_desktop_mode;
            _i_desktop_wait = localCtx._i_desktop_wait;
            _s_client_ip = localCtx._s_client_ip;
            _i_refresh_order = localCtx._i_refresh_order;

            _s_client_mask = localCtx._s_client_mask;
            _s_config_psw = localCtx._s_config_psw;

            _s_client_version = localCtx._s_client_version;
            _s_console_version = localCtx._s_console_version;

            _i_need_update = localCtx._i_need_update;
            _i_relogin = localCtx._i_relogin;
            _i_need_reg = localCtx._i_need_reg;
            _i_client_id = localCtx._i_client_id;
            _s_os_name = localCtx._s_os_name;

            _s_shutdown_param = localCtx._s_shutdown_param;
            _ctx_listener = localCtx._ctx_listener;
        }

        public LocalContext copy() {
            // TODO Auto-generated method stub
            return new LocalContext(this);
        }
    }

    ;

    final private LocalContext localCtx;
    final private ConsoleLogic cslLogic;
    final private SpiceManager spiceMgr;
  
    final private OemManager oemMgr;

    public LocalLogic() {
        localCtx = new LocalContext();
        spiceMgr = new SpiceManager(LocalUtils.getInst(), localCtx);
        cslLogic = new ConsoleLogic(this, spiceMgr);
        
        oemMgr = new OemManager(this);

        //system must be rooted
        initPersistData();
        initClientLocalCtx();

        //must ensure su,it 's necessary after reboot.
        initEnvirment();
    }

    public static String getDefInitResPathName() {
         
        return "file:///android_asset/client_init/index.html";
    }

    public void initEnvirment() {
    
    }


    private void initPersistData() {
        // TODO Auto-generated method stub
 /*       localCtx._s_client_uuid = LocalUtils.LoadPersist(LocalUtils.configs.oevdiuuid);
        localCtx._s_client_mac = LocalUtils.LoadPersist(LocalUtils.configs.oevdifakemac);

        if (localCtx._s_client_uuid == null || localCtx._s_client_uuid.isEmpty()
                || localCtx._s_client_mac == null) {
            localCtx._s_client_uuid = LocalUtils.Get_UUID();
            localCtx._s_client_mac = LocalUtils.fakeMacByUUID(localCtx._s_client_uuid);
            LocalUtils.SavePersist(LocalUtils.configs.oevdiuuid, localCtx._s_client_uuid);
            LocalUtils.SavePersist(LocalUtils.configs.oevdifakemac, localCtx._s_client_mac);
        }

        LocalUtils.printLog("LocalLogic", "uuid_mac:" + localCtx._s_client_uuid);
        String strSvrData = LocalUtils.LoadPersist(LocalUtils.configs.oevdiserverip);
        if (strSvrData != null && !strSvrData.isEmpty()) {
            localCtx._s_svr_ip = strSvrData;
            strSvrData = LocalUtils.LoadPersist(LocalUtils.configs.oevdiserverport);
            if (strSvrData != null && !strSvrData.isEmpty())
                localCtx._i_svr_port = Integer.valueOf(strSvrData);

            localCtx._s_svr_base_URL = getSvrBaseUrl();
        }

        localCtx._s_shutdown_param = LocalUtils.LoadPersist(LocalUtils.configs.vm_shutdown_param);
        if (localCtx._s_shutdown_param == null || localCtx._s_shutdown_param.isEmpty())
            localCtx._s_shutdown_param = "0101"; //defaut :t_v2t...
        //localCtx._s_source_id = LocalUtils.LoadInfo("oemresid");
*/
        localCtx._s_client_name = LocalUtils.LoadPersist(LocalUtils.configs.oevdiclientname);
        if (localCtx._s_client_name == null || localCtx._s_client_name.isEmpty())
            localCtx._s_client_name = "sim-and-terminal";
    }

    private void initClientLocalCtx() {
        // TODO Auto-generated method stub
        try {

            //localCtx._s_shutdown_param = strParam;
            /*
            NetInfo info = LocalUtils.getNetInfo();
            JSONObject jso = info.getInfoData();

            localCtx._s_client_ip = jso.getString("address");
            localCtx._s_client_mask = jso.getString("mask");
            */
            
            String desktopMode = LocalUtils.LoadInfo(LocalUtils.configs.desktop_mode);
            if (!desktopMode.isEmpty())
                localCtx._i_desktop_mode = Integer.valueOf(desktopMode);

            String deskWaitTime = LocalUtils.LoadInfo(LocalUtils.configs.desktop_wait);
            if (!deskWaitTime.isEmpty()) //  waiting 10 seconds by default
                localCtx._i_desktop_wait = Integer.valueOf(deskWaitTime);

            localCtx._s_config_psw = LocalUtils.LoadInfo(LocalUtils.configs.config_psw);
            if (localCtx._s_config_psw.isEmpty())
                localCtx._s_config_psw = "oe1234";

            String finalMode = LocalUtils.LoadInfo(LocalUtils.configs.final_mode);
            if (!finalMode.isEmpty()) //  waiting 10 seconds by default
                localCtx._i_final_mode = Integer.valueOf(finalMode);

            String modeCountdown = LocalUtils.LoadInfo(LocalUtils.configs.mode_countdown);
            if (!modeCountdown.isEmpty()) //  waiting 10 seconds by default
                localCtx._i_mode_countdown = Integer.valueOf(modeCountdown);

            SystemInfo sysInfo = LocalUtils.getSystemInfo();
            JSONObject joSysInfo = sysInfo.getInfoData();
            localCtx._s_meminfo = joSysInfo.getString("memory_size");
            localCtx._s_cpu_info = joSysInfo.getString("cpu_info");
            localCtx._s_client_version = joSysInfo.getString("version");
            localCtx._i_client_version_code = joSysInfo.getInt("version_code");

            localCtx._s_cookie = LocalUtils.loadCookie();

            // res.put("final_mode", localCtx._i_final_mode);
            // res.put("mode_countdown",localCtx._i_mode_countdown);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateOem() {
        LocalUtils.printLog("LocalLogic", "updateOem called!");
        
        if(VdiConfigure.getInst().data.reqOemRSEnable != 1)
            return;
        oemMgr.Start(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //need to download oem resources.
                int nret  =0;
                try{
                     nret = oemMgr.isOemSourceReady();
                }
                catch(Exception e){
                    return;
                }
                if (1 == nret) {
                    //reload url ,if download oem success.
                    oemMgr.update(new OemManager.OemUpdateListener() {

                        @Override
                        public void onUpdateFinish(String strUrl) {
                            // TODO Auto-generated method stub
                            //WebClient.mXWalkView.load("file:///" + strUrl, null);
                            String resID = oemMgr.getCurResourceID();
                            try {

                                if (resID != null && !resID.isEmpty())
                                    LocalUtils.SaveInfo(LocalUtils.configs.oemresid, resID);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                           if (localCtx._ctx_listener != null)
                                localCtx._ctx_listener.onEvent(listenerEvent.OEM_UPDATED);

                        }

                        @Override
                        public String onGetMD5() {
                            // TODO Auto-generated method stub
                            return oemMgr.getCurResourceID();
                        }
                    });
                } else {

                    if (localCtx._ctx_listener != null)
                        localCtx._ctx_listener.onEvent(listenerEvent.TRY_LOAD_RES);

                    switch (nret) {
                        case OemManager.OEMINFO_ERROR:
                        case OemManager.QUERYMD5_FAILED: {
                            LocalUtils.printLog("OemManager", "Error: http request 'get_md5'.");
                        }
                        break;

                    }
                }

            }   
        });

    }

    public void SetSvrIp(String strip, int port)
    {
        localCtx._i_svr_port = port;
        localCtx._s_svr_ip = strip;
        localCtx._s_client_mask = "255.255.248.0";
        localCtx._s_svr_base_URL = this.getSvrBaseUrl();
    }
    
    public void SetSimIp(String servip, int port,String termip,String mac)
    {
       SetSvrIp(servip,port);
       localCtx._s_client_ip = termip;
       localCtx._s_client_mac = mac;
    }
      
    public void registerListener(LocalLogicListener listener) {
        // TODO Auto-generated method stub
        localCtx._ctx_listener = listener;
    }

    public String getSvrIp() {
        return localCtx._s_svr_ip;
    }

    public String getSvrBaseUrl() {
        if (localCtx._s_svr_ip != null) {
            if (localCtx._s_svr_ip.isEmpty() || localCtx._i_svr_port <= 0)
                return null;
        }

        return "http://" + localCtx._s_svr_ip + ":" + String.valueOf(localCtx._i_svr_port);
    }

    public SpiceManager getSpiceMgr() {
        return this.spiceMgr;
    }

    public OemManager getOemManager() {
        return this.oemMgr;
    }

    public String processGet(String fun, Map<String, String> params) {

        if (fun.equalsIgnoreCase(LocalUtils.page_req.loop_data)) {
            //return VdiHttpd.RES_FAIL;
            //if(localCtx._i_reg_succ == 0)
            //{
            //	this.clientRegister();
            //}

            return onLoop();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.get_server_ip)) {
            return onGetSvrIP();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.system_info)) {
            return onGetSysInfo();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.get_ip)) {
            //LocalUtils.app_toastmessage("get ip params:" + params.toString(), 10000);
            return onGetIP();
        }
        
        if (fun.equalsIgnoreCase(LocalUtils.page_req.get_teacher_vms)) {
            return onGetTeacherVMS();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.get_client_type)) {
            return onGetClientType();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.desktop_mode)) {
            return onGetDesktopMode();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.personal_config)) {
            return onGetPerConfig();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.teacher_login)) {
            return onTeacherLogin();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.get_wifi_list)) {
            JSONObject jo = LocalUtils.defaultJsonObj(0, null);
            try {
                jo.put("result", "");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return jo.toString();
        }

        //shutdown or reboot VDI.
        if (fun.equalsIgnoreCase(LocalUtils.page_req.shutdown)) {
            return onShutdown(fun);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.reboot)) {
            LocalUtils.printLog("LocalLogic", "page req reboot: start reboot...");

            spiceMgr.clientLogout(false);
            LocalUtils.os_reboot("LocalUtils.page_req.reboot");

            return LocalUtils.defaultJsonObj(0, "").toString();
        }


        if (fun.equalsIgnoreCase(LocalUtils.page_req.windows_mode)) {

            JSONObject jo = LocalUtils.defaultJsonObj(0, "");
            JSONObject jres = new JSONObject();
            try {
                jres.put("windows_mode", -1);
                jo.put("result", jres);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return jo.toString();
        }

        //return  VdiHttpd.RES_FAIL;
        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.vms_power_off)) {
            return onVMSPowerOff();
        }

        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.vms_power_on)) {
            return onVMSPowerOn();
        }

        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.get_toolbar_status)) {
            return onGetToolbarStatus();
        }

        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.get_vms_info)) {
            return onGetVMSInfo();
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.browser_file)) {
            return onBrowserFile();
        }

        if (fun.contains(LocalUtils.page_req.app_exit)) {
           // onAppExit();
        }
        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    private String onBrowserFile() {
        // TODO Auto-generated method stub

       
        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    public void DoQuicklyShutdown(final String reason) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                spiceMgr.powerAction(false);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                LocalUtils.os_shutdown(reason);
            }
        }).start();

    }

    private String onShutdown(String reason) {
        LocalUtils.printLog("LocalLogic", "shutdown page req: shutdown...");

        byte[] vars = localCtx._s_shutdown_param.getBytes();
        boolean bt_t2v = vars[0] == '1';//0
        boolean bp_t2v = vars[2] == '1';
		
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
        boolean bshutdown = false;
        if (localCtx._i_login_type == 0 && bt_t2v)
            bshutdown = true;
        if (localCtx._i_login_type == 1 && bp_t2v)
            bshutdown = true;

        spiceMgr.clientLogout(bshutdown);

        LocalUtils.os_shutdown(reason);

        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    private String onVMSPowerOff() {
        this.spiceMgr.powerAction(false);
        return "ok";
    }

    private String onVMSPowerOn() {
        this.spiceMgr.powerAction(true);
          return "ok";
    }


    private String onGetToolbarStatus() {
        String strval = LocalUtils.LoadPersist_settings(LocalUtils.vdi_req.get_toolbar_status);
        JSONObject res = new JSONObject();
        try {
            res.put("showbar", "1");
            if (strval != null)
                res.put("showbar", strval);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res.toString();
    }

    private String onGetVMSInfo() {
		/*
		vmid:"id"
		name: "pc_name/pernal_name"
		toolbar: "0/1
		*/
        JSONObject jb = this.spiceMgr.getVMSDesc();
        if (jb != null)
            return jb.toString();
        jb = new JSONObject();
        try {
            jb.put("name", "unknown");
            jb.put("toolbar", "1");
            jb.put("vmid", "0");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jb.toString();
    }

    private String onGetClientType() {
        // TODO Auto-generated method stub
        //client_type
        JSONObject jo = LocalUtils.defaultJsonObj(0, null);
        JSONObject res = new JSONObject();
        try {
			 
			 /*
			String strType = LocalUtils.LoadInfo("auto_start"); 
			if(strType != null && !strType.isEmpty())
			{
				localCtx._i_client_type = Integer.valueOf(strType);
				res.put("client_type", localCtx._i_client_type);
			}*/
            res.put("client_type", 3); //android
            jo.put("result", res);
            return jo.toString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return LocalUtils.defaultJsonObj(51011, "").toString();
        //return VdiHttpd.RES_FAIL;

    }

    private String onGetDesktopMode() {
        JSONObject jo = LocalUtils.defaultJsonObj(0, null);
        JSONObject res = new JSONObject();
        try {

            res.put("desktop-mode", localCtx._i_desktop_mode);
            res.put("waittime", localCtx._i_desktop_wait);
            res.put("final_mode", localCtx._i_final_mode);
            res.put("mode_countdown", localCtx._i_mode_countdown);
            jo.put("result", res);
            return jo.toString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return VdiHttpd.RES_FAIL;
        return LocalUtils.defaultJsonObj(51011, "").toString();
    }


    private String onGetPerConfig() {
        String strConfig = LocalUtils.LoadInfo(LocalUtils.configs.personal);
        JSONObject res = null;//new JSONObject();
        JSONObject jo = null;

        try {
            if (strConfig == null || strConfig.isEmpty()) {
                res = new JSONObject();

                res.put("password", "");
                res.put("auto_login", 0);
                res.put("save_pwd", 0);
                res.put("username", "");
                //res.put(name, value)
            } else {
                res = new JSONObject(strConfig);
            }

            jo = LocalUtils.defaultJsonObj(0, null);
            jo.put("result", res);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //return VdiHttpd.RES_FAIL;
            return LocalUtils.defaultJsonObj(51011, "").toString();
        }

        return jo.toString();

    }


    //using asyntask to request vms data.
    private String onGetTeacherVMS() {
        //return this.onTeacherLogin();

        JSONObject joCmd = new JSONObject();
        try {
            joCmd.put("client_mac", localCtx._s_client_mac);
            LocalUtils.CookieHelper.setCookieEnable(true);
            
            int reqTimeout = VdiConfigure.getInst().data.reqVmsInfoTimeout;
            JSONObject jors = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/select_mode", joCmd, reqTimeout);
            if (jors == null) {
                return LocalUtils.defaultJsonObj(0, "").toString();
            }

            if (jors.has("error_code")) {
                int ncode = jors.getInt("error_code");
                return LocalUtils.defaultJsonObj(ncode, "").toString();
            }

            LocalUtils.printLog("LocalLogic", "select_mode:" + jors.toString());
            JSONObject jo = LocalUtils.defaultJsonObj(0, "");
            jo.put("result", jors);
            return jo.toString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return  VdiHttpd.RES_FAIL;
        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    //
    //maybe need asynctask
    //
    private String onTeacherLogin() {
        //JSONObject jo = LocalUtils.defaultJsonObj(0,null);
        JSONObject joCmd = new JSONObject();
        localCtx._i_login_type = 0;
        try {
            LocalUtils.printLog("LocalLogic", "client login name:" + localCtx._s_client_login_name);
            joCmd.put("name", localCtx._s_client_login_name);
            joCmd.put("passwd", localCtx._s_client_mac);
            joCmd.put("client_mac", localCtx._s_client_mac);
            joCmd.put("client_ip", localCtx._s_client_ip);

            localCtx._s_personal_name = "";
            localCtx._s_personal_pwd = "";

            LocalUtils.CookieHelper.setCookieEnable(true);
            int reqTimeout = VdiConfigure.getInst().data.userloginTimeout;
            JSONObject jors = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/auth_login", joCmd, reqTimeout);


            if (jors == null)
                return LocalUtils.defaultJsonObj(8000, "").toString();

            if (jors != null) {
                if (jors.has("error_code")) {
                    return LocalUtils.defaultJsonObj(jors.getInt("error_code"), jors.getString("error")).toString();
                }

                if (!jors.has("vms")) {
                    return LocalUtils.defaultJsonObj(0, "").toString();
                }

                LocalUtils.printLog("auth_login", "client login　rs:" + jors.toString());
                localCtx._s_client_trace = jors.getString("client_trace");
                JSONObject jo = LocalUtils.defaultJsonObj(0, "");
                jo.put("result", jors);

                return jo.toString();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return VdiHttpd.RES_FAIL;
        return LocalUtils.defaultJsonObj(8000, "").toString();
    }


    private String onPersonalLogin(Map<String, String> params) //auth_login
    {
        // TODO Auto-generated method stub

        String strdata = params.get("data-json");
        LocalUtils.printLog("LocalLogic", "post json:" + strdata);

        localCtx._i_login_type = 1;
        if (strdata != null && !strdata.isEmpty()) {
            try {
                JSONObject jo = new JSONObject(strdata);

                //error result
                if (!jo.has("password")) {
                    return LocalUtils.defaultJsonObj(0, "").toString();
                }

                JSONObject joCmd = new JSONObject();

                localCtx._s_personal_name = jo.getString("username");
                localCtx._s_personal_pwd = jo.getString("password");

                joCmd.put("name", localCtx._s_personal_name);
                joCmd.put("passwd", localCtx._s_personal_pwd);
                joCmd.put("client_mac", localCtx._s_client_mac);
                joCmd.put("client_ip", localCtx._s_client_ip);

                try {
                    if (jo.getInt("save_pwd") == 1)
                        LocalUtils.SaveInfo(LocalUtils.configs.personal, jo.toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    try {
                        LocalUtils.SaveInfo(LocalUtils.configs.personal, "");
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }


                LocalUtils.CookieHelper.setCookieEnable(true);
                int loginTimeout = VdiConfigure.getInst().data.userloginTimeout;
                JSONObject jors =
                        LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/auth_login", joCmd, loginTimeout);

                LocalUtils.printLog("LocalLogic", "onPersonalLogin() console return:" + jors.toString());

                //login success,try to store user name and pwd ..&& jors.getInt("error_code") == 0

                if (jors == null)
                    return LocalUtils.defaultJsonObj(0, "").toString();

                if (jors.has("error_code")) {
                    return LocalUtils.defaultJsonObj(jors.getInt("error_code"), jors.getString("error")).toString();
                }

                if (jors.has("client_trace"))
                    localCtx._s_client_trace = jors.getString("client_trace");

                jo = LocalUtils.defaultJsonObj(0, "");
                jo.put("result", jors);

                return jo.toString();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        //return LocalUtils.defaultJsonObj(0 ,"").toString();
        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    
    private String onGetIP() {
        // NetAddresslocalCtx
        //return VdiHttpd.RES_FAIL;

        JSONObject jo = LocalUtils.defaultJsonObj(0, null);
        int type = -1;
        try {

            NetInfo info = LocalUtils.getNetInfo();
            JSONObject res = info.getInfoData();

            type = Integer.valueOf(res.getString("net-type"));
            String ipAddress = null;

            if (res.has("address"))
                ipAddress = res.getString("address");
			/*
			for(int i = 0;i < 1;i++)//try ... times...
			{
				if(ipAddress == null || ipAddress.isEmpty())
				{
					info = LocalUtils.getNetInfo();
					res = info.getInfoData();
					if(res.has("address"))
					ipAddress = res.getString("address");
					//continue;
				}
				
				if(ipAddress != null && ipAddress.length() >4)
					break;
			}*/

            jo.put("result", res);
            if (ipAddress == null || ipAddress.length() < 8) {
                //no svr ip
                //if(localCtx._s_svr_ip == null || localCtx._s_svr_ip.isEmpty())
                //jo.put("result", res);

                if (type == 1)//dhcp
                    jo.put("code", 0);
                else
                    jo.put("code", 0);
            } else if (false == localCtx._s_client_ip.equalsIgnoreCase(ipAddress)) {
                localCtx._i_need_reg = 1;
                localCtx._s_client_ip = ipAddress;
            }
            //Toast.makeText(LocalUtils.getInst(), jo.toString(), 10000);
            //LocalUtils.app_toastmessage(jo.toString() + " line 912",10000);
            //VdiCrashHandler chHandler = new VdiCrashHandler();

            return jo.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //LocalUtils.app_toastmessage(e.toString(),10000);
        }

        if (type == -1) {
            return LocalUtils.defaultJsonObj(51011, "").toString();
        }
        //Toast.makeText(LocalUtils.getInst(), jo.toString(), 10000);
        //LocalUtils.app_toastmessage(jo.toString(),10000);
        try {
            if (type == 1)//dhcp
                jo.put("code", 51002);
            else
                jo.put("code", 51003);
        } catch (Exception e) {
        }
        //LocalUtils.app_toastmessage(jo.toString(),10000);
        return jo.toString();
    }

    private String onGetSysInfo() {
        SystemInfo info = LocalUtils.getSystemInfo();
        JSONObject jo = LocalUtils.defaultJsonObj(0, null);

        try {
            JSONObject res = info.getInfoData();
            jo.put("result", res);
            return jo.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    private String onGetSvrIP() {

        JSONObject jo = LocalUtils.defaultJsonObj(0, null);
        try {

            JSONObject je = new JSONObject();
            je.put("rabbitmq_ip", localCtx._s_rabbitmq_ip);
            je.put("console_ip", localCtx._s_svr_ip);
            if (localCtx._i_svr_port <= 0)
                je.put("console_port", "8090");
            else
                je.put("console_port", localCtx._i_svr_port);

            jo.put("result", je);

            return jo.toString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    private boolean bupdateGate = true;

    public void updateClose() {
        // TODO Auto-generated method stub
        bupdateGate = false;
    }

    public void updateOpen() {
        // TODO Auto-generated method stub
        bupdateGate = true;
    }

    private String onLoop() {

        JSONObject jo = LocalUtils.defaultJsonObj(0, null);
        JSONObject res = new JSONObject();
        
        try {
            res.put("Net_card_connected", localCtx._i_net_connected);
            res.put("Mq_connected", localCtx._i_svr_connected);
            res.put("Begin_order", localCtx._i_begin_order);
            res.put("End_order", localCtx._i_end_order);
            res.put("Refresh_order_id", localCtx._i_refresh_order);
            res.put("Interval", 1000);
            res.put("config_pwd", localCtx._s_config_psw);
            res.put("relogin", localCtx._i_relogin);
            // res.put("final_mode", localCtx._i_final_mode);
            // res.put("mode_countdown",localCtx._i_mode_countdown);

            //res.put("is_need_upgrate", 1);
            if (bupdateGate == false)
                res.put("is_need_upgrate", 0);
            else {
                res.put("is_need_upgrate", localCtx._i_need_update);
                // localCtx._i_need_update  ?
            }
            JSONObject reg_state = new JSONObject();
            reg_state.put("success", localCtx._i_reg_succ);
            reg_state.put("error", localCtx._i_reg_err);
            res.put("register_state", reg_state);

            jo.put("result", res);

            if (localCtx._i_relogin == 1)
                localCtx._i_relogin = 0;
			 
            /*
            if(localCtx._i_force_update == 1)
            {
                    localCtx._i_force_update = 0;
                    updateFromServer();
            }*/

            return jo.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch blocklocalCtx
            e.printStackTrace();
        }
        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    public String processPost(String fun, Map<String, String> params) {

        //return Post.process(module,fun,params);
        if (fun.equalsIgnoreCase(LocalUtils.page_req.vm_connect)) {
            return onVMConnect(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.set_server_ip)) {
            return onSetSvrIP(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.personal_login)) {
            return onPersonalLogin(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.set_ip)) {
            return onSetIP(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.set_order)) {
            return onSetOrder(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.ping_echo)) {
            return onPingEcho(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.start_ping)) {
            return onStartPing(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.end_ping)) {
            return onEndPing(params);
        }
		/*
		if(fun.equalsIgnoreCase(LocalUtils.page_req.change_pwd))
		{
			return onChangePwd(params);
		}*/

        if (fun.equalsIgnoreCase(LocalUtils.page_req.personal_modify_pwd)) {
            return onChangePwd(params);
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.upgrate_client)) {
            return onUpdateClient(params);
        }
        //if(fun.equalsIgnoreCase( LocalUtils.page_req.))
        //return  VdiHttpd.RES_FAIL;
        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.vms_power_off)) {
            return onVMSPowerOff();
        }

        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.vms_power_on)) {
            return onVMSPowerOn();
        }

        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.get_toolbar_status)) {
            return onGetToolbarStatus();
        }

        if (fun.equalsIgnoreCase(LocalUtils.vdi_req.get_vms_info)) {
            return onGetVMSInfo();
        }

        //shutdown or reboot VDI.
        if (fun.equalsIgnoreCase(LocalUtils.page_req.shutdown)) {
            return onShutdown(fun);
        }

        if(fun.contains(LocalUtils.page_req.feature_config))
        {
            return  onFeatureConfig();
        }

        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    //
    //using for webpage configuration
    //
    private String onFeatureConfig()
    {
        //{ "settingbtn":1,"shutdownbtn":1,"rebootbtn:1} fileexplorerbtn  exitbtn
        try {
            JSONObject jo = new JSONObject();
            jo.put("settingbtn",1);
            jo.put("shutdownbtn",1);
            jo.put("rebootbtn",1);
            jo.put("fileexplorerbtn",1);
            jo.put("exitbtn",1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return LocalUtils.defaultJsonObj(0, "").toString();
    }
    
    private String onEndPing(Map<String, String> params) {
        // TODO Auto-generated method stub

        

        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    private String onPingEcho(Map<String, String> params) {
        // TODO Auto-generated method stub
		/*
		 start_ping
			请求信息：{ip:"10.2.2.2",number:4}
			返回信息：{code:errCode,message:"",id:11},
		ping_echo
			请求信息：{ip:"10.2.2.2",number:4,id:11}，
			返回信息：{code:errCode,message:"",id:11},
						返回信息无ID则表示结束
		end_ping
			请求信息：{id:11}
			返回信息：{code:errCode,message:""},
		 */
        /*
        String strJson = params.get("data-json");
        if (strJson == null || strJson.isEmpty()) {
            LocalUtils.printLog("LocalLogic", "onStartPin，json data error");
            return LocalUtils.defaultJsonObj(80002, "").toString();
        }

        try {
            JSONObject jo = new JSONObject(strJson);
            int id = jo.getInt("id");
            //int	   number = jo.getInt("number");
            PingClient pc = icmpMgr.getClientByID(id);
            if (pc == null) {
                LocalUtils.printLog("LocalLogic", "get ping client failed!");

                jo = LocalUtils.defaultJsonObj(0, "");
                JSONObject jres = new JSONObject();
                jres.put("runing", 0);
                jo.put("result", jres);

                return jo.toString();
                //return VdiHttpd.RES_FAIL;
            }

            String pingline = pc.getLine();

            LocalUtils.printLog("onPingEcho", "get line:" + pingline);

            jo.put("code", 0);
            jo.put("message", "");

            JSONObject jres = new JSONObject();
            jres.put("message", pingline);

            if (pc.isPingActive())
                jres.put("running", 1);
            else {
                jres.put("running", 0);
                icmpMgr.rmDeadClient(id);
            }

            jo.put("result", jres);
            return jo.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    private String onStartPing(Map<String, String> params) {
        // TODO Auto-generated method stub
        /*
        String strJson = params.get("data-json");
        if (strJson == null || strJson.isEmpty()) {
            LocalUtils.printLog("LocalLogic", "onStartPin，json data error");
            return LocalUtils.defaultJsonObj(80002, "").toString();
        }

        try {
            JSONObject jo = new JSONObject(strJson);
            String strip = jo.getString("ip");
            int number = jo.getInt("number");
            PingClient pc = icmpMgr.createClient();
            pc.pingStart(strip, number, -1);

            int id = pc.getID();

            jo = LocalUtils.defaultJsonObj(0, "");
            //jo.put("id", id);
            JSONObject jres = new JSONObject();
            jres.put("id", id);
            jres.put("message", "");
            jo.put("result", jres);

            return jo.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        return LocalUtils.defaultJsonObj(51011, "").toString();
        //return null;
    }

    private String onUpdateClient(Map<String, String> params) {
        // TODO Auto-generated method stub
        String joStr = params.get("data-json");
        if (joStr == null || joStr.isEmpty()) {
            LocalUtils.printLog("LocalLogic", "onUpdateClient params empty");
            return LocalUtils.defaultJsonObj(80002, "").toString();
        }

        JSONObject jo;
        try {

            jo = new JSONObject(joStr);
            if (jo.has("upgrate_file")) {
                String strFile = jo.getString("upgrate_file");

                if (strFile != null && !strFile.isEmpty())
                {
                    
                }
            }

            if (jo.has("is_local")) {
                int is_local = jo.getInt("is_local");
                if (is_local == 0) {
                    updateFromServer();
//                    newTrdUpdateFromServer();
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    public void newTrdUpdateFromServer() {
     //   LocalUtils.printLog("onUpdateClient", "pgDialog.show()");
    //    final ProgressDialog pgDialog = new ProgressDialog(LocalUtils.getInst());
     //   pgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      //  pgDialog.setCancelable(false);
     //   pgDialog.setCanceledOnTouchOutside(false);
    //    pgDialog.setMessage("Upgrading ...");
    //    pgDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                updateFromServer();
               // pgDialog.cancel();
            }
        }).start();

    }


    private volatile boolean downloading = false;

    public void updateFromServer() {

        if (downloading == false)
            downloading = true;
        else
            return;
        
        if(VdiConfigure.getInst().data.reqAppPackEnable != 1)
            return;
        //kill vdi ,scrplayer and movie player to turn webclient to foreground.
   
        //LocalUtils.clearInternalCache();

        //LocalUtils.killApp("com.oe.vdi");

        //start download app,and install it.
        //"ftp://isouser:isouser@10.2.2.188/iso/repo/android_client/evdi-android-client.apk"
        String apkftppath = "ftp://isouser:isouser@";
        apkftppath += localCtx._s_svr_ip;
        apkftppath += "/iso/repo/android_client/evdi-android-client.apk";

        FtpUpdateHelper.startJob(apkftppath, new FtpUpdateHelper.FtpUpdateListener() {

            @Override
            public String onGetStorePath() {
                // TODO Auto-generated method stub
                //try to use external path at first,otherwise use tmp path.
                //LocalUtils.getInst();
                //File extf = android.os.Environment.getExternalStorageDirectory();
                //LocalUtils.printLog("onUpdateClient", "exteranl path:" + extf.toString());
                String path = LocalUtils.external_pub_dir + "/" +  localCtx._s_client_mac;
                File f = new File(path);
                f.mkdirs();
                return path +"/webclient.apk";
            }

            //
            //onDownloadFinish:
            //just telling the truth about download progress are finished.success or failure distinguished
            //by 'err' parameter.
            //
            @Override
            public void onDownloadFinish(String localpath, int err) {
                // TODO Auto-generated method stub
                downloading = false;
                if (err != 0) {
                    LocalUtils.printLog("onUpdateClient", "download error!");
                    return;
                }

                LocalUtils.printLog("onUpdateClient", "download success!");
              //  int nret = LocalUtils.pm_installapk(localpath, "com.oevdi.client", LocalUtils._bSystemRooted);
               
            }

            //
            //onError : response for errors that occur before beginning of ftp download.
            //
            @Override
            public void onError(String strError) {
                // TODO Auto-generated method stub
                downloading = false;
                LocalUtils.printLog("onUpdateClient", "FTP error:" + strError);
            }

            @Override //mf
            public String onGetMD5() {

                try {
                    Thread.sleep(6000); //delay 6 seconds to wait for md5.
                } catch (InterruptedException e) {
                }

                String strMd5 = "";

                synchronized (localCtx._s_apk_md5) {
                    strMd5 = localCtx._s_apk_md5;
                }
                return strMd5;
            }
        });

    }

    private String onChangePwd(Map<String, String> params) {
        // TODO Auto-generated method stub
        String joStr = params.get("data-json");

        //LocalUtils.writeExternalLog("web","onChangePwd()",joStr);
        try {
            JSONObject jo = new JSONObject(joStr);
            if (jo.has("user_name")) {
                LocalUtils.CookieHelper.setCookieEnable(true);
                JSONObject joRes = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/change_pwd", jo, 10000);
                //spiceMgr.startSpice(id);
                if (joRes == null)
                    return LocalUtils.defaultJsonObj(51009, "").toString();
                if (joRes.has("error_code"))
                    return LocalUtils.defaultJsonObj(joRes.getInt("error_code"),
                            joRes.getString("error")).toString();

                String strOld = LocalUtils.LoadInfo(LocalUtils.configs.personal);
                if (strOld != null && !strOld.isEmpty()) {
                    JSONObject jso = new JSONObject(strOld);
                    if (jso.has("auto_login")) {
                        int al = jso.getInt("auto_login");
                        if (al == 1) {
                            jso.put("password", jo.get("new_pwd"));
                            try {
                                LocalUtils.SaveInfo(LocalUtils.configs.personal, jso.toString());
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                }

                //LocalUtils.writeExternalLog("web","onChangePwd() returned",joRes.toString());
                return LocalUtils.defaultJsonObj(0, null).put("result", joRes).toString();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    private String onVMConnect(Map<String, String> params) {
        // TODO Auto-generated method stub
        String joStr = params.get("data-json");
        if (joStr == null || joStr.isEmpty()) {
            LocalUtils.writeExternalLog("httpserver", "onVMConnect() exception:", "data error:" + params.toString());
            return LocalUtils.defaultJsonObj(0, "").toString();
        }

        try {
            JSONObject jo = new JSONObject(joStr);
            if (jo.has("vm_id")) {
                //int id = jo.getInt("vm_id");
                jo.put("client_mac", localCtx._s_client_mac);
                LocalUtils.CookieHelper.setCookieEnable(true);

                String strTimeout = LocalUtils.LoadPersist_settings(LocalUtils.configs.oevdiclientvmsconnectingtime);

                int timeout = 600000; //10 minutes
                if (strTimeout != null && !strTimeout.isEmpty()) {
                    try {
                        timeout = Integer.valueOf(strTimeout);
                    } catch (Exception e) {
                    }
                } else {
                    LocalUtils.SavePersist_settings(LocalUtils.configs.oevdiclientvmsconnectingtime, "600000");
                }

                JSONObject joRes = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/vm_connect", jo, 10000);
                while (timeout >= 0) {
                    if (joRes != null)
                        break;

                    try {

                        Random ran = new Random(System.currentTimeMillis());
                        int delay = ran.nextInt() % 10;

                        if (delay <= 0)
                            delay = 5;
                        if (delay <= 3)
                            delay += 5;

                        Thread.sleep(delay * 1000);
                        timeout -= delay * 1000;

                        joRes = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/vm_connect", jo, 10000);
                    } catch (Exception e) {
                        LocalUtils.writeExternalLog("httpserver", "request vm_connect exception:", e.toString());
                    }
                }

                //spiceMgr.startSpice(id);
                //LocalUtils.printLog("LocalLogic","vm_connect res:" + joRes.toString());
                if (joRes != null && joRes.has("error_code")) {
                    LocalUtils.writeExternalLog("httpserver", "vm_connect contain error:", joRes.toString());
                    return LocalUtils.defaultJsonObj(joRes.getInt("error_code"),
                            joRes.getString("error")).toString();
                }

                spiceMgr.callSpice(jo.getString("vm_id"), joRes);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LocalUtils.writeExternalLog("httpserver", "onVMConnect() exception:", e.toString());

        }

        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    private String onSetOrder(Map<String, String> params) {
        // TODO Auto-generated method stub
        String strdata = params.get("data-json");
        LocalUtils.printLog("LocalLogic", "post json:" + strdata);
        if (strdata == null || strdata.isEmpty())
            return null;

        try {
            JSONObject jo = new JSONObject(strdata);
            localCtx._i_refresh_order = jo.getInt("order");

            //String svrUrl = localCtx._s_svr_base_URL +"/set_order";
            jo.put("client_id", localCtx._i_client_id);

            LocalUtils.CookieHelper.setCookieEnable(true);
            int timeout  =VdiConfigure.getInst().data.setorderTimeout ;
            JSONObject jores = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/set_order", jo, timeout);
            LocalUtils.printLog("locallogic", "console set_order:" + jores.toString());

            if (jores == null || jores.length() <= 0)
                return LocalUtils.defaultJsonObj(51009, "").toString();

            if (jores.has("error_code")) {
                return LocalUtils.defaultJsonObj(jores.getInt("error_code"),
                        jores.getString("error")).toString();
            }

            JSONObject joret = LocalUtils.defaultJsonObj(0, null);
            joret.put("result", jores);

            return joret.toString();

        } catch (Exception e) {
            // TODO Auto-generated catch block`
            e.printStackTrace();
        }


        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    public void clientLogout() {
        // TODO Auto-generated method stub
        byte[] vars = localCtx._s_shutdown_param.getBytes();
        boolean bt_t2v = vars[0] == '1';//0
        boolean bp_t2v = vars[2] == '1';

        boolean bshutdown = false;
        if (localCtx._i_login_type == 0)
            bshutdown = true;
        if (localCtx._i_login_type == 1 && bp_t2v)
            bshutdown = true;

        spiceMgr.clientLogout(bshutdown);
    }

    //
    //no implementation,for android system limition.
    //
    private String onSetIP(Map<String, String> params) {
        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    private String onSetSvrIP(Map<String, String> params) {

        String strJson = null;
        try {

            strJson = params.get("data-json");
            JSONObject jo = new JSONObject(strJson);

            String svrip = jo.getString("console_ip");
            int port = jo.getInt("console_port");

            localCtx._s_svr_ip = svrip;
            localCtx._i_svr_port = port;

            if (localCtx._s_svr_ip != null && !localCtx._s_svr_ip.isEmpty()
                    || localCtx._i_svr_port > 0) {
                LocalUtils.SavePersist(LocalUtils.configs.oevdiserverip, localCtx._s_svr_ip);
                LocalUtils.SavePersist(LocalUtils.configs.oevdiserverport,
                        String.valueOf(localCtx._i_svr_port));

                if (localCtx._ctx_listener != null)
                    localCtx._ctx_listener.onEvent(listenerEvent.SVR_CHANGED);

                if (false == svrip.contentEquals(localCtx._s_pre_svr_ip))
                    localCtx._i_need_reg = 1;
                else {
                    localCtx._i_need_reg = 0;
                    localCtx._i_reg_succ = 1;
                    localCtx._i_reg_err = 0;
                }

                return LocalUtils.defaultJsonObj(0, "").toString();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return LocalUtils.defaultJsonObj(51011, "").toString();
    }

    //
    //option requery
    //
    public String processOption(String fun, Map<String, String> params) {
        // TODO Auto-generated method stub

        LocalUtils.printLog("LocalLogic", "option params:" + params.toString());
        if (fun.equalsIgnoreCase(LocalUtils.page_req.personal_login)) {
            String strdata = params.get("data-json");
            LocalUtils.printLog("LocalLogic", "option json:" + strdata);
            //return VdiHttpd.RES_OK;
        }

        if (fun.equalsIgnoreCase(LocalUtils.page_req.set_server_ip)) {
            String strdata = params.get("data-json");
            LocalUtils.printLog("LocalLogic", "option json:" + strdata);
            //return VdiHttpd.RES_OK;
        }

        //support all requry on server
        return LocalUtils.defaultJsonObj(0, "").toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //console protocal
    //
    //Client register
    // 1. client login 2. register user
    //

    private void delegateEvent(listenerEvent event) {
        if (event == listenerEvent.SVR_REGISTERED)
            localCtx._s_pre_svr_ip = localCtx._s_svr_ip;

        if (localCtx._ctx_listener != null)
            localCtx._ctx_listener.onEvent(event);
    }

    public void clientRegister() {
        //
        JSONObject json = new JSONObject();
        try {
            json.put("client_name", localCtx._s_client_name);
            json.put("client_ip", localCtx._s_client_ip);
            json.put("client_mac", localCtx._s_client_mac);
            json.put("client_os", localCtx._s_os_name);
            json.put("client_netmask", localCtx._s_client_mask);
            json.put("client_cpu", localCtx._s_cpu_info);
            json.put("client_memory", localCtx._s_meminfo);

            json.put("client_version", localCtx._s_client_version);

            json.put("routing_key", localCtx._s_client_mac);
            json.put("token", LocalUtils.Encrypt.getSha256(localCtx._s_client_mac + "," + "oe1234"));
            json.put("pool", 1);

            LocalUtils.printLog("clientRegister", "register localCtx:" + localCtx._s_client_mac);
        } catch (Exception e) {
            // TODO Auto-generated catch blockc
            e.printStackTrace();
        }

        LocalUtils.CookieHelper.setCookieEnable(false);
        int timeout = VdiConfigure.getInst().data.registClientTimeout;
        JSONObject jb = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/register", json, timeout);
        
        if (jb == null) {
            localCtx._i_svr_connected = 0;
            localCtx._i_reg_succ = 0;
            localCtx._i_reg_err = 50012;
            localCtx._i_need_reg = 1;
            delegateEvent(listenerEvent.SVR_REGISTER_FAILED);
            return;
        }
        LocalUtils.printLog("clientRegister", "register localCtx:" + jb.toString());
        //return damaged data or not

        if (jb.has("mode_login_name")) {
            try {
                localCtx._s_client_login_name = jb.getString("mode_login_name");
                localCtx._s_config_psw = jb.getString("configPwd");
                localCtx._i_client_id = jb.getInt("id");

                localCtx._i_reg_succ = 1;
                localCtx._i_reg_err = 0;
                //localCtx._i_relogin = 1; //need to relogin again.
                localCtx._i_svr_connected = 1;
                localCtx._i_need_reg = 0;
                delegateEvent(listenerEvent.SVR_REGISTERED);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                delegateEvent(listenerEvent.SVR_REGISTER_FAILED);
            }
        } else {
            LocalUtils.printLog("clientRegister", "register localCtx:" + jb.toString());
            //process data responsed.
            localCtx._i_reg_succ = 0;
            localCtx._i_reg_err = 50012;
            localCtx._i_svr_connected = 0;
            localCtx._i_need_reg = 1;
            delegateEvent(listenerEvent.SVR_REGISTER_FAILED);
        }

        //localCtx._i_need_update = 1;
        //for update
        localCtx._i_need_update = 0;
        if (jb.has("version")) {
            try {
                String strVer = jb.getString("version").trim();

                if (strVer.isEmpty() || (false == strVer.contains("-"))) // incorrect version
                {
                    LocalUtils.printLog("clientRegister", "incorrect version:" + strVer);
                    return;
                }

                String vars[] = strVer.split("-");

                String strCode = null;
                if (vars.length == 2)
                    strCode = vars[vars.length - 1];
                if (vars.length == 3)
                    strCode = vars[vars.length - 2];

                LocalUtils.printLog("LocalLogic", "register detect version:" + jb.toString());

               // PackageInfo pkgInfo = LocalUtils.getPackInfo("com.oevdi.client");
                localCtx._i_client_version_code = 999; //pkgInfo.versionCode;
                if (Integer.valueOf(strCode) > localCtx._i_client_version_code) {
                    localCtx._i_need_update = 1;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        return;
    }

    //
    //update localCtx for aliving and something else..
    //
    private void pullCommands() {
        // TODO Auto-generated method stub
 
        JSONObject json = new JSONObject();
        try {
            json.put("client_mac", localCtx._s_client_mac);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LocalUtils.CookieHelper.setCookieEnable(true);
        
        int pulltimeout = VdiConfigure.getInst().data.pullTimeout;
        JSONObject jb = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/pull", json, pulltimeout);
        if (jb == null || jb.length() <= 0) //connection fail or null cmds
        {
            return;
        }
        
        LocalUtils.printLog("LocalLogic", "get pull :" + jb.toString());
        
       if(!LocalUtils.enable_log)
            Logger.getLogger(ConsoleServer.class.getName()).log(Level.INFO, "get pull :" + jb.toString());
        //LocalUtils.printLog("LocalLogic","pull json length:" + String.valueOf(jb.length()));
        if (jb.has("error_code")) //server return error
            return;

        processPullCmd(jb);

    }

    //using asynTask to do pull cmd
    private void processPullCmd(JSONObject jb) {
        // TODO Auto-generated method stub
        LocalUtils.printLog("LocalLogic", "pull:" + jb.toString());
        String fun = null;
        String funID = null;
        try {
            fun = jb.getString("cmd_func");
            funID = jb.getString("cmd_id");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (fun == null || fun.isEmpty())
            return;

        //create worker trds to process commands.
		/*
		 *{
			  "cmd_id": "XXXXXXXXXXX"
			  "cmd_func": "changeName"
			  "cmd_args": {
			    "name": "abc"
			  }
		  }
		 */
        try {

            JSONObject args = jb.getJSONObject("cmd_args");
            LocalUtils.printLog("LocalLogic", "pull fun:" + fun + " parms:" + args.toString());

            //{"client_mac":"aabbccddeeff", reply_cmd_id: "NNNN", "reply_body": {}}
            boolean bRet = cslLogic.callfunc(fun, funID, args);
            if (bRet) {
                jb.remove("cmd_args");
                jb.remove("cmd_id");
                jb.remove("cmd_func");

                jb.put("client_mac", localCtx._s_client_mac);
                jb.put("replay_cmd_id", funID);
                jb.put("reply_body", "{}");

                LocalUtils.CookieHelper.setCookieEnable(true);
                
                int pullreplyTimeout = VdiConfigure.getInst().data.pullReplyTimeout;
                JSONObject res = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/pull", jb, pullreplyTimeout);
                LocalUtils.printLog("LocalLogic", "request:" + jb.toString() + " res:" + res.toString());
                jb = null;
                res = null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private int dieTime = 0;

    private boolean ConnectionAreadyDie() {
        return dieTime >= 8;
    }

    private void resetDieTime() {
        dieTime = 0;
    }

    private void justCountupDie() {
        dieTime++;
    }

    private boolean keepAlive() {

        JSONObject json = new JSONObject();
        try {
            json.put("client_name", localCtx._s_client_name);
            json.put("client_ip", localCtx._s_client_ip);
            json.put("client_mac", localCtx._s_client_mac);
            json.put("client_id", localCtx._i_client_id); //client uuid

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LocalUtils.CookieHelper.setCookieEnable(true);
        int timeout = VdiConfigure.getInst().data.keepaliveTimeout;
        JSONObject jb = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/keep_alive", json, timeout);

        // always really timeout or net-cable plug out,
        // but localCtx was returned as timeout.
        if (jb == null) {
            LocalUtils.printLog("LocalLogic", "get keepAlive localCtx:" + "query keep_alive failed");
            if (dieTime >= 3)
                localCtx._i_svr_connected = 0;
            dieTime++;
            return false;
        }

        dieTime = 0;
        LocalUtils.printLog("LocalLogic", "get keepAlive result:" + jb.toString());
        if (jb.length() >= 2) //not null element
        {
            //LocalUtils.printLog("LocalLogic","pull json length:" + String.valueOf(jb.length()));
            if (jb.has("error_code")) //server return error
            {
                try {
                    if (jb.getInt("error_code") == 50012) //authorization expired,need to register client.
                        localCtx._i_need_reg = 1;
                    else {
                        if (dieTime >= 3)
                            localCtx._i_svr_connected = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else
                localCtx._i_svr_connected = 1;

            try {
                synchronized (localCtx._s_apk_md5) {
                    localCtx._s_apk_md5 = jb.getString("md5");
                }
            } catch (JSONException e) {
            }

        } else {
            if (dieTime >= 3)
                localCtx._i_svr_connected = 0;
        }
        return true;
    }

    private static Context getInst() {
        // TODO Auto-generated method stub
        return null;
    }

    public LocalContext getlocalCtxCopy() {
        return this.localCtx.copy();
    }

    public LocalContext getlocalCtx() {
        return this.localCtx;
    }

    public String getWebMainPath() {
        //LocalUtils.SaveInfo("oemresid", resID);
        String strDefInitPath = LocalLogic.getDefInitResPathName();
        String strID = LocalUtils.LoadInfo(LocalUtils.configs.oemresid);

        if (oemMgr.isExistInLocal(strID))
            return "file:///android_asset/" + strID + "/arm_windows/main.html";

        String extraPath = "/data/data/com.oevdi.client/" + strID;

        File f = new File(extraPath);
        if (f.isDirectory()) {
            String str = LocalUtils.findPage(f, "main.html");
            if (str != null && !str.isEmpty()) {
                //detect config.js in each loading time.
                LocalUtils.createConfigFile(str);
                return "file://" + str;
            }
        }

        //return defAssetPath;
        return strDefInitPath;
    }

    //
    //detect netcard ,and ensure console service alive or not,
    //then access and process commands from console.
    //
    public void updateLocal() {
        // TODO Auto-generated method stub

        localCtx._i_net_connected = 1;//LocalUtils.netCardIsOk();

        if (0 == localCtx._i_net_connected) {
            localCtx._i_svr_connected = 0;
            justCountupDie();
            if (ConnectionAreadyDie()) {
                if (spiceMgr.getCurSpiceInstID() != null) {
                  //  LocalUtils.exitSpiceBroadcast("updateLoacal:ConnectionAreadyDie");
                    spiceMgr.reset();
                }

                resetDieTime();
            }

            return;
        }

        localCtx._s_svr_base_URL = getSvrBaseUrl();
        if (null == localCtx._s_svr_base_URL ||
                localCtx._s_svr_base_URL.isEmpty()) {
            //localCtx._i_relogin = 1;
            return;
        }

        if (1 == localCtx._i_need_reg) {
            this.clientRegister();
            return;
        }
        //connection is die .....
        if (ConnectionAreadyDie()) {
            if (spiceMgr.getCurSpiceInstID() != null) {
                spiceMgr.reset();
                resetDieTime();
            }
        }

       boolean  bkeeplive = (VdiConfigure.getInst().data.keepaliveEnable == 1);
       if(bkeeplive)
           this.keepAlive();
        
        
        pullCommands();
        
    }

}
