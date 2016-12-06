package com.zheng.hostsupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {


    TextView mAboutTxt, mHostGithubTxt, mAppGithubTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mAboutTxt = (TextView) findViewById(R.id.tips);
        mHostGithubTxt = (TextView) findViewById(R.id.host_github_txt);
        mAppGithubTxt = (TextView) findViewById(R.id.author_github_txt);

        String aboutText = getString(R.string.about_detail);
        String hostGithub = getString(R.string.host_github);
        String authorGithub = getString(R.string.author_github);

        mAboutTxt.setText(Html.fromHtml(aboutText));
        mHostGithubTxt.setText(Html.fromHtml(hostGithub));
        mAppGithubTxt.setText(Html.fromHtml(authorGithub));

        //mAboutTxt.setMovementMethod(LinkMovementMethod.getInstance());
        //mHostGithubTxt.setMovementMethod(LinkMovementMethod.getInstance());
        //mAppGithubTxt.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
