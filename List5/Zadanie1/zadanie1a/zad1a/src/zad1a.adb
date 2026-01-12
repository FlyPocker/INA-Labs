with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with list; use list;

procedure Zad1a is
   L : ListT;
   Running : Boolean := True;
   Buffer : String (1 .. 100);
   Last : Natural;
   R, I : Integer;
   Len : Integer;

begin
   while Running loop
      Put (" Command : ");
      Get_Line (Buffer, Last);

      declare
         Cmd : constant String := Buffer (1 .. Last);
      begin
         if Cmd = "exit" then
            Running := False;
            Put_Line ("Ending the code...");

         elsif Cmd = "pop" then
            if isEmpty (L) then
               Put_Line (" Error - list is empty!");
            else
               R := Pop (L);
               Put_Line (" Result : " & Integer'Image (R));
            end if;

         elsif Cmd = "push" then
            Put (" Value : ");
            Get (R);
            Skip_Line;
            Push (L, R);
            Put_Line (" Result : OK ");

         elsif Cmd = "append" then
            Put (" Value : ");
            Get (R);
            Skip_Line;
            Append (L, R);
            Put_Line (" Result : OK ");

         elsif Cmd = "get" then
            Put (" Index : ");
            Get (I);
            Skip_Line;

            Len := Length (L);
            if isEmpty (L) then
               Put_Line (" Error - list is empty!");
            elsif I < 1 or I > Len then
               Put_Line (" Error - index out of bounds!");
            else
               R := Get (L, I);
               Put_Line (" Result : " & Integer'Image (R));
            end if;

         elsif Cmd = "put" then
            Put (" Index : ");
            Get (I);
            Put (" Value : ");
            Get (R);
            Skip_Line;

            Len := Length (L);
            if isEmpty (L) then
               Put_Line (" Error - list is empty!");
            elsif I < 1 or I > Len then
               Put_Line (" Error - index out of bounds!");
            else
               Put (L, I, R);
               Put_Line (" Result : OK ");
            end if;

         elsif Cmd = "insert" then
            Put (" Index : ");
            Get (I);
            Put (" Value : ");
            Get (R);
            Skip_Line;

            Len := Length (L);
            if I < 1 or I > Len + 1 then
               Put_Line (" Error - index out of bounds!");
            else
               Insert (L, I, R);
               Put_Line (" Result : OK ");
            end if;

         elsif Cmd = "delete" then
            Put (" Index : ");
            Get (I);
            Skip_Line;

            Len := Length (L);
            if isEmpty (L) then
               Put_Line (" Error - list is empty!");
            elsif I < 1 or I > Len then
               Put_Line (" Error - index out of bounds!");
            else
               Delete (L, I);
               Put_Line (" Result : OK ");
            end if;

         elsif Cmd = "printl" then
            Put (" Result : ");
            if isEmpty (L) then
               Put_Line ("Empty list");
            else
               Print (L);
            end if;

         elsif Cmd = "length" then
            R := Length (L);
            Put_Line (" Result : " & Integer'Image (R));

         elsif Cmd = "clean" then
            if isEmpty (L) then
               Put_Line (" Info - list is already empty");
            else
               Clean (L);
               Put_Line (" Result : OK ");
            end if;

         else
            Put_Line (" Error - unknown command!");
         end if;

      exception
         when Data_Error =>
            Put_Line (" Error - invalid number format!");
            Skip_Line;
      end;
   end loop;

   Clean (L);
end Zad1a;