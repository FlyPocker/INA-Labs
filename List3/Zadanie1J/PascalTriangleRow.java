public class PascalTriangleRow {
    private int[] row;

    private void solveRow(int m){
        row[0]=1;
        row[m]=1;
        for (int i=1;i<m;i++){
            row[i]=(row[i-1]*(m-i+1))/i;
        }
    }

    public int getBinom(int m){
        return row[m];
    }

    public PascalTriangleRow(int n){
        row = new int[n+1];         
        solveRow(n);
    }
}
