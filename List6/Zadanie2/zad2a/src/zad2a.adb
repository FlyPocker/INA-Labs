with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Command_Line; use Ada.Command_Line;
with Ada.Unchecked_Deallocation;

procedure Zad2a is
   type Bool_Arr is array (Integer range <>) of Boolean;
   type Int_Arr is array (Integer range <>) of Integer;
   type Bool_Ptr is access Bool_Arr;
   type Int_Ptr is access Int_Arr;

   procedure FreeB is new Ada.Unchecked_Deallocation(Bool_Arr, Bool_Ptr);
   procedure FreeI is new Ada.Unchecked_Deallocation(Int_Arr, Int_Ptr);

   N : Integer;
   Count : Integer := 0;
   Tab : Int_Ptr;
   Bije_Wiersz : Bool_Ptr;
   Bije_Przek1 : Bool_Ptr;
   Bije_Przek2 : Bool_Ptr;

   procedure Ustaw(i : Integer) is
   begin
      for j in 1 .. N loop
         if not Bije_Wiersz(j) and not Bije_Przek1(i + j) and not Bije_Przek2(i - j) then
            Tab(i) := j;
            Bije_Wiersz(j) := True;
            Bije_Przek1(i + j) := True;
            Bije_Przek2(i - j) := True;

            if i < N then
               Ustaw(i + 1);
            else
               Count := Count + 1;
               for k in 1 .. N loop
                  Put(Tab(k), 2);
               end loop;
               New_Line;
            end if;

            Bije_Wiersz(j) := False;
            Bije_Przek1(i + j) := False;
            Bije_Przek2(i - j) := False;
         end if;
      end loop;
   end Ustaw;

begin
   if Argument_Count /= 1 then
      Put_Line("Zla liczba argumentow");
      return;
   end if;

   N := Integer'Value(Argument(1));
   
   Tab := new Int_Arr(1 .. N);
   Bije_Wiersz := new Bool_Arr(1 .. N);
   Bije_Przek1 := new Bool_Arr(2 .. 2 * N);
   Bije_Przek2 := new Bool_Arr(-N + 1 .. N - 1);

   Bije_Wiersz.all := (others => False);
   Bije_Przek1.all := (others => False);
   Bije_Przek2.all := (others => False);

   Ustaw(1);

   Put_Line("Liczba rozwiazan: " & Integer'Image(Count));

   FreeI(Tab);
   FreeB(Bije_Wiersz);
   FreeB(Bije_Przek1);
   FreeB(Bije_Przek2);
end Zad2a;