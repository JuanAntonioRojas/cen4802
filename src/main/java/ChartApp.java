import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

public class ChartApp extends Application {

    private static final int TOT_RAND_INTS = 2_000_000;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Data Structure Performance (2M Integers)");

        // 1. Run Live Benchmarks
        long[] alTimes = benchmarkArrayList();
        long[] llTimes = benchmarkLinkedList();
        long[] htTimes = benchmarkHashtable();

        // 2. Setup Chart Axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Data Structure");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Duration (Milliseconds)");

        BarChart barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("2,000,000 Elements: Insertion vs Deletion Time");

        // 3. Add Series Data
        XYChart.Series insertSeries = new XYChart.Series<>();
        insertSeries.setName("Insertion Time (ms)");
        insertSeries.getData().add(new XYChart.Data<>("ArrayList", alTimes[0]));
        insertSeries.getData().add(new XYChart.Data<>("LinkedList", llTimes[0]));
        insertSeries.getData().add(new XYChart.Data<>("Hashtable", htTimes[0]));

        XYChart.Series deleteSeries = new XYChart.Series<>();
        deleteSeries.setName("Deletion Time (ms)");
        deleteSeries.getData().add(new XYChart.Data<>("ArrayList (tail)", alTimes[1]));
        deleteSeries.getData().add(new XYChart.Data<>("LinkedList (head)", llTimes[1]));
        deleteSeries.getData().add(new XYChart.Data<>("Hashtable (key)", htTimes[1]));

        barChart.getData().addAll(insertSeries, deleteSeries);

        VBox root = new VBox(barChart);
        Scene scene = new Scene(root, 800, 500);

        stage.setScene(scene);
        stage.show();
    }

    private long[] benchmarkArrayList() {
        ArrayList list = new ArrayList<>();
        Random rand = new Random();

        long startAdd = System.currentTimeMillis();
        for (int i = 0; i < TOT_RAND_INTS; i++) list.add(rand.nextInt());
        long addTime = System.currentTimeMillis() - startAdd;

        long startDel = System.currentTimeMillis();
        for (int i = list.size() - 1; i >= 0; i--) list.remove(i);
        long delTime = System.currentTimeMillis() - startDel;

        return new long[]{addTime, delTime};
    }

    private long[] benchmarkLinkedList() {
        LinkedList list = new LinkedList<>();
        Random rand = new Random();

        long startAdd = System.currentTimeMillis();
        for (int i = 0; i < TOT_RAND_INTS; i++) list.add(rand.nextInt());
        long addTime = System.currentTimeMillis() - startAdd;

        long startDel = System.currentTimeMillis();
        while (!list.isEmpty()) list.removeFirst();
        long delTime = System.currentTimeMillis() - startDel;

        return new long[]{addTime, delTime};
    }

    private long[] benchmarkHashtable() {
        Hashtable table = new Hashtable<>(TOT_RAND_INTS);
        Random rand = new Random();

        long startAdd = System.currentTimeMillis();
        for (int i = 0; i < TOT_RAND_INTS; i++) table.put(i, rand.nextInt());
        long addTime = System.currentTimeMillis() - startAdd;

        long startDel = System.currentTimeMillis();
        for (int i = 0; i < TOT_RAND_INTS; i++) table.remove(i);
        long delTime = System.currentTimeMillis() - startDel;

        return new long[]{addTime, delTime};
    }

    public static void main(String[] args) {
        launch(args);
    }
}