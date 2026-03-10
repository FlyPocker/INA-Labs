with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Unchecked_Deallocation;

procedure Zad3a is
   type CodeType is array (1 .. 4) of Integer;
   type AllCodesArray is array (1 .. 1296) of CodeType;
   type PossibleFlagsArray is array (1 .. 1296) of Boolean;
   
   type AllCodesPtr is access AllCodesArray;
   type PossibleFlagsPtr is access PossibleFlagsArray;

   procedure FreeCodes is new Ada.Unchecked_Deallocation(AllCodesArray, AllCodesPtr);
   procedure FreeFlags is new Ada.Unchecked_Deallocation(PossibleFlagsArray, PossibleFlagsPtr);

   procedure EvaluateFeedback(TrialCode, SecretCode : CodeType; CountOnPlace, CountOffPlace : out Integer) is
      UsedSecret, UsedTrial : array(1..4) of Boolean := (others => False);
   begin
      CountOnPlace := 0; 
      CountOffPlace := 0;
      
      for I in 1..4 loop
         if TrialCode(I) = SecretCode(I) then
            CountOnPlace := CountOnPlace + 1;
            UsedSecret(I) := True; 
            UsedTrial(I) := True;
         end if;
      end loop;
      
      for I in 1..4 loop
         if not UsedTrial(I) then
            for J in 1..4 loop
               if not UsedSecret(J) and then TrialCode(I) = SecretCode(J) then
                  CountOffPlace := CountOffPlace + 1;
                  UsedSecret(J) := True;
                  exit;
               end if;
            end loop;
         end if;
      end loop;
   end EvaluateFeedback;

   AllPossibleCodes : AllCodesPtr := new AllCodesArray;
   IsCodePossible : PossibleFlagsPtr := new PossibleFlagsArray;
   CountOnPlace, CountOffPlace : Integer;
   CurrentCodeIndex : Integer := 1;
begin
   for I in 1..6 loop
      for J in 1..6 loop
         for K in 1..6 loop
            for L in 1..6 loop
               AllPossibleCodes(CurrentCodeIndex) := (I, J, K, L);
               IsCodePossible(CurrentCodeIndex) := True;
               CurrentCodeIndex := CurrentCodeIndex + 1;
            end loop;
         end loop;
      end loop;
   end loop;

   for CurrentTurn in 1 .. 8 loop
      CurrentCodeIndex := 0;
      for I in 1..1296 loop
         if IsCodePossible(I) then 
            CurrentCodeIndex := I; 
            exit; 
         end if;
      end loop;

      if CurrentCodeIndex = 0 then
         Put_Line("Oszukujesz!");
         return;
      end if;

      Put(CurrentTurn, 1); Put(": ");
      for I in 1..4 loop 
         Put(AllPossibleCodes(CurrentCodeIndex)(I), 1); 
      end loop; 
      Put(" ?"); New_Line;

      Put("Na swoim miejscu: "); Get(CountOnPlace);
      Put("Nie na swoim miejscu: "); Get(CountOffPlace);

      if CountOnPlace = 4 then
         Put_Line("Wygralem.");
         exit;
      end if;

      for I in 1..1296 loop
         if IsCodePossible(I) then
            declare
               TempOnPlace, TempOffPlace : Integer;
            begin
               EvaluateFeedback(AllPossibleCodes(CurrentCodeIndex), AllPossibleCodes(I), TempOnPlace, TempOffPlace);
               if TempOnPlace /= CountOnPlace or TempOffPlace /= CountOffPlace then
                  IsCodePossible(I) := False;
               end if;
            end;
         end if;
      end loop;
   end loop;

   FreeCodes(AllPossibleCodes); 
   FreeFlags(IsCodePossible);
end Zad3a;