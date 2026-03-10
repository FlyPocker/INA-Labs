Package Primes_Lib is
   type PrimaryN is array (Positive range <>) of Natural;
   type PrimaryN_Ptr is access PrimaryN;

   function PrimeNumbers (n : Natural) return Natural;
   function Prime (n : Natural) return Natural;
   function IsPrime (n : Natural) return Boolean;
   function PrimeFactors (n_arg : Natural) return PrimaryN_Ptr;
   function Totient(n : Natural) return Natural;

private
   type PrimaryB is array (Positive range <>) of Boolean;
   type PrimaryB_Ptr is access PrimaryB;
   procedure Sieve(P : PrimaryB_Ptr; n : Natural);
   
end Primes_Lib;
