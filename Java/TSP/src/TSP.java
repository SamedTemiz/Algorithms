import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSP {
    public static int inputValue;//Koordinat sayımız
    public static double[][] coordinates;//Koordinatlar
    public static double[][] distances;//Tüm mesafe permütasyonları
    private List<Integer> path;//Gidiş yolu
    private int pathLength;//Yolun maliyeti

    //Koordinatları alan constructor method
    public TSP(double[][] coords) {
        distances = new double[inputValue][inputValue];
        path = new ArrayList<>();

        for (int i = 0; i < inputValue; i++) {
            for (int j = 0; j < inputValue; j++) {
                //Tüm mesafe kombinasyonlarını hesapla- Euclidean Hesabı ile
                distances[i][j] = Math.round(Math.sqrt(Math.pow(coords[i][0] - coords[j][0], 2)
                        + Math.pow(coords[i][1] - coords[j][1], 2)));
            }
        }
    }

    //Dosyadan verileri okuyup coordinates dizisini dolduruyoruz
    public static void readInputFile(String fileName) {
        try {
            File file = new File("C:\\Users\\Samed\\JavaProjects\\TSP\\" + fileName);

            Scanner myReader = new Scanner(file);
            inputValue = Integer.parseInt(myReader.nextLine());
            coordinates = new double[inputValue][2];
            int index = 0;
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(" ");
                double x = Double.parseDouble(data[0]);
                double y = Double.parseDouble(data[1]);

                coordinates[index][0] = x;
                coordinates[index][1] = y;

                index++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //En kısa yol permütasyonunu oluşturuyor
    public List<Integer> calculateSortestPath() {
        List<Integer> initialPath = new ArrayList<>();
        for (int i = 0; i < inputValue; i++) {
            initialPath.add(i);
        }
        pathLength = getPathLength(initialPath);//Yol uzunluğunu alıyoruz
        path = new ArrayList<>(initialPath);

        boolean visited = true;//Aynı noktadan bir defa geçilmesini sağlıyoruz
        while (visited) {
            visited = false;
            for (int i = 0; i < inputValue - 1; i++) {
                for (int j = i + 1; j < inputValue; j++) {
                    List<Integer> newTour = swapCoords(i, j, path);//Farklı kombinasyonlar için swap metodu
                    int newLength = getPathLength(newTour);
                    if (newLength < pathLength) {//Yol maliyeti kısa ise yeni path bu oluyor
                        pathLength = newLength;
                        path = new ArrayList<>(newTour);
                        visited = true;
                    }
                }
            }
        }
        return path;
    }

    //En iyi yol için kombinasyonları deniyoruz
    private List<Integer> swapCoords(int i, int j, List<Integer> tour) {
        List<Integer> newPath = new ArrayList<>(tour.subList(0, i));
        for (int k = j; k >= i; k--) {
            newPath.add(tour.get(k));
        }
        newPath.addAll(tour.subList(j + 1, inputValue));
        return newPath;
    }

    //Yolun uzunluğu yani maliyeti hesaplanıyor
    private int getPathLength(List<Integer> path) {
        int length = 0;
        for (int i = 0; i < inputValue - 1; i++) {
            length += distances[path.get(i)][path.get(i + 1)];
        }
        //Başa döndüreceksek bu mesafeyi hesaba katmalıyız
//        length += distances[tour.get(inputValue - 1)][tour.get(0)];
        return length;
    }

    //Main method
    public static void main(String[] args) {
        long begin = 0, end = 0;
        readInputFile("tsp_5_1"); //Dosyadan okuma yapılıyor
        begin = System.currentTimeMillis();

        TSP tsp = new TSP(coordinates); //TSP nesnesi coordinates dizisi ile çağırılıyor
        List<Integer> sortestPath = tsp.calculateSortestPath();//En iyi yol bulunuyor

        end = System.currentTimeMillis();
        //Dosya Boyutu -> inputValue
        //Maliyet      -> pathLength
        System.out.print("5. Sonuç: "+ tsp.inputValue+", "+ tsp.pathLength+", ");
        for(int x : sortestPath){
            System.out.print(x+" ");
        }
        System.out.println("\nWorking Time : " + ((double) (end - begin) / 1000+" sn"));
    }
}