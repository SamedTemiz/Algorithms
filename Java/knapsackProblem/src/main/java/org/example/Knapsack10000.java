package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Knapsack10000{
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://raw.githubusercontent.com/merttfazli/Knapsack/main/ks_10000_0");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String[] firstLine = reader.readLine().split("\\s+");
        int n = Integer.parseInt(firstLine[0]);
        int capacity = Integer.parseInt(firstLine[1]);
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] line = reader.readLine().split("\\s+");
            int value = Integer.parseInt(line[0]);
            int weight = Integer.parseInt(line[1]);
            items.add(new Item(value, weight));
        }
        reader.close();

        // Eşyaları ağırlık/fayda oranına göre sırala
        items.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                double r1 = o1.value / (double) o1.weight;
                double r2 = o2.value / (double) o2.weight;
                return Double.compare(r2, r1);
            }
        });

        // Greedy algoritmasını kullanarak maksimum değeri ve optimal çözümü hesapla
        int maxVal = 0;
        ArrayList<Integer> includedItems = new ArrayList<>();
        for (Item item : items) {
            if (item.weight <= capacity) {
                includedItems.add(items.indexOf(item));
                maxVal += item.value;
                capacity -= item.weight;
            }
        }

        // Sonucu yazdır
        System.out.println("Optimal value değeri: " + maxVal);
        System.out.print("Optimal çözüm : ");
        for (int i = 0; i < n; i++) {
            System.out.print(includedItems.contains(i) ? "1 " : "0 ");
        }
        System.out.println();
        System.out.print("Optimal çözüme dahil edilen itemler: ");
        for (int index : includedItems) {
            System.out.print(items.get(index).value + " ");
        }
        System.out.print("\nOptimal çözüme dahil edilen itemler: ");
        for (int index : includedItems) {
            System.out.println(items.get(index).weight + " ");
        }
    }

    static class Item {
        int value;
        int weight;

        public Item(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }
}