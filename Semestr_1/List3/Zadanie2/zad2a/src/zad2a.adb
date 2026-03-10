with Ada.Text_IO; use Ada.Text_IO;
with Ada.Unchecked_Deallocation;
with Ada.Command_Line; use Ada.Command_Line;

procedure Zad2a is
   
   type Row is array (Natural range <>) of Integer;
   type Row_Ptr is access Row;
   procedure Free is
      new Standard.Ada.Unchecked_Deallocation (Row, Row_Ptr);
   function Binomial (R : Row_Ptr; n : Integer; k : Integer) return Integer is
      d : Integer;
      j : Integer;
   begin
      R.all := (others => 1);
      for i in 1 .. n loop
         d := k;
         if i <= k then
            d := i-1;
         end if;
         j := d;
         while j > 0 loop
            R(j) := R(j) + R(j-1);
            j := j-1;
         end loop;
      end loop;
      return R(k);
   end Binomial;

   R : Row_Ptr;
   n : Integer;
   k : Integer;

begin
   if Argument_Count /= 2 then
      Put_Line ("Zla liczba argumentow");
      return;
   end if;
   begin
      n := Integer'Value (Argument (1));
      k := Integer'Value (Argument (2));
   exception
      when Constraint_Error =>
         Put_Line ("Zly typ zmiennych");
         return;
   end;
   Put ("Dwumian Newtona" & Integer'Image(n) & " po" & Integer'Image(k) & " to");
   if n-k < k then
      k := n-k;
   end if;
   R := new Row(0 .. k+1);
   Put_Line (Integer'Image(Binomial(R, n, k)));

   Free (R);
end Zad2a;
