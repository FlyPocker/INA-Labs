with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Numerics; use Ada.Numerics;
with Ada.Numerics.Elementary_Functions; use Ada.Numerics.Elementary_Functions;
with Ada.Strings.Fixed; use Ada.Strings.Fixed;
with Ada.Strings; use Ada.Strings;
procedure Zad3a is
   procedure rozkladCzynnikow(a : in Integer) is
      count, n, i : Integer;
      even : Boolean;
   begin
      n := a;
      count := 0;
      even := False;
      while n mod 2 = 0 loop
         count := count + 1;
         n := n/2;
      end loop;
      if count > 0 then
         even := True;
         Put("2^" & Trim(Integer'Image(count), Left));
      end if;
      i := 3;
      while i*i <= n loop
         count := 0;
         while n mod i = 0 loop
            count := count + 1;
            n := n/i;
         end loop;
         if count >0 then
            if even then
               Put("*" & Trim(Integer'Image(i),Left) & "^" & Trim(Integer'Image(count),Left));
            else
               even := True;

               Put(Trim(Integer'Image(i),Left) & "^" & Trim(Integer'Image(count),Left));
            end if;
         end if;
         i := i+2;
      end loop;
      if n > 1 then
         if even = True then
            Put("*" & Trim(Integer'Image(n),Left) & "^1");
         else
            Put(Trim(Integer'Image(n),Left) & "^1");
         end if;
      end if;
   end rozkladCzynnikow;
   a : Integer;
begin
   Put("Podaj liczbe: ");
   Get(a);
   rozkladCzynnikow(a);
end Zad3a;