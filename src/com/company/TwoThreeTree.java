package com.company;

import java.lang.reflect.Array;
import java.util.*;

public class TwoThreeTree<T extends Comparable<T>> {
    private TwoThreeNode<T> root;

    public TwoThreeTree() {
        root = null;
    }
    //Implementovane podla prednasky
    public boolean insert(T data) {
        if (root == null) {
            root = new TwoThreeNode<>(data);
            return true;
        }

        if (root.getLeftData() == null && root.getRightData() == null) {
            root.setLeftData(data);
            return true;
        }

        TwoThreeNode<T> node;
        if (find(data) == null) {
            node = findLeafNode(data);
            ArrayList<T> dataList;
            //leaf is 2node
            if (node.getRightData() == null) {
                node.setRightData(data);
                CorrectLeftAndRightData(node);
                return true;
            }
            //leaf is 3node
            else {
                TwoThreeNode<T> pushedNode = null;
                int branch = 0;
                while (node != null) {
                    if (pushedNode != null) {
                        data = pushedNode.getLeftData();
                    }
                    dataList = new ArrayList<>(Arrays.asList(node.getLeftData(), node.getRightData(), data));
                    Collections.sort(dataList);
                    T leftData = dataList.get(0);
                    T centerData = dataList.get(1);
                    T rightData = dataList.get(2);

                    if (pushedNode == null) {
                        pushedNode = new TwoThreeNode<>(centerData);
                        Split(pushedNode, leftData, rightData);
                    } else {
                        pushedNode = Split(node, pushedNode, branch);
                    }

                    if (node == root) {
                        root = pushedNode;
                        pushedNode.getLeftSon().setParent(pushedNode);
                        pushedNode.getCenterSon().setParent(pushedNode);
                        return true;

                    } else {
                        if (node.getParent().getRightData() == null) {
                            //2node parent insert from left
                            if (node == node.getParent().getLeftSon()) {
                                branch = -1;
                            } //2node parent insert from right
                            else {
                                branch = 1;
                            }

                            node = node.getParent();

                            node.setRightData(pushedNode.getLeftData());

                            if (branch == -1) {
                                node.setLeftSon(pushedNode.getLeftSon());
                                node.setRightSon(node.getCenterSon());
                                node.setCenterSon(pushedNode.getCenterSon());

                                node.getLeftSon().setParent(node);
                            }
                            if (branch == 1) {
                                node.setCenterSon(pushedNode.getLeftSon());
                                node.setRightSon(pushedNode.getCenterSon());

                                node.getRightSon().setParent(node);
                            }

                            node.getCenterSon().setParent(node);

                            CorrectLeftAndRightData(node);
                            return true;
                        } //parent 3node
                        else {
                            if (node == node.getParent().getLeftSon()) {
                                branch = -1;
                            }
                            if (node == node.getParent().getCenterSon()) {
                                branch = 0;
                            }
                            if (node == node.getParent().getRightSon()) {
                                branch = 1;
                            }
                            node = node.getParent();
                        }
                    }
                }
            }
        }
        return false;
    }

    private void Split(TwoThreeNode<T> node, T leftData, T rightData) {
        TwoThreeNode<T> leftNode = new TwoThreeNode<>(leftData);
        TwoThreeNode<T> centerNode = new TwoThreeNode<>(rightData);

        node.setLeftData(node.getLeftData());
        node.setRightData(null);

        leftNode.setParent(node);
        centerNode.setParent(node);

        node.setLeftSon(leftNode);
        node.setCenterSon(centerNode);
    }

    private TwoThreeNode<T> Split(TwoThreeNode<T> node, TwoThreeNode<T> pushedNode, int branch) {
        ArrayList<T> dataList = new ArrayList<>(Arrays.asList(node.getLeftData(), node.getRightData(), pushedNode.getLeftData()));
        Collections.sort(dataList);
        T leftData = dataList.get(0);
        T centerData = dataList.get(1);
        T rightData = dataList.get(2);
        TwoThreeNode<T> newNode = new TwoThreeNode<>(centerData);
        node.setRightData(null);

        if (pushedNode.getLeftSon().getParent() != pushedNode) {
            pushedNode.getLeftSon().setParent(pushedNode);
        }
        if (pushedNode.getCenterSon().getParent() != pushedNode) {
            pushedNode.getCenterSon().setParent(pushedNode);
        }

        if (branch == -1) {
            pushedNode.setLeftData(leftData);

            node.setLeftData(rightData);
            node.setLeftSon(node.getCenterSon());
            node.setCenterSon(node.getRightSon());
            node.setRightSon(null);

            newNode.setLeftSon(pushedNode);
            newNode.setCenterSon(node);

            return newNode;
        } else if (branch == 1) {
            pushedNode.setLeftData(rightData);

            node.setLeftData(leftData);
            node.setRightSon(null);

            newNode.setLeftSon(node);
            newNode.setCenterSon(pushedNode);

            return newNode;
        } else {
            node.setCenterSon(pushedNode.getLeftSon());

            pushedNode.setLeftData(rightData);
            pushedNode.setLeftSon(pushedNode.getCenterSon());
            pushedNode.setCenterSon(node.getRightSon());
            pushedNode.getCenterSon().setParent(pushedNode);

            node.setLeftData(leftData);
            node.setRightSon(null);
            node.getCenterSon().setParent(node);

            newNode.setLeftSon(node);
            newNode.setCenterSon(pushedNode);

            return newNode;
        }
    }

    private void CorrectLeftAndRightData(TwoThreeNode<T> node) {
        if (node.getLeftData().compareTo(node.getRightData()) > 0) {
            T tmpData = node.getRightData();
            node.setRightData(node.getLeftData());
            node.setLeftData(tmpData);
        }
    }

    private TwoThreeNode<T> findLeafNode(T data) {
        int resultLeft;
        int resultRight;
        TwoThreeNode<T> node = root;
        while (!node.isLeaf()) {
            resultRight = 100;
            resultLeft = data.compareTo(node.getLeftData());
            if (node.getRightData() != null) {
                resultRight = data.compareTo(node.getRightData());
            }
            if (resultLeft < 0) {
                node = node.getLeftSon();
            } else if (node.getRightData() == null || resultRight < 0) {
                node = node.getCenterSon();
            } else {
                node = node.getRightSon();
            }
        }
        return node;
    }

    //Implementovane podla prednasky
    public T find(T data) {
        TwoThreeNode<T> node;
        node = findNode(data);
        T returnData = null;
        if (node != null) {
            if (node.getLeftData().compareTo(data) == 0) {
                returnData = node.getLeftData();
            }
            if (node.getRightData() != null && node.getRightData().compareTo(data) == 0) {
                returnData = node.getRightData();
            }
        }
        return returnData;
    }
    
    private TwoThreeNode<T> findNode(T data) {
        if (root == null) {
            return null;
        } else {
            int resultLeft;
            int resultRight = 100;
            TwoThreeNode<T> node = root;
            while (node != null) {
                resultLeft = data.compareTo(node.getLeftData());
                if (node.getRightData() != null) {
                    resultRight = data.compareTo(node.getRightData());
                }
                if (resultLeft == 0 || resultRight == 0) {
                    return node;
                } else {
                    if (resultLeft < 0) {
                        node = node.getLeftSon();
                    } else if (node.getRightData() == null || resultRight < 0) {
                        node = node.getCenterSon();
                    } else {
                        node = node.getRightSon();
                    }
                }
            }
        }
        return null;
    }
    //Implementovane pomocou clanku z https://www.cs.princeton.edu/~dpw/courses/cos326-12/ass/2-3-trees.pdf?fbclid=IwAR3SMu7v3IbeMZx0d0tDSglu7Kn4kggtdQixRNW29PemWOi3tJXITn-iH_I
    public void delete(T data) {
        TwoThreeNode<T> node = findNode(data);
        TwoThreeNode<T> predecessor = inOrderPredecessor(node, data);
        if (root == null || node == null) {
            return;
        }
        if (node.isLeaf()) {
            if (node.getRightData() != null) {
                if (node.getLeftData().compareTo(data) == 0) {
                    node.setLeftData(node.getRightData());

                }
                node.setRightData(null);
                return;
            } else {
                node.setLeftData(null);
                if (node == root) {
                    return;
                }
            }
        } else {

            if (predecessor == null) {
                System.out.println("predecessor is null problem");
                return;
            }
            if (node.getRightData() != null) {
                if (node.getLeftData().compareTo(data) == 0) {
                    if (predecessor.getRightData() != null) {
                        node.setLeftData(predecessor.getRightData());
                        predecessor.setRightData(null);
                        return;
                    } else {
                        node.setLeftData(predecessor.getLeftData());
                        predecessor.setLeftData(null);
                    }

                } else {
                    if (predecessor.getRightData() != null) {
                        node.setRightData(predecessor.getRightData());
                        predecessor.setRightData(null);
                        return;
                    } else {
                        node.setRightData(predecessor.getLeftData());
                        predecessor.setLeftData(null);
                    }
                }
            } else {
                if (predecessor.getRightData() != null) {
                    node.setLeftData(predecessor.getRightData());
                    predecessor.setRightData(null);
                    return;
                } else {
                    node.setLeftData(predecessor.getLeftData());
                    predecessor.setLeftData(null);
                }
            }
            node = predecessor;
        }

        int branch = 0;

        while (node != null) {
            if (node.getParent() != null) {
                if (node == node.getParent().getLeftSon()) {
                    branch = -1;
                }
                if (node == node.getParent().getCenterSon()) {
                    branch = 0;
                }
                if (node == node.getParent().getRightSon()) {
                    branch = 1;
                }
                switch (caseNumber(node, branch)) {
                    case 1:
                        node = deleteCase1(node, branch);
                        break;
                    case 2:
                        deleteCase2(node, branch);
                        return;
                    case 31:
                        deleteCase3a(node, branch);
                        return;
                    case 32:
                        deleteCase3b(node, branch);
                        return;
                    case 41:
                        deleteCase4a(node, branch);
                        return;
                    case 42:
                        deleteCase4b(node, branch);
                        return;
                    case -1:
                        System.out.println("Didnt find case that would match problem");
                        break;
                    default:
                        break;
                }
                if (node == root) {
                    root = node.getLeftSon();
                    return;
                }
            }
        }
    }

    private int caseNumber(TwoThreeNode<T> node, int branch) {
        if (node.getParent().getRightData() == null && node.getParent().getCenterSon().getRightData() == null && branch == -1) {
            return 1;
        }
        if (node.getParent().getRightData() == null && node.getParent().getLeftSon().getRightData() == null && branch == 0) {
            return 1;
        }
        if (node.getParent().getRightData() == null && node.getParent().getCenterSon().getRightData() != null && branch == -1) {
            return 2;
        }
        if (node.getParent().getRightData() == null && node.getParent().getLeftSon().getRightData() != null && branch == 0) {
            return 2;
        }
        if (node.getParent().getRightData() != null && node.getParent().getCenterSon().getRightData() == null && branch == -1) {
            return 31;
        }
        if (node.getParent().getRightData() != null && node.getParent().getLeftSon().getRightData() == null && branch == 0) {
            return 31;
        }
        if (node.getParent().getRightData() != null && node.getParent().getCenterSon().getRightData() == null && branch == 1) {
            return 32;
        }
        if (node.getParent().getRightData() != null && node.getParent().getCenterSon().getRightData() != null && branch == -1) {
            return 41;
        }
        if (node.getParent().getRightData() != null && node.getParent().getLeftSon().getRightData() != null && branch == 0) {
            return 41;
        }
        if (node.getParent().getRightData() != null && node.getParent().getCenterSon().getRightData() != null && branch == 1) {
            return 42;
        }
        return -1;
    }

    private TwoThreeNode<T> deleteCase1(TwoThreeNode<T> node, int branch) {
        TwoThreeNode<T> tmp;
        if (branch == -1) {
            tmp = node.getParent().getCenterSon();
            node.setLeftData(node.getParent().getLeftData());
            node.setRightData(tmp.getLeftData());
            CorrectLeftAndRightData(node);
            node.getParent().setLeftData(null);

            node.setCenterSon(tmp.getLeftSon());
            node.setRightSon(tmp.getCenterSon());

            if (node.getCenterSon() != null) {
                node.getCenterSon().setParent(node);
            }
            if (node.getRightSon() != null) {
                node.getRightSon().setParent(node);
            }
        }
        if (branch == 0) {
            tmp = node.getParent().getLeftSon();
            tmp.setRightData(node.getParent().getLeftData());
            node.getParent().setLeftData(null);

            tmp.setRightSon(node.getLeftSon());
            if (tmp.getRightSon() != null) {
                tmp.getRightSon().setParent(tmp);
            }
        }
        node = node.getParent();
        node.setCenterSon(null);
        return node;
    }

    private void deleteCase2(TwoThreeNode<T> node, int branch) {
        TwoThreeNode<T> tmp;

        if (branch == -1) {
            tmp = node.getParent().getCenterSon();

            node.setLeftData(node.getParent().getLeftData());
            node.getParent().setLeftData(tmp.getLeftData());
            tmp.setLeftData(tmp.getRightData());

            tmp.setRightData(null);
            node.setCenterSon(tmp.getLeftSon());
            tmp.setLeftSon(tmp.getCenterSon());
            tmp.setCenterSon(tmp.getRightSon());
            tmp.setRightSon(null);

            if (node.getCenterSon() != null) {
                node.getCenterSon().setParent(node);
            }
        }
        if (branch == 0) {
            tmp = node.getParent().getLeftSon();

            node.setLeftData(node.getParent().getLeftData());
            node.getParent().setLeftData(tmp.getRightData());
            tmp.setLeftData(tmp.getLeftData());

            tmp.setRightData(null);
            node.setCenterSon(node.getLeftSon());
            node.setLeftSon(tmp.getRightSon());
            tmp.setRightSon(null);

            if (node.getLeftSon() != null) {
                node.getLeftSon().setParent(node);
            }
        }
    }

    private void deleteCase3a(TwoThreeNode<T> node, int branch) {
        TwoThreeNode<T> tmp;
        if (branch == -1) {
            tmp = node.getParent().getCenterSon();
            node.setLeftData(node.getParent().getLeftData());
            node.setRightData(tmp.getLeftData());

            node.getParent().setLeftData(node.getParent().getRightData());
            node.getParent().setRightData(null);

            node.setCenterSon(tmp.getLeftSon());
            node.setRightSon(tmp.getCenterSon());
            node.getParent().setCenterSon(node.getParent().getRightSon());
            node.getParent().setRightSon(null);

            if (node.getLeftSon() != null) {
                node.getLeftSon().setParent(node);
            }
            if (node.getCenterSon() != null) {
                node.getCenterSon().setParent(node);
            }
            if (node.getRightSon() != null) {
                node.getRightSon().setParent(node);
            }

        }
        if (branch == 0) {
            tmp = node.getParent().getLeftSon();
            tmp.setRightData(node.getParent().getLeftData());

            node.getParent().setLeftData(node.getParent().getRightData());
            node.getParent().setRightData(null);

            tmp.setRightSon(node.getLeftSon());
            if (tmp.getRightSon() != null) {
                tmp.getRightSon().setParent(tmp);
            }

            node.getParent().setCenterSon(node.getParent().getRightSon());
            node.getParent().setRightSon(null);
        }
    }

    private void deleteCase3b(TwoThreeNode<T> node, int branch) {
        TwoThreeNode<T> tmp;
        if (branch == 1) {
            tmp = node.getParent().getCenterSon();
            tmp.setRightData(node.getParent().getRightData());
            node.getParent().setRightData(null);

            tmp.setRightSon(node.getLeftSon());
            node.getParent().setRightSon(null);

            if (node.getLeftSon() != null) {
                node.getLeftSon().setParent(tmp);
            }
        }
    }

    private void deleteCase4a(TwoThreeNode<T> node, int branch) {
        TwoThreeNode<T> tmp;
        if (branch == -1) {
            tmp = node.getParent().getCenterSon();
            node.setLeftData(node.getParent().getLeftData());
            node.getParent().setLeftData(tmp.getLeftData());

            tmp.setLeftData(tmp.getRightData());
            tmp.setRightData(null);

            node.setCenterSon(tmp.getLeftSon());
            tmp.setLeftSon(tmp.getCenterSon());
            tmp.setCenterSon(tmp.getRightSon());
            tmp.setRightSon(null);

            if (node.getLeftSon() != null) {
                node.getCenterSon().setParent(node);
            }
        }
        if (branch == 0) {
            tmp = node.getParent().getLeftSon();
            node.setLeftData(node.getParent().getLeftData());
            node.getParent().setLeftData(tmp.getRightData());

            tmp.setRightData(null);

            node.setCenterSon(node.getLeftSon());
            node.setLeftSon(tmp.getRightSon());
            tmp.setRightSon(null);

            if (node.getLeftSon() != null) {
                node.getLeftSon().setParent(node);
            }
        }
    }

    private void deleteCase4b(TwoThreeNode<T> node, int branch) {
        TwoThreeNode<T> tmp;

        if (branch == 1) {
            tmp = node.getParent().getCenterSon();
            node.setLeftData(node.getParent().getRightData());
            node.getParent().setRightData(tmp.getRightData());

            tmp.setRightData(null);

            node.setCenterSon(node.getLeftSon());
            node.setLeftSon(tmp.getRightSon());
            tmp.setRightSon(null);

            if (node.getLeftSon() != null) {
                node.getLeftSon().setParent(node);
            }

        }
    }

    public ArrayList<T> inOrder() {
        ArrayList<T> list = new ArrayList<>();
        if (root == null || (root.getLeftData() == null && root.getRightData() == null)) {
            System.out.println("There is no root or root has no data(inOrder)");
            return list;
        }
        TwoThreeNode<T> node = root;
        int tmpCompare = -1;
        int level = 0;
        Stack<TwoThreeNode<T>> leftNode = new Stack<>();
        Stack<TwoThreeNode<T>> centerNode = new Stack<>();
        Stack<TwoThreeNode<T>> rightNode = new Stack<>();
        Stack<TwoThreeNode<T>> nodeStack = new Stack<>();

        nodeStack.push(node);

        while (nodeStack.size() != 0) {
            if (nodeStack.peek() != node && node != root) {
                nodeStack.push(node);
            }
            if (node.getLeftSon() != null && (leftNode.isEmpty() || node.getLeftSon() != leftNode.peek())) {
                level++;
                node = node.getLeftSon();
            } else if (node.getCenterSon() != null && (centerNode.isEmpty() || node.getCenterSon() != centerNode.peek())) {
                level++;
                list.add(node.getLeftData());
                node = node.getCenterSon();
            } else if (node.getRightData() != null && node.getRightSon() != null && (rightNode.isEmpty() || node.getRightSon() != rightNode.peek())) {
                level++;
                list.add(node.getRightData());
                node = node.getRightSon();
            } else {
                if (node.getLeftSon() == null || (leftNode.isEmpty() || node.getLeftSon() == leftNode.peek()) && node.getCenterSon() == null || (centerNode.isEmpty() || node.getCenterSon() == centerNode.peek()) && node.getRightSon() == null || node.getRightSon() != null && (rightNode.isEmpty() || node.getRightSon() == rightNode.peek())) {
                    if (node.isLeaf()) {
                        if (tmpCompare == -1) {
                            tmpCompare = level;
                        } else {
                            if (tmpCompare != level) {
                                System.out.println("ERROR");
                            }
                        }
                        list.add(node.getLeftData());
                        if (node.getRightData() != null) {
                            list.add(node.getRightData());
                        }
                        if (node.getParent() != null) {
                            if (node.getParent().getLeftSon() == node) {
                                leftNode.push(node);
                            }
                            if (node.getParent().getCenterSon() == node) {
                                centerNode.push(node);
                            }
                            if (node.getParent().getRightSon() == node) {
                                rightNode.push(node);
                            }
                        }
                    } else {
                        if (!leftNode.isEmpty()) {
                            leftNode.pop();
                        }
                        if (!centerNode.isEmpty()) {
                            centerNode.pop();
                        }
                        if (!rightNode.isEmpty()) {
                            rightNode.pop();
                        }
                        if (node.getParent() != null) {
                            if (node.getParent().getLeftSon() == node) {
                                leftNode.push(node);
                            }
                            if (node.getParent().getCenterSon() == node) {
                                centerNode.push(node);
                            }
                            if (node.getParent().getRightSon() == node) {
                                rightNode.push(node);
                            }
                        }
                    }
                    nodeStack.pop();
                    if (nodeStack.size() != 0) {
                        level--;
                        node = nodeStack.peek();
                    }
                }
            }
        }
        return list;
    }

    private TwoThreeNode<T> inOrderPredecessor(TwoThreeNode<T> node ,T data) {
        if (root == null) {
            return null;
        }
        if (!node.isLeaf()) {
            if (node.getLeftData().compareTo(data) == 0) {
                node = node.getLeftSon();
            } else {
                node = node.getCenterSon();
            }
            while (!node.isLeaf()) {
                if (node.getRightSon() != null) {
                    node = node.getRightSon();
                } else {
                    node = node.getCenterSon();
                }
            }
        }
        return node;
    }

    public ArrayList<T> getInterval(T minData, T maxData) {
        TwoThreeNode<T> node = findIntervalMinNode(minData);
        if (node == null) {
            System.out.println("node is null in getInterval");
        }
        return inOrderIntervalMinMax(node, minData, maxData);
    }

    private TwoThreeNode<T> findIntervalMinNode(T minData) {
        if (root == null) {
            return null;
        } else {
            int resultLeft;
            int resultRight = 100;
            TwoThreeNode<T> node = root;
            TwoThreeNode<T> tmp = root;
            while (node != null) {
                resultLeft = minData.compareTo(node.getLeftData());
                if (node.getRightData() != null) {
                    resultRight = minData.compareTo(node.getRightData());
                }
                tmp = node;
                if (resultLeft == 0 || resultRight == 0) {
                    return node;
                } else {
                    if (resultLeft < 0) {
                        node = node.getLeftSon();
                    } else if (node.getRightData() == null || resultRight < 0) {
                        node = node.getCenterSon();
                    } else {
                        node = node.getRightSon();
                    }
                }
            }

            if (tmp.getRightData() != null) {
                if (tmp.getRightData().compareTo(minData) < 0) {
                    tmp = tmp.getParent();
                }
            } else {
                if (tmp.getLeftData().compareTo(minData) < 0) {
                    tmp = tmp.getParent();
                }
            }
            return tmp;
        }
    }

    private ArrayList<T> inOrderIntervalMinMax(TwoThreeNode<T> minNode, T minData, T maxData) {
        ArrayList<T> list = new ArrayList<>();
        TwoThreeNode<T> node;

        T current = minData;
        node = minNode;

        while (node != null) {
            if (node.isLeaf()) {
                if (node.getRightData() != null) {
                    if (node.getLeftData().compareTo(current) > -1) {
                        if (node.getLeftData().compareTo(maxData) > 0) {
                            break;
                        }
                        list.add(node.getLeftData());
                    }
                    if (node.getRightData().compareTo(maxData) > 0) {
                        break;
                    }
                    list.add(node.getRightData());
                    current = node.getRightData();
                } else {
                    if (node.getLeftData().compareTo(maxData) > 0) {
                        break;
                    }
                    list.add(node.getLeftData());
                    current = node.getLeftData();
                }
                node = inOrderSuccessor(node, current);
            } else {
                if (node.getRightData() != null) {
                    if (node.getLeftData().compareTo(current) > -1) {
                        if (node.getLeftData().compareTo(maxData) > 0) {
                            break;
                        }
                        list.add(node.getLeftData());
                        current = node.getLeftData();
                    } else {
                        if (node.getRightData().compareTo(maxData) > 0) {
                            break;
                        }
                        list.add(node.getRightData());
                        current = node.getRightData();
                    }
                } else {
                    if (node.getLeftData().compareTo(maxData) > 0) {
                        break;
                    }
                    list.add(node.getLeftData());
                    current = node.getLeftData();
                }
                node = inOrderSuccessor(node, current);
            }
        }
        return list;
    }

    private TwoThreeNode<T> inOrderSuccessor(TwoThreeNode<T> node, T data) {
        boolean down = false;
        if (!node.isLeaf()) {
            down = true;
            if (node.getLeftData().compareTo(data) == 0) {
                node = node.getCenterSon();
            }
            if (node.getRightData() != null && node.getRightData().compareTo(data) == 0) {
                node = node.getRightSon();
            }
        }
        while (node != null) {
            if (down) {
                if (node.isLeaf()) {
                    break;
                }
                node = node.getLeftSon();
                continue;
            }
            if (node.getParent() != null) {
                //2 node
                if (node.getParent().getRightData() == null) {
                    if (node.getParent().getLeftSon() == node) {
                        return node.getParent();
                    }
                } //3 node
                else {
                    if (node.getParent().getLeftSon() == node) {
                        return node.getParent();
                    }
                    if (node.getParent().getCenterSon() == node) {
                        return node.getParent();
                    }
                }
            }
            node = node.getParent();
        }
        return node;
    }
}
