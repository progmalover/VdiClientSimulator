/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vdiclientsimulator.oevdi.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.text.DateFormat.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;   
import javax.xml.parsers.DocumentBuilderFactory;   
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;   
import javax.xml.transform.Transformer;   
import javax.xml.transform.TransformerFactory;   
import javax.xml.transform.dom.DOMSource;   
import javax.xml.transform.stream.StreamResult;   
import org.w3c.dom.Document;   
import org.w3c.dom.Element;   
import org.w3c.dom.Node;   
import org.w3c.dom.NodeList;   
import org.xml.sax.InputSource;   
/**
 *
 * @author danny
 */
public class VdiConfigure {
     public class configure{
         public int pullTimeout = 20000;//
         public int pullReplyTimeout = 10000;
         public int pullDuration = 3000;//
         public int pullEnable = 1;
         
         public int keepaliveEnable = 1;
         public int keepaliveDuration = 20000;//
         public int keepaliveTimeout = 5000;//
         
         public int teacherMode = 1;
         public int userloginTimeout = 10000;
         public int reqVmsInfoTimeout = 10000;
         public int reqVmsDuration = 5000;
        
        // public int registClientPoolNumber = 1;
         public int registClientTimeout = 30000;
         public int setorderTimeout = 5000;
         
         public int reqOemRSEnable = 0;
         public int reqAppPackEnable = 0;
         
         public  String baseIps[]= {"10.1.44.","10.1.45.","10.1.46.","10.1.47.","10.1.48.",
                                    "10.1.49.","10.1.50.","10.1.51.","10.1.52.","10.1.53."};
         //public int  ipSectionNumber = 200;
         //public int  macOffset = 0;
         
         public String  personalName = "admin";
         public String  personalKey="admin";
         public String  servIp="";
     }
     
      VdiConfigure()
     {
          data= new configure();
     }
       
     public configure  data;
     public boolean makeconfigure()
     {
         saveconfig();
         return true;
     }
    
     void CreateXmlElement(Element root,Document doc,String name,String val)
     {
          Element Ele = doc.createElement(name);  
          Ele.appendChild(doc.createTextNode(val));
          root.appendChild(Ele);
     }
     
     protected void saveconfig()
     {
         try {
             String strLocalPath = LocalUtils.getLocalDir();
             File cfgFile = new File(strLocalPath + "\\" + "config.xml");
             
             if(cfgFile.exists())
             {
                 cfgFile.delete();
             }
             
             DocumentBuilderFactory factory = DocumentBuilderFactory
                     .newInstance();
             DocumentBuilder builder = factory.newDocumentBuilder();
             Document doc = builder.newDocument();
             Element properties = doc.createElement("properties");  
             doc.appendChild(properties);
             
            CreateXmlElement(properties,doc,"pullTimeout",String.valueOf(data.pullTimeout));
            CreateXmlElement(properties,doc,"pullReplyTimeout",String.valueOf(data.pullReplyTimeout));
            CreateXmlElement(properties,doc,"pullDuration",String.valueOf(data.pullDuration));
            CreateXmlElement(properties,doc,"pullEnable",String.valueOf(data.pullEnable));
          
            CreateXmlElement(properties,doc,"keepaliveEnable",String.valueOf(data.keepaliveEnable));
            CreateXmlElement(properties,doc,"keepaliveDuration",String.valueOf(data.keepaliveDuration));
            CreateXmlElement(properties,doc,"keepaliveTimeout",String.valueOf(data.keepaliveTimeout));
           
            CreateXmlElement(properties,doc,"teacherMode",String.valueOf(data.teacherMode));
            CreateXmlElement(properties,doc,"userloginTimeout",String.valueOf(data.userloginTimeout));
            CreateXmlElement(properties,doc,"reqVmsInfoTimeout",String.valueOf(data.reqVmsInfoTimeout)); 
            CreateXmlElement(properties,doc,"reqVmsDuration",String.valueOf(data.reqVmsDuration)); 
              
            CreateXmlElement(properties,doc,"registClientTimeout",String.valueOf(data.registClientTimeout));
            CreateXmlElement(properties,doc,"setorderTimeout",String.valueOf(data.setorderTimeout));
            
            CreateXmlElement(properties,doc,"reqOemRSEnable",String.valueOf(data.reqOemRSEnable));
            CreateXmlElement(properties,doc,"reqAppPackEnable",String.valueOf(data.reqAppPackEnable));
             
            String ips="";
            for(int i = 0;i < data.baseIps.length;i++)
            {
                 if(0 == i)
                     ips = data.baseIps[i];
                 else
                     ips += data.baseIps[i];
                 
                 if( i + 1 < data.baseIps.length)
                     ips += ";";
                 
            }
            
            CreateXmlElement(properties,doc,"baseIps",ips);
            CreateXmlElement(properties,doc,"personalName",data.personalName);
            CreateXmlElement(properties,doc,"personalKey",data.personalKey);
            CreateXmlElement(properties,doc,"servIp",data.servIp);

            Transformer transformer=TransformerFactory.newInstance().newTransformer();//得到转换器
            //设置换行
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //写入文件
            transformer.transform(new DOMSource(doc), new StreamResult(cfgFile));
         } catch (Exception ex) {
             Logger.getLogger(VdiConfigure.class.getName()).log(Level.SEVERE, null, ex);
         }
         
     }
     
      public boolean parseToConf(String strXML) {   
          
        try {   
             DocumentBuilderFactory factory = DocumentBuilderFactory   
                     .newInstance();   
             DocumentBuilder builder = factory.newDocumentBuilder();   
             Document doc = builder   
                     .parse(new InputSource(new StringReader(strXML)));   
  
             Element root = doc.getDocumentElement();   
             NodeList books = root.getChildNodes();   
             
             if (books != null) {   
                for (int i = 0; i < books.getLength(); i++) {   
                     Node item = books.item(i);   
                 
                    // String strval3 = item.getNodeValue();
                     switch(item.getNodeName())
                     {
                        case "pullTimeout":
                             data.pullTimeout = Integer.valueOf(item.getTextContent());
                             break;
                        case "pullReplyTimeout":
                             data.pullReplyTimeout = Integer.valueOf(item.getTextContent());
                             break;
                        case "pullDuration":
                                data.pullDuration = Integer.valueOf(item.getTextContent());
                             break;
                        case "pullEnable":
                              data.pullEnable = Integer.valueOf(item.getTextContent());
                             break;
                        case "keepaliveEnable":
                             data.keepaliveEnable = Integer.valueOf(item.getTextContent());
                             break;
                             
                        case "keepaliveDuration":
                             data.keepaliveDuration = Integer.valueOf(item.getTextContent());
                             break;
                        case "keepaliveTimeout":
                             data.keepaliveTimeout = Integer.valueOf(item.getTextContent());
                             break;
                        case "teacherMode":
                             data.teacherMode = Integer.valueOf(item.getTextContent());
                             break;
                        case "userloginTimeout":
                             data.userloginTimeout = Integer.valueOf(item.getTextContent());
                            break;
                        case "reqVmsInfoTimeout":
                             data.reqVmsInfoTimeout = Integer.valueOf(item.getTextContent());
                            break;
                        case "reqVmsDuration":
                             data.reqVmsDuration = Integer.valueOf(item.getTextContent());
                            break;
                              
                        case "registClientTimeout":
                             data.registClientTimeout = Integer.valueOf(item.getTextContent());
                            break;
                        case "setorderTimeout":
                             data.setorderTimeout = Integer.valueOf(item.getTextContent());
                            break;
                        
                        case "reqOemRSEnable":
                             data.reqOemRSEnable = Integer.valueOf(item.getTextContent());
                            break;
                        case "reqAppPackEnable":
                             data.reqAppPackEnable = Integer.valueOf(item.getTextContent());
                            break;
                
                        case "baseIps":
                        {
                             //data.baseIps = Integer.valueOf(book.getNodeValue());
                             String str = item.getTextContent();
                             data.baseIps = str.split(";");
                        }
                             break;
                      
                        case "personalName":
                             data.personalName =item.getTextContent();
                             break;
                        case "personalKey":
                             data.personalKey = item.getTextContent();
                             break;
                        case "servIp":
                             data.servIp = item.getTextContent();
                             break;
                     }
                     
                 }   
             }   
             return true;
         } catch (Exception e) {   
             e.printStackTrace();   
         }  
         return false;
     }   
      
     public boolean loadFromFile()
     {
         String strLocalPath = LocalUtils.getLocalDir();
         File cfgFile = new File(strLocalPath + "\\" + "config.xml");
         
         //configure file exists, loading it from disk.
         if(cfgFile.exists())
         {
             try {
                 //read file from disk.
                 BufferedReader inf = new BufferedReader(new FileReader(cfgFile));
                 StringBuffer sb = new StringBuffer();
                 
                 String line = inf.readLine();
                 while(line != null && !line.isEmpty())
                 {
                     sb.append(line);
                     line = inf.readLine();
                 }
                 //parse context
                 return parseToConf(sb.toString());
             } catch (Exception ex) {
                 Logger.getLogger(VdiConfigure.class.getName()).log(Level.SEVERE, null, ex);
             }

         }
         return false;
     }
     
     static VdiConfigure _vdiconfig;
     public static VdiConfigure getInst()
     {
         if(null == _vdiconfig)
             _vdiconfig = new VdiConfigure();
         return _vdiconfig;
     }
     
}
