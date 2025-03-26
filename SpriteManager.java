import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;

public class SpriteManager {
    private static final Map<Pieces,SpriteLocation> locations = Map.ofEntries(
            entry(Pieces.BLACK_BISHOP,new SpriteLocation(0,0,150,150)),
            entry(Pieces.BLACK_KING,new SpriteLocation(0,150,150,300)),
            entry(Pieces.BLACK_KNIGHT,new SpriteLocation(0,300,150,450)),
            entry(Pieces.BLACK_PAWN,new SpriteLocation(0,450,150,600)),
            entry(Pieces.BLACK_QUEEN,new SpriteLocation(0,600,150,750)),
            entry(Pieces.BLACK_ROOK,new SpriteLocation(0,750,150,900)),
            entry(Pieces.WHITE_BISHOP,new SpriteLocation(150,0,300,150)),
            entry(Pieces.WHITE_KING,new SpriteLocation(150,150,300,300)),
            entry(Pieces.WHITE_KNIGHT,new SpriteLocation(150,300,300,450)),
            entry(Pieces.WHITE_PAWN,new SpriteLocation(150,450,300,600)),
            entry(Pieces.WHITE_QUEEN,new SpriteLocation(150,600,300,750)),
            entry(Pieces.WHITE_ROOK,new SpriteLocation(150,750,300,900))
    );
    private static final File image = new File("chess.png");
    private static BufferedImage spriteSheet;
    static {
        try {
            spriteSheet = ImageIO.read(image);
            System.out.println("success");
        } catch (IOException e) {
            throw new RuntimeException("Invalid image file! Please check that \"chess_pieces.png\" exists!");
        }
    }
    public static ImageIcon getSprite(Pieces piece,int height,int width){
        SpriteLocation location = (SpriteLocation)locations.get(piece);
        BufferedImage currSprite = spriteSheet.getSubimage(location.getTopLeftY(),location.getTopLeftX(),150,150);
        return new ImageIcon(resizeImage(currSprite,width,height));
    }
    public static Color getColor(Pieces piece){
        if(piece == Pieces.BLACK_BISHOP
                || piece==Pieces.BLACK_KING
                || piece==Pieces.BLACK_KNIGHT
                || piece==Pieces.BLACK_PAWN
                || piece==Pieces.BLACK_QUEEN
                || piece==Pieces.BLACK_ROOK){
            return Color.BLACK;
        }else{
            return Color.WHITE;
        }
    }
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight){
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
