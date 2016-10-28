package vdiclientsimulator.oevdi.netupdate;
import vdiclientsimulator.oevdi.client.LocalUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class FtpUpdateHelper {

    //private static FtpUpdateHelper _instance;
    private String mStrFtp = null;
    private FtpUpdateListener mUpdateListener = null;

    public interface FtpUpdateListener {
        public void onDownloadFinish(String str, int err);

        public String onGetStorePath();

        public void onError(String strError);

        public String onGetMD5();

    }

    ;

    private FtpUpdateHelper(String strURL, FtpUpdateListener listener) {
        mStrFtp = strURL;
        mUpdateListener = listener;
    }

    public static void startJob(String strURL, FtpUpdateListener listener) {
        if (strURL == null) {
            LocalUtils.printLog("FtpUpdateHelper", "strURL error!");
            return;
        }

        new FtpUpdateHelper(strURL, listener).doJob();
    }

    private void doJob() {
        //using temporary directory to store apk.
        //ftp://isouser:isouser@10.2.2.188/iso/repo/android_client/evdi-android-client.apk
        String localPath = null;

        if (mUpdateListener != null)
            localPath = mUpdateListener.onGetStorePath();

        if (localPath == null) {
            //LocalUtils.printLog("FtpUpdateHelper", "localpath is null,unavailable!");
            if (mUpdateListener != null)
                mUpdateListener.onError("local path not present!");
            return;
        }

        File f = new File(localPath);
        if (f.exists())
            f.delete();

        f = null;

        boolean bsucc = false;
        try {
            bsucc = downFile(mStrFtp, localPath);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        if (bsucc) {
            if (mUpdateListener != null)
                mUpdateListener.onDownloadFinish(localPath, 0);
            return;
        }

        if (mUpdateListener != null)
            mUpdateListener.onError("FTP download failed,localpath:" + localPath + " url:" + mStrFtp);

    }

    public boolean downFile(String url, String localPath) throws URISyntaxException {
        URI uri = new URI(url);
        String host = uri.getHost();
        String path = uri.getPath();
        String userinfo = uri.getUserInfo();
        String user = userinfo.split(":")[0];
        String psw = userinfo.split(":")[1];

        File f = new File(path);
        String refpath = f.getParent();
        String strfName = f.getName().toString();
        f = null;

        int port = uri.getPort();
        port = port > 0 ? port : 21;

        int trytimes = 3;
        boolean bsuccess = _downFile(host, port, user, psw, refpath, strfName, localPath);
        while (bsuccess == false) {
            trytimes--;
            if (trytimes < 0)
                break;

            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            bsuccess = _downFile(host, port, user, psw, refpath, strfName, localPath);
        }

        return true;
    }

    private boolean doMD5Detect(String localPath) {
        boolean success = true;
        if (mUpdateListener != null) {
            String strMd5 = mUpdateListener.onGetMD5();
            if (strMd5 != null && !strMd5.isEmpty()) {
                try {
                    String _fileMd5 = LocalUtils.getFileMD5(new File(localPath));

                    //oLogger.getLogger().info("MD5 from server:" + strMd5 + ", Downloaded file MD5:" + _fileMd5);
                    LocalUtils.writeExternalLog("tool", "ftp", "md5 from server:" + strMd5 + " downloaded file md5:" + _fileMd5);

                    String lowStrMd5 = strMd5.toLowerCase();
                    String _lowFileMd5 = _fileMd5.toLowerCase();

                    if (lowStrMd5.contains(_lowFileMd5))
                        success = true;
                    else
                        success = false;

                } catch (NoSuchAlgorithmException e) {
                   // LocalUtils.app_toastmessage("No MD5 Method Founded in android !", 5000);
                    success = true;
                } catch (IOException e) {
                    //LocalUtils.app_toastmessage("Read localfile for md5 failed !", 5000);
                    success = true;
                }
            }
        }

        return success;
    }

    private boolean _downFile(
            String url,
            int port,
            String username,
            String password,
            String remotePath,
            String fileName,
            String localPath
    ) {

        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(url, port);
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }

            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); //文件类型为二进制文件
            //ftp.changeToParentDirectory();
            String[] dirs = remotePath.split("/");
            for (int i = 0; i < dirs.length; i++)
                ftp.changeWorkingDirectory(dirs[i]);

            boolean bfounded = false;
            FTPFile[] fs = ftp.listFiles();
            if (fs.length > 0) {
                for (FTPFile ff : fs) {
                    //LocalUtils.printLog("FtpUpdateHelper", ff.getName());

                   // oLogger.getLogger().info("Find file:" + ff.getName());
                    LocalUtils.writeExternalLog("tool", "ftp","find file:" + ff.getName());

                    if (ff.getName().equalsIgnoreCase(fileName)) {
                        bfounded = true;

                        File localFile = new File(localPath);
                        if (localFile.exists())
                            localFile.delete();

                        long fsize = ff.getSize();
                        String fName = ff.getName();

                      //  oLogger.getLogger().info("Source size:" + String.valueOf(fsize) + " fname:" + fName);
                        LocalUtils.writeExternalLog("tool", "ftp","source size:" + String.valueOf(fsize) + " fname:" + fName);

                        OutputStream is = new FileOutputStream(localFile);
                        ftp.retrieveFile(ff.getName(), is);
                        is.close();

                        long dsize = localFile.length();

                       // oLogger.getLogger().info("Downloaded size:" + String.valueOf(dsize));
                        LocalUtils.writeExternalLog("tool", "ftp","downloaded size:" + String.valueOf(dsize));

                        localFile = null;

                        if (fsize != dsize)
                            success = false;
                        else
                            success = true;
                    }
                }
            } else {
               // oLogger.getLogger().info("No file founded, null directory!");
                LocalUtils.writeExternalLog("tool", "no file founded", "failed ,null directory");
            }

            ftp.logout();
            if (!bfounded) {
              //  oLogger.getLogger().info("Not found file:" + fileName  + " on server!");
                LocalUtils.writeExternalLog("tool", "file name:" + fileName, "failed ,not found file on server !");

                success = true; //allow use default resource,if return 'false' ,while looping to timeout.
            }

        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }

        if (success)
            return this.doMD5Detect(localPath);

        return success;
    }
}
