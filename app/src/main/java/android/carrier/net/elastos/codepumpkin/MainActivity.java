package android.carrier.net.elastos.codepumpkin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //for carrier















        CCGLSurfaceView ccglSurfaceView = new CCGLSurfaceView(this);//cocos2d提供的SurfaceView

        setContentView(ccglSurfaceView);//设置Activity显示的view

        CCDirector.sharedDirector().attachInView(ccglSurfaceView);
        // CCDirector 导演，负责管理和切换场景，负责初始化OPENGL的各项参数
        // CCDirector 采用单例模式，通过sharedDirector()方法获取其唯一的实例
        // attachInView 与OpenGL的SurfaceView进行连接，意思是将surfaceView交给cocos2d来管理

        CCDirector.sharedDirector().setDisplayFPS(true);
        // 显示fps，需要添加fps_images.png到assets中，否则会报nullpointer，在CCDirector源码中可以看出

        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60);// 设置帧率 60帧每秒，人的肉眼识别帧率为60，所以设置为60最为合理

        CCDirector.sharedDirector().setDeviceOrientation(
                CCDirector.kCCDeviceOrientationPortrait);// 设置为横屏显示

        CCDirector.sharedDirector().setScreenSize(800, 480);
        // 设置横屏后的分辨率,其实是将屏幕横竖分成800和480个小块

        CCScene ccScene = CCScene.node();  //创建一个场景，用来显示游戏界面
        ccScene.addChild(new MyCCLayer()); //将MyCCLayer层加到场景里
        CCDirector.sharedDirector().runWithScene(ccScene);// 运行场景
    }


    @Override
    protected void onResume() {
        super.onResume();
        CCDirector.sharedDirector().resume();
        //恢复游戏运行
        // cocos2d提供3个生命周期方法，对应android的三个生命周期
    }

    @Override
    protected void onPause() {
        super.onPause();
        CCDirector.sharedDirector().onPause();
        //暂停，游戏切出时候调用
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CCDirector.sharedDirector().end();
        // 结束，游戏退出时调用
    }
}
