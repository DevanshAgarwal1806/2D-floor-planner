package Assets;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class Shower implements DesignElement{
    private static final int DEFAULT_BATH_WIDTH = 40;
    private static final int DEFAULT_BATH_HEIGHT = 80;
    private static final int DEFAULT_SHOWER_WIDTH = 3;
    private static final int DEFAULT_SHOWER_HEIGHT = 14;

    private int bathWidth = DEFAULT_BATH_WIDTH;
    private int bathHeight = DEFAULT_BATH_HEIGHT;
    private int showerWidth = DEFAULT_SHOWER_WIDTH;
    private int showerHeight = DEFAULT_SHOWER_HEIGHT;

    private Point startPoint;
    private boolean isSelected = false;
    private int rotationAngle = 0;

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public void draw(Graphics2D g) {
        if (isSelected) {
            g.setColor(Color.MAGENTA);
        } else {
            g.setColor(Color.BLACK);
        }
        g.setStroke(new BasicStroke(2));

        // Save the current graphics transformation
        AffineTransform oldTransform = g.getTransform();

        // Translate and rotate the graphics context to draw the stove at the desired position and angle
        g.translate(startPoint.x, startPoint.y);
        g.rotate(Math.toRadians(rotationAngle));

        // Draw bath
        g.drawRect(-bathWidth/2, -bathHeight/2, bathWidth, bathHeight);

        //Shower
        g.fillRect(-showerWidth/2 , -bathHeight/2, showerWidth, showerHeight);
        int []xTrap = {-showerWidth/2, showerWidth/2 , showerWidth*3 , -showerWidth*3} ;
        int []yTrap = {-bathHeight/2 +showerHeight,-bathHeight/2+showerHeight,-bathHeight/2+showerHeight+showerWidth*3,-bathHeight/2+showerHeight+showerWidth*3};
        Polygon Trap = new Polygon(xTrap, yTrap , 4);
        g.fillPolygon(Trap);
        
        // Restore the old graphics transformation
        g.setTransform(oldTransform);
    }

    @Override
    public Shape getBounds() {
        // Calculate the coordinates of the corners of the unrotated rectangle
        int x1 = -bathWidth / 2;
        int y1 = -bathHeight / 2;
        int x2 = bathWidth / 2;
        int y2 = -bathHeight / 2;
        int x3 = bathWidth / 2;
        int y3 = bathHeight / 2;
        int x4 = -bathWidth / 2;
        int y4 = bathHeight / 2;

        // Apply the rotation to each corner
        double cosTheta = Math.cos(Math.toRadians(rotationAngle));
        double sinTheta = Math.sin(Math.toRadians(rotationAngle));

        int[] xPoints = {(int) (x1 * cosTheta - y1 * sinTheta), (int) (x2 * cosTheta - y2 * sinTheta),
                (int) (x3 * cosTheta - y3 * sinTheta), (int) (x4 * cosTheta - y4 * sinTheta)};
        int[] yPoints = {(int) (x1 * sinTheta + y1 * cosTheta), (int) (x2 * sinTheta + y2 * cosTheta),
                (int) (x3 * sinTheta + y3 * cosTheta), (int) (x4 * sinTheta + y4 * cosTheta)};

        // Create a polygon from the rotated corners
        Polygon polygon = new Polygon(xPoints, yPoints, 4);

        // Translate the polygon to the start point
        polygon.translate(startPoint.x, startPoint.y);

        return polygon;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void resize(double scale) {
        bathWidth = (int) (scale * DEFAULT_BATH_WIDTH);
        bathHeight = (int) (scale * DEFAULT_BATH_HEIGHT);
        showerHeight = (int )(scale * DEFAULT_SHOWER_HEIGHT);
        showerWidth = (int )(scale * DEFAULT_SHOWER_WIDTH);
    }

    @Override
    public void rotate(int angle) {
        rotationAngle = angle;
    }

    public int getWidth() {
        if (rotationAngle % 180 == 0) {
            return bathWidth;
        } else {
            return bathHeight;
        }
    }

    public int getHeight() {
        if (rotationAngle % 180 == 0) {
            return bathHeight;
        } else {
            return bathWidth;
        }
    }
    

    public int getRotationAngle() {
        return rotationAngle;
    }

    @Override
    public boolean isFixture() {
        return true;
    }

    private Room inRoom = null;

    @Override
    public Room getRoom() {
        return inRoom;
    }

    @Override
    public void setRoom(Room room) {
        inRoom = room;
    }

    public int savedRotationAngle;
    public void saveRotationAngle(int angle) {
        savedRotationAngle = angle;
    }
    public int getSavedRotationAngle() {
        return savedRotationAngle;
    }
}