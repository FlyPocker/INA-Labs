with Ada.Text_IO; use Ada.Text_IO;
with Primes_Lib;  use Primes_Lib;
with Ada.Command_Line; use Ada.Command_Line;

procedure Zadanie2a is
   n : Natural;
begin
   if Argument_Count < 1 then
      Put_Line ("Za malo argumentow!");
   end if;

   begin
      for i in 1 .. Argument_Count loop
         n := Natural'Value(Argument(i));
         Put_Line ("totient(" & Natural'Image(n) & " ) =" & Natural'Image(Totient(n)));
      end loop;
   exception
      when Constraint_Error =>
         Put_Line ("Zly typ argumentu");
         return;
   end;
end Zadanie2a;