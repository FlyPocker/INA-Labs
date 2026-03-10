public class zadanie3j {
 public static void main(String[] args){
    int n = 0;
    int najwiekszyDzielnik = 1;
    for (int i=0; i<args.length; i++){
        try {n=Integer.parseInt(args[i]);}
        catch (NumberFormatException ex) {
        System.out.println(args[i] + " nie jest liczba calkowita");
        continue;
        }
        najwiekszyDzielnik = 1;
        for (int j=2; j*j<=n;j++){
            if (n % j ==0){
                najwiekszyDzielnik = n/j;
                break;
            }
        }
        System.out.println(n + " -> " + najwiekszyDzielnik);
    }
 }
}
