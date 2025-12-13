with Ada.Text_IO; use Ada.Text_IO;

package body list is

   function isEmpty(l : ListT) return Boolean is
   begin
      return l.first = null;
   end isEmpty;

   function Pop(l : in out ListT) return Integer is
      n : NodePtr := l.first;
      e : Integer;
   begin
      e := n.elem;
      l.first := n.next;
      if l.first = null then
         l.last := null;
      end if;
      Free(n);
      l.length := l.length - 1;
      return e;
   end Pop;

   procedure Push(l : in out ListT; e : Integer) is
      n : NodePtr;
   begin
      n := new Node;
      n.elem := e;
      n.next := l.first;
      l.first := n;
      if l.last = null then
         l.last := n;
      end if;
      l.length := l.length + 1;
   end Push;

   procedure Append(l : in out ListT; e : Integer) is
      n : NodePtr := new Node;
   begin
      n.elem := e;
      if l.first = null then
         l.first := n;
      else
         l.last.next := n;
      end if;
      l.last := n;
      l.length := l.length + 1;
   end Append;

   function Get(l : ListT; i : Integer) return Integer is
      n : NodePtr := l.first;
      idx : Integer := i;
   begin
      while n /= null and idx > 1 loop
         n := n.next;
         idx := idx - 1;
      end loop;
      return n.elem;
   end Get;

   procedure Put(l : in out ListT; i : Integer; e : Integer) is
      n : NodePtr := l.first;
      idx : Integer := i;
   begin
      while n /= null and idx > 1 loop
         n := n.next;
         idx := idx - 1;
      end loop;
      n.elem := e;
   end Put;

   procedure Insert(l : in out ListT; i : Integer; e : Integer) is
      nnew : NodePtr;
      n : NodePtr := l.first;
      idx : Integer := i;
   begin
      if i = 1 then
         Push(l, e);
      elsif i = l.length + 1 then
         Append(l, e);
      else
         nnew := new Node;
         nnew.elem := e;
         idx := idx - 1;
         while n /= null and idx > 1 loop
            n := n.next;
            idx := idx - 1;
         end loop;
         nnew.next := n.next;
         n.next := nnew;
         l.length := l.length + 1;
      end if;
   end Insert;

   procedure Delete(l : in out ListT; i : Integer) is
      n : NodePtr := l.first;
      prev : NodePtr := l.first;
      idx : Integer := i;
      temp : Integer;
   begin
      if i = 1 then
         temp := Pop(l);
      else
         while n /= null and idx > 1 loop
            prev := n;
            n := n.next;
            idx := idx - 1;
         end loop;
         if n = l.last then
            l.last := prev;
         end if;
         prev.next := n.next;
         Free(n);
         l.length := l.length - 1;
      end if;
   end Delete;

   procedure Print(l : ListT) is
      n : NodePtr := l.first;
   begin
      while n /= null loop
         Put(Integer'Image(n.elem));
         n := n.next;
      end loop;
      New_Line;
   end Print;
   
   procedure Clean(l : in out ListT) is
      n : NodePtr := l.first;
      temp : NodePtr;
   begin
      while n /= null loop
         temp := n.next;
         Free(n);
         n := temp;
      end loop;
      l.first := null;
      l.last := null;
      l.length := 0;
   end Clean;

   function Length(l : ListT) return Integer is
   begin
      return l.length;
   end Length;

end list;