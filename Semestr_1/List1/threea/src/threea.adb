with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;

procedure Threea is
   n : Integer;
   p : Integer;
   m : Integer;
   t : Integer;
begin
   Put_Line ("Podaj liczbe n: ");
   Get (n);
   Put_Line ("Podaj podstawe systemu p: ");
   Get (p);
   m := 0;
   t := n;
   while t>0 loop
      m := (m*p) + (t mod p);
      t := t / p;
   end loop;   
   if m = n then
      Put_Line ("Liczba "& n'Image & " jest palindromem w systemie "& p'Image);
   else
      Put_Line ("Liczba "& n'Image & " NIE jest palindromem w systemie "& p'Image);
   end if;
end Threea;
