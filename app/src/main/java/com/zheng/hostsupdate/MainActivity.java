package com.zheng.hostsupdate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootShell.RootShell;
import com.stericson.RootTools.RootTools;
import com.zheng.hostsupdate.util.CloseUtil;
import com.zheng.hostsupdate.util.DownloadUtil;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button mProxyHostBtn, cleanHostBtn, readHostBtn, aboutBtn;
    TextView tipsView;
    ProgressDialog mProgressDialog;
    Handler mHandler = new Handler();
    private long mExitTime = 0;
    private boolean isMobileRoot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProxyHostBtn = (Button) findViewById(R.id.proxy_btn);
        cleanHostBtn = (Button) findViewById(R.id.clean_host);
        readHostBtn = (Button) findViewById(R.id.read_host);
        aboutBtn = (Button) findViewById(R.id.about_button);
        tipsView = (TextView) findViewById(R.id.tips);

        //Get按钮设置监听
        mProxyHostBtn.setOnClickListener(view ->  {
            if (!isMobileRoot) {
                Toast.makeText(getContext(), R.string.get_root_fail, Toast.LENGTH_SHORT).show();
                return;
            }

            showProgressDialog();
            DownloadUtil.downloadHostFile(MainActivity.this, new DownloadUtil.DownloadListener() {
                @Override
                public void success(final File file) {
                    Log.e(TAG, "success: Thread = " + Thread.currentThread());
                    // 需要host
                    AsyncTask.execute( () -> {
                        try {
                            RootShell.getShell(true);
                        } catch (Exception e) {
                            mHandler.post( () ->
                                    dismissDialog()
                            );
                            e.printStackTrace();
                        }

                        try {
                            RootTools.copyFile(file.getAbsolutePath(), "/system/etc/hosts", true, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.post( () -> {
                            tipsView.setText(getString(R.string.get_last_host_tips));
                            dismissDialog();
                        });
                    });
                }

                @Override
                public void error() {
                    mHandler.post( () -> {
                        tipsView.setText(getString(R.string.network_error));
                        dismissDialog();
                    });
                }
            });
        });

        //Restore按钮设置监听
        cleanHostBtn.setOnClickListener(view ->{
            if (!isMobileRoot) {
                tipsView.setText(getString(R.string.get_root_fail));
                return;
            }
            AsyncTask.execute( () -> {
                try {
                    RootShell.getShell(true);
                } catch (Exception e) {
                    mHandler.post( () ->
                            dismissDialog()
                    );
                    e.printStackTrace();
                }
                RootTools.copyFile(getVoidHostPath(), "/system/etc/hosts", true, false);
                mHandler.post( () ->
                        tipsView.setText(getString(R.string.huifu_host_tips))
                );
            });
        });


        //Read按钮设置监听
        readHostBtn.setOnClickListener(view -> startActivity(new Intent(getContext(), HostDetailActivity.class)));

        //About按钮设置监听
        aboutBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AboutActivity.class)));

        initVoidHost();
        checkMobileIsRoot();

    }

    private void checkMobileIsRoot() {

        AsyncTask.execute( () -> {
            isMobileRoot = RootShell.isRootAvailable();
            if (!isMobileRoot) {
                mHandler.post( () -> {
                    tipsView.setText(getString(R.string.your_mobile_have_no_root));
                    mProxyHostBtn.setEnabled(false);
                    cleanHostBtn.setEnabled(false);
                });
            }
        });
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        mProgressDialog.setTitle("");
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();
    }

    private void dismissDialog() {
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.dismiss();
    }


    public String getRealFileDirPath() {
        File dir = getFilesDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    private String getVoidHostPath() {
        return getRealFileDirPath() + File.separator + Constants.VOID_HOST_NAME;
    }

    private Context getContext() {
        return this;
    }

    private void initVoidHost() {
        File voidHostFile = new File(getVoidHostPath());
        if (voidHostFile.exists()) {
            return;
        }
        AsyncTask.execute( () -> {
            File file = new File(getRealFileDirPath() + File.separator + Constants.VOID_HOST_NAME);
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(Constants.VOID_HOST_VALUE);
                fileWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CloseUtil.close(fileWriter);
            }
        });
    }

    //设置按两次返回键退出功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - mExitTime) > 2000){
                //如果两次点击返回按钮，则退出
                Toast.makeText(this, R.string.exit, Toast.LENGTH_LONG).show();
                mExitTime = System.currentTimeMillis();
            }else {
                System.exit(0);//否则就退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
