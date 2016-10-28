package vdiclientsimulator.oevdi.oem;

import java.io.File;
import java.io.IOException;
import org.json.JSONException; 
import org.json.JSONObject;
import vdiclientsimulator.oevdi.client.LocalLogic;
import vdiclientsimulator.oevdi.client.LocalLogic.LocalContext;
import vdiclientsimulator.oevdi.client.LocalUtils;
import vdiclientsimulator.oevdi.client.VdiConfigure;
import vdiclientsimulator.oevdi.client.VdiConfigure.configure;
import vdiclientsimulator.oevdi.netupdate.FtpUpdateHelper;

public class OemManager {

    static public boolean bIsWorking;

    private LocalLogic localLogic;
    private LocalContext localCtx;
    private OemUpdateListener updateListener;
    private String newMD5ID;
    private String newZipName;
    private int nStatus;
    private Thread mThread;

    public final static int QUERYMD5_FAILED = -1;
    public final static int OEMINFO_ERROR = -2;
    public final static int OEMRES_EXIST = -3;
    public final static int OEMRES_LOCAL = -4;

    public interface OemUpdateListener {
        void onUpdateFinish(String strUrl);

        String onGetMD5();
    }

    public String getCurResourceID() {
        return newMD5ID;
    }

    public OemManager(LocalLogic llogic) {
        localLogic = llogic;
        localCtx = localLogic.getlocalCtx();
        nStatus = 0;
    }

    public int isOemSourceReady() throws JSONException {
        nStatus = 0;
        String url = "http://" + localCtx._s_svr_ip + ":8081";//localLogic.getSvrBaseUrl();

        if (url != null) {
            //ftp://10.2.2.88/iso/zip/
            //isouser
            //String verReqUrl wget http://10.1.41.99:8081/thor/toolkite/get_md5";
            JSONObject jo = LocalUtils.requestUrl(url, "/thor/toolkite/get_md5", null, 10000);

            int tryCount = 10;
            while (jo == null && --tryCount >= 0) //try roughly 30 secs.
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                jo = LocalUtils.requestUrl(url, "/thor/toolkite/get_md5", null,10000);
            }

            if (jo == null)
                return QUERYMD5_FAILED;

           // oLogger.getLogger().info("Oem source desc:" + jo.toString());
            LocalUtils.writeExternalLog("tool", "ftp", "oem source desc:" + jo.toString());
            LocalUtils.printLog("LocalLogic", "get_md5:" + jo.toString());
            /*
                {
                "code": 0,
                "lang_type": "e-vdi",
                "md5": "e0f7c5dd97dbf1a901ecb64dcaabf899",
                "name": "e-vdi.zip"
		}
	    */
            //request fail
            if (jo.has("error_code") || jo.getInt("code") != 0) {
                LocalUtils.printLog("OemManager", "request oem info failed!");
                return OEMINFO_ERROR;
            }
            newZipName = jo.getString("name");
            String md5ID = jo.getString("md5");
            String curSourceID = LocalUtils.LoadInfo(LocalUtils.configs.oemresid);
            if (md5ID.equalsIgnoreCase(curSourceID)) {
                LocalUtils.printLog("OemManager", "equ id  ,not need reload.");
                
                //oLogger.getLogger().info("This resources already existed, not need reload.");
                LocalUtils.writeExternalLog("tool", "ftp", "equ id :" + curSourceID + " need't reload.");
                nStatus = 0; //not needed reload
                setCurrentID(md5ID);
                return OEMRES_EXIST;
            }
            if (isExistInLocal(md5ID)) {
                LocalUtils.printLog("OemManager", "resouce present needn't download.");
                setCurrentID(md5ID);
                
                //  oLogger.getLogger().info("Resources already embeded, not need download.");
                LocalUtils.writeExternalLog("tool", "ftp", "embeded resources,not need download.");
                nStatus = 1;
                return OEMRES_LOCAL;
            }
            if (isExistInDataDir(md5ID)) {
                LocalUtils.printLog("OemManager", "resouce present needn't download.");
                
                //   oLogger.getLogger().info("This resources already existed, not need reload.");
                LocalUtils.writeExternalLog("tool", "ftp", "already have this resources ,not need reload.");
                setCurrentID(md5ID);
                nStatus = 1; //sources can be loaded in web client.
                return OEMRES_EXIST; //not need update from server
            }
            newMD5ID = md5ID;
        }

        return 1;
    }

    public void setCurrentID(String md5id) {
        // TODO Auto-generated method stub
        try {
            LocalUtils.SaveInfo(LocalUtils.configs.oemresid, md5id);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isExistInDataDir(String md5id) {
       
        return false;
    }

    public boolean isExistInLocal(String md5id) {
        // TODO Auto-generated method stub
        return false;
    }

    public void update(OemUpdateListener listener) {
        // TODO Auto-generated method stub
        updateListener = listener;

        String zipftpurl = "ftp://isouser:isouser@";
        zipftpurl += localCtx._s_svr_ip;
        zipftpurl += "/iso/zip/" + newZipName;

        nStatus = 0;
        FtpUpdateHelper.startJob(zipftpurl, new FtpUpdateHelper.FtpUpdateListener() {

            @Override
            public String onGetStorePath() {
                // TODO Auto-generated method stub
                String path = LocalUtils.local_tmp_dir + "/" +  localCtx._s_client_mac;
                File f = new File(path);
                f.mkdirs();
                return path + "/" + newMD5ID + newZipName;
            }

            @Override
            public void onDownloadFinish(String strlocal, int err) {
                // TODO Auto-generated method stub
                if (err != 0) {
                    if (updateListener != null)
                        updateListener.onUpdateFinish(null);
                    return;
                }

                LocalUtils.printLog("OemManager", "download success:" + strlocal);
                //download zip success, the default local path is : data/data/app dir/cache/
                //unzip zip package.
                String strResDir = "/data/data/com.oevdi.client/" + newMD5ID;

                File f = new File(strResDir);
                if (f.exists()) {
                    f.delete();
                }

                f.mkdir();
                boolean bsucc = LocalUtils.copyAndUnzip(strlocal, strResDir, "oeresvdi.zip");
                if (bsucc)
                    nStatus = 1;

                if (updateListener != null) {
                    File fdir = new File(strResDir);

                    String strurl = LocalUtils.findPage(fdir, "main.html");
                    LocalUtils.printLog("OemManager", "main.html:" + strurl);
                    if (strurl != null)
                        updateListener.onUpdateFinish(strurl);
                }
            }

            @Override
            public void onError(String strError) {
                // TODO Auto-generated method stub
                LocalUtils.printLog("OemManager", strError);
                updateListener.onUpdateFinish(null);
                nStatus = 0;
            }

            @Override
            public String onGetMD5() {
                // TODO Auto-generated method stub
                if (updateListener != null)
                    return updateListener.onGetMD5();
                return null;
            }
        });

    }

    public int getStatus() {
        // TODO Auto-generated method stub
        return nStatus;
    }

    public boolean isWorking() {
        if (mThread == null || mThread.isAlive() == false)
            return true;

        return false;
    }

    //not allow concurrent downloading....
    public void Start(Runnable runnable) {
        // TODO Auto-generated method stub
        if (mThread == null || mThread.isAlive() == false) {
            mThread = new Thread(runnable);
            mThread.start();

            LocalUtils.printLog("OemManager", "start loading thread!");

        } else {
            LocalUtils.printLog("OemManager", "calling is ignored for downloading");
        }

    }

}
