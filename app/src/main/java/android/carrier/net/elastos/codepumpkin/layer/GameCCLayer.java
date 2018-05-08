package android.carrier.net.elastos.codepumpkin.layer;

import android.app.Application;
import android.carrier.net.elastos.codepumpkin.Bean.Action;
import android.carrier.net.elastos.codepumpkin.Bean.GameUser;
import android.carrier.net.elastos.codepumpkin.common.GameCommon;
import android.carrier.net.elastos.codepumpkin.util.SpriteUtil;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCLabelAtlas;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.List;

public class GameCCLayer extends CCLayer {

    private CCSprite spriteBg;
    private CCSprite spriteGrad;
    private CCSprite spriteStep;
    private CCSprite spriteTurn;
    private CCSprite spriteLeft;
    private CCSprite spriteRight;
    private CCSprite spriteStepBox;

    private CCLabelAtlas spriteStep1;
    private CCLabelAtlas spriteStep2;


    private List<CCSprite> bushList = new ArrayList<CCSprite>();
    private List<CCSprite> pumpkinList = new ArrayList<>();
    private List<GameUser> gameUserList = new ArrayList<GameUser>();
    private List<CCLabel> stepPromptList = new ArrayList<CCLabel>();

    private List<Action> actionList = new ArrayList<>();
    private int actionIndex = 0;


    private int currentUser = 0;

    public GameCCLayer() {
        isTouchEnabled_ = true;
        init();
    }

    private void init() {

        SpriteUtil.boxSize = this.getContentSize();
        initBg();
        initCtrlView();
        initView();
        //schedule("actionLoop", 0.1f);
       // scheduleUpdate();
    }

//    public void update(float dt){
//        actionLoop();
//    }

    /**
     * 触摸事件
     */
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint p = CCDirector.sharedDirector().convertToGL(SpriteUtil.cratePoint(event.getX(), event.getY()));
        //CGPoint p = SpriteUtil.converPx(event.getX(),event.getY());

        Log.i("action", p.toString());

        //左转
        if (SpriteUtil.isContainsPointByView(spriteLeft, p)) {
            actionHandler(new Action(currentUser, GameCommon.ACTION_ROTA, -90));
            return false;
        }

        //右转
        if (SpriteUtil.isContainsPointByView(spriteRight, p)) {
            //actionHandler(new Action(currentUser, GameCommon.ACTION_ROTA, 90));
            actionHandler(new Action(currentUser,GameCommon.ACTION_MOVE,GameCommon.DEFAULT_SIZE));
            return false;
        }
//
//        // -60
//        if(SpriteUtil.isContainsPointByView(spriteStep1,p)){
//            actionHandler(new Action(currentUser,GameCommon.ACTION_MOVE,~GameCommon.DEFAULT_SIZE));
//            return false;
//        }
//
//        // +60
//        if(SpriteUtil.isContainsPointByView(spriteStep2,p)){
//            actionHandler(new Action(currentUser,GameCommon.ACTION_MOVE,GameCommon.DEFAULT_SIZE));
//            return false;
//        }

        //重放
        if (SpriteUtil.isContainsPointByView(spriteGrad, p)) {

        }

        return super.ccTouchesBegan(event);
    }

    /**事件处理*/
    public void  actionHandler(Action action) {
        //添加到任务队列
        actionList.add(action);
        actionLoop();
    }

    public void actionLoop() {
        Log.i("loop","-----------------------------------"+actionIndex);
        if (actionIndex < actionList.size()) {
            Action action = actionList.get(actionIndex);
            GameUser user = gameUserList.get(action.getUserId());

            switch (action.getType()) {
                case GameCommon.ACTION_MOVE:
                    user.getSprite().runAction(CCSequence.actions(
                            createMoveAction(user, action.getValue()), CCCallFuncN.actionWithTarget(this, "actionCall")
                    ));
                    break;
                case GameCommon.ACTION_ROTA:
                    user.getSprite().runAction(CCSequence.actions(
                            createRotaAction(user, action.getValue()), CCCallFuncN.actionWithTarget(this, "actionCall")
                    ));
                    break;
            }
        }
    }

    public void userHandler(GameUser user) {

    }

    public void gameHandler() {

    }

    /**
     * 回调
     */
    public void actionCall() {
        Log.i("info",actionIndex+"");
        actionIndex++;
        updateView();
    }

    /**
     * 刷新
     */
    public void updateView() {
        //   schedule();
        //遍历吃南瓜
        for (int j = 0; j < pumpkinList.size(); j++) {
            if (SpriteUtil.isContainsRect(gameUserList.get(currentUser).getSprite(), pumpkinList.get(j))) {
                pumpkinList.get(j).setUserData(0);      //吃掉
                pumpkinList.get(j).setOpacity(0);


            }
        }
        //遍历障碍物
        for (int j = 0; j < bushList.size(); j++) {
            if (SpriteUtil.isContainsRect(gameUserList.get(currentUser).getSprite(), bushList.get(j))) {
                Log.e("status", "游戏结束");

            }
        }
    }


    /***
     * 创建移动事件
     * @param gameUser
     * @param step
     * @return
     */
    public CCMoveBy createMoveAction(GameUser gameUser, float step) {
        CCMoveBy moveAction = null;
        float duration = (Math.abs(step) / GameCommon.DEFAULT_SIZE) * GameCommon.DEFAULT_TIME;
        switch (gameUser.getDirection()) {        //方向  上、右、下、左
            case GameCommon.DIRECTION_UP:
            default:
                moveAction = CCMoveBy.action(duration, SpriteUtil.cratePoint(0, step));
                break;
            case GameCommon.DIRECTION_RIGHT:
                moveAction = CCMoveBy.action(duration, SpriteUtil.cratePoint(step, 0));
                break;
            case GameCommon.DIRECTION_DOWN:
                moveAction = CCMoveBy.action(duration, SpriteUtil.cratePoint(0, 0 - step));
                break;
            case GameCommon.DIRECTION_LEFT:
                moveAction = CCMoveBy.action(duration, SpriteUtil.cratePoint(0 - step, 0));
                break;

        }

        return moveAction;
    }

    /***
     * 创建旋转事件
     * @param gameUser
     * @param
     * @return
     */
    public CCRotateBy createRotaAction(GameUser gameUser, float value) {

        return CCRotateBy.action(GameCommon.DEFAULT_TIME, value);

//        if (type == 1) {
//            // if (acc.direction > 1) {
//            //     acc.direction--;
//            // } else {
//            //     acc.direction = 4;
//            // }
//
//            //  acc.setRotation(-90);
//        } else {
//            // if (acc.direction < 4) {
//            //     acc.direction++;
//            // } else {
//            //     acc.direction = 1;
//            // }
//            //acc.setRotation(90);
//            return CCRotateBy.action(GameCommon.DEFAULT_TIME,value);
//
//        }


    }


    /**
     * 初始化背景
     */
    private void initBg() {
        spriteBg = CCSprite.sprite("grass-big.jpg");
        spriteBg.setAnchorPoint(CGPoint.getZero());
        spriteBg.setPosition(CGPoint.getZero());
        this.addChild(spriteBg, 0, 0);

        spriteGrad = CCSprite.sprite("dragon-body.png");
        spriteGrad.setPosition((float) (spriteGrad.getContentSize().getWidth() * 0.2)
                , (float) (spriteGrad.getContentSize().getHeight() * 0.2));
        spriteGrad.setScale(0.4);
        this.addChild(spriteGrad);
    }

    /**
     * 初始化控制控件
     */
    private void initCtrlView() {
        spriteStep = new CCSprite("icon-step.png");
        spriteTurn = new CCSprite("icon-turn.png");
        spriteLeft = new CCSprite("icon-left.png");
        spriteRight = new CCSprite("icon-right.png");

        spriteStep.setPosition(SpriteUtil.cratePoint(this.getContentSize().getWidth() * 0.6, spriteStep.getContentSize().getHeight() / 2 + 10));
        spriteTurn.setPosition(SpriteUtil.cratePoint(this.getContentSize().getWidth() * 0.8, spriteTurn.getContentSize().getHeight() / 2 + 10));
        spriteLeft.setPosition(SpriteUtil.cratePoint(this.getContentSize().getWidth() * 0.8 - 40, spriteTurn.getContentSize().getHeight() + 40));
        spriteRight.setPosition(SpriteUtil.cratePoint(this.getContentSize().getWidth() * 0.8 + 40, spriteTurn.getContentSize().getHeight() + 40));


        this.addChild(spriteStep, 10);
        this.addChild(spriteTurn, 10);
        this.addChild(spriteLeft, 10);
        this.addChild(spriteRight, 10);

        this.spriteStep1 = SpriteUtil.createStepText("-60",
                SpriteUtil.cratePoint(this.getContentSize().getWidth()*0.6-30,spriteStep.getContentSize().getHeight()+40)
                ,-60);
        this.spriteStep2 = SpriteUtil.createStepText("60",
                SpriteUtil.cratePoint(this.getContentSize().getWidth()*0.6+30,spriteStep.getContentSize().getHeight()+40)
                ,60);

        this.addChild(this.spriteStep1);
        this.addChild(this.spriteStep2);
    }

    /**
     * 初始化其他视图控件
     */
    private void initView() {
        //添加玩家
        GameUser user = new GameUser();
        user.setDirection(1);
        user.setSprite(SpriteUtil.createGameUser());
        user.setStartPosition(user.getSprite().getPosition());
        gameUserList.add(user);
        currentUser = 0;

        this.addChild(user.getSprite());

        // 添加南瓜和障碍物


        for (int i = 0; i < GameCommon.PUMPKIN_COUNT; i++) {
            pumpkinList.add(SpriteUtil.createPumpkin());
            pumpkinList.get(i).setPosition(randomPosition(pumpkinList.get(i)));
            this.addChild(pumpkinList.get(i));
        }

        for (int i = 0; i < GameCommon.BUSH_COUNT; i++) {
            bushList.add(SpriteUtil.createBush());
            bushList.get(i).setPosition(randomPosition(bushList.get(i)));
            this.addChild(bushList.get(i));
        }

//        createStepPrompt();


    }


    /***
     * 遍历南瓜，创建步数提示
     */
    private void createStepPrompt() {
        for (int i = 0; i < stepPromptList.size(); i++) {
            this.removeChild(stepPromptList.get(i), true);
        }
        stepPromptList.clear();

        CGPoint p = gameUserList.get(currentUser).getSprite().getPosition();

        for (int i = 0; i < pumpkinList.size(); i++) {
            if ((int) (pumpkinList.get(i).getUserData()) != 1) {
                CGRect b = pumpkinList.get(i).getBoundingBox();
                float t = 0;
                if (gameUserList.get(currentUser).getDirection() % 2 != 0) {  //x轴
                    if (p.x > b.origin.x && p.x < b.origin.x + b.size.width) {   // x轴重合
                        t = b.origin.y - p.y + 10;
                    }
                } else {                                                      //y轴
                    if (p.y > b.origin.y && p.y < b.origin.y + b.size.height) {   // x轴重合
                        t = b.origin.x - p.x + 10;
                    }
                }
              //  stepPromptList.add(SpriteUtil.createStepText((t > 0 ? "+" + t : "" + t), createStepPromptPosition(), t));
                this.addChild(stepPromptList.get(stepPromptList.size() - 1));
            }
        }

        Log.e("step", stepPromptList.toString());

    }

    /**
     * 创建步数提示的位置
     */
    private CGPoint createStepPromptPosition() {          //为步数提示栏确认位置
        if (stepPromptList.size() > 0) {
            CGPoint item = stepPromptList.get(stepPromptList.size() - 1).getPosition();
            return SpriteUtil.cratePoint(item.x + 60, item.y);
        } else {
            return SpriteUtil.cratePoint(SpriteUtil.boxSize.width * 0.6 - 30, 130);
        }
    }

    /**
     * 检查位置是否已经有控件存在
     */
    private boolean checkPosition(CGPoint cc) {
        for (int i = 0; i < gameUserList.size(); i++) {
            if (SpriteUtil.isContainsPointByView(gameUserList.get(i).getSprite(),cc)) {
                return false;
            }
        }
        for (int i = 0; i < pumpkinList.size(); i++) {
            if (SpriteUtil.isContainsPointByView(pumpkinList.get(i),cc)) {
                return false;
            }
        }
        for (int i = 0; i < bushList.size(); i++) {
            if (SpriteUtil.isContainsPointByView(bushList.get(i),cc)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 随机位置
     */
    private CGPoint randomPosition(CCSprite view) {
        CGPoint p;
        while (true) {
            p = SpriteUtil.randomPosition(view);
            // cc.log("random:"+JSON.stringify(p));
            if (checkPosition(p)) {
                break;
            }
        }
        return p;
    }




}