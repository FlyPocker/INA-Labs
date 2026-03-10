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
   
   begin
      Open(InFile, In_File, Argument(1));
   exception
      when Name_Error =>
         Put_Line("Plik nie istnieje");
         return;
   end;
   begin
      if End_Of_File(InFile) then
         Put_Line("Plik jest pusty");
         return;
      end if;
   Get(InFile, D_size);
   if D_size <= 0 then
      Put_Line("Liczba nominalow musi byc dodatnia");
      Close(InFile);
      return;
   end if;

   Denom := new Arr(1 .. D_size);
   Denom_Count := new Arr(1 .. D_size);
   Denom_Count.all := (others => 0);
   
   for i in 1 .. D_size loop
      if End_Of_File(InFile) then
         raise End_Error;
      end if;
      Get(InFile, Temp);
      if Temp <= 0 then
         Put_Line("Nominal musi byc dodatni");
         Close(InFile);
         goto Cleanup;
      end if;
      Denom(i) := Temp;
   end loop;
   Close(InFile);

   exception
      when Data_Error =>
         Put_Line("Zly typ zmiennych w pliku");
         goto Cleanup;
      when End_Error =>
         Put_Line("Zla ilosc nominalow w pliku");
         goto Cleanup;
   end;

   MaxChange := 0;
   for i in 2 .. Argument_Count loop
      begin
         Temp := Integer'Value(Argument(i));
         if MaxChange < Temp then
            MaxChange := Temp;
         end if;
         if Temp < 0 then
            Put_Line("Zly typ argumentu");
            goto Cleanup;
         end if;
      exception
         when Constraint_Error =>
            Put_Line("Zly typ argumentu");
            goto Cleanup;
      end;
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
   <<Cleanup>>
   if Is_Open(InFile) then Close(InFile); end if;
   if Denom_Count /= null then Free(Denom_Count); end if;
   if Denom /= null then Free(Denom); end if;
   if Cost /= null then Free(Cost); end if;
   if Used /= null then Free(Used); end if;

end Zadanie1a;
