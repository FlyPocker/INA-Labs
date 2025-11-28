with Primes;
with Ada.Text_IO; use Ada.Text_IO;
with Ada.Command_Line; use Ada.Command_Line;

procedure Zad2a is
   n : Natural;

begin
   if Argument_Count /= 2 then
      Put_Line ("Zla liczba argumentow");
      return;
   end if;
   declare
      FunctionType : constant String := Argument(1);
   begin
      n := Natural'Value(Argument(2));
      if FunctionType = "pn" then
         Put_Line (Natural'Image(Primes.PrimeNumbers(n)));
      elsif FunctionType = "pr" then
         Put_Line (Natural'Image(Primes.Prime(n)));
      elsif FunctionType = "ip" then
         Put_Line (Boolean'Image(Primes.IsPrime(n)));
      else
         Put_Line ("Nieznana komenda");
      end if;
   exception
      when Constraint_Error =>
         Put_Line ("Zly typ zmiennej");
         return;
   end;

   
end Zad2a;
