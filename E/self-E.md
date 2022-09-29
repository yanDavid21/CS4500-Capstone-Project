## Self-Evaluation Form for TAHBPL/E

A fundamental guideline of Fundamentals I and II is "one task, one
function" or, more generally, separate distinct tasks into distinct
program units. Even exploratory code benefits from this much proper
program design. 

This assignment comes with three distinct, unrelated tasks.

So, indicate below each bullet which file/unit takes care of each task:


1. dealing with the command-line argument (PORT)  
Function ``toValidatedPortNumber`` starting at line:
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/eb93e80190a2f93803937916c118c2897fb10576/E/Other/Xtcp.kt#L35   


2. connecting the client on the specified port to the functionality  
In the ``main`` function from lines 20 to 24:   
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/eb93e80190a2f93803937916c118c2897fb10576/E/Other/Xtcp.kt#L20-L24  
While lines 20-24 form a logical unit, we believe it was better to just write the constinuent lines. Since each line is a function call in itself and does not contain any logic, we did not see the need to compose these function calls into a separate function. We maintain that this is readable code.


3. core functionality (either copied or imported from `C`)  
Called from main on line 24, copied over from C, function ``readFromInputStream``: 
https://github.khoury.northeastern.edu/CS4500-F22/yanda1928-josuma26/blob/eb93e80190a2f93803937916c118c2897fb10576/E/Other/C/XJson.kt#L14  


The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request. If you did *not* factor
out these pieces of functionality into separate functions/methods, say
so.

