import contours.DisplayImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Test {
    public static void main(String args[]) throws Exception {
        //Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String file = "C:/Users/Kuba/Downloads/wallpaper.jpg";
        Mat image = Imgcodecs.imread(file);


        //Reading the Image from the file and storing it in to a Matrix object
        Mat patternImage = Imgcodecs.imread(Constants.PATTERN_PATH);
        DisplayImage.displayImage(patternImage, "pattern image");

        Mat puzzleImage = Imgcodecs.imread(Constants.PUZZLE_PATH);
        DisplayImage.displayImage(puzzleImage, "puzzle image");

        System.out.println("Image Loaded");
    }
}
