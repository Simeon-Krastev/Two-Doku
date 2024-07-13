# Two-Doku
A java implementation of a Sudoku-based game based on an idea by [Christopher F. Clark](https://www.linkedin.com/in/christopherfclark)

## Game Premise

The basic idea of the game is that there are two players (red and blue) and an empty Sudoku board with “dominos” that have one digit on them with the numbers 1 to 9. Because there are 81 squares on the board (9x9), there are 90 dominoes (10x9)—that way, at the end, there are at least 9 dominoes (1-9) that can’t be placed.

At the start of the game, the players take turns coloring squares (36 of them per player) to indicate which squares they intend to fill in. It is okay for a square to have more than one color (i.e., both players want to fill it in) or no color. The players, when playing their dominos, will fill in the squares with their color on them first.
After the squares are colored, the players are each given 4 random dominos that they cannot see and place those face (number side) down on the board on squares that are not yet colored. Note that these dominos do not need to follow the rules of Sudoku; for example, two of the face-down dominos might have the same number but be in the same row, column, or 3x3 square.
After placing the random dominos, the players each get 40 random dominos of their own, with 2 dominos being left over in the pot. The computer will ensure that the random dominos of each player add up to the same number.
At this point, the game is set up and the main part of play begins, with the players alternating turns.
Each turn consists of:

* Optionally removing a domino from the board (or from the pot) and adding it to the player's own pile.
* Playing a domino onto the board from their pile.
  a. The domino played must obey the rules of Sudoku and not cause the row, column, or 3x3 square to have a duplicate number in it.
      i. If the domino played violates the rules because of dominos visible on the board, no other dominos are revealed.
      ii. However, if the domino played is only in conflict with a face-down domino (i.e., one of the ones originally played on white squares), the computer will reveal that face-down domino.
      iii. If the domino played is in conflict with more than one face-down domino, only one of those dominos is revealed (the computer will randomly pick which one from the conflicting ones).
  b. The player must also play their dominos only on squares they have colored in as long as there are those squares left. Once the player has filled in all of their colored squares, they may next fill in white squares. Finally, if the player has filled in all the squares marked with their own color and all the white squares, the player may play dominos onto the squares marked with the other player's color.
  c. If the player's domino breaks the rules of Sudoku or is played onto a square not properly colored, the domino is returned to the player's pile, and the player forfeits the right to play a domino that turn.
* If the player has removed a domino from the pot and not placed a domino on the board, the player may put a domino from their own pile back into the pot.
* Or doing none of the above and “passing.”

The game ends when one player's pile is empty or when both players have not successfully played a domino onto the board (either by violating the rules above or by passing). Note that the players can create unsolved Sudoku puzzles where there are empty spots left, but no valid plays. In fact, this should be common as the goal is not to solve the puzzle but to leave the other player with more valuable dominos than one has oneself.


When the game is over, the dominos left in each player's pile are added up. The player with the lower score wins (with a score equal to his opponent's score plus the dominos left in the pot).

