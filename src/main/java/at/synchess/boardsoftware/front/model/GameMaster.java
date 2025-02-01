package at.synchess.boardsoftware.front.model;


import at.synchess.utils.*;
public class GameMaster {
   ChessBoard chessUtils;
   int gameId;

   public void joinGame(int GameID){
   chessUtils = new ChessBoard(false);
   }
   public void createGame(){
   chessUtils = new ChessBoard(false);

   }

   private void onTurnEnd(){
      int[][] temp = scanBoard();
      chessUtils.tempBoard = temp;
     // if (chessUtils.detectMove().getClass() != Move.class)
   }

   private void sendMove(Move move){

   }

    private void parseAndMove(Move move){

    }

   private void receiveMove(){

   }

   private void onGameEnd(){

   }

   private int[][] scanBoard(){
     return null;
   }
}
