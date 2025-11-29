with Ada.Unchecked_Deallocation;
with Primes_Lib;

package body Primes_Lib is
   
   
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

   function PrimeFactors(n_arg : Natural) return PrimaryN_Ptr is
      -- Size of this array must be aranged to the size of "n", it's enough for 2^100 basically
      Temp_Arr : array (1 .. 100) of Natural;
      Res : PrimaryN_Ptr;
      Count : Integer := 0;
      i : Natural;
      n : Natural;
   begin
      if n_arg <= 1 then
         return new PrimaryN(1 .. 0);
      end if;
      n := n_arg;
      while n mod 2 = 0 loop
         Count := Count + 1;
         Temp_Arr(Count) := 2;
         n := n/2;
      end loop;
      i := 3;
      while i*i <= n loop
         while n mod i = 0 loop
            Count := Count + 1;  
            Temp_Arr(Count) := i;
            n := n/i;
         end loop;
         i := i+2;
      end loop;
      if n > 1 then
         Count := Count + 1;
         Temp_Arr(Count) := n;
      end if;
      Res := new PrimaryN(1 .. Count);
      for I in 1 .. Count loop
         Res(I) := Temp_Arr(I);
      end loop;
      return Res;
   end PrimeFactors;

   function Totient(n : Natural) return Natural is
      Factors : PrimaryN_Ptr;
      phi : Natural;
      len : Natural;
   begin
      if n = 1 then
         return 1;
      end if;
      Factors := PrimeFactors(n);
      if Factors = null then
         return 0;
      end if;
      len := Factors'Length;
      phi := Factors(1)-1;
      for i in 2 .. len loop
         if Factors(i) /= Factors(i-1) then
            phi := phi*(Factors(i)-1);
         else
            phi := phi*(Factors(i));
         end if;
      end loop;
      FreeN(Factors);
      return phi;
   end Totient;

end Primes_Lib;