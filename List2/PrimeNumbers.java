public class PrimeNumbers {

    private boolean[] isPrime;

    public int getNumber(int m){
        int i = 2;
        while (m>0 & i<isPrime.length - 1){
            i += 1;
            if (isPrime[i]){
                m -= 1;
            }
        }
        if (m==0){
            return i;
        }
        return -1;
    }

    public PrimeNumbers(int n){
        isPrime = new boolean[n+1];
        for (int i=2;i<=n;i++){
            isPrime[i] = true;
        }
        for (int i=2;i<=n;i++){
            if (isPrime[i]){
                for (int j=i+i;j<=n;j=j+i){
                    isPrime[j] = false;
                }
            }
        }
    }
}
