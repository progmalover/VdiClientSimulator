/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vdiclientsimulator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Map;
 
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
 
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.labelers.ProcessIdLabeler;
import org.pmw.tinylog.labelers.TimestampLabeler;
import org.pmw.tinylog.policies.SizePolicy;
import org.pmw.tinylog.policies.StartupPolicy;
import org.pmw.tinylog.writers.RollingFileWriter;
import vdiclientsimulator.oevdi.client.LocalUtils;
import vdiclientsimulator.oevdi.client.VdiApplication;
import vdiclientsimulator.oevdi.client.VdiConfigure;

 
/**
 *
 * @author danny
 */
public class VdiClientSimulator
{

    /**
     * @param args the command line arguments
     */
    static final int WIDTH=800;
    static final int HEIGHT=600;
    static int sim_num = 200;
    
    private void RegisterExitHandler() {
         Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){

             @Override
             public void run() {
                 LocalUtils.printLog("VdiClientSimulator", "shutdown!");
                 
                 //make report for exception here
                 //content: 1.report exception content and number 2...
                 MakeReport();
             }
         }));
    }
    
    void MakeReport()
    {
        
    }
    
    boolean MakeConfig()
    {
        LocalUtils.printLog("VdiClientSimulator", "make config file...");
        VdiConfigure.getInst().makeconfigure();
        return true;
    }
    
    boolean StartVdiInsts( int num, int offset, String serverIp)
    {
        final int start_ip = 30;
        final int ip_sec = 200;
        final String baseIps[]= VdiConfigure.getInst().data.baseIps;
      
        //import java.util.logging.Level;
        //import java.util.logging.Logger;
        if( num <= 0)
        {
           java.util.logging.Logger.getLogger(VdiClientSimulator.class.getName()).log(java.util.logging.Level.INFO, "Error params, missing vdi number and server ip!");
           return false;
        }
        
        java.util.logging.Logger.getLogger(VdiClientSimulator.class.getName()).log(java.util.logging.Level.INFO,  "create " + num +" vdi...");
        
        int startIndex = offset / ip_sec;
        int startrm = offset  % ip_sec;
 
        int endIndex = (offset + num)/ ip_sec;
        int endrm = (offset + num) % ip_sec;
      
        int curIndex = startIndex;
        for(int i = startIndex;i <= endIndex;i++)
        {
            int count = ip_sec;
            if(i == startIndex)
                count = (ip_sec - startrm);
            if(i == endIndex)
                count = endrm;
            
            for(int j = 0;j < count;j++)
            {
                String strIp;
                String strMac;
      
                if( i == startIndex && startrm > 0)
                {
                   strIp = baseIps[i] + String.valueOf( startrm + j + start_ip);
                   strMac = MacStore.macs[offset + j];
                }else
                {
                   strIp = baseIps[i] + String.valueOf(  j + start_ip);
                   strMac = MacStore.macs[i* ip_sec + j];
                }
                
                VdiApplication vdi = new VdiApplication();
                vdi.Start(serverIp, 8090,strIp,strMac);
            }
        }
        return true;
    }
    
    boolean StartVDISimulate(String[] args)
    {
        if(args.length == 1)
        {
             String strCmd = args[0];
             if(strCmd.equalsIgnoreCase("mkconfig"))
             {
                 return MakeConfig();
             }
            // LocalUtils.printLog("VdiClientSimulator", "Error params: [vdinumber] [mac offset] [serv ip]");
             java.util.logging.Logger.getLogger(VdiClientSimulator.class.getName()).log(java.util.logging.Level.INFO, "VdiClientSimulator Error params: [vdinumber] [mac offset] [serv ip]");
             return false;
        }
  
        if(args.length < 2)
        {
             java.util.logging.Logger.getLogger(VdiClientSimulator.class.getName()).log(java.util.logging.Level.INFO, "Error params, missing vdi number and server ip!");
            // LocalUtils.printLog("VdiClientSimulator", "Error params, missing vdi number and server ip!");
             return false;
        }
        
        VdiConfigure.getInst().loadFromFile();
        
        int num = Integer.valueOf(args[0]);
        int offset = Integer.valueOf(args[1]);
        
        String serverIp = VdiConfigure.getInst().data.servIp;
        if(args.length == 3)
            serverIp = args[2];
        
        return StartVdiInsts(num,offset,serverIp);
    }
    
    public void Run(String[] args)
    {
        
       if(false == StartVDISimulate(args))
       {
           LocalUtils.printLog("VdiClientSimulator", "Simulator exit!");
           System.exit(-1);
           return;
       }   
        RegisterExitHandler();
    }
  
    public static void main(String[] args)
    {
        
         Configurator.currentConfig().writer(
                new RollingFileWriter("logs/log.txt", 10, 
                        new ProcessIdLabeler(),
                        new StartupPolicy(), 
                        new SizePolicy(10 * 1024))).activate();
           
    	VdiClientSimulator sim = new VdiClientSimulator();
      //  String args2[]={"2","0","10.1.41.16"};
       // String args2[]={"mkconfig"};
        sim.Run(args);
                
       // TimestampLabeler labler = new   TimestampLabeler();
      
        
        
    }
    
}
 