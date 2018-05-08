package android.carrier.net.elastos.codepumpkin;

import android.carrier.net.elastos.codepumpkin.Bean.Action;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btnStart;
    private Button btnMyInfo;
    private Button btnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLandscape();
        setContentView(R.layout.launcher);

        initView();
        testGson();
    }


    private void initView(){
        this.btnStart = (Button)findViewById(R.id.btn_start);
        this.btnMyInfo = (Button)findViewById(R.id.btn_my_info);
        this.btnAddFriend = (Button)findViewById(R.id.btn_add_friend);
        this.btnStart.setOnClickListener(this);
        this.btnMyInfo.setOnClickListener(this);
        this.btnAddFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //以下添加事件
        switch (v.getId()){
            case R.id.btn_start:
                startActivity(new Intent(LauncherActivity.this,MainActivity.class));
                break;
            case R.id.btn_my_info:

                break;
            case R.id.btn_add_friend:

                break;
        }
    }

    private void initLandscape() {
        /* 隐藏标题栏 */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /* 隐藏状态栏 */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* 设定屏幕显示为横屏 */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void testGson(){
        Action action = new Action(1,1,1);
        Gson gson = new Gson();
        String json = gson.toJson(action);
        Log.i("gson",json);

        Action action1 = gson.fromJson(json,Action.class);
        Log.i("gson",action1.toString());
    }



}
