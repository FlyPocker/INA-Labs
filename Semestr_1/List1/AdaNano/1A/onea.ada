with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;



procedure Onea is
   a, b, c : Integer;
begin
   Put("Podaj pierwsza liczbe: ");
   Get(a);
   Put("Podaj druga liczbe: ");
   Get(b);
   while b /= 0 loop
	c := b;
	b := a mod c;
	a := c;
   end loop;
   Put_Line("NWD podanych liczb to: " & Integer'Image(a));
end Onea;
