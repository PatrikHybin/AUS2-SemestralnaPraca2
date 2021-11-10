package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

public class TwoThreeTreeTester {
    private TwoThreeTree<PCRTestUUID> tree = new TwoThreeTree<>();
    private ArrayList<PCRTestUUID> pcrTests = new ArrayList<>();
    private ArrayList<PCRTestUUID> inOrderTests = new ArrayList<>();

    public TwoThreeTreeTester() {

    }

    public void testInsertDelete() {
        Random r = new Random();
        //r.setSeed(100);
        //r.nextInt() % (100 - 1) + 1

        int count = 0;
        int chance = 100;
        while (count != 200000) {
            if (count == 100000) {
                System.out.println("100k");
                chance = 30;
            }
            if (count == 120000) {
                System.out.println("120k");
            }
            if (count == 140000) {
                System.out.println("140k");
            }
            if (count == 160000) {
                System.out.println("160k");
            }
            if (count == 180000) {
                System.out.println("180k");
            }
            count++;
            //insert
            if (r.nextInt(100) + 1 <= chance) {
                PCRTestData test = new PCRTestData(UUID.randomUUID());
                PCRTestUUID pcrTestUUID = new PCRTestUUID(test);
                pcrTests.add(pcrTestUUID);
                tree.insert(pcrTestUUID);
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
        inOrderTests = tree.inOrder();
        if (inOrderTests.size() != pcrTests.size()) {
            System.out.println("sizes dont match");
        }
        for (int i = 0; i < inOrderTests.size(); i++) {
            if (!pcrTests.get(i).equals(inOrderTests.get(i))) {
                System.out.println(pcrTests.get(i).getPcrTestData().getTestCode());
                System.out.println(inOrderTests.get(i).getPcrTestData().getTestCode());
                System.out.println("ERROR");
                break;
            }
        }
        System.out.println(pcrTests.size());
        System.out.println(tree.inOrder().size());
    }

    public void testIntervalSearch() {
        Random r = new Random();
        int min = r.nextInt(pcrTests.size());
        PCRTestUUID pcrTestUUIDmin = pcrTests.get(min);
        int max = r.nextInt(pcrTests.size() - min);
        PCRTestUUID pcrTestUUIDmax = pcrTests.get(min + max);

        ArrayList<PCRTestUUID> inOrderIntervalTests;
        inOrderIntervalTests = tree.getInterval(pcrTestUUIDmin, pcrTestUUIDmax);
        int index = inOrderTests.indexOf(inOrderIntervalTests.get(0));
        int count = 0;
        for (int i = 0; i < inOrderIntervalTests.size(); i++) {
            if (inOrderIntervalTests.get(i).getPcrTestData().getTestCode() != inOrderTests.get(i + index).getPcrTestData().getTestCode()) {
                System.out.println("Bad interval search");
            } else {
                count++;
                //System.out.println(inOrderIntervalTests.get(i).getPcrTestData().getTestCode());
            }
        }
        System.out.println();
        System.out.println(inOrderIntervalTests.size());
        System.out.println(count);
    }
}
