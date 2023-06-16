package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class KnapsackDynamic {
    private final String urlString;      //Kaynak dosyanın url'si
    private int itemCount;               //Eşya sayısı
    private int maxWeight;               //Çantanın alabildiği maksimum değer
    private int[][] combinations;        //Çantanın içine girebilen eşyalar
    private int totalValue;              //En iyi değer
    private int[] values;                //Eşyaların değerleri
    private int[] weights;               //Ağırlıklar - Maliyetler
    private int[] solutionArr;           //Alınan eşyaları

    public KnapsackDynamic(String url){
        this.urlString = url;
    }

    //Veri setini programa ekliyoruz
    public void fileRead(){
        try{
            //Url nesnesini oluşturuyoruz
            URL url = new URL(urlString);
            BufferedReader read = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            //İlk satırdaki bilgileri alıyoruz
            String firstLine = read.readLine();
            itemCount = Integer.parseInt(firstLine.split(" ")[0]);
            maxWeight = Integer.parseInt(firstLine.split(" ")[1]);

            //Dizilerimiz için bellekte yer açıyoruz
            values = new int[itemCount+1];
            weights = new int[itemCount+1];
            combinations = new int[itemCount + 1][maxWeight + 1];

            //Dosya okuma ve dizileri doldurma
            String line;
            int count = 1;
            while((line = read.readLine()) != null){
                int value = Integer.parseInt(line.split(" ")[0]);
                int weight = Integer.parseInt(line.split(" ")[1]);

                values[count] = value;
                weights[count] = weight;

                count++;
            }
            read.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Algoritmayı çalıştırıyoruz
    public void solve(){
        //Kullanılacak eşyaları sayan döngü
        for(int i = 1; i < itemCount + 1; i++){
            //Ağırlık - maliyet değerini döndürüyoruz
            for(int w = 1; w < maxWeight+1; w++){
                //Formüle göre alınmayan eşyayı tanımlıyoruz
                int notTakingItem = combinations[i - 1][w];
                int takingItem = 0;
                //Eğer çantaya giriyorsa al
                if(weights[i] <= w){
                    //Alınan eşyayı tanımlıyoruz
                    takingItem = values[i] + combinations[i - 1][w - weights[i]];
                }
                //En yüksek değeri alıyoruz
                combinations[i][w] = Math.max(notTakingItem, takingItem);
            }

        }
        //Oluşturulan 2 boyutlu dizide sağ alttaki kombinasyon en verimli olanı
        totalValue = combinations[itemCount][maxWeight];

        //Alınan eşyaların indislerini işaretliyoruz
        solutionArr = new int[itemCount+1];
        int w = maxWeight;
        for (int i = itemCount; i > 0 && w > 0; i--) {
            if (combinations[i][w] != combinations[i-1][w]) {
                solutionArr[i] = 1;
                w -= weights[i];
            }
        }

        //Ekrana yazdırma bölümü
        System.out.println("Total Value: "+ totalValue);
        System.out.println("Selected Item: ");
        for(int i = 1; i < solutionArr.length; i++){
            if(solutionArr[i] == 1){
                System.out.print(i+" ");
            }
        }
        System.out.println("\n0/1 Knapsack: ");
        for(int i = 1; i < itemCount+1; i++){
            System.out.print(solutionArr[i]+" ");
        }

    }
    public void run(){
        fileRead();
        solve();
    }

    public static void main(String[] args) {
        String sample = "ks_4_0";
        String file1 = "ks_19_0";
        String file2 = "ks_200_0";
        long begin = 0, end = 0;
        begin = System.currentTimeMillis();
        ///////////////////////////////////////////////////////////////////

        KnapsackDynamic knapsackProblem =
                new KnapsackDynamic("https://raw.githubusercontent.com/SamedTemiz/samedtemiz.github.io/main/knapsack/"+ file2);

        knapsackProblem.run();

        ///////////////////////////////////////////////////////////////////
        end = System.currentTimeMillis();
        System.out.println("\nWorking Time: " + ((double) (end - begin) / 100+" ms"));
    }
}
