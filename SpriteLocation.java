public class SpriteLocation {
    private int topLeftX;
    private int topLeftY;
    private int bottomRightX;
    private int bottomRightY;
    public SpriteLocation(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
    }
    public int getTopLeftX() {
        return topLeftX;
    }
    public int getTopLeftY() {
        return topLeftY;
    }
    public int getBottomRightX() {
        return bottomRightX;
    }
    public int getBottomRightY() {
        return bottomRightY;
    }
}
