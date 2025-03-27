import java.awt.*;
import java.util.*;

public class Game {

  private static Player wp;
  private static Player bp;

  private static final Color HIGHLIGHT = new Color(241, 245, 2);
  private static final Color CHECK = new Color(255, 51, 51);
  private static final Color CHECKMATE = new Color(255, 255, 255);

  private static final int backRank = 7;
  private static final int frontRank = 6;

  private static final int[] kdy = {4};
  private static final int[] qdy = {3};
  private static final int[] rdy = {0, 7};
  private static final int[] bdy = {2, 5};
  private static final int[] ndy = {1, 6};
  private static final int[] pdy = {0, 1, 2, 3, 4, 5, 6, 7};

  private static boolean nextTurn(Board b, BoardDisplay d, Player p, boolean inCheck) {
    if (!(wp instanceof StockfishPlayer || bp instanceof StockfishPlayer)) d.setTitle(p.getName());

    Move m = p.nextMove(inCheck);

    if (m == null) {
      System.out.println("checkmate");
      checkmate(b, d);
      return false;
    }

    if(inCheck) d.setTitle("CHECK");

    // promotion
    if ((p.getColor().equals(Color.WHITE) && m.getPiece() instanceof Pawn && m.getDestination().getRow() == 0) ||
        (p.getColor().equals(Color.BLACK) && m.getPiece() instanceof Pawn && m.getDestination().getRow() == 7)) {
      b.get(m.getSource()).removeSelfFromGrid();

      // what to promote to
      System.out.println("\n\tpromote to");
      Scanner sc = new Scanner(System.in);
      String piece = sc.nextLine();
      sc.close();

      Piece pp;

      switch (piece) {
        case "n":
          System.out.println("knight");
          pp = (p.getColor().equals(Color.WHITE)) ? new Knight(Pieces.WHITE_KNIGHT) : new Knight(Pieces.BLACK_KNIGHT);
          break;
        case "b":
          System.out.println("bishop");
          pp = (p.getColor().equals(Color.WHITE)) ? new Bishop(Pieces.WHITE_BISHOP) : new Bishop(Pieces.BLACK_BISHOP);
          break;
        case "r":
          System.out.println("rook");
          pp = (p.getColor().equals(Color.WHITE)) ? new Rook(Pieces.WHITE_ROOK) : new Rook(Pieces.BLACK_ROOK);
          break;
        default:
          System.out.println("default");
          pp = (p.getColor().equals(Color.WHITE)) ? new Queen(Pieces.WHITE_QUEEN) : new Queen(Pieces.BLACK_QUEEN);
          break;
      }

      pp.putSelfInGrid(b, m.getDestination());
    } else {
      b.executeMove(m);
    }

    d.clearColors();

    // check
    if (b.inCheck(p.getColor())) {
      d.setColor(m.getSource(), HIGHLIGHT);
      d.setColor(m.getDestination(), CHECK);
      try {Thread.sleep(10);} catch (Exception e) {}
      return true;
    } else {
      d.setColor(m.getSource(), HIGHLIGHT);
      d.setColor(m.getDestination(), HIGHLIGHT);
      try {Thread.sleep(10);} catch (Exception e) {}
      return false;
    }
  }

  public static void play(Board b, BoardDisplay d, Player p, boolean inCheck) {
    boolean next = p.getColor().equals(Color.BLACK);

    play(b, d, ((next) ? wp : bp), nextTurn(b, d, p, inCheck));
  }

  public static void checkmate(Board b, BoardDisplay d) {
    d.setTitle("CHECKMATE");

    ArrayList<Location> ls = b.getOccupiedLocations();
    for (Location l : ls) {
      if (b.get(l) instanceof King) {
        d.setColor(l, CHECKMATE);
      }
    }

    try {Thread.sleep(10000);} catch (Exception e) {}
    System.exit(0);
  }

  public static void main(String[] args) {
    Board b = new Board();

    // KINGS
    for (int i = 0; i < 1; i++) {
      Piece wk = new King(Pieces.WHITE_KING);
      wk.putSelfInGrid(b, new Location(backRank, kdy[i]));

      Piece bk = new King(Pieces.BLACK_KING);
      bk.putSelfInGrid(b, new Location(7-backRank, kdy[i]));
    }

    // QUEENS
    for (int i = 0; i < 1; i++) {
      Piece wq = new Queen(Pieces.WHITE_QUEEN);
      wq.putSelfInGrid(b, new Location(backRank, qdy[i]));

      Piece bq = new Queen(Pieces.BLACK_QUEEN);
      bq.putSelfInGrid(b, new Location(7-backRank, qdy[i]));
    }
    
    // ROOKS
    for (int i = 0; i < 2; i++) {
      Piece wr = new Rook(Pieces.WHITE_ROOK);
      wr.putSelfInGrid(b, new Location(backRank, rdy[i]));

      Piece br = new Rook(Pieces.BLACK_ROOK);
      br.putSelfInGrid(b, new Location(7-backRank, rdy[i]));
    }

    // BISHOPS 
    for (int i = 0; i < 2; i++) {
      Piece wb = new Bishop(Pieces.WHITE_BISHOP);
      wb.putSelfInGrid(b, new Location(backRank, bdy[i]));

      Piece bb = new Bishop(Pieces.BLACK_BISHOP);
      bb.putSelfInGrid(b, new Location(7-backRank, bdy[i]));
    }

    // KNIGHTS
    for (int i = 0; i < 2; i++) {
      Piece wn = new Knight(Pieces.WHITE_KNIGHT);
      wn.putSelfInGrid(b, new Location(backRank, ndy[i]));

      Piece bn = new Knight(Pieces.BLACK_KNIGHT);
      bn.putSelfInGrid(b, new Location(7-backRank, ndy[i]));
    }

    // PAWNS 
    for (int i = 0; i < 8; i++) {
      Piece wp = new Pawn(Pieces.WHITE_PAWN);
      wp.putSelfInGrid(b, new Location(frontRank, pdy[i]));

      Piece bp = new Pawn(Pieces.BLACK_PAWN);
      bp.putSelfInGrid(b, new Location(7-frontRank, pdy[i]));
    }

    // DISPLAY
    BoardDisplay d = new BoardDisplay(b);
    
    wp = new HumanPlayer(b, "box", Color.WHITE, d);
    bp = new StockfishPlayer(b, "xob", Color.BLACK, d, 1);
    //bp = new StockfishPlayer(b, "xob", Color.BLACK, d, 25);
    // last param of StockfishPlayer constructor the max search depth
    // <30 for performance, >20 for decently smart play

    // stockfish message
    if (wp instanceof StockfishPlayer && bp instanceof StockfishPlayer) {
      d.setTitle("white stockfish depth " + ((StockfishPlayer) wp).getDepth() + 
          "; black stockfish depth " + ((StockfishPlayer) bp).getDepth());
    } else if (wp instanceof StockfishPlayer) {
      d.setTitle("white stockfish depth " + ((StockfishPlayer) wp).getDepth());
    } else if (bp instanceof StockfishPlayer) {
      d.setTitle("black stockfish depth " + ((StockfishPlayer) bp).getDepth());
    }

    play(b, d, wp, false);
  }
}
