package vdiclientsimulator.oevdi.client;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import jdk.nashorn.internal.runtime.Context;
import org.json.JSONException;
import org.json.JSONObject;
import vdiclientsimulator.oevdi.client.LocalLogic.LocalContext;

public class SpiceManager {

    Context mCtx;
    String mVMStrID;
    JSONObject mVMDesc;
    LocalContext localCtx;
    int errCode;

    DelayedCallRunnable mDelayedCallRunnable;
    class DelayedCallRunnable implements Runnable {

        String mStrVMID;
        JSONObject mParams;

        public void setVars(String strVMID, JSONObject params) {
            mStrVMID = strVMID;
            mParams = params;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            startVMS(mStrVMID, mParams);
        }
    };

    public SpiceManager(Context ctx, LocalContext localCtx) {
        mCtx = ctx;
        mVMStrID = null;
        this.localCtx = localCtx;
        errCode = 0;
    }

    public boolean isRemoteVMClosed() {
        if (mVMStrID != null) {
            return isVMShutdown(mVMStrID);
        }

        return false;
    }

    public String makePWD(String id) {
        String oestr = "Tcloudi-";
        int zeroLen = 8 - id.length();
        while (--zeroLen >= 0)
            oestr += "0";
        oestr += id;

        String md5 = null;
        try {
            md5 = LocalUtils.Encrypt.getMD5(oestr);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return md5.substring(md5.length() - 6, md5.length());
    }

    public void processDelayCall() {
        if (mDelayedCallRunnable != null) {
            mDelayedCallRunnable.run();
            mDelayedCallRunnable = null;
        }
    }

    public boolean callSpice(String strVMID, JSONObject params) {

      //  oLogger.getLogger().info("Start Spice");
        LocalUtils.writeExternalLog("spice", "callSpice", "Start Spice.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return startVMS(strVMID, params);
    }

    public boolean startVMS(String strVMID, JSONObject params) {
        return true;
    }

    //
    //rooted android os only can use 'kill' command with pid.
    //
    public String getCurSpiceInstID() {
        return mVMStrID;
    }

    public JSONObject getVMSDesc() {
        return mVMDesc;
    }

    public void reset() {
        mVMStrID = null;
        mVMDesc = null;
    }

    public boolean powerAction(boolean enable) {
        if (null == mVMStrID)
            return false;
        
        String poweract = "power-on";
        if (false == enable)
            poweract = "power-off";

        JSONObject jo = new JSONObject();
        try {
            jo.put("vm_id", mVMStrID);
            jo.put("action", poweract);
            jo.put("client_id", localCtx._i_client_id);
            LocalUtils.CookieHelper.setCookieEnable(true);
            JSONObject joRes = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/vm_action", jo, 2000);
            if (joRes == null) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }

                    joRes = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/vm_action", jo, 2000);
                    if (joRes != null)
                        break;
                }
            }

            if (joRes == null) {
                errCode = 1; //net issue.
                jo = null;
                LocalUtils.printLog("LocalLogic", "vm status failed(fatal net error or service no response.)!");
                return false;
            }
            
            return true;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }


    public boolean killSpice() {
        //enum activity insts and kill spice.
        return false;
    }

    public void setVMDesc(JSONObject jo) {
        mVMDesc = jo;
    }

    private boolean isVMShutdown(String strVMID) {
        errCode = 0;
        //vm_status
        if (strVMID == null || strVMID.isEmpty())
            return false;

        JSONObject jo = new JSONObject();
        try {
            jo.put("vm_id", strVMID);
            LocalUtils.CookieHelper.setCookieEnable(true);

            JSONObject joRes = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/vm_status", jo, 3000);
            if (joRes == null) {
                for (int i = 0; i < 10; i++)//try 10 timnes....
                {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    joRes = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/vm_status", jo, 3000);

                    if (joRes != null)
                        break;
                }
            }

            if (joRes == null) {
                errCode = 1; //net issue.
                jo = null;
                LocalUtils.printLog("LocalLogic", "vm status failed(fatal net error or service no response.)!");
                return false;
            }

            boolean bResult = joRes.getBoolean("result");
            return !bResult;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public boolean clientLogout(boolean bShutdown) //auth_logout
    {
        errCode = 0;
        JSONObject joCmd = new JSONObject();
        /*
        client_mac,        string,      是,     mac
        client_trace,      string,      否,     ??
        shutdown_mode_vm,  bool,        否,     虚拟机关机
        */
        try {
            joCmd.put("client_mac", localCtx._s_client_mac);
            joCmd.put("client_trace", localCtx._s_client_trace);//
            joCmd.put("shutdown_mode_vm", bShutdown);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LocalUtils.CookieHelper.setCookieEnable(true);
        JSONObject jors = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/auth_logout", joCmd, 2000);

        int trytimes = 2;
        do {
            if (jors != null)
                break;
            jors = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/auth_logout", joCmd, 2000);
        } while (--trytimes >= 0);

        if (jors == null) {
            LocalUtils.printLog("LocalLogic", "calling clientLogout() failed in 3 times");

           // oLogger.getLogger().info("Call clientLogout failed!");
            LocalUtils.writeExternalLog("spice", "clientLogout() ", "calling 3 times, failed!");
            return false;
        }

        LocalUtils.printLog("LocalLogic", "ondestory logout:" + jors.toString());

       // oLogger.getLogger().info("Calling clientLogout...");
        LocalUtils.writeExternalLog("spice", "clientLogout() ", "calling...");
        if (jors.has("result")) {
            try {
                return jors.getBoolean("result");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }

    static class shutownPolicy {
        boolean _0_vpc2terminal, _0_terminal2vpc;
        boolean _1_vpc2terminal, _1_terminal2vpc;

        public shutownPolicy(String shutdown_param) {
            _0_terminal2vpc = shutdown_param.charAt(0) != '0';
            _0_vpc2terminal = shutdown_param.charAt(1) != '0';
            _1_terminal2vpc = shutdown_param.charAt(2) != '0';
            _1_vpc2terminal = shutdown_param.charAt(3) != '0';
        }

        public String toString() {
            return String.format("_0_vpc2terminal:{0}\t_0_terminal2vpc:{1}\t_1_vpc2terminal:{2}\t_1_terminal2vpc:{3} ",
                    _0_vpc2terminal, _0_terminal2vpc, _1_vpc2terminal, _1_terminal2vpc);
        }
    }

    public boolean execShutdownPolicy() {
        // TODO Auto-generated method stub
        shutownPolicy sp = new shutownPolicy(localCtx._s_shutdown_param);
        
        this.clientLogout(false);
        //oLogger.getLogger().info("Excute shutdown policy.");
        LocalUtils.writeExternalLog("spice", "shutdown policy", "Excute shutdown policy");
        if (isRemoteVMClosed()) {
            //killSpice();
            LocalUtils.printLog("SpiceManager",
                    "execShutdownPolicy(): shutdown...:" + localCtx._i_login_type + ", shutownPolicy" + sp.toString());
            LocalUtils.writeExternalLog("spice", "vm status", "remote vm has been closed");
   
            if (localCtx._i_login_type == 0 && sp._0_vpc2terminal) //teacher login
                LocalUtils.os_shutdown("execShutdownPolicy:" + sp.toString());
            if (localCtx._i_login_type == 1 && sp._1_vpc2terminal)
                LocalUtils.os_shutdown("execShutdownPolicy:" + sp.toString());

        } else {
            LocalUtils.writeExternalLog("spice", "vm status", "remote vm is still runing.");
            if (errCode == 1) //is net issue
            {
                return false;
            }
        }

        reset();
        return true;
    }
}
