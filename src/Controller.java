import contours.DisplayImage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    private Mat patternImage;
    private Mat puzzleImage;
    private Mat borderMask;
    private Mat croppedPuzzles;
    List<Rect> contourRects = new ArrayList<>();
    private Mat[][] patternTiles = new Mat[5][5];
    private Mat[][] puzzleTiles = new Mat[5][5];
    Controller() {
        //Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void loadImages() {
        patternImage = Imgcodecs.imread(Constants.PATTERN_PATH);
        puzzleImage = Imgcodecs.imread(Constants.PUZZLE_PATH);
    }

    public void numberPuzzles() {
        getBorderMask();
        extractPuzzles();
    }

    public void findContours() {
        Mat res = new Mat();
        Imgproc.cvtColor(croppedPuzzles, res, Imgproc.COLOR_BGR2GRAY, CvType.CV_8UC1);
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(res, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Rect> rects = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            rects.add(rect);
        }

        List<Rect> collect = rects.stream().filter(r -> r.height * r.width > 2000).collect(Collectors.toList());
        contourRects = collect;
        Mat copy = new Mat();
        puzzleImage.copyTo(copy);
        for (Rect r : collect){
            Imgproc.rectangle(copy, r, new Scalar(0, 256, 0));
        }
    }

    class SortByX implements Comparator<Rect>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Rect a, Rect b)
        {
            return a.x - b.x;
        }
    }

    class SortByY implements Comparator<Rect>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Rect a, Rect b)
        {
            return a.y - b.y;
        }
    }

    public void cutPuzzleTiles(){
        contourRects.sort(new SortByX());
        contourRects.sort(new SortByY());
        Rect rect = contourRects.get(0);
        int i = 1;
        for (Rect r : contourRects){
            Imgproc.putText(croppedPuzzles, String.valueOf(i), new Point(r.x + r.width / 4.0 , r.y + 3 * r.height / 4.0 ), 1, 1.5, new Scalar(0, 0, 256), 3);
            i++;
        }
    }

    private void extractPuzzles() {
        Mat holder = new Mat();
        puzzleImage.copyTo(holder, borderMask);
        croppedPuzzles = new Mat();
        Core.subtract(puzzleImage, holder, croppedPuzzles);
    }

    private void getBorderMask() {
        Point first = new Point(10, 10);
        Mat mat = inRange(puzzleImage, 5, puzzleImage.get((int) first.x, (int) first.y));

        Point second = new Point(puzzleImage.rows() - 10, puzzleImage.cols() - 10);
        Mat full = inRange(puzzleImage, 8, puzzleImage.get((int) second.x, (int) second.y));

        Mat smthNew = new Mat();
        Core.add(mat, full, smthNew);

        borderMask = new Mat();
        smthNew.convertTo(borderMask, CvType.CV_8UC3);
    }

    public void drawCircle(Mat image) {
        Imgproc.circle(image, new Point(5, 5), 5, new Scalar(256, 256, 256));
        DisplayImage.displayImage(image, "puzzle image");
    }

    private Mat inRange(Mat image, int offset, double[] doubles) {
        Point p = new Point(0, 0);
        Mat result = new Mat();
        Core.inRange(image, new Scalar(doubles[0] - offset, doubles[1] - offset, doubles[2] - offset),
                new Scalar(doubles[0] + offset, doubles[1] + offset, doubles[2] + offset), result);
        return result;
    }

    public Mat getCroppedPuzzles() {
        return croppedPuzzles;
    }
}
