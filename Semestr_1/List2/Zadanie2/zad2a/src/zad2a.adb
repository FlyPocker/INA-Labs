with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Numerics; use Ada.Numerics;
with Ada.Numerics.Elementary_Functions; use Ada.Numerics.Elementary_Functions;

procedure Zad2a is
   function isPrime(a:Integer) return Boolean is
      n, i : Integer;
   begin
      n := a;
      if n <= 1 then
         return False;
      end if;
      if n = 2 then
         return True;
      end if;
      if n mod 2 = 0 then
         return False;
      end if;
      i := 3;
      while i*i<= n loop
         if n mod i = 0 then
            return False;
         end if;
         i := i+2;
      end loop;
      return True;
   end isPrime;
   a : Integer;
begin
   Put("Podaj liczbe: ");
   Get(a);
   if isPrime(a) then
      Put_Line("Liczba" & Integer'Image(a) & " jest liczba pierwsza");
   else
      Put_Line("Liczba" & Integer'Image(a) & " NIE jest pierwsza");
   end if;
end Zad2a;
