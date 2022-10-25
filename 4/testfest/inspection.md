Pair: yanda1928-josuma26 \
Commit: [b203e91a0efb9817836a5e88f90baeeb07a6a027](https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/tree/b203e91a0efb9817836a5e88f90baeeb07a6a027) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/39925e302b80cf21099bc0d856bb01edf8e895d6/4/self-4.md \
Score: 50/110 \
Grader: Eshwari Bhide

0/10: Self-eval

- Though you did not make changes after the deadline to the functionality you linked to, in some of your self-eval links, you did link to 5c8a869cd30913216b9db03865f0f9fa2ae8f2d7 from 10-21 4:42 PM, which is after the deadline. This leads to 0 points for the self-eval.; all self-eval links should be your last commit before the deadline.


`strategy.PP`: 50/100
- [10/20] a data definition for the return value of a call to strategy
  - -10 No interpretation for the data definition
- [10/15] - good name, signature/types, and purpose statement for the top-level function that *composes* generating a sequence of spots to move to and searching.
  - -5 because it is not exactly composed of these two things: getAllCoordinates (to generate sequence of spots the player may wish to move to) is called within the tryToReachAllAlternativeTiles function
- [10/15] - good name, signature/types, and purpose statement for generating the sequence of spots the player may wish to move to
  - No purpose statement (for getAllCoordinates function) 
- [10/15] - good name, signature/types, and purpose statement for searching rows then columns.
  - No purpose statement
- [0/15] - good name, signature/types, and purpose statement for some function/method that validates the location of the avatar after slide/insert is not the target and the current target is reachable.
  - Nothing listed on self-eval for this
 
- [10/10] - for unit test that produces an action to move the player
- [0/10] - for unit test that forces player to pass on turn

`player-protocol.md` (Ungraded)
- No mention of referee notifying player of its goal tile 
- No explicit mentioning of what Referee does if player cheats 

