package vdiclientsimulator.oevdi.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
 
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import jdk.nashorn.internal.runtime.Context;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vdiclientsimulator.VdiClientSimulator;
import vdiclientsimulator.oevdi.client.LocalLogic.LocalContext;
 
public class LocalUtils {
    public static boolean enable_log = false;
    static class page_req {
        public static String set_server_ip = "set_server_ip";
        public static String get_server_ip = "get_server_ip";
        public static String get_ip = "get_ip";
        public static String set_ip = "set_ip";
        public static String loop_data = "loop_data";
        public static String system_info = "system_info";
        public static String desktop_mode = "desktop_mode";
        public static String get_client_type = "get_client_type";
        public static String shutdown = "shutdown";
        public static String quickly_shutdown = "quickly_shutdown";
        public static String reboot = "reboot";

        public static String get_resolution = "get_resolution";
        public static String set_resolution = "set_resolution";

        public static String get_upgrate_file = "get_upgrate_file";
        public static String upgrate_client = "upgrate_client";
        public static String get_teacher_vms = "get_teacher_vms";
        public static String teacher_login = "teacher_login";
        public static String personal_config = "personal_config";
        public static String personal_login = "personal_login";
        public static String get_wifi_list = "get_wifi_list";
        public static String set_order = "set_order";
        public static String vm_connect = "vm_connect";
        public static String get_teacher_ip = "get_teacher_ip";
        public static String change_pwd = "change_pwd";
        public static String personal_modify_pwd = "personal_modify_pwd";
        public static String start_ping = "start_ping";
        public static String ping_echo = "ping_echo";
        public static String end_ping = "end_ping";
        public static String windows_mode = "windows_mode";
        public static String browser_file = "browser_file";
        public static String app_exit = "app_exit";
        public static String feature_config = "feature_config";
    }

    static class console_req {
        public static String ping = "ping";
        public static String reboot = "reboot";
        public static String shutdown = "shutdown";
        public static String changeName = "changeName";
        public static String ChangeClientConfig = "ChangeClientConfig";
        public static String beginOrder = "beginOrder";
        public static String refreshOrder = "refreshOrder";
        public static String endOrder = "endOrder";
        public static String loginMode = "loginMode";
        public static String upgrade_client_force = "upgrade_client_force";
        public static String upgrade_from_server = "upgrade_from_server";
    }

    static class vdi_req {
        public static String vms_power_off = "vms_power_off";
        public static String vms_power_on = "vms_power_on";
        public static String get_vms_info = "get_vms_info";
        public static String get_toolbar_status = "get_toolbar_status";
    }

    static class ETHSettings {

        public static String ethernet_dns1 = "ethernet_static_dns1";
        public static String ethernet_dns2 = "ethernet_static_dns2";
        public static String ethernet_ip = "ethernet_static_ip";
        public static String ethernet_gateway = "ethernet_static_gateway";
        public static String ethernet_netmask = "ethernet_static_netmask";
        public static String ethernet_use_static_ip = "ethernet_use_static_ip";
    }

    static public class configs {
        public static String oemresid = "oemresid";
        public static String oevdiserverip = "oevdiserverip";
        public static String oevdiserverport = "oevdiserverport";
        public static String oevdiuuid = "oevdiuuid";
        public static String oevdifakemac = "oevdifakemac";
        public static String oevdiclientname = "oevdiclientname";
        public static String oevdiclientvmsconnectingtime = "oevdiclientvmsconnectingtime";
        public static String desktop_mode = "desktop_mode";
        public static String desktop_wait = "desktop_wait";
        public static String config_psw = "config_psw";
        public static String vm_shutdown_param = "vm_shutdown_param";
        public static String auto_start = "auto_start";
        public static String personal = "personal";
        public static String final_mode = "final_mode";
        public static String mode_countdown = "mode_countdown";
    }

    static public class controls {

        public static final String PLAYER_QUIT = "com.oe.videoplayer.QUIT";
        public static final String LIBSDL_QUIT = "org.libsdl.app";
        public static final String VDI_QUIT = "com.oe.vdi.QUIT";
        public static final String CLIENT_RESTART = "com.oevdi.client.RESTART";
    }

    //
    //server ip and mac、uuid，need to be stored.
    //
    public final static String external_pub_dir = LocalUtils.getLocalDir() + "/external";
    public final static String local_tmp_dir = LocalUtils.getLocalDir() + "/temp";

    public final static String persisit_dir = "data/data/vdi-persist/";
    public final static String format_tag = "runtime_exec:: ";

    public static boolean SavePersist(String name, String value) {
        return SavePersist_settings(name, value);
    }

    public static String LoadPersist(String name) {
        return LoadPersist_settings(name);
    }

    static boolean SavePersist_settings(String name, String value) {
      
        return true;
    }

    static String LoadPersist_settings(String name) {
     
        return "";
    }


    public static boolean DetectHidden(String packName) {
        
        return true;
    }

    public static boolean EnsureToForeground(String packName) {
        
        return true;
    }

    
    public static boolean SaveInfo(String name, String value) throws IOException {
        if (value == null)
            return false;
 
        return true;
    }

    public static String LoadInfo(String name) {
        
        return "";
    }

    public static boolean isNewInstalltionRun() {
        // TODO Auto-generated method stub
        String strFirstRun = LoadInfo("isFirstRun"); //new installation
        if (strFirstRun == null || strFirstRun.isEmpty()) {
            try {
                SaveInfo("isFirstRun", "no");
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return true;
            }

        }
        //
        return false;
    }

    public static JSONObject defaultJsonObj(int code, String msg) {
        JSONObject oj = new JSONObject();
        if (msg == null) msg = "";
        try {
            oj.put("message", msg);
            oj.put("code", code);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return oj;
    }

    public static void printErrorLog( String txt)
    {
       org.pmw.tinylog.Logger.error(txt);
    }
    
    public static void printLog(String tag, String txt) {
         
         if(LocalUtils.enable_log)
         {
             /*
             Logger lg = Logger.getLogger(tag);
             if(lg != null)
                 lg.log(Level.SEVERE, txt, txt);
             */
         }
    }

    private static final int CHUNK_SIZE = 2048;
    public static String ReadPropertyFileByURL(String file_uri, boolean b) {
        if (file_uri == null || file_uri.isEmpty())
            return null;

        String strFile = "";
        URL url = null;
        HttpURLConnection conn = null;
        InputStream inb = null;

        byte[] buff;
        try {
            url = new URL(file_uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(4000);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(CHUNK_SIZE);

            inb = new BufferedInputStream(conn.getInputStream());
            buff = new byte[CHUNK_SIZE];

            int nSize = 0;
            int nSizeCount = 0;
            while ((nSize = inb.read(buff)) > 0) {
                strFile += AsciiArrayToString(buff, 0, nSize);
                nSizeCount += nSize;
            }
            inb.close();
            conn.disconnect();

            buff = null;
            inb = null;
            url = null;
            conn = null;

            if (nSizeCount <= 0) {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();

            url = null;
            conn = null;
            inb = null;

            return null;
        }

        return strFile;
    }

    static public String AsciiArrayToString(byte asciibytes[], int begin, int end) {
        if (asciibytes == null)
            return null;

        if (end <= begin)
            return null;

        String str = "";

        int nend = (end > asciibytes.length) ? asciibytes.length : end;
        for (int i = begin; i < nend; i++) {
            str += String.valueOf(Character.toChars(asciibytes[i]));
        }
        return str;

    }

    static public class CookieHelper {
        private static String _cur_cookie;
       // private static CookieStore _cookie_store;
        private static boolean _b_enable_cookie;

        public static void setCookieEnable(boolean bEnable) {
            _b_enable_cookie = bEnable;
        }

        public static boolean isEnableCookie() {
            return _b_enable_cookie;
        }

        public static void clearLocalStoreCookie() {
            //_cookie_store = null;
            _cur_cookie = null;
        }

        public static void keepCurCookie(String cookieStr) {
            if (cookieStr == null)
                return;

            //String vars[] = cookie.split(";");
            try {
                _cur_cookie = cookieStr.split(";")[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String getCurCookie() {
            return _cur_cookie;
        }
    }

    static public JSONObject requestPostUrl(String url, JSONObject jsData, int timeout) {
        try {
            final String strUrl = url;

            if (strUrl == null)
                return null;

            DefaultHttpClient httpClient = null;
            if (timeout > -1) //for check alive,about 5 mins
            {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 60000);
                HttpConnectionParams.setSoTimeout(httpParams, timeout);
                httpClient = new DefaultHttpClient(httpParams);
            } else
                httpClient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(strUrl);

            httppost.addHeader("Accept", "application/json");
            httppost.addHeader("Content-type", "application/json");

            if (jsData != null) {
                StringEntity myEntity = new StringEntity(jsData.toString());
                httppost.setEntity(myEntity);
            }

            HttpResponse response = httpClient.execute(httppost);

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                LocalUtils.printLog("LocalUtils", "requesturl:" + status.toString());
                return null;
            }

            String strRes = EntityUtils.toString(response.getEntity(), "UTF-8");

            return new JSONObject(strRes);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static public JSONObject requestGetUrl(String url, JSONObject header, int timeout) {
        try {
            final String strUrl = url;
            DefaultHttpClient httpClient = null;
            if (timeout > -1) //for check alive,about 5 mins
            {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
                HttpConnectionParams.setSoTimeout(httpParams, timeout);
                httpClient = new DefaultHttpClient(httpParams);
            } else
                httpClient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(strUrl);
            //httpClient.
            if (header != null) {
                httpget.setHeader("User-Agent", header.getString("User-Agent"));
            }

            httpget.addHeader("Accept", "application/json");
            httpget.addHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(httpget);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                LocalUtils.printLog("LocalUtils", "requesturl:" + status.toString());
                return null;
            }

            String strRes = EntityUtils.toString(response.getEntity(), "UTF-8");
            return new JSONObject(strRes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static public JSONObject requestUrl(String baseUrl, String url,
                                        JSONObject jo /*default null*/, int timeout) {
        //timeout = 60000;
        if (baseUrl == null)
            return null;

        try {
            final String strUrl = baseUrl + url;
            DefaultHttpClient httpClient = null;
            if (timeout > -1) //for check alive,about 5 mins
            {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, timeout);
                HttpConnectionParams.setSoReuseaddr(httpParams, true);
                HttpConnectionParams.setSoKeepalive(httpParams, false);
                HttpConnectionParams.setTcpNoDelay(httpParams, true);
                HttpConnectionParams.setLinger(httpParams, 0);
                HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
                httpClient = new DefaultHttpClient(httpParams);
            } else
                httpClient = new DefaultHttpClient();


            HttpPost httppost = new HttpPost(strUrl);
            //httpClient.
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            if (CookieHelper.isEnableCookie()) {
                /*
                CookieStore store = CookieHelper.getLocalStoreCookie();
				if(store != null)
				{
					httpClient.setCookieStore(store);
				}*/
                String strCookie = CookieHelper.getCurCookie();
                httppost.setHeader("Cookie", strCookie);
            }

            if (jo != null) {
                StringEntity myEntity = new StringEntity(jo.toString());
                httppost.setEntity(myEntity);
            }

            HttpResponse response = httpClient.execute(httppost);
            Header cookieHdr = response.getFirstHeader("Set-Cookie");
            if (cookieHdr != null) {
                //if(cookieHdr.getName().equalsIgnoreCase("session")) //session cookie
                CookieHelper.keepCurCookie(cookieHdr.getValue());
                //CookieHelper.storeCookie2Local(cookieHdr.getValue());
            }

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                LocalUtils.printLog("LocalUtils", "requesturl:" + status.toString());
                LocalUtils.printErrorLog("requesturl:" + status.toString());
                return null;
            }

            String strRes = EntityUtils.toString(response.getEntity(), "UTF-8");

            //service portal has much connections reserved,so try to close on client side.
            ClientConnectionManager cm = httpClient.getConnectionManager();
            cm.shutdown();

            return new JSONObject(strRes);


        } catch (Exception e) {
           // e.printStackTrace();
            java.util.logging.Logger.getLogger(VdiClientSimulator.class.getName()).
                     log(java.util.logging.Level.INFO, e.toString());
           // LocalUtils.writeExternalLog("nethttp", "request url:" + url, e.toString());
            LocalUtils.printErrorLog(e.toString());
        }

        return null;
    }

    public static void shellCreateFile(String strFile, String strValue, boolean broot) {
        String strUser = "sh";
        if (broot)
            strUser = "su";
        try {
            java.lang.Process proc = Runtime.getRuntime().exec(new String[]{strUser, "-c", "echo " + strValue + " > " + strFile});
            proc.waitFor();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  //关机
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String shellReadFile(String strFile, boolean broot) {

        String strUser = "sh";
        if (broot)
            strUser = "su";

        try {
            java.lang.Process proc = Runtime.getRuntime().exec(new String[]{strUser, "-c", "cat " + strFile});
            //proc.waitFor();
            //java.lang.Process proc = Runtime.getRuntime().exec("cat " + strFile);
            InputStream ins = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(isr);

            String strValue = null;
            String line = null;
            while ((line = br.readLine()) != null) {
                if (strValue == null)
                    strValue = line;
                else
                    strValue += ";" + line;
                //System.out.println(line);
            }
            return strValue;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  //关机
        return null;
    }

    private static void shellRemovefile(String strFile) {
        
    }

    public static void os_shutdown(String reason) {
        // TODO Auto-generated method stub
    }


    public static void os_reboot(String reason) {

    }

    public static boolean ensureEnvirment() {
      
        return true;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /*\
     res.put("version", 	"4.2.0-0-gbdb33c1\n");
				res.put("disk_size", 12825);
				res.put("memory_size",1911);
				res.put("cpu_info"," Intel(R) Celeron(R) CPU  J1800  @ 2.41GHz");
				res.put("mac" ,"00:E0:B4:12:80:67");
			//	jo.put("result", res);
	 */
    
    public static class SystemInfo {
        private JSONObject mInfo;

        public SystemInfo() {
            mInfo = new JSONObject();
            try {
                mInfo.put("version","5555" + "-" + "999");
                mInfo.put("version_code","5555");
                mInfo.put("disk_size", "30GB");
                mInfo.put("memory_size", "2GB");

                mInfo.put("cpu_info", LocalUtils.getCpuInfo()[0]);
                mInfo.put("mac", LocalUtils.LoadPersist("oevdifakemac"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public JSONObject getInfoData() {
            return mInfo;
        }
    }

    static public SystemInfo getSystemInfo() {
        // TODO Auto-generated method stub\
        SystemInfo info = new SystemInfo();
      
        return info;
    }


    public static class NetInfo {
        private JSONObject mInfo;

        public NetInfo() {
            mInfo = new JSONObject();
        }

        public void init() {

        }

        public JSONObject getInfoData() {
            return mInfo;
        }
    }


    public static NetInfo getNetInfo() {
        // TODO Auto-generated method stub
        NetInfo info = new NetInfo();
        info.init();
        return info;
    }

    public static LocalLogic getLocalLogic() {
        return null;
    }

    public static Context getInst() {
        // TODO Auto-generated method stub
        return null;
    }

    private static void shell_timeout_guard(String name, int time) {
        class GuardRunnanble implements Runnable {
            String strName;
            int nTimeout;

            public GuardRunnanble SetProperties(String name, int time) {
                strName = name;
                nTimeout = time;
                return this;
            }

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(nTimeout);
                    //String []vars = strName.split(";");
                    //for(int i = 0;i < vars.length;i++)
                   // LocalUtils.pkillApp(strName);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean changeCurrentNet(JSONObject jo) {
        // TODO Auto-generated method stub
        //os_reboot();
        //ifconfig <interface> up <ip address> mask <netmask> gw <gateway>
        int staticIpMode = 0;
        int preIpMode = 0;

        String strPreMode = LocalUtils.LoadPersist_settings(ETHSettings.ethernet_use_static_ip);
        if (strPreMode == null || strPreMode.isEmpty()) {
            strPreMode = "0";
        }

        preIpMode = Integer.valueOf(strPreMode);
        try {
            staticIpMode = jo.getInt(ETHSettings.ethernet_use_static_ip);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }

        if (staticIpMode != preIpMode && staticIpMode == 0) //static -> dhcp
        {
            java.lang.Process proc;
            try {

                shell_timeout_guard("netcfg", 6000);
                proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "netcfg eth0 dhcp"});
                Thread.sleep(6000);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }

        }

        if (staticIpMode != preIpMode && staticIpMode == 1) //dhcp -> static
        {
            java.lang.Process proc;
            try {

                proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "netcfg eth0 down"});
                proc.waitFor();
                proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "netcfg eth0 up"});
                proc.waitFor();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //goto _Exit;
                return false;
            }

        }

        String cmdStr = "";//ip netmask
        try {
            if (jo.has(ETHSettings.ethernet_ip))
                cmdStr = "ifconfig eth0 " + jo.getString(ETHSettings.ethernet_ip);
            if (jo.has(ETHSettings.ethernet_netmask)) {
                if (cmdStr.isEmpty())
                    cmdStr = "ifconfig eth0 ";
                cmdStr += " netmask " + jo.getString(ETHSettings.ethernet_netmask);
            }

            if (!cmdStr.isEmpty()) {
                java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdStr});
                proc.waitFor();
                cmdStr = "";
            }

            //cmdStr += " gw " + jo.getString(ETHSettings.ethernet_static_gateway);
            if (jo.has(ETHSettings.ethernet_gateway))
                cmdStr = "route add default gw " + jo.getString(ETHSettings.ethernet_gateway) + " dev eth0";

            if (!cmdStr.isEmpty()) {
                java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdStr});
                proc.waitFor();
                cmdStr = "";
            }

            if (jo.has(ETHSettings.ethernet_dns1))
                cmdStr = "setprop net.dns1 " + jo.getString(ETHSettings.ethernet_dns1);
            if (!cmdStr.isEmpty()) {
                java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdStr});
                proc.waitFor();
                cmdStr = "";
            }

            if (jo.has(ETHSettings.ethernet_dns2))
                cmdStr = "setprop net.dns2 " + jo.getString(ETHSettings.ethernet_dns2);
            if (!cmdStr.isEmpty()) {
                java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", cmdStr});
                proc.waitFor();
                cmdStr = null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }  //关机

        return true;
    }

    /*
    0 - LCD_720X480_60HZ
    1 - LCD_1024X768_60HZ
    2 - LCD_1280X720_60HZ
    3 - LCD_1280X1024_60HZ
    4 - LCD_1366X768_60HZ
    5 - LCD_1440X900_60HZ
    6 - LCD_1600X900_60HZ
    7 - LCD_1920X1080_60HZ
     */
    static List<String> dspList = new ArrayList<String>();
    static String resolutionDescription;

    private static void parseSystemResolutions(String txt, List<String> dspList) {
        //
        String[] lines = txt.split(";");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains("LCD_") && line.contains("_60HZ")) {
                String secs[] = line.split("_");
                dspList.add(secs[1]);
            }
        }
    }

    public static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }


    public static List<String> getUdiskPaths() {
        ///mnt/usb_storage
        List<String> u_paths = new ArrayList<String>();
        String basePath = "/mnt/usb_storage/USB_DISK";
        for (int i = 0; i < 4; i++) {
            String tpPath = basePath + String.valueOf(i);
            File f = new File(tpPath);
            if (f.exists()) {
                f = null;
                tpPath += "/udisk";
                for (int j = 0; j < 10; j++) {
                    f = new File(tpPath + String.valueOf(j));
                    if (f.exists())
                        u_paths.add(f.getPath());
                    f = null;
                    tpPath = null;
                }
            }
        }
        if (u_paths.size() <= 0) {
            u_paths.add("/mnt/usb_storage");
            u_paths.add("/mnt/usb_storage/USB_DISK");
        }

        return u_paths;
        //"/mnt/usb_storage/USB_DISK0/udisk0";
    }



    public static String fakeMacByUUID(String strUUID) {
        if (strUUID == null)
            return null;
        String subStr = strUUID.substring(strUUID.length() - 12, strUUID.length());
        String strMAC = "";
        for (int i = 0; i < subStr.length(); i += 2) {
            strMAC += subStr.substring(i, i + 2);
            if (i + 2 < subStr.length())
                strMAC += ":";
        }
        return strMAC;
    }

    public static String Get_UUID() {
        // TODO Auto-generated method stub
        final String alphaStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final byte[] alpha = alphaStr.getBytes();
     
        Random ran = new Random(System.currentTimeMillis());
        String strTime="";
        for (int i = 0; i < 100; i++) {
            strTime += "\n" + ran.nextInt(100) + alpha[ran.nextInt(1000) % 50];
        }
        String uuid = UUID.nameUUIDFromBytes(
             (UUID.randomUUID().toString() + strTime).getBytes()).toString();
        return uuid;
       // return "aaaaaabbbbbbcccc";
    }

    public static String CreateInstallBashFile(String apkPath, String fName, String packname, boolean bReboot) {
        // TODO Auto-generated method stub
        String bashPath = local_tmp_dir + "/" + fName;
        File f = new File(bashPath);
        if (f.exists()) {
            f = null;
            return bashPath;
        }

        try {
            if (f.createNewFile()) {
                OutputStreamWriter fp = new OutputStreamWriter(new FileOutputStream(f));
                fp.write("chmod  777 " + apkPath + "\n");

                fp.write("pm install -r " + apkPath + "\n");
                fp.write("wait \n");
                // fp.write("reboot\n");
                //  fp.write("wait \n");
                // fp.write("reboot\n");
                // fp.write("reboot\n");
                //  fp.write("reboot\n");
                fp.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bashPath;
    }

    public static int pm_installapk2(String apkPath, boolean bsu) {
        // TODO Auto-generated method stub
        String strUsr = (bsu) ? "su" : "sh";
        try {

            java.lang.Process proc = Runtime.getRuntime().
                    exec(new String[]{strUsr, "-c", "chmod 777 " + apkPath});
            proc.waitFor();

            LocalUtils.printLog("pm_installapk2", "app path:" + apkPath);
            proc = Runtime.getRuntime().
                    exec(new String[]{strUsr, "-c", "pm install -r " + apkPath});
            proc.waitFor();

            int exitcode = proc.exitValue();
            LocalUtils.printLog("pm_installapk2", "exit code:" + exitcode);
            return exitcode;
        } catch (Exception e) {
            LocalUtils.printLog("pm_installapk2", "exception:" + e.toString());
        }
        return -1;
    }

    public static int pm_installapk(String apkPath, String packagename, boolean reboot) {
        // TODO Auto-generated method stub
        try {
            //

            String strSHFile = CreateInstallBashFile(apkPath, "install", packagename, reboot);
            java.lang.Process proc = Runtime.getRuntime().
                    exec(new String[]{"su", "-c", "chmod 777 " + strSHFile});
            proc.waitFor();

            proc = Runtime.getRuntime().
                    exec(new String[]{"su", "-c", strSHFile});
            proc.waitFor();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  //关机
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }


    public static class Zip {
        private ZipInputStream zipIn;      //解压Zip
        private ZipOutputStream zipOut;     //压缩Zip
        private ZipEntry zipEntry;
        private int bufSize;    //size of bytes
        private byte[] buf;
        private int readedBytes;

        public Zip(int bufSize) {
            this.bufSize = bufSize;
            this.buf = new byte[this.bufSize];
        }

        //压缩文件夹内的文件
        public void doZip(String zipDirectory) {//zipDirectoryPath:需要压缩的文件夹名
            File file;
            File zipDir;

            zipDir = new File(zipDirectory);
            String zipFileName = zipDirectory + ".zip";//压缩后生成的zip文件名

            try {
                this.zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
                handleDir(zipDir, this.zipOut);
                this.zipOut.close();
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }

        //由doZip调用,递归完成目录文件读取
        private void handleDir(File dir, ZipOutputStream zipOut) throws Exception {
            FileInputStream fileIn;
            File[] files;

            files = dir.listFiles();
            if (files.length == 0) {//如果目录为空,则单独创建之.
                //ZipEntry的isDirectory()方法中,目录以"/"结尾.
                this.zipOut.putNextEntry(new ZipEntry(dir.toString() + "/"));
                this.zipOut.closeEntry();
            } else {//如果目录不为空,则分别处理目录和文件.
                for (File fileName : files) {

                    if (fileName.isDirectory()) {
                        handleDir(fileName, this.zipOut);
                    } else {
                        fileIn = new FileInputStream(fileName);
                        String name = dir.getName();
                        //生成的压缩包存放在原目录下
                        this.zipOut.putNextEntry(new ZipEntry(name + "/" + fileName.getName().toString()));

                        //此方法存放在该项目目录下
                        //this.zipOut.putNextEntry(new ZipEntry(fileName.toString()));
                        while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
                            this.zipOut.write(this.buf, 0, this.readedBytes);
                        }
                        this.zipOut.closeEntry();
                    }
                }
            }
        }

        //解压指定zip文件
        public void unZip(String unZipfileName) {//unZipfileName需要解压的zip文件名
            FileOutputStream fileOut;
            File file;
            String baseDir = unZipfileName.substring(0, unZipfileName.length() - 4);
            File ff = new File(baseDir);
            try {
                this.zipIn = new ZipInputStream(new
                        BufferedInputStream(new FileInputStream(unZipfileName)));
                while ((this.zipEntry = this.zipIn.getNextEntry()) != null) {
                    file = new File(baseDir + "/" + this.zipEntry.getName());
                    if (this.zipEntry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        //如果指定文件的目录不存在,则创建之.
                        File parent = file.getParentFile();
                        if (!parent.exists()) {
                            parent.mkdirs();
                        }
                        if (!ff.exists()) {
                            ff.mkdir();
                        }
                        fileOut = new FileOutputStream(file.getPath());

                        //fileOut = new FileOutputStream(file); 此方法存放到该项目目录下
                        while ((this.readedBytes = this.zipIn.read(this.buf)) > 0) {
                            fileOut.write(this.buf, 0, this.readedBytes);
                        }
                        fileOut.close();
                    }

                    this.zipIn.closeEntry();
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }

    }

    public static class Encrypt {
        public static String getMD5(String val) throws NoSuchAlgorithmException {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            byte[] m = md5.digest();//encrypt
            return getString(m);
        }

        public static String getSha256(String val) throws NoSuchAlgorithmException {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(val.getBytes());
            byte[] m = sha.digest();//encrypt
            return getString(m);
        }

        public static final String toHexString(byte b) {
            String s = Integer.toHexString(b & 0xFF);
            if (s.length() == 1) {
                s = "0" + s; //  pad 0
            }
            return s;
        }

        private static String getString(byte[] b) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                sb.append(toHexString(b[i]));
            }

            return sb.toString();
        }
    }

    public static String loadCookie() {
        // TODO Auto-generated method stub

        return null;
    }

    public static String findPage(File fdir, String strname) {
        // TODO Auto-generated method stub
        String files[] = fdir.list();
        List<String> dirs = new ArrayList<String>();

        for (int i = 0; i < files.length; i++) {
            String strFile = fdir.getAbsolutePath() + "/" + files[i];
            File f = new File(strFile);
            if (f.isFile()) {
                if (strFile.contains(strname)) {
                    return strFile;
                }

            } else {
                //return findPage(f, strname);
                dirs.add(strFile);
            }

            f = null;
        }

        for (int j = 0; j < dirs.size(); j++) {
            File f = new File(dirs.get(j));
            String strFile = findPage(f, strname);
            if (strFile != null)
                return strFile;
        }

        return null;
    }

    public static boolean su_copy(String strTarget, String strDesk) {
        java.lang.Process proc;
        try {
            proc = Runtime.getRuntime().
                    exec(new String[]{"su", "-c", "mv " + strTarget + "  " + strDesk});
            proc.waitFor();

            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public static boolean java_unzip(String strTarget) {
        File f = new File(strTarget);
        if (!f.exists())
            return false;

        f = null;
        Zip zip = new LocalUtils.Zip(1024);
        zip.unZip(strTarget);
        return true;
    }

    public static String CreateUnzipBashFile(String targetDir, String zipFile) {
        // TODO Auto-generated method stub
        if (targetDir.charAt(targetDir.length() - 1) != '/') {
            targetDir += "/";
        }

        String strBashFile = targetDir + "unzipbatch";
        File f = new File(strBashFile);
        if (f.exists())
            return strBashFile;

        try {
            if (f.createNewFile()) {
                OutputStreamWriter fp = new OutputStreamWriter(new FileOutputStream(f));

                fp.write("busybox unzip " + targetDir + zipFile + "\n");
                fp.write("wait \n");
                fp.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strBashFile;
    }

    public static boolean shell_unzip(String strTargetDir, String strTarget) {
        List<String> cmds = new ArrayList<String>();
        String strSHFile = CreateUnzipBashFile(strTargetDir, strTarget);

        try {

            java.lang.Process proc = Runtime.getRuntime().
                    exec(new String[]{"su", "-c", "chmod 777 " + strSHFile});
            proc.waitFor();

            proc = Runtime.getRuntime().
                    exec(new String[]{"su", "-c", strSHFile});
            proc.waitFor();

            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public static boolean java_copy(String strlocal, String strResDir, String strName) {
        File f = new File(strlocal);
        return f.renameTo(new File(strResDir + "/" + strName));
    }

    public static boolean copyAndUnzip(String strlocal, String strResDir, String filename) {
        // TODO Auto-generated method stub
        boolean bok = false;
        if (java_copy(strlocal, strResDir, filename)) {
            bok = java_unzip(strResDir + "/" + filename);
            //bok = shell_unzip(strResDir, filename);
        }
        return bok;
    }


    public static void createConfigFile(String pathMainURL) {
        File file = new File(pathMainURL);
        String strParentDir = file.getParent();

        if (strParentDir.charAt(strParentDir.length() - 1) != '/')
            strParentDir += "/";
        strParentDir += "config.js";

        file = new File(strParentDir);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            OutputStreamWriter fs = new OutputStreamWriter(new FileOutputStream(file));
            fs.write("var $API = \"http://127.0.0.1:7081\";");
            fs.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    //3 days to clear logs.
    public static boolean canClearExternal() {
        String str = LocalUtils.LoadPersist("oeclearLogData");
        Calendar calendar = Calendar.getInstance();
        int curDayNum = calendar.get(Calendar.DAY_OF_YEAR);

        if (str == null || str.isEmpty()) {
            LocalUtils.SavePersist("oeclearLogData", String.valueOf(curDayNum));
            return false;
        }

        int dayNum = Integer.valueOf(str);
        //3 days or cross the year
        if (((curDayNum - dayNum) >= 2) || (curDayNum < dayNum))
            return true;

        return false;
    }

    public static void writeExternalLog(String caption, String strTag, String string) {
        // TODO Auto-generated method stub
        if (strTag.contains(LocalUtils.page_req.get_teacher_vms))
            return;
        if (strTag.contains(LocalUtils.page_req.personal_config))
            return;
        if (strTag.contains(LocalUtils.page_req.personal_login))
            return;

        if (strTag.contains(LocalUtils.page_req.loop_data))
            return;
/*
        long timemillis = System.currentTimeMillis();
        CharSequence timedate = DateFormat.format("yyyyMMdd", timemillis);
        CharSequence timestamp = DateFormat.format("kk:mm:ss", timemillis);
        String filename = LocalUtils.external_pub_dir + File.separator + caption + "_" + timedate + ".log";

        LocalUtils.printLog("ExternalLog", "FILE ---- " + filename + "\n");
        String log = timestamp + " " + " Pid: " + Process.myPid() + " " + strTag + ":\n" + string + "\n";
        LocalUtils.printLog("ExternalLog", log);

        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write(timestamp + " " + strTag + "\n");
            writer.write(string + "\n");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
*/
    }

    public static void clearXWalkAppBuffer() {
       
    }

    /**
     * get file md5
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
    
    public static String getAPKMd5FromServer(LocalContext localCtx) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_name", localCtx._s_client_name);
            json.put("client_ip", localCtx._s_client_ip);
            json.put("client_mac", localCtx._s_client_mac);
            json.put("client_id", localCtx._i_client_id); //client uuid

        } catch (Exception e) {
        }

        LocalUtils.CookieHelper.setCookieEnable(true);
        JSONObject jb = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/keep_alive", json, 2000);

        int trytimes = 3;
        while (jb == null && --trytimes >= 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            jb = LocalUtils.requestUrl(localCtx._s_svr_base_URL, "/keep_alive", json, 2000);

            if (jb != null && jb.has("md5"))
                break;
        }

        if (jb != null && jb.has("md5")) {
            try {
                return jb.getString("md5");
            } catch (JSONException e) {
            }
        }

        return null;
    }

    static public String getSystemMAC() {
        String macSerial = null;
        String str = "";

        try {
            java.lang.Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
        }

        return macSerial;
    }
 
    static public String getLocalDir()
    {
            java.net.URL url = VdiClientSimulator.class.getProtectionDomain().getCodeSource().getLocation();
            String filePath = null;
            try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
            File path = new File(filePath);
            filePath = path.getParent();
            } catch (Exception e) {
            e.printStackTrace();
            }
            
            return filePath;
    }
    static public int GetCFI(String str, int defVal) {
        String strPathName = "/oe_cfis/" + str + ".cfi";

        File f = new File(LocalUtils.external_pub_dir + strPathName);
        LocalUtils.printLog("WebClient", "write cfi file :" + LocalUtils.external_pub_dir + strPathName);
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String strVal = br.readLine();
                if (strVal.length() > 0)
                    return Integer.valueOf(strVal);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return defVal;
    }
    
   
}
