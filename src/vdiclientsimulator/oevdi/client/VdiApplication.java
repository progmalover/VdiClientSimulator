/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vdiclientsimulator.oevdi.client;

/**
 *
 * @author danny
 */
public class VdiApplication {
    private LocalLogic mLocalLogic;
    private ConsoleServer  mConsoleServer;
    private SimServer  mSimServer;
    
    public VdiApplication(){
        mLocalLogic = new LocalLogic();
        mSimServer = new SimServer(mLocalLogic);
        mConsoleServer = new ConsoleServer(mLocalLogic);
    }
  
    public void Start(String strSvrIp,int port,String clientIp,String mac)
    {
        mLocalLogic.SetSimIp(strSvrIp, port,clientIp ,mac);
        mConsoleServer.Start();
        mSimServer.Start();
       
    }
    
    
}
