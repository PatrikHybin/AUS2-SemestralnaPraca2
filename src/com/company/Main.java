package com.company;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Random r = new Random();
        //r.setSeed(100);
        //r.nextInt() % (100 - 1) + 1
        TwoThreeTree<PCRTest> tree = new TwoThreeTree<>();
        ArrayList<PCRTest> pcrTests = new ArrayList<>();
        int count = 0;
        int chance = 100;
        while (count != 200000) {
            if (count == 100000) {
                System.out.println("100k");
                chance = 50;
            }
            count++;
            //insert
            if (r.nextInt(100) + 1 <= chance) {
                PCRTest test = new PCRTest(UUID.randomUUID());
                pcrTests.add(test);
                tree.insert(test);
            } //delete
            else {
                if (pcrTests.size() != 0) {
                    int index = r.nextInt(pcrTests.size());
                    tree.delete(pcrTests.get(index));
                    pcrTests.remove(index);
                }
            }


        }
        Collections.sort(pcrTests);
        ArrayList<PCRTest> inOrderTests;
        inOrderTests = tree.inOrder();
        if (inOrderTests.size() != pcrTests.size()) {
            System.out.println("sizes dont match");
        }
        for (int i = 0; i < inOrderTests.size(); i++) {
            if (!pcrTests.get(i).equals(inOrderTests.get(i))) {
                System.out.println(pcrTests.get(i).getTestCode());
                System.out.println(inOrderTests.get(i).getTestCode());
                System.out.println("ERROR");
                break;
            }
        }
        System.out.println(pcrTests.size());
        System.out.println(tree.inOrder().size());

    }
}
