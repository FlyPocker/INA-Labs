with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;

procedure Zad1a is
   a, b, c : Integer;
   function NWD(x : in Integer; y : in Integer) return Integer is
      a, b, c : Integer;
   begin
      a := x;
      b := y;
      while b /= 0 loop
      c := b;
      b := a mod c;
      a := c;
      end loop;
      return a;
   end NWD;
begin
   Put("Podaj pierwsza liczbe: ");
   Get(a);
   Put("Podaj druga liczbe: ");
   Get(b);
   c := NWD(a,b);
   Put_Line("NWD podanych liczb to: " & Integer'Image(c));
end Zad1a;