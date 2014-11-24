package com.digdream.androidbreakout.game.oneplayer;
/**
 * 关卡数据类
 * @author user
 *
 */
public class StageData {
	public int[][] GameDataArray = new int [10][10];
	public StageData(int stage){
		switch(stage){
			case 0:
				GameDataArray[0][0] = 1;
				GameDataArray[0][1] = 1;
				GameDataArray[0][2] = 1;
				GameDataArray[0][3] = 1;
				GameDataArray[0][4] = 1;
				GameDataArray[0][5] = 1;
		}
	}
}
