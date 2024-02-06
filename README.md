# JavaFX Chess Engine
Chess Engine developed by a team of 4 people. Written in Java with the JavaFX library. 
The application is available in German.

## Start of the Program
After starting the program, you can change several parameters and settings. <br>
<img src="https://github.com/Lutu-gl/Chess-JavaFx/assets/74654393/1e3249f7-30b7-4d33-a929-2c096acdd698" alt="Program" style="width: 50%;" />

You can select different modes:
- **Player against computer**
  - Playing with white pieces
  - Playing with black pieces
- **Player against player**
  - Playing local
  - Playing as host
  - Join the host
- **Computer against computer**


When selecting *player against player*, then local means playing on one PC. 
*Playing as host* and *Join the host* mean that you can play on 2 devices in the same local network. One is the host and the other one joins the host. 
The host can select the settings such as time and color.

### Figure design and the field design:
The figure design and field design can be selected manually. This allows you to set how the figures look and what color the fields should be.<br>

<img src="https://github.com/Lutu-gl/Chess-JavaFx/assets/74654393/3922a710-c747-4a40-96fb-789d2d73602f" alt="Fielddesign" style="width: 50%;" />
<img src="https://github.com/Lutu-gl/Chess-JavaFx/assets/74654393/d844902e-e076-435b-ab89-8fe0b8cd2ec1" alt="Piecedesing" style="width: 50%;" />



### AI
If you play against the computer, you compete against the AI built into the program. It
works with a self-created opening book. The AI then calculates its move using the
minimax algorithm with alpha beta pruning and evaluates the position using various criterias:
- Material
- Position of the figures
- Mobility of the pieces
- Safety of the king

## Playing chess
After you press 'start', the settings window closes and the game board appears. You can move pieces by clicking or dragging.
There is the possiblity to make premoves (A premove in chess is when a player sets up a move that plays automatically as soon as it's their turn, independently of the opponent's move) <br>
<img src="https://github.com/Lutu-gl/Chess-JavaFx/assets/74654393/0bc292ad-96bb-4e42-838e-5fc5fdfbb9ea" alt="Fielddesign" style="width: 50%;" />

If you play against the computer with black or have 2 computers playing, you start the AI by clicking once on the chessboard. <br>
<img src="https://github.com/Lutu-gl/Chess-JavaFx/assets/74654393/2e0314c1-34b2-4772-bac0-e5b83bb92d43" alt="Fielddesign" style="width: 50%;" />

Various information is displayed on the right-hand side:
- Clock
- Moves in PGN notation
- 'Take back' button
- 'Resign' button

## Installation
The application requires a Java Runtime Enviroment 15.0.2 - 16.0.1. <br>
If you want to compile the code, the JavaFX library is needed.

## Conclusion
We hope you enjoy trying out and exploring our chess engine!
