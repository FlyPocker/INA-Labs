Package Primes is
   function PrimeNumbers (n : Natural) return Natural;
   function Prime (n : Natural) return Natural;
   function IsPrime (n : Natural) return Boolean;
private
   type PrimaryB is array (Positive range <>) of Boolean;
   type PrimaryB_Ptr is access PrimaryB;

   type PrimaryN is array (Positive range <>) of Natural;
   type PrimaryN_Ptr is access PrimaryN;
   procedure Sieve(P : PrimaryB_Ptr; n : Natural);
   
end Primes;
