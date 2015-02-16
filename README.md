Welcom to MCIB - a Markovian Scenario Analysis Approach using the Stochastic Cross-Impact Balance Method 

There are currently three main packages.

- edu.csss2013.cib contains the interfaces for all files
- edu.csss2013.cib.impl implements the interfaces and provides basic solvers and utility methods
- edu.csss2013.cib.io provides some methods to read from and write to files.

Quick Start
---------------------------

Go to the edu.csss2013.cib.io.commandLine.CibCmd file and look at the main method.
It demonstrates a sweep through several beta values for a BoltzmannSolver (a.k.a. exponential distribution), calculates steady state probability (=Eigenvector Centrality), Consistency and Self-Link score for every node, and also includes a method for entropy and entropy production calculation. 

