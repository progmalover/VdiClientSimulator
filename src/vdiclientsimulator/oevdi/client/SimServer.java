/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vdiclientsimulator.oevdi.client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import vdiclientsimulator.oevdi.client.LocalLogic.LocalContext;

/**
 * @author danny
 */
class SimServer {
    
    private LocalLogic mlocalLogic;
    private Thread mWorkTrd = 
    new Thread(new Runnable(){
        int mTicks;
        @Override
        public void run() {
           mTicks = 0;
           VdiConfigure.configure conf = VdiConfigure.getInst().data;
           mlocalLogic.updateOem();
           while(true)
           {
             
                try {
                    if(mlocalLogic == null)
                        return;
                    int isucc  = 0;
                    int ierr = 0;
                    
                    JSONObject jo = new JSONObject(mlocalLogic.processGet(LocalUtils.page_req.loop_data, null));
                    if(null != jo && jo.has("result"))
                    {
                        JSONObject jr = (JSONObject)jo.get("result");
                        if(jr.has("register_state"))
                        {
                            JSONObject jrr = jr.getJSONObject("register_state");
                            isucc= jrr.getInt("success");
                            ierr = jrr.getInt("error");
                            if(ierr  > 1 || isucc <= 0)
                            {
                                LocalUtils.printLog("register_state", "client not register");
                               // continue;
                                // Logger.getLogger(SimServer.class.getName()).log(Level.SEVERE, "regester_state", "regiest serv failured!");
                            }
                        }
                    }
                    
                   // mlocalLogic.processPost(null, null);
                      
                    if(mTicks % conf.reqVmsDuration == 0 && isucc != 0 )
                    {
                       
                        LocalContext lc = mlocalLogic.getlocalCtx();
                        
                        if(lc._i_reg_succ > 0 || lc._i_reg_err <= 0)
                        switch(lc._i_desktop_mode)
                        {
                            case 0: //lesson mode
                            {
                                    String strJson = mlocalLogic.processGet(LocalUtils.page_req.teacher_login, null);
                                    jo = new JSONObject(strJson);
                                    
                                    LocalUtils.printLog("get_teacher_vms", "get_teacher_vms "+jo.toString());
                                    
                                    if(jo.has("code"))
                                    {
                                       int errcode = jo.getInt("code");
                                       if(errcode != 8000)
                                       {
                                            while( true)
                                            {
                                                 Thread.sleep(conf.reqVmsDuration);
                                                 strJson = mlocalLogic.processGet(LocalUtils.page_req.get_teacher_vms, null);
                                                 LocalUtils.printLog("", "get_teacher_vms " + strJson);
                                                //Logger.getLogger(SimServer.class.getName()).log(Level.SEVERE, "get_teacher_vms", strJson);
                                                // if(lc._i_desktop_mode != 0)
                                                        // break;
                                             }
                                       }
                                       
                                  
                                    }else
                                    {
                                        LocalUtils.printLog("get_teacher_vms", jo.toString());
                                    }
                            }
                                break;
                            case 1://personal mode
                            {
                                 JSONObject js = new JSONObject();
                                 
                                 js.put("username", conf.personalName);
                                 js.put("password", conf.personalKey);
                                 
                                 Map<String,String> params = new HashMap<String,String>();
                                 params.put("data-json", js.toString());
                                 mlocalLogic.processPost(LocalUtils.page_req.personal_login, params);
                             
                            }break;
                        }
                    }
                    
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SimServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    mTicks++;
                } catch (Exception ex) {
                    Logger.getLogger(SimServer.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
        }
    });
    public SimServer(LocalLogic localLogic) {
        mlocalLogic = localLogic;
    }
    
    public boolean Start()
    { 
         mWorkTrd.start();
         return true;
    }
}
