public class zad1j{
    public static void main(String[] args){
        
        if (args.length < 2){
            System.out.println("Zła liczba danych");
            return;
        }
        
        int n;
        PascalTriangleRow triangleRow;
        try{
            n=Integer.parseInt(args[0]);
            if (n<0){
                System.out.println(args[0] + " nieprawidłowa dana, nie może być ujemna");    
                return;
            }
            triangleRow = new PascalTriangleRow(n);
        }
        catch (NumberFormatException ex){
            System.out.println(args[0] + " nieprawidłowa dana");
            return;
        }
        int m;
        for (int i=1; i<args.length; i++){
            try {
                m=Integer.parseInt(args[i]);
                if (0<=m & m<=n){
                    System.out.println(m + " -> " + triangleRow.getBinom(m));
                }else{
                    System.out.println(m + " -> " + "liczba spoza zakresu");
                }
            }
            catch (NumberFormatException ex) {
            System.out.println(args[i] + " nieprawidłowa dana");
            }
            
        }
    }
}