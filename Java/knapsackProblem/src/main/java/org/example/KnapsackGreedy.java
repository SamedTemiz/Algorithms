package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class KnapsackGreedy {
    private static String urlString;    //Kaynak dosyanın url'si
    private static int itemCount;       //Eşya sayısı
    private static int maxWeight;       //Çantanın alabildiği maksimum değer
    private static int[] values;        //Eşyaların değerleri
    private static int[] weights;       //Eşyaların ağırlıkları - maliyetleri

    //Veri setini programa ekliyoruz
    public static void fileRead() {
        try {
            //Url nesnesini oluşturuyoruz
            URL url = new URL(urlString);
            BufferedReader read = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            //İlk satırdaki bilgileri alıyoruz
            String firstLine = read.readLine();
            itemCount = Integer.parseInt(firstLine.split(" ")[0]);
            maxWeight = Integer.parseInt(firstLine.split(" ")[1]);

            //Dizilerimiz için bellekte yer açıyoruz
            values = new int[itemCount];
            weights = new int[itemCount];

            //Dosya okuma ve dizileri doldurma
            String line;
            int count = 0;
            while ((line = read.readLine()) != null) {
                int value = Integer.parseInt(line.split(" ")[0]);
                int weight = Integer.parseInt(line.split(" ")[1]);

                values[count] = value;
                weights[count] = weight;

                count++;
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Algoritmayı çalıştırıyoruz
    public int[] solve(int[] values, int[] weights, int maxWeight) {
        int n = values.length;
        int[] solution = new int[n];    //0/1 çözümü için dizi oluşturur.

        // Adım 1 : Greedy Yaklaşımı -> Values / Weight değerlerini hesaplıyoruz
        double[] ratios = new double[n];
        for (int i = 0; i < n; i++) {
            ratios[i] = (double) values[i] / weights[i]; //Her eşya için ratio değeri belirleniyor
        }

        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        int weight = 0;
        for (int i = 0; i < n; i++) {
            int index = indices[i];
            if (weights[index] + weight <= maxWeight) {
                solution[index] = 1;
                weight += weights[index];
            }
        }

        // Adım 2 : En iyi sonuç için arama yap
        while (true) {
            boolean improved = false;
            for (int i = 0; i < n; i++) {
                if (solution[i] == 1) {
                    solution[i] = 0;
                    int newWeight = weight - weights[i];
                    int newProfit = calculateProfit(solution, values);
                    if (newWeight <= maxWeight && newProfit > calculateProfit(solution, values)) {
                        weight = newWeight;
                        improved = true;
                    } else {
                        solution[i] = 1;
                    }
                }
            }
            if (!improved) {
                break;
            }
        }

        return solution;
    }
    private int calculateProfit(int[] solution, int[] profits) {
        int n = solution.length;
        int profit = 0;
        for (int i = 0; i < n; i++) {
            if (solution[i] == 1) {
                profit += profits[i];
            }
        }
        return profit;
    }
    private int calculateWeight(int[] solution, int[] weights) {
        int n = solution.length;
        int weight = 0;
        for (int i = 0; i < n; i++) {

            if (solution[i] == 1) {
                weight += weights[i];
            }
        }
        return weight;
    }

    public static void main(String[] args) throws InterruptedException {
        long begin = 0, end = 0;
        begin = System.currentTimeMillis();
        ///////////////////////////////////////////////////////////////////
        urlString = "https://raw.githubusercontent.com/SamedTemiz/samedtemiz.github.io/main/knapsack/ks_10000_0";
        fileRead();
        KnapsackGreedy knapsack = new KnapsackGreedy();

        int[] solution = knapsack.solve(values, weights, maxWeight);
        int totalValue = knapsack.calculateProfit(solution, values);

        System.out.println("Total Value: "+ totalValue);
        System.out.println("Selected Item: ");
        for(int i = 0; i < itemCount; i++){
            if(solution[i] == 1){
                System.out.print(i+1+" ");
            }
        }
        System.out.println("\n0/1 Knapsack: ");
        for(int x : solution){
            System.out.print(x+" ");
        }
        ///////////////////////////////////////////////////////////////////
        end = System.currentTimeMillis();
        System.out.println("\nWorking Time: " + ((double) (end - begin) / 100+" ms"));
    }
}

