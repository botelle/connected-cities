# connected-cities

This is basically a simplified version of Dijkstra's algorithm without edge weights

Left unanswered	are how	big the	list of	cities,
how often they'd be updated, and what other values (cost, distance, etc) would be added

If the list of cities wasn't changing often and you wanted to stick to the pattern of reading the file, 
you could serialize a data structure into a separate file along with the hash of the cities file
to make it a little faster.

You would typically represent these as an undirected graph, or even a matrix of n elements, with n corresponding to the number of unique cities with a 1 or 0 for connected.

This code starts by generating a map of neighbors.. when it encounters a city pair, it adds the left side as a key and appending right side to the list that comprises the value ()
To make it symmetrical it also adds the right side as a key with the left side appended to the value

Then to check connectivity, it does a breadth first search, using a queue to add neighbors and then check them against the ending city