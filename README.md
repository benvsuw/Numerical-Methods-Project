# Numerical-Methods-Project

Solving Truss Systems
---------------------
To solve a truss system, run the java application and type in the input file. Note the 
input file must be in the same directory as the project. 

Follow the sample input files if you want to create your own. 
Note: input parameters 
Coordinates are in m 
Forces are in N with a degree clockwise form the origin 
E is in MPa
Area is in mm^2
A node can be constrained as a ROLLERX, ROLLERY or FIXED
Treat a 1D problem as a 2D input


Solving Mass Spring Damper System
---------------------------------
To solve a mass spring damper system, run the java application and type in the input file. 
Note the input file must be in the same directory as the project .
Follow the sample input files if you want to create your own. 
Note: input parameters 
Coordinates are in m
Forces are in N with a degree clockwise form the origin 
Mass is in Kg
A node can be constrained as a ROLLERX, ROLLERY or FIXED
A damper and a spring are treated as two separate elements
K is in N/m
C is in  Ns/m
Treat a 1D problem as a 2D input
Time1 is you start of your interval (sec)
Time2 is the end of your interval (sec)
Step is your time step (sec)

Q4 serves as a special case, select appropriate button, but input file follows the same 
guideline as mass spring damper system.


Solving Electrical Systems
--------------------------
Construct a .csv file and populate it with each element in the circuit for each row. This first row is omitted upon runtime and can therefore be used for titles of columns. For each element enter the following, delimited by commas: 
"Loop One, Loop Two, Value, Type".
Loops begin counting from 1. Loop One is required. If Loop Two is not applicable, leave this column empty and preserve the comma. The Value is the value of the resistor, capacitor, inductor, or signal of a voltage source in ohms, farads, henrys and volts, respectively. Set the type of the element to one of the following: 'resistor', 'capacitor', 'inductor' or 'voltage source'.

Run the java application. Select the electrical solver, and then specify the input values .csv file. This input file must be in the same directory as the program. The java application will generate a file containing the ODE of the electrical system.
