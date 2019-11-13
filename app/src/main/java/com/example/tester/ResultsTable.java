package com.example.tester;

public class ResultsTable {

    final int[][] results = new int[24][6];

    public void fillResults() {
        for (int i = 0; i < 24; i ++) {
            for (int j = 0; j < 6; j++) {
                results[i][j] = 0;
            }
        }
        results[0][0] = 1;
        results[0][3] = 2;
        results[0][5] = 3;
        results[1][1] = 1;
        results[1][3] = 2;
        results[1][4] = 3;
        results[2][0] = 1;
        results[2][1] = 2;
        results[2][3] = 3;
        results[3][2] = 1;
        results[3][4] = 2;
        results[3][5] = 3;
        results[4][0] = 1;
        results[4][1] = 2;
        results[4][2] = 3;
        results[5][0] = 1;
        results[5][1] = 2;
        results[5][5] = 3;
        results[6][1] = 1;
        results[6][2] = 2;
        results[6][3] = 3;
        results[7][0] = 1;
        results[7][4] = 2;
        results[7][5] = 3;
        results[8][1] = 1;
        results[8][3] = 2;
        results[8][4] = 3;
        results[9][3] = 1;
        results[9][4] = 2;
        results[9][5] = 3;
        results[10][0] = 1;
        results[10][1] = 2;
        results[10][2] = 3;
        results[11][2] = 1;
        results[11][3] = 2;
        results[11][4] = 3;
        results[12][0] = 1;
        results[12][4] = 2;
        results[12][5] = 3;
        results[13][1] = 1;
        results[13][3] = 2;
        results[13][4] = 3;
        results[14][0] = 1;
        results[14][2] = 2;
        results[14][4] = 3;
        results[15][0] = 1;
        results[15][2] = 2;
        results[15][5] = 3;
        results[16][3] = 1;
        results[16][4] = 2;
        results[16][5] = 3;
        results[17][0] = 1;
        results[17][1] = 2;
        results[17][2] = 3;
        results[18][2] = 1;
        results[18][4] = 2;
        results[18][5] = 3;
        results[19][0] = 1;
        results[19][2] = 2;
        results[19][5] = 3;
        results[20][1] = 1;
        results[20][2] = 2;
        results[20][3] = 3;
        results[21][1] = 1;
        results[21][2] = 2;
        results[21][3] = 3;
        results[22][1] = 1;
        results[22][3] = 2;
        results[22][5] = 3;
        results[23][0] = 1;
        results[23][4] = 2;
        results[23][5] = 3;

    }

    public int getResults(int i, int n) {
        return results[i][n];
    }
}
