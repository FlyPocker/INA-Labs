public class zadanie1j {
    public static void main(String[] args){
    if (args.length < 2){
        System.out.println("Zła liczba danych");
        return;
    }
    int n;
    PrimeNumbers primes;
    try{
        n=Integer.parseInt(args[0]);
        if (n<0){
            System.out.println(args[0] + " nieprawidłowa dana, nie może być ujemna");    
            return;
        }
        primes=new PrimeNumbers(n);
    }
    catch (NumberFormatException ex){
        System.out.println(args[0] + " nieprawidłowa dana");
        return;
    }
    primes.sievePrime();
    for (int i=1; i<args.length; i++){
        try {
            n=Integer.parseInt(args[i]);
            if (primes.getNumber(n) >1){
                System.out.println(n + " -> " + primes.getNumber(n));
            }else{
                System.out.println(n + " -> " + "liczba spoza zakresu");
            }
        }
        catch (NumberFormatException ex) {
        System.out.println(args[i] + " nieprawidłowa dana");
        }
        
    }
    }
}
