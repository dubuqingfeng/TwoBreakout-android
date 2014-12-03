package com.digdream.androidbreakout.game.oneplayer;
/**
 * 关卡数据类
 * @author user
 *
 */
public class StageData {
	public static int[][] GameDataArray = new int [10][10];
	public StageData(int stage){
		switch(stage){
			case 0:
				GameDataArray[0][0] = 2;
				GameDataArray[0][1] = 2;
				GameDataArray[0][2] = 2;
				GameDataArray[0][3] = 2;
				GameDataArray[0][4] = 2;
				GameDataArray[0][5] = 2;
				GameDataArray[0][6] = 2;
				GameDataArray[0][7] = 2;
				GameDataArray[0][8] = 2;
				GameDataArray[0][9] = 2;
				GameDataArray[1][0] = 2;
				GameDataArray[1][1] = 2;
				GameDataArray[1][2] = 2;
				GameDataArray[1][3] = 2;
				GameDataArray[1][4] = 2;
				GameDataArray[1][6] = 2;
				GameDataArray[1][7] = 2;
				GameDataArray[1][8] = 2;
				GameDataArray[1][9] = 2;
				GameDataArray[2][2] = 2;
				GameDataArray[2][3] = 2;
				GameDataArray[2][4] = 2;
				GameDataArray[2][6] = 2;
				GameDataArray[2][7] = 2;
				GameDataArray[2][8] = 2;
				GameDataArray[2][9] = 2;
				GameDataArray[2][1] = 2;
				GameDataArray[3][2] = 2;
				GameDataArray[4][3] = 2;
				break;
			case 1:
				GameDataArray[0][0] = 2;
				GameDataArray[0][1] = 2;
				GameDataArray[0][2] = 2;
				GameDataArray[0][3] = 2;
				GameDataArray[0][4] = 2;
				GameDataArray[0][5] = 2;
				GameDataArray[0][6] = 2;
				GameDataArray[0][7] = 2;
				GameDataArray[0][8] = 2;
				GameDataArray[0][9] = 2;
				GameDataArray[1][0] = 2;
				GameDataArray[1][1] = 2;
				GameDataArray[1][2] = 2;
				GameDataArray[1][3] = 2;
				GameDataArray[1][4] = 2;
				GameDataArray[1][6] = 2;
				GameDataArray[1][7] = 2;
				GameDataArray[1][8] = 2;
				GameDataArray[1][9] = 2;
				GameDataArray[2][2] = 2;
				GameDataArray[2][3] = 2;
				GameDataArray[2][4] = 2;
				GameDataArray[2][6] = 2;
				GameDataArray[2][7] = 2;
				GameDataArray[2][8] = 2;
				GameDataArray[2][9] = 2;
				GameDataArray[2][1] = 2;
				GameDataArray[3][2] = 2;
				GameDataArray[4][3] = 2;
				break;
			case 2:
				GameDataArray[1][0] = 1;
				GameDataArray[1][1] = 1;
				GameDataArray[1][2] = 1;
				GameDataArray[1][3] = 1;
				GameDataArray[1][4] = 1;
				GameDataArray[1][6] = 1;
				GameDataArray[1][7] = 1;
				GameDataArray[1][8] = 1;
				GameDataArray[1][9] = 1;
				GameDataArray[2][2] = 1;
				GameDataArray[2][3] = 1;
				GameDataArray[2][4] = 1;
				GameDataArray[2][6] = 1;
				GameDataArray[2][7] = 1;
				GameDataArray[2][8] = 1;
				GameDataArray[2][9] = 1;
				GameDataArray[2][1] = 1;
				GameDataArray[3][2] = 1;
				GameDataArray[4][3] = 1;
				GameDataArray[5][6] = 1;
				GameDataArray[4][8] = 1;
				GameDataArray[6][2] = 1;
				GameDataArray[7][1] = 1;
				GameDataArray[8][2] = 1;
				break;
			case 3:
				break;
				
		}
	}
}
