with Ada.Text_IO; use Ada.Text_IO;
with Ada.Unchecked_Deallocation;
with Ada.Command_Line; use Ada.Command_Line;
with Ada.IO_Exceptions; use Ada.IO_Exceptions;

procedure Zad1a is

   type Primary is array (Positive range <>) of Boolean;
   type Primary_Ptr is access Primary;
   procedure Free is
      new Standard.Ada.Unchecked_Deallocation(Primary,Primary_Ptr);

   procedure Sieve (P : Primary_Ptr; n : Natural) is
      j : Natural;
      i : Natural;
   begin
      P.all := (others => True);
      i := 2;
      while i*i <= n loop
         if P(i) then
            j := i*i;
            while j <= n loop
               P(j) := False;
               j := j+i;
            end loop;
         end if;
         i := i+1;
      end loop;
   end Sieve;
   function countPrimes (P : Primary_Ptr; n : Natural) return Natural is
      count : Natural;
   begin
      count := 0;
      for i in 2 .. n loop
         if P(i) then
            count := count+1;
         end if;
      end loop;
      return count;
   end countPrimes;

   n : Natural;
   P : Primary_Ptr;
begin
   if Argument_Count /= 1 then
      Put_Line ("Zla liczba argumentow");
   end if;
   begin
      n := Natural'Value(Argument(1));
   exception
      when Constraint_Error =>
         Put_Line ("Zly typ zmiennej");
         return;
   end;
   P := new Primary(2 .. n);
   Sieve (P, n);
   Put_Line("Liczb pierwszych nie wikeszych od" & Integer'Image(n) &" jest" & Integer'Image(countPrimes(P, n)));
   Free (P);
end Zad1a;
