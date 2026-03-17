public class zadanie3j {
    public static int div(int a){
        int najwiekszyDzielnik = 1;
        for (int j=2; j*j<=a;j++){
            if (a % j ==0){
                najwiekszyDzielnik = a/j;
                break;
            }
        }
        return najwiekszyDzielnik;
    }

 public static void main(String[] args){
    int n = 0;
    for (int i=0; i<args.length; i++){
        try {n=Integer.parseInt(args[i]);
            System.out.println(n + " -> " + div(Math.abs(n)));
        }
        catch (NumberFormatException ex) {
        System.out.println(args[i] + " nie jest liczba calkowita");
        }
        
    }
 }
}
