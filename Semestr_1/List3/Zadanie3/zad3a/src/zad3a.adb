with Ada.Text_IO; use Ada.Text_IO;
with Ada.Unchecked_Deallocation;
with Ada.Command_Line; use Ada.Command_Line;

procedure Zad3a is
   type Primary is array (Positive range <>) of Natural;
   type Primary_Ptr is access Primary;
   procedure Free is
      new Standard.Ada.Unchecked_Deallocation(Primary,Primary_Ptr);

   procedure primesToN(P : Primary_Ptr; n : Natural) is
      k : Natural;
      j : Natural;
      is_Prime : Boolean;
      i : Natural;
   begin
      P(1) := 2;
      k := 3;
      i := 1;
      while i < n loop
         j := 1;
         is_Prime := True;
         while (j <= i) and (P(j)*P(j) <= k) and (is_Prime) loop
            if (k mod P(j)) = 0 then
               is_Prime := False;
            else
               j := j+1;
            end if;
         end loop;
         if is_Prime then
            i := i+1;
            P(i) := k;
         end if;
         k := k+2;
      end loop;
   end primesToN;

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
   P := new Primary(1 .. n);
   primesToN (P, n);
   Put_Line(Natural'Image(n)& "-ta liczba pierwsza to" & Natural'Image(P(n)));
   Free (P);
end Zad3a;