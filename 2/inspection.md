Pair: yanda1928-josuma26 \
Commit: [f5da4a5dd44055864b694ede57b38b69d9494b33](https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/tree/f5da4a5dd44055864b694ede57b38b69d9494b33) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/1d30fac1724a327130e00e937a4cf7a8afcf3cd2/2/self-2.md \
Score: 0/80 \
Grader: James Packard \

Programming task:

> I had to give the programming task a 0/80 because you used `null` to represent an empty space on the board without documenting it. You would get a 20/80 if you mentioned that the `Array<Array<Tile>>` field in `BoardTiles` could contain `null`s.  
> The problem with using `null` is that it disguises the fact that tiles can either be present or removed, and prevents the type checker from preventing the kinds of mistakes that arise from this.  
> But also, does it make sense for a Board to have an empty space? Or would it be better to think of the insertion of a tile as 'pushing' the new spare tile out?  
> I marked the points for the rest of this assignment as if you did not make the `null` mistake, so that you can see what score you would have received.

[30/40] an operation that determines the reachable tiles from some spot
  1. [0/10] : missing data definition for coordinates
     > You have a Coordinates class, but it does not have a data definition.
       What does a coordinate represent? Where would a `new Coordinates(0,0)`
       be on the board?  
  2. [+10] : signature/purpose statement 
     > Great, very thorough description!  
  3. [+10] : method is clear about how it checks for cycles  
  4. [+10] : at least one unit test with a non-empty expected result

---

[26/30] an operation for sliding a row or column in a direction 
  - [+6/10] missing signature/purpose statement, but you admitted it.
  - [+10] the method checks that row/column has even index 
  - [+10] at least one unit test for rows and one for columns

---

[5/10] an operation for rotating and inserting the spare tile
  - [-5] no purpose statement. I see your argument and I counter with the fact that your method can throw an exception, but you do not document it. Regardless, public methods should always have purpose statements.

---

Some thoughts about your code:

> You use an interface to represent a Direction and dynamic dispatch on their methods, but you also perform a case analysis in many of your BoardTiles methods. It's confusing to use both of these techniques instead of just one or the other. The fact that you have an exception case in methods that accept a Direction should indicate something is wrong.  
> Use mutation carefully. If you decide to make a class mutable, there should be a good reason why.  

---

Design task (ungraded):

> IDs are necessary when you need to serialize 'reference' equality. If you have an object representing a Player, do you need to store their ids to compare reference equality?  
> The wish list is supposed to contain methods/functions for the game state, not the referee. Note that these are two separate components. Does a game state need to know about a `Response`?
