with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;



procedure Onea is
   a, b, c : Integer;
begin
   Put("Podaj pierwsza liczbe: ");
   Get(a);
   Put("Podaj druga liczbe: ");
   Get(b);
   if b > a then
      c := a;
   else
      c := b;
   end if;
   
   while c > 0 loop
      if a mod c = 0 and b mod c = 0 then
         Put_Line("NWD to: " & Integer'Image(c));
         exit;
      else
         c := c-1;
      end if;
   end loop;
end Onea;
