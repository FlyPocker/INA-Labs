with Ada.Unchecked_Deallocation;

package body Primes is
   
   
   procedure FreeB is
      new Standard.Ada.Unchecked_Deallocation(PrimaryB,PrimaryB_Ptr);

   procedure FreeN is
      new Standard.Ada.Unchecked_Deallocation(PrimaryN,PrimaryN_Ptr);
   

   procedure Sieve (P : PrimaryB_Ptr; n : Natural) is
      j : Natural;
      i : Natural;
   begin
      P.all := (others => True);
      i := 2;
      while i*i <= n loop
         if P(i) then
            j := i*i;
            while j <= n loop
               P(j) := False;
               j := j+i;
            end loop;
         end if;
         i := i+1;
      end loop;
   end Sieve;

   function PrimeNumbers(n : Natural) return Natural is
      P : PrimaryB_Ptr;
      count : Natural;
   begin
      P := new PrimaryB(2 .. n);
      Sieve(P, n);
      count := 0;
      for i in 2 .. n loop
         if P(i) then
            count := count+1;
         end if;
      end loop;
      FreeB(P);
      return count;
   end PrimeNumbers;

   function Prime(n : Natural) return Natural is
      k : Natural;
      j : Natural;
      is_Prime : Boolean;
      i : Natural;
      P : PrimaryN_Ptr;
      Res : Natural;
   begin
      P := new PrimaryN(1 .. n);
      P(1) := 2;
      k := 3;
      i := 1;
      while i < n loop
         j := 1;
         is_Prime := True;
         while (j <= i) and (P(j)*P(j) <= k) and (is_Prime) loop
            if (k mod P(j)) = 0 then
               is_Prime := False;
            else
               j := j+1;
            end if;
         end loop;
         if is_Prime then
            i := i+1;
            P(i) := k;
         end if;
         k := k+2;
      end loop;
      Res := P(n);
      FreeN (P);
      return Res;
   end Prime;

   function IsPrime(n : Natural) return Boolean is
      P : PrimaryB_Ptr;
      Res : Boolean;
   begin
      if n < 2 then
         return False;
      end if;
      P := new PrimaryB(2 .. n);
      Sieve(P, n);
      Res := P(n);
      FreeB(P);
      return Res;
   end IsPrime;

end Primes;