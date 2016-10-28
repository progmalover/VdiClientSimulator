/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vdiclientsimulator.oevdi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danny
 */
class ConsoleServer  {
    private LocalLogic mlocalLogic;
    private Thread mWorkTrd = new Thread(new Runnable(){

        @Override
        public void run() {
            
            int pulltime = VdiConfigure.getInst().data.pullDuration;
            while(true)
            {
                if(null == mlocalLogic)
                    return;

                mlocalLogic.updateLocal();
                try {
                    Thread.sleep(pulltime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConsoleServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    });
    
    public ConsoleServer(LocalLogic localLogic) {
        mlocalLogic = localLogic;
    }
   
    public boolean  Start()
    { 
         if(null == mWorkTrd)
             return false;
         
         mWorkTrd.start();
         return true;
    }
}
