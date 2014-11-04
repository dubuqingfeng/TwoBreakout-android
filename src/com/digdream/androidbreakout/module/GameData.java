package com.digdream.androidbreakout.module;

import java.util.Arrays;
import java.util.Random;

public class GameData {
    public static final int BLOCK_ROW_COUNT = 50;
    public static final int BLOCK_COLUMN_COUNT = 4;
    public static final int BLOCK_ROW_COUNT_IN_SCREEN = 5;

    public static final int BLOCK_NO_EGGPLANT = 0;
    public static final int BLOCK_HAS_EGGPLANT = 1;
    public static final int BLOCK_PICKED_EGGPLANT = 2;
    public static final int BLOCK_PICKED_NONE = 3;

    public int[] mBlockDatas = null;
    
    public static final int levels = 3;
    // Local user related game data
    public int mMode = 1;
    public boolean mIsInviter = true;
    public int mLocalStep = 0;
    public long mLocalSpentTime = 0;
    public boolean mIsLocalCompleted = false;
    public boolean mIsLocalPrepared = false;

    // Remote user related game data
    public int mRemoteStep = 0;
    public long mRemoteSpentTime = 0;
    public boolean mIsRemotePrepared = false;
    public boolean mIsRemoteCompleted = false;

    private static GameData sInstance = null;

    private GameData(int mode, boolean isInviter) {
        mMode = mode;
        mIsInviter = isInviter;
        reset();
    }

    public static GameData getGameData() {
        return sInstance;
    }

    public static GameData createGameData(int mode, boolean isInviter) {
        GameData gameData = new GameData(mode, isInviter);
        sInstance = gameData;
        return gameData;
    }

    public void generateGameData(int rowCount, int columnCount) {
        mBlockDatas = generateBlockDatas(rowCount, columnCount);
        reset();
    }

    public void generateGameData(Float values) {
        
        reset();
    }

    public void setLocalCompleted(long spentTime) {
        mLocalStep = BLOCK_ROW_COUNT;
        mLocalSpentTime = spentTime;
        mIsLocalCompleted = true;
        mIsLocalPrepared = false;
    }

    public void setRemoteCompleted(long spentTime) {
        mRemoteStep = BLOCK_ROW_COUNT;
        mRemoteSpentTime = spentTime;
        mIsRemoteCompleted = true;
        mIsRemotePrepared = false;
    }

    public boolean isAllCompleted() {
        return (mIsLocalCompleted && mIsRemoteCompleted);
    }

    public int getTotalStep() {
        return mBlockDatas.length / BLOCK_COLUMN_COUNT;
    }

    private void reset() {
        mLocalStep = 0;
        mLocalSpentTime = 0;
        mIsLocalPrepared = false;
        mIsLocalCompleted = false;
        mRemoteStep = 0;
        mRemoteSpentTime = 0;
        mIsRemotePrepared = false;
        mIsRemoteCompleted = false;
    }

    private static int[] generateBlockDatas(int rowCount, int columnCount) {
        int[] blockDatas = new int[rowCount * columnCount];
        Random random = new Random();

        Arrays.fill(blockDatas, BLOCK_NO_EGGPLANT);
        for (int i = 0; i < rowCount; i++) {
            int blockIndex = random.nextInt(4);
            blockDatas[i * columnCount + blockIndex] = BLOCK_HAS_EGGPLANT;
        }
        return blockDatas;
    }

    public static int[] string2Array(String values) {
        if (values == null || values.length() < 1)
            return null;

        try {
            int[] blockDatas = new int[values.length()];
            for (int i = 0; i < values.length(); i++) {
                blockDatas[i] = Integer.parseInt(values.charAt(i) + "");
            }
            return blockDatas;
        } catch (Exception e) {
            return null;
        }
    }

    public static String array2String(int level) {
        
          String  value = String.valueOf(level);
        return value;
    }
}
