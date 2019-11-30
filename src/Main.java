import contours.DisplayImage;

public class Main {
    public static void main(String args[])  {
        Controller controller = new Controller();
        controller.loadImages();
        controller.numberPuzzles();
        controller.findContours();
        controller.cutPuzzleTiles();

        DisplayImage.displayImage(controller.getCroppedPuzzles(), "croppedPuzzles");
    }
}
