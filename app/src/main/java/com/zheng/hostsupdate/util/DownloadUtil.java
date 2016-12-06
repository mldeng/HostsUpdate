package com.zheng.hostsupdate.util;

import android.content.Context;
import android.os.AsyncTask;

import com.zheng.hostsupdate.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;



public class DownloadUtil {

    public static void downloadHostFile(final Context context, final DownloadListener downloadListener) {

        AsyncHttpClient mClient = new AsyncHttpClient();

        //TODO umeng配置下载链接
        String url = "https://raw.githubusercontent.com/racaljk/hosts/master/hosts";
        mClient.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, final Header[] headers, final byte[] bytes) {
                AsyncTask.execute( () -> {
                    OutputStream os = null;
                    try {
                        long totalLength = 0;
                        for (int i = 0; i < headers.length; i++) {
                            Header header = headers[i];
                            if ("Content-Length".equals(header.getName())) {
                                try {
                                    totalLength = Long.parseLong(header.getValue());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (bytes == null || bytes.length < totalLength) {
                            downloadListener.error();
                            return;
                        }

                        File saveDir = context.getFilesDir();
                        if (!saveDir.exists()) {
                            saveDir.mkdirs();
                        }

                        File hostFile = new File(saveDir.getAbsolutePath() + File.separator + Constants.DOWNLOAD_HOST_NAME);
                        os = new FileOutputStream(hostFile);
                        os.write(bytes);
                        downloadListener.success(hostFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        downloadListener.error();
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                downloadListener.error();
            }
        }).setTag(url);
    }


    public interface DownloadListener {
        void success(File file);

        void error();
    }

}
