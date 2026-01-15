with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Unchecked_Deallocation;
with Ada.Command_Line; use Ada.Command_Line;
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;
with Ada.Strings.Unbounded.Text_IO; use Ada.Strings.Unbounded.Text_IO;

procedure Zadanie1a is
   type Arr is array (Natural range <>) of Integer;
   type Arr_Ptr is access Arr;
   procedure Free is new Ada.Unchecked_Deallocation(Arr, Arr_Ptr);

   procedure CalculateChange(C, U : out Arr_Ptr; d : Arr_Ptr; C_size, d_size : Integer) is 
      d_min, C_min, Temp : Integer; -- d_min-indeks nominalu dla najmniejszego wyniku C_min-najmnijeszy wynik
   begin
      for i in 1 .. C_size loop
         C_min := -1;
         d_min := 0;
         for j in 1 .. d_size loop
            if d(j) <= i then
               Temp := C(i-d(j))+1;
               if Temp /= 0 and (Temp < C_min or C_min = -1) then
                  C_min := Temp;
                  d_min := j;
               end if;
            else
               exit;
            end if;
         end loop;
         C(i) := C_min;
         U(i) := d_min;
      end loop;
   end CalculateChange;

   Cost, Used, Denom, Denom_Count : Arr_Ptr;
   MaxChange, Temp, D_size : Integer;
   InFile : File_Type;
begin

   if Argument_Count < 2 then
      Put_Line("Zla liczba argumentow");
      return;
   end if;
   Open(InFile, In_File, Argument(1));
   Get(InFile, D_size);
   Denom := new Arr(1 .. D_size);
   Denom_Count := new Arr(1 .. D_size);
   Denom_Count.all := (others => 0);
   for i in 1 .. D_size loop
      Get(InFile, Temp);
      Denom(i) := Temp;
   end loop;
   Close(InFile);   
   MaxChange := 0;
   for i in 2 .. Argument_Count loop
      Temp := Integer'Value(Argument(i));
      if MaxChange < Temp then
         MaxChange := Temp;
      end if;
   end loop;

   Cost := new Arr(0 .. MaxChange);
   Cost.all := (others => -1);
   Cost(0) := 0;
   Used := new Arr(0 .. MaxChange);
   Used.all := (others => 0);
   
   CalculateChange (Cost, Used, Denom, MaxChange, D_size);

   for i in 2 .. Argument_Count loop
      Temp := Integer'Value(Argument(i));
      if Cost(Temp) = -1 then
         Put_Line(Argument(i) & " ==> No solution!");
      else
         Put_Line(Argument(i) & " ==>" & Integer'Image(Cost(Temp)));
         while Temp /= 0 loop
            Denom_Count(Used(Temp)) := Denom_Count(Used(Temp))+1;
            Temp := Temp-Denom(Used(Temp));
         end loop;
         for i in 1 .. D_size loop
            if Denom_Count(i) /= 0 then
               Put_Line(Integer'Image(Denom_Count(i)) & " x" & Integer'Image(Denom(i)));
               Denom_Count(i) := 0;
            end if;
         end loop;
      end if;
   end loop;

   Free(Denom_Count);
   Free(Cost);
   Free(Used);
   Free(Denom);
end Zadanie1a;
