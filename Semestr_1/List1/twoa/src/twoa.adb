with Ada.Text_IO; use Ada.Text_IO;
with Ada.Float_Text_IO; use Ada.Float_Text_IO;
with Ada.Numerics.Elementary_Functions; use Ada.Numerics.Elementary_Functions;

procedure Twoa is
   a    : Float;
   b    : Float;
   c    : Float;
   delt : Float;
   x1   : Float;
   x2   : Float;
begin
   Put("Podaj wspolczynnik przy x^2: ");
   Get(a);
   Put("Podaj wspolczynnik przy x: ");
   Get(b);
   Put("Podaj wyraz wolny: ");
   Get(c);
   if a = 0.0 then
      Put_Line ("To nie jest rownanie kwadratowe");
   else
      delt := b * b - 4.0 * a * c;
      if delt < 0.0 then
         Put_Line ("Brak rozwiazan w zbiorze liczb rzeczywistych");
      elsif delt = 0.0 then
         x1 := -b / (2.0 * a);
         Put_Line ("Jest jedno rozwiazanie: " & x1'Image);
      else
         x1 := (-b - Sqrt(delt)) / (2.0 * a);
         x2 := (-b + Sqrt(delt)) / (2.0 * a);
         Put_Line ("Sa dwa rozwiazania: " & x1'Image & " oraz " & x2'Image);
      end if;
   end if;
end Twoa;