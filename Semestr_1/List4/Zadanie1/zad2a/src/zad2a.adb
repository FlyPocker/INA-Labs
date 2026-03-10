with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Command_Line; use Ada.Command_Line;
with Ada.Unchecked_Deallocation;

procedure Zad1a is
   type Arr is array (Positive range <>) of Integer;
   type Arr_Ptr is access Arr;
   
   procedure Free is new Ada.Unchecked_Deallocation(Arr, Arr_Ptr);

   function Is_Valid(Tab : Arr_Ptr; N : Integer) return Boolean is
   begin
      for i in 1 .. N loop
         for j in i + 1 .. N loop
            if abs(i - j) = abs(Tab(i) - Tab(j)) then [cite: 339]
               return False;
            end if;
         end loop;
      end loop;
      return True;
   end Is_Valid;

   function Next_Permutation(Tab : Arr_Ptr; N : Integer) return Boolean is
      i, j, k : Integer;
      Temp : Integer;
   begin
      i := N - 1;
      while i > 0 and then Tab(i) >= Tab(i + 1) loop
         i := i - 1;
      end loop;
      
      if i = 0 then 
         return False; 
      end if;

      j := N;
      while Tab(j) <= Tab(i) loop
         j := j - 1;
      end loop;

      Temp := Tab(i); 
      Tab(i) := Tab(j); 
      Tab(j) := Temp;
      
      k := i + 1;
      j := N;
      while k < j loop
         Temp := Tab(k); 
         Tab(k) := Tab(j); 
         Tab(j) := Temp;
         k := k + 1; 
         j := j - 1;
      end loop;
      return True;
   end Next_Permutation;

   N : Integer;
   Tab : Arr_Ptr;
   Count : Integer := 0;
begin
   if Argument_Count /= 1 then [cite: 368]
      Put_Line("Zla liczba argumentow. Podaj n.");
      return;
   end if;

   begin
      N := Integer'Value(Argument(1)); [cite: 368]
   exception
      when Constraint_Error =>
         Put_Line("Zly typ zmiennej");
         return;
   end;

   if N < 1 then
      Put_Line("Liczba rozwiazan: 0");
      return;
   end if;

   Tab := new Arr(1 .. N);
   for i in 1 .. N loop 
      Tab(i) := i; 
   end loop;

   loop
      if Is_Valid(Tab, N) then [cite: 342, 367]
         Count := Count + 1; [cite: 368]
         for i in 1 .. N loop 
            Put(Tab(i), 2); 
         end loop;
         New_Line;
      end if;
      exit when not Next_Permutation(Tab, N); [cite: 342, 367]
   end loop;

   Put_Line("Number of solutions: " & Integer'Image(Count)); [cite: 368, 378]
   Free(Tab);
end Zad1a;