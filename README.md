# WI26-CSCI-345-Object-Oriented-Design-Assignment-2
# WI26-CSCI-345-Object-Oriented-Design-Assignment-2

## Implementing Deadwood Game

This part of the assignment is asking you to implement! You analyzed the problem, designed a solution, now it is time to implement the design.

---

## Outcomes

Upon successful completion of this assignment, you will:

- Convert your software design into a working program
- Demonstrate how the following class relationships are realized in Java:
  - Association
  - Aggregation
  - Composition
  - Implementation
  - Inheritance
- Have a text-based (command-based) implementation of Deadwood

---

## Problem Statement

Implement your Deadwood design as a console application. This version of your game is intended to be playable but may not be aesthetically pleasing. The point of the program is to verify that your design correctly models the game play logic. For example, players can only move to adjacent rooms and your program must enforce this restriction.

### Required Tasks/Actions

The program MUST accommodate the following tasks/actions:

**Player Management:**
- Identify the active player
- Display the player's information
- Display location of all players and indicate the active player

**Movement:**
- Move from one location to another
- Display the source and destination

**Working on a Part:**
- The active player works on a part
- Display part-related information

**Upgrading Level:**
- Display current and target level
- Support different types of upgrades

**Rehearsing:**
- The active player rehearses
- Display related information

**Acting:**
- The active player acts on a role
- Display related information

**Turn Management:**
- End the current player's turn
- Active player can decide to end a turn
- The end command is recommended even if the active player has no legal actions available

**Game Control:**
- An End Game command
- This will help with testing as the game can be ended at any given time

---

## Example Interactions

### Example 1: Player Working a Part

If the active player is working a part, the interaction could be the following:

```
> who
The active player is Jane Doe. She has $15, 3 credits and 10 fames. She is working Crusty Prospector, "Aww, peaches!"

> where
in Train Station shooting Law and the Old West scene 20

> act
success! You got $1

> end
```

### Example 2: Player Not Working a Part

If a player is not working a part, the interaction might look like the following:

```
> who
player blue ($1, 5cr)

> where
Jail wrapped

> move Train Station

> where
Train Station shooting Law and the Old West scene 20

> work Talking Mule

> end
```

### Notes on Examples

We are showing additional information like the player's money and credits. This would be nice but is not required. Remember, the point is to test the design and to have a robust working model, it is not to have a friendly user interface.

In addition, you may find that your design (the one you submitted) has weaknesses. This is typical of any significantly sized software project. It is difficult to anticipate every possible detail of the system. Feel free to modify your design. However, you must update your design document (class diagram) as you modify the design. We will use this document to understand your software.

Your program should accept a single parameter: the number of players. Like the rule book says, the game is designed for two to eight players.

---

## Deliverables

### Code Submission
- You should submit your code via Canvas and give us access to your git repository
- We will download your implementation from the repository, so please make sure we have full access to your repository
- The file `Deadwood.java` should contain the main program
- Our GitHub account information is available on Canvas
- **Points: [20 points]**

### Code Quality
- Coding style, comments, descriptive names, and organization of files
- **Points: [2.5 points]**

### Class Diagram
- Submit a revised Class Diagram (Canvas) that reflects your implementation
- Explain (1-2 paragraphs) why you chose this design
- **Points: [5 points]**

### Documentation
- Submit your Read Me file (Canvas)

### Peer Evaluation
- Email peer evaluation individually to: sharmim@wwu.edu
- Subject must include "CSCI345 peer evaluation"
- **Points: [2.5 points]**

### Design Report
- Submit a report (Canvas) specifying what types of Cohesion and Coupling you utilized in your design and your rationale for using them
- Discuss your overall design choice and how that relates to SOLID design principles
- This is a critical component
- The grades you receive on this will be part of your Writing Proficiency (WP) points
- **Points: [5% of overall grade]**

---

## Due Dates and Mini-Milestones

### First Milestone: Friday, Feb 6th
- Submit your partially implemented program
- You MUST have all the classes and the method signatures and attributes
- You MUST complete the implementation for at least 2 of your classes
- We will compile your code
- **Deduction: If it doesn't compile, we will deduct 5 points**

### Second Milestone: Saturday, Feb 14th
- Submit your partially implemented program
- You MUST have all the classes and the method signatures and attributes
- You MUST complete the implementation for at least 5 of your classes
- We will compile your code
- **Deduction: If it doesn't compile, we will deduct 5 points**

### Final Submission: Friday, Feb 20th
- Complete code
- Read Me file with instructions about how to compile and run your code
- Class Diagram
- Report specifying the use of cohesion and coupling and your design rationale related to SOLID

---

## Grading Rubric

| Item | Points |
|------|--------|
| Software correctly plays Deadwood and uses Object-Oriented principles | 20 |
| Code Quality (comments, well-defined methods, descriptive names) | 2.5 |
| Design Choices & Rationale (SOLID principles, Cohesion, Coupling) | 10 |
| Class Diagram | 5 |
| Peer Evaluation (Contribution Summary) | 2.5 |

---

## Major Deductions

**Case 1:** Your program does not compile
- If it does not run, we can't give you any points

**Case 2:** Your program compiles but breaks repeatedly
- If every time we try to interact with it the program breaks, we won't be able to test the correctness

**Case 3:** Your program runs but does not work correctly
- Example: Players can move to non-adjacent rooms or players can act in roles above their rank
