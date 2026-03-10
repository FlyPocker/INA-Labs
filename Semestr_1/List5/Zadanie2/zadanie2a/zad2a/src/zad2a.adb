with Ada.Text_IO; use Ada.Text_IO;
with Ada.Long_Float_Text_IO; use Ada.Long_Float_Text_IO;
with Ada.Numerics.Long_Elementary_Functions; use Ada.Numerics.Long_Elementary_Functions;

procedure Zad2a is
   type FuncType is access function (x : Long_Float) return Long_Float;
   
   function FindZero (f : FuncType; a_start,b_start,eps : Long_Float) return Long_Float is
      c : Long_Float;
      a : Long_Float := a_start;
      b : Long_Float := b_start;
   begin
      while (b-a)/2.0 > eps loop
         c := a+((b-a)/2.0);
         if f(c) = 0.0 then
            return c;
         end if;
         if f(a)*f(c)<0.0 then
            b := c;
         else
            a := c;
         end if;
      end loop;
      return a+((b-a)/2.0);
   end FindZero;

   function CosFunction (x : Long_Float) return Long_Float is
   begin
      return Cos (x/2.0);
   end CosFunction;
   a,b,eps,res : Long_Float;
begin
   a := 2.0;
   b := 4.0;
   eps := 0.1;
   for i in 1..8 loop
      res := FindZero(CosFunction'Access, a, b, eps);
      Put("Dla eps=");
      Put(eps, Fore => 1, Aft => 0, Exp => 2);
      Put(" , oszacowane miejsce zerowe cos(x/2) to: ");
      Put(res, Fore => 1, Aft => 10, Exp => 0);
      New_Line;
      eps := eps/10.0;
      
   end loop;

end Zad2a;
