# **Aadit's ATM Application Project**

## The Proposal

My application will be an ATM application. The application will be able to do all the normal functions a normal ATM can do. Things 
such as withdrawing or depositing money, seeing your transactions, and being able to work with the chequings or savings accounts 
from a bank account. The people who will be able to use it is almost anyone who has a banking account and a card that would
like to access their account from an ATM who wish to do these certain operations. The app will provide them with the operations that they would use on a normal
ATM. This project has interest in me because coming from a family full of finance people, I've always wondered how 
software can be used in the banking world, even if it's simple. I felt like this project was something that wouldn't be 
super difficult for my first ever project but at the same time it isn't easy and has some challenges to it since I am 
using Java for the first time and can help me learn the language as I create this application.

## User Stories

- As a user, I want to be able to withdraw money to my account
- As a user, I want to be able to deposit money to my account
- As a user, I want to be able to choose from Chequing or Savings Account and be able to transfer funds between the two.
- As a user, I want to add a transaction to my account after completing one of the following: withdraw, deposit, and/or transfer
- As a user, I want to be able to check my balance or check the list of transactions that have happened since I have used the ATM

- As a user, I want to save my transaction list and balance to file
- As a user, I want to load my transaction list and balance from file
- As a user, I want the option to save my transaction list and balance when quitting the ATM
- As a user, I want the option to load my transaction list and balance when opening the ATM

# Instructions for Grader

- To log in, use the ID: 12345678 and the PIN: 1234
- You can generate the first required event related to adding Transactions to an Account by completing a withdrawal, deposit, or transfer operation.
- You can generate the second required event related to adding Xs to a Y by completing a withdrawal, deposit, or transfer operation.
- You can see the view the list of Transactions by clicking on Transactions in the Menu
- You can locate my visual component in the Login and Menu page.
- You can save the state of my application by clicking the Save operation in the Menu or by having the option when quitting the ATM.
- You can reload the state of my application by clicking the Load operation in the Menu or by having the option when logging in successfully in the ATM.

# Phase 4: Task 2
Event Log:

Wed Nov 23 16:49:09 PST 2022 <br>
Withdrew $100.0 to Chequing Account

Wed Nov 23 16:49:09 PST 2022 <br>
Transaction added to: Chequing Account

Wed Nov 23 16:49:14 PST 2022 <br>
Deposited $30.0 to Savings Account

Wed Nov 23 16:49:14 PST 2022 <br>
Transaction added to: Savings Account

This shows a sample Event log when you quit my ATM when I completed two transactions to my ATM account.
It shows a description of how much was deposited or withdrawn to/from which account and another event of the transaction being added
to the account. If nothing displays in the log when the user quits, it means that the user has not either deposited money, 
withdrawn money, or transferred money between accounts so there would be nothing to log in this case. In cases as well where a transaction
has not gone through, such as putting in a negative amount to deposit or withdraw or having insufficient funds, will not log to the console.

# Phase 4: Task 3
Reflecting on my project, I think I did a good job with handling cohesion and coupling within my design. Like for example, my Account class had good cohesion
since it only focused on methods that had to do with the account itself, such as handling operations with the balance. So having a higher cohesion
made my code a lot easier to maintain, so I could focus on one class if I needed to change anything in my code.  I also do have low coupling since I 
don't use code from one module in another such as between my Transaction object class and my Account object class. I do think if I were to
refactor my code, I would want to get rid of a lot of my duplication in my GUI and how I could do it is by that I could create methods 
that combine the duplicated code to refactor my app and make it a lot easier to read since it is making my GUI class very large and 
hard to maintain such as if I were to find a specific method, I'd have to go through a lot of duplicated code to find what I'm looking for.