本项目目录文件：

.
|-- AndroidManifest.xml
|-- README.md
|-- assets
|-- bin
|   |-- Android-Breakout.apk
|   |-- AndroidManifest.xml
|   |-- classes
|   |   `-- com
|   |       `-- digdream
|   |           `-- androidbreakout
|   |               |-- BuildConfig.class
|   |               |-- Global.class
|   |               |-- R$attr.class
|   |               |-- R$color.class
|   |               |-- R$dimen.class
|   |               |-- R$drawable.class
|   |               |-- R$id.class
|   |               |-- R$layout.class
|   |               |-- R$raw.class
|   |               |-- R$string.class
|   |               |-- R$style.class
|   |               |-- R.class
|   |               |-- data
|   |               |   `-- UserPreferences.class
|   |               |-- game
|   |               |   |-- oneplayer
|   |               |   |   |-- Ball.class
|   |               |   |   |-- BitmapBlock.class
|   |               |   |   |-- Block.class
|   |               |   |   |-- Breakout.class
|   |               |   |   |-- GameView.class
|   |               |   |   |-- Item.class
|   |               |   |   |-- Paddle.class
|   |               |   |   `-- StageData.class
|   |               |   |-- onetwoplayer
|   |               |   |   |-- Ball.class
|   |               |   |   |-- Block.class
|   |               |   |   |-- Breakout.class
|   |               |   |   |-- GameView2p.class
|   |               |   |   |-- Paddle.class
|   |               |   |   |-- TopPaddle.class
|   |               |   |   `-- TwoDataStage.class
|   |               |   `-- twoplayer
|   |               |       |-- Ball.class
|   |               |       |-- Ball2p.class
|   |               |       |-- Block.class
|   |               |       |-- Breakout2p$1.class
|   |               |       |-- Breakout2p$2.class
|   |               |       |-- Breakout2p.class
|   |               |       |-- GameView2p$1.class
|   |               |       |-- GameView2p.class
|   |               |       |-- Paddle.class
|   |               |       |-- TopPaddle.class
|   |               |       `-- TwoStageData.class
|   |               |-- module
|   |               |   |-- GameData.class
|   |               |   |-- GameMessages$AbstractGameMessage.class
|   |               |   |-- GameMessages$GameBallDataMessage.class
|   |               |   |-- GameMessages$GameBeginMessage.class
|   |               |   |-- GameMessages$GameDataMessage.class
|   |               |   |-- GameMessages$GameEndMessage.class
|   |               |   |-- GameMessages$GameLevelMessage.class
|   |               |   |-- GameMessages$GamePreparedMessage.class
|   |               |   |-- GameMessages$GameStepMessage.class
|   |               |   `-- GameMessages.class
|   |               |-- switchlayout
|   |               |   |-- OnViewChangeListener.class
|   |               |   `-- SwitchLayout.class
|   |               |-- ui
|   |               |   |-- AboutActivity.class
|   |               |   |-- AppActivity$1.class
|   |               |   |-- AppActivity$2.class
|   |               |   |-- AppActivity.class
|   |               |   |-- BaseActivity$1.class
|   |               |   |-- BaseActivity$2.class
|   |               |   |-- BaseActivity$3.class
|   |               |   |-- BaseActivity$4.class
|   |               |   |-- BaseActivity$5.class
|   |               |   |-- BaseActivity$6.class
|   |               |   |-- BaseActivity$7.class
|   |               |   |-- BaseActivity.class
|   |               |   |-- LevelChooseActivity$1.class
|   |               |   |-- LevelChooseActivity.class
|   |               |   |-- OptionActivity$1.class
|   |               |   |-- OptionActivity.class
|   |               |   |-- ResultActivity$1.class
|   |               |   |-- ResultActivity.class
|   |               |   |-- ShopActivity.class
|   |               |   |-- StageActivity$1.class
|   |               |   |-- StageActivity.class
|   |               |   |-- SwitchActivity$MOnClickListener.class
|   |               |   |-- SwitchActivity$MOnViewChangeListener.class
|   |               |   |-- SwitchActivity.class
|   |               |   |-- TestActivity$TouchListenerImp.class
|   |               |   |-- TestActivity.class
|   |               |   `-- stage
|   |               |       |-- StageSwitch1Activity$MOnClickListener.class
|   |               |       |-- StageSwitch1Activity$MOnViewChangeListener.class
|   |               |       `-- StageSwitch1Activity.class
|   |               `-- utils
|   |                   `-- UserIconUtils.class
|   |-- classes.dex
|   |-- dexedLibs
|   |   |-- GameShareSDK-40942498b2c04e1c314224b0db14c9e1.jar
|   |   `-- android-support-v4-998df6102bba5b7815f6a677cc929913.jar
|   |-- jarlist.cache
|   |-- res
|   |   `-- crunch
|   |       |-- drawable
|   |       |   |-- a.png
|   |       |   |-- b.png
|   |       |   |-- guide_dot_black.png
|   |       |   `-- guide_dot_white.png
|   |       |-- drawable-hdpi
|   |       |   |-- about.png
|   |       |   |-- background.png
|   |       |   |-- bg211.png
|   |       |   |-- btn_develop.png
|   |       |   |-- btn_option.png
|   |       |   |-- btn_prepare.png
|   |       |   |-- btn_single.png
|   |       |   |-- btn_two.png
|   |       |   |-- button_sound_off.png
|   |       |   |-- button_sound_on.png
|   |       |   |-- chara1.png
|   |       |   |-- chara1_block.png
|   |       |   |-- chara2.png
|   |       |   |-- chara2_block.png
|   |       |   |-- chara3.png
|   |       |   |-- chara3_block.png
|   |       |   |-- chara4.png
|   |       |   |-- chara4_block.png
|   |       |   |-- chara5.png
|   |       |   |-- chara5_block.png
|   |       |   |-- chara6.png
|   |       |   |-- chara6_block.png
|   |       |   |-- circle_background.png
|   |       |   |-- eggplant.png
|   |       |   |-- ic_launcher.png
|   |       |   |-- icon.png
|   |       |   |-- levelbackground.png
|   |       |   |-- levelchoose.png
|   |       |   |-- num1.png
|   |       |   |-- num1_lock.png
|   |       |   |-- num4.png
|   |       |   |-- option.png
|   |       |   |-- share_user_icon_1.png
|   |       |   |-- share_user_icon_2.png
|   |       |   |-- share_user_icon_3.png
|   |       |   |-- share_user_icon_4.png
|   |       |   |-- share_user_icon_5.png
|   |       |   |-- share_user_icon_6.png
|   |       |   |-- share_user_icon_7.png
|   |       |   |-- share_user_icon_8.png
|   |       |   |-- stage1a.png
|   |       |   |-- stage1b.png
|   |       |   |-- stage2a.png
|   |       |   |-- stage2b.png
|   |       |   |-- stage3a.png
|   |       |   |-- stage3b.png
|   |       |   |-- stage4a.png
|   |       |   |-- stage4b.png
|   |       |   |-- stage5a.png
|   |       |   |-- stage5b.png
|   |       |   |-- stage6a.png
|   |       |   |-- stage6b.png
|   |       |   `-- title.png
|   |       |-- drawable-mdpi
|   |       |   |-- ic_action_search.png
|   |       |   `-- ic_launcher.png
|   |       |-- drawable-nodpi
|   |       |   |-- item1.png
|   |       |   |-- item2.png
|   |       |   |-- item3.png
|   |       |   |-- item4.png
|   |       |   |-- item5.png
|   |       |   |-- item6.png
|   |       |   `-- r0.png
|   |       |-- drawable-xhdpi
|   |       |   |-- ic_action_search.png
|   |       |   `-- ic_launcher.png
|   |       `-- drawable-xxhdpi
|   |           `-- ic_launcher.png
|   `-- resources.ap_
|-- docs
|   |-- Readme.md
|   |-- Readme.md~
|   |-- game.md
|   `-- game.md~
|-- filename.md
|-- gen
|   `-- com
|       `-- digdream
|           `-- androidbreakout
|               |-- BuildConfig.java
|               `-- R.java
|-- ic_launcher-web.png
|-- libs
|   |-- GameShareSDK.jar
|   `-- android-support-v4.jar
|-- lint.xml
|-- proguard-project.txt
|-- project.properties
|-- release
|-- res
|   |-- drawable
|   |   |-- a.png
|   |   |-- b.png
|   |   |-- btn_level_1.xml
|   |   |-- button_prepare_breakout.xml
|   |   |-- create_account_button.xml
|   |   |-- guide_dot_black.png
|   |   |-- guide_dot_white.png
|   |   |-- guide_round.xml
|   |   |-- prepare_btn_bg.xml
|   |   |-- sign_in_button.xml
|   |   |-- sign_in_button_disabled.xml
|   |   `-- sign_in_button_enabled.xml
|   |-- drawable-hdpi
|   |   |-- about.png
|   |   |-- background.png
|   |   |-- bg211.png
|   |   |-- btn_develop.png
|   |   |-- btn_option.png
|   |   |-- btn_prepare.png
|   |   |-- btn_single.png
|   |   |-- btn_two.png
|   |   |-- button_sound_off.png
|   |   |-- button_sound_on.png
|   |   |-- chara1.png
|   |   |-- chara1_block.png
|   |   |-- chara2.png
|   |   |-- chara2_block.png
|   |   |-- chara3.png
|   |   |-- chara3_block.png
|   |   |-- chara4.png
|   |   |-- chara4_block.png
|   |   |-- chara5.png
|   |   |-- chara5_block.png
|   |   |-- chara6.png
|   |   |-- chara6_block.png
|   |   |-- circle_background.png
|   |   |-- eggplant.png
|   |   |-- ic_launcher.png
|   |   |-- icon.png
|   |   |-- levelbackground.png
|   |   |-- levelchoose.png
|   |   |-- num1.png
|   |   |-- num1_lock.png
|   |   |-- num4.png
|   |   |-- option.png
|   |   |-- share_user_icon_1.png
|   |   |-- share_user_icon_2.png
|   |   |-- share_user_icon_3.png
|   |   |-- share_user_icon_4.png
|   |   |-- share_user_icon_5.png
|   |   |-- share_user_icon_6.png
|   |   |-- share_user_icon_7.png
|   |   |-- share_user_icon_8.png
|   |   |-- stage1a.png
|   |   |-- stage1b.png
|   |   |-- stage2a.png
|   |   |-- stage2b.png
|   |   |-- stage3a.png
|   |   |-- stage3b.png
|   |   |-- stage4a.png
|   |   |-- stage4b.png
|   |   |-- stage5a.png
|   |   |-- stage5b.png
|   |   |-- stage6a.png
|   |   |-- stage6b.png
|   |   `-- title.png
|   |-- drawable-ldpi
|   |-- drawable-mdpi
|   |   |-- ic_action_search.png
|   |   `-- ic_launcher.png
|   |-- drawable-nodpi
|   |   |-- item1.png
|   |   |-- item2.png
|   |   |-- item3.png
|   |   |-- item4.png
|   |   |-- item5.png
|   |   |-- item6.png
|   |   `-- r0.png
|   |-- drawable-xhdpi
|   |   |-- ic_action_search.png
|   |   `-- ic_launcher.png
|   |-- drawable-xxhdpi
|   |   `-- ic_launcher.png
|   |-- layout
|   |   |-- activity_about.xml
|   |   |-- activity_app.xml
|   |   |-- activity_game.xml
|   |   |-- activity_levelchoose.xml
|   |   |-- activity_main.xml
|   |   |-- activity_option.xml
|   |   |-- activity_result.xml
|   |   |-- activity_stage.xml
|   |   |-- activity_test.xml
|   |   |-- mainswitch.xml
|   |   |-- splash.xml
|   |   |-- stage_switch1.xml
|   |   |-- stage_switch2.xml
|   |   |-- stage_switch3.xml
|   |   |-- stage_switch4.xml
|   |   |-- stage_switch5.xml
|   |   `-- stage_switch6.xml
|   |-- raw
|   |   |-- block.ogg
|   |   |-- bottom.ogg
|   |   `-- paddle.ogg
|   |-- values
|   |   |-- colors.xml
|   |   |-- dimens.xml
|   |   |-- strings.xml
|   |   `-- styles.xml
|   |-- values-large
|   |   `-- dimens.xml
|   |-- values-v11
|   |   `-- styles.xml
|   `-- values-v14
|       `-- styles.xml
`-- src
    `-- com
        `-- digdream
            `-- androidbreakout
                |-- Global.java
                |-- data
                |   `-- UserPreferences.java
                |-- game
                |   |-- oneplayer
                |   |   |-- Ball.java
                |   |   |-- BitmapBlock.java
                |   |   |-- Block.java
                |   |   |-- Breakout.java
                |   |   |-- GameView.java
                |   |   |-- Item.java
                |   |   |-- Paddle.java
                |   |   `-- StageData.java
                |   |-- onetwoplayer
                |   |   |-- Ball.java
                |   |   |-- Block.java
                |   |   |-- Breakout.java
                |   |   |-- GameView2p.java
                |   |   |-- Paddle.java
                |   |   |-- TopPaddle.java
                |   |   `-- TwoDataStage.java
                |   `-- twoplayer
                |       |-- Ball.java
                |       |-- Ball2p.java
                |       |-- Block.java
                |       |-- Breakout2p.java
                |       |-- GameView2p.java
                |       |-- Paddle.java
                |       |-- TopPaddle.java
                |       `-- TwoStageData.java
                |-- module
                |   |-- GameData.java
                |   `-- GameMessages.java
                |-- switchlayout
                |   |-- OnViewChangeListener.java
                |   `-- SwitchLayout.java
                |-- ui
                |   |-- AboutActivity.java
                |   |-- AppActivity.java
                |   |-- BaseActivity.java
                |   |-- LevelChooseActivity.java
                |   |-- OptionActivity.java
                |   |-- ResultActivity.java
                |   |-- ShopActivity.java
                |   |-- StageActivity.java
                |   |-- SwitchActivity.java
                |   |-- TestActivity.java
                |   `-- stage
                |       `-- StageSwitch1Activity.java
                `-- utils
                    `-- UserIconUtils.java

60 directories, 317 files
