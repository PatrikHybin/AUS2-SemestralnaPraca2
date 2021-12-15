package data;

import com.UFile;
import com.IRecord;

import java.util.*;

public class BTree<T extends IRecord<T>> implements IRecord<BTree<T>> {
    private static long nullValue = -100;

    //private BTreeNode<T> root;
    private long rootAddress = nullValue;


    public int dataNum;

    public static final int case1 = 1;
    public static final int case2 = 2;
    public static final int case3a = 31;
    public static final int case3b = 32;
    public static final int case4a = 41;
    public static final int case4b = 42;
    private Class<T> gClass;

    private UFile<BTreeNode<T>> file;
    private UFile<T> dataFile;

    public BTree(Class<T> gClass) {
        rootAddress = nullValue;
        this.gClass = gClass;

    }

    public BTree(Class<T> gClass, String nodePathFile, String nodePathFileFS, UFile<T> dataFile) {
        rootAddress = nullValue;
        this.gClass = gClass;
        createFile(nodePathFile, nodePathFileFS);
        this.dataFile = dataFile;
        if (this.file.getLength() != 0 && rootAddress == nullValue) {
            rootAddress = file.loadTreeData();
        } else {
            file.createTreeData();
        }
    }

    public void createFile(String pathData, String pathFreeSpace) {
        this.file = new UFile<>(pathData, pathFreeSpace, BTreeNode.class);
    }

    //Implementovane podla pseudokodu z http://frdsa.fri.uniza.sk/~jankovic/WEB/struktury/ADS/interaktivny_mod/ads_2_3_strom_im.html
    public boolean insert(T data) {

        if (rootAddress == nullValue) {
            BTreeNode<T> tmpNode = new BTreeNode<T>(data, this.gClass);
            file.insert(tmpNode);
            rootAddress = tmpNode.getAddress();
            dataNum++;
            file.updateTreeData(rootAddress);
            return true;
        }
        BTreeNode<T> tmpNode = file.find(rootAddress);
        if (tmpNode.getLeftData() == nullValue && file.find(rootAddress).getRightData() == nullValue) {
            tmpNode.setLeftData(data.getAddress());
            file.insertAtAddress(tmpNode, tmpNode.getAddress());

            dataNum++;
            rootAddress = tmpNode.getAddress();
            file.updateTreeData(rootAddress);
            return true;
        }

        BTreeNode<T> node;
        if (find(data) == null) {
            //System.out.println("mas kde insert");
            dataNum++;
            node = findLeafNode(data);
            ArrayList<T> dataList;
            //leaf is 2node
            if (node.getRightData() == nullValue) {
                node.setRightData(data.getAddress());
                CorrectLeftAndRightData(node);
                file.insertAtAddress(node, node.getAddress());
                return true;
            }
            //leaf is 3node
            else {
                BTreeNode<T> pushedNode = null;
                int branch = 0;
                while (node != null) {
                    if (pushedNode != null) {
                        data = dataFile.find(pushedNode.getLeftData());
                    }
                    dataList = new ArrayList<>(Arrays.asList(dataFile.find(node.getLeftData()), dataFile.find(node.getRightData()), data));
                    Collections.sort(dataList);
                    T leftData = dataList.get(0);
                    T centerData = dataList.get(1);
                    T rightData = dataList.get(2);
                    int split = -1;
                    if (pushedNode == null) {
                        pushedNode = new BTreeNode<T>(centerData, this.gClass);
                        file.insert(pushedNode);
                        Split(pushedNode, leftData, rightData);
                        split = 0;
                    } else {
                        pushedNode = Split(node, pushedNode, branch);
                        split = 1;
                    }

                    if (node.getAddress() == rootAddress) {
                        if (split == 0) {
                            file.delete(rootAddress);
                        }
                        tmpNode = pushedNode;
                        tmpNode.setParent(nullValue);
                        changeParentsAddressAtSon(pushedNode.getLeftSon(), pushedNode.getAddress());
                        changeParentsAddressAtSon(pushedNode.getCenterSon(), pushedNode.getAddress());

                        file.insertAtAddress(pushedNode, pushedNode.getAddress());
                        rootAddress = tmpNode.getAddress();
                        file.updateTreeData(rootAddress);
                        return true;
                    } else {
                        BTreeNode<T> nodeGetParent = file.find(node.getParent());
                        //System.out.println(nodeGetParent.toString() + " " + this.rootAddress);
                        if (nodeGetParent.getRightData() == nullValue) {
                            //2node parent insert from left
                            if (node.getAddress() == nodeGetParent.getLeftSon()) {
                                branch = -1;
                            } //2node parent insert from right
                            else {
                                branch = 1;
                            }
                            //node = node.getParent();
                            node = nodeGetParent;

                            node.setRightData(pushedNode.getLeftData());

                            if (branch == -1) {

                                if (file.find(node.getLeftSon()).isLeaf()) {
                                    file.delete(node.getLeftSon());
                                }

                                node.setLeftSon(pushedNode.getLeftSon());

                                node.setRightSon(node.getCenterSon());

                                node.setCenterSon(pushedNode.getCenterSon());

                                file.insertAtAddress(node, node.getAddress());
                                //file.insertAtAddress(node, node.getAddress());
                                //node.getLeftSon().setParent(node);
                                changeParentsAddressAtSon(node.getLeftSon(), node.getAddress());
                            }
                            if (branch == 1) {
                                if (file.find(node.getCenterSon()).isLeaf()) {
                                    file.delete(node.getCenterSon());
                                }
                                node.setCenterSon(pushedNode.getLeftSon());

                                node.setRightSon(pushedNode.getCenterSon());

                                //node.getRightSon().setParent(node);
                                changeParentsAddressAtSon(node.getRightSon(), node.getAddress());

                            }
                            //node.getCenterSon().setParent(node);
                            changeParentsAddressAtSon(node.getCenterSon(), node.getAddress());

                            CorrectLeftAndRightData(node);

                            file.delete(pushedNode.getAddress());
                            file.insertAtAddress(node, node.getAddress());
                            rootAddress = tmpNode.getAddress();
                            file.updateTreeData(rootAddress);

                            return true;
                        } //parent 3node
                        else {
                            if (node.getAddress() == nodeGetParent.getLeftSon()) {
                                branch = -1;
                            }
                            if (node.getAddress() == nodeGetParent.getCenterSon()) {
                                branch = 0;
                            }
                            if (node.getAddress() == nodeGetParent.getRightSon()) {
                                branch = 1;
                            }

                            //node = node.getParent();
                            node = nodeGetParent;
                        }
                    }
                }
            }
        }
        return false;
    }


    private void Split(BTreeNode<T> node, T leftData, T rightData) {
        BTreeNode<T> leftNode = new BTreeNode<>(leftData, this.gClass);
        BTreeNode<T> centerNode = new BTreeNode<>(rightData, this.gClass);

        node.setLeftData(node.getLeftData());
        node.setRightData(nullValue);

        //leftNode.setParent(node);
        leftNode.setParent(node.getAddress());
        file.insert(leftNode);
        //centerNode.setParent(node);
        centerNode.setParent(node.getAddress());
        file.insert(centerNode);

        //node.setLeftSon(leftNode);
        node.setLeftSon(leftNode.getAddress());
        //node.setCenterSon(centerNode);
        node.setCenterSon(centerNode.getAddress());

        file.insertAtAddress(node, node.getAddress());
    }

    private BTreeNode<T> Split(BTreeNode<T> node, BTreeNode<T> pushedNode, int branch) {
        ArrayList<T> dataList = new ArrayList<>(Arrays.asList(dataFile.find(node.getLeftData()), dataFile.find(node.getRightData()), dataFile.find(pushedNode.getLeftData())));
        Collections.sort(dataList);
        T leftData = dataList.get(0);
        T centerData = dataList.get(1);
        T rightData = dataList.get(2);
        BTreeNode<T> newNode = new BTreeNode<>(centerData, this.gClass);
        file.insert(newNode);
        node.setRightData(nullValue);


        if (file.find(pushedNode.getLeftSon()).getParent() != pushedNode.getAddress()) {
            changeParentsAddressAtSon(pushedNode.getLeftSon(), pushedNode.getAddress());
        }

        if (file.find(pushedNode.getCenterSon()).getParent() != pushedNode.getAddress()) {
            changeParentsAddressAtSon(pushedNode.getCenterSon(), pushedNode.getAddress());
        }

        if (branch == -1) {
            pushedNode.setLeftData(leftData.getAddress());

            node.setLeftData(rightData.getAddress());

            if (file.find(node.getLeftSon()).isLeaf()) {
                file.delete(node.getLeftSon());
            }

            node.setLeftSon(node.getCenterSon());
            node.setCenterSon(node.getRightSon());


            node.setRightSon(nullValue);

            newNode.setLeftSon(pushedNode.getAddress());
            newNode.setCenterSon(node.getAddress());


        } else if (branch == 1) {
            pushedNode.setLeftData(rightData.getAddress());

            node.setLeftData(leftData.getAddress());

            if (file.find(node.getRightSon()).isLeaf()) {
                file.delete(node.getRightSon());
            }
            node.setRightSon(nullValue);


            newNode.setLeftSon(node.getAddress());
            newNode.setCenterSon(pushedNode.getAddress());


        } else {
            if (file.find(node.getCenterSon()).isLeaf()) {
                file.delete(node.getCenterSon());
            }
            node.setCenterSon(pushedNode.getLeftSon());

            pushedNode.setLeftData(rightData.getAddress());
            pushedNode.setLeftSon(pushedNode.getCenterSon());
            pushedNode.setCenterSon(node.getRightSon());

            //pushedNode.getCenterSon().setParent(pushedNode);
            changeParentsAddressAtSon(pushedNode.getCenterSon(), pushedNode.getAddress());


            node.setLeftData(leftData.getAddress());
            //node.setRightSon(null);

            node.setRightSon(nullValue);
            //node.getCenterSon().setParent(node);
            changeParentsAddressAtSon(node.getCenterSon(), node.getAddress());

            newNode.setLeftSon(node.getAddress());
            newNode.setCenterSon(pushedNode.getAddress());

        }
        file.insertAtAddress(pushedNode, pushedNode.getAddress());
        file.insertAtAddress(node, node.getAddress());
        file.insertAtAddress(newNode, newNode.getAddress());
        return newNode;
    }

    private void changeParentsAddressAtSon(long sonAddress, long parentAddress) {
        BTreeNode<T> son = file.find(sonAddress);
        son.setParent(parentAddress);
        file.insertAtAddress(son, sonAddress);
    }


    private void CorrectLeftAndRightData(BTreeNode<T> node) {
        if (dataFile.find(node.getLeftData()).compareTo(dataFile.find(node.getRightData())) > 0) {
            T tmpData = dataFile.find(node.getRightData());
            node.setRightData(node.getLeftData());
            node.setLeftData(tmpData.getAddress());
            file.insertAtAddress(node, node.getAddress());
        }
    }

    private BTreeNode<T> findLeafNode(T data) {
        int resultLeft;
        int resultRight;
        BTreeNode<T> node = file.find(rootAddress);
        while (!node.isLeaf()) {
            resultRight = 100;
            resultLeft = data.compareTo(dataFile.find(node.getLeftData()));
            if (node.getRightData() != nullValue) {
                resultRight = data.compareTo(dataFile.find(node.getRightData()));
            }
            if (resultLeft < 0) {
                node = findInFileAtAddress(node.getLeftSon());
            } else if (node.getRightData() == nullValue || resultRight < 0) {
                node = findInFileAtAddress(node.getCenterSon());
            } else {
                node = findInFileAtAddress(node.getRightSon());
            }

        }
        return node;
    }

    private BTreeNode<T> findInFileAtAddress(long address) {
        if (address == nullValue) {
            return null;
        }
        return file.find(address);
    }

    //Implementovane podla prednasky/pseudokodu z http://frdsa.fri.uniza.sk/~jankovic/WEB/struktury/ADS/interaktivny_mod/ads_2_3_strom_im.html
    public T find(T data) {
        BTreeNode<T> node;
        node = findNode(data);
        T returnData = null;
        if (node != null) {
            if (dataFile.find(node.getLeftData()).compareTo(data) == 0) {
                returnData = dataFile.find(node.getLeftData());
            }
            if (node.getRightData() != nullValue && dataFile.find(node.getRightData()).compareTo(data) == 0) {
                returnData = dataFile.find(node.getRightData());
            }
        }
        return returnData;
    }

    private BTreeNode<T> findNode(T data) {
        if (rootAddress == nullValue) {
            return null;
        } else {
            int resultLeft;
            int resultRight = 100;
            BTreeNode<T> node = file.find(rootAddress);
            while (node != null) {
                resultLeft = data.compareTo(dataFile.find(node.getLeftData()));
                if (node.getRightData() != nullValue) {
                    resultRight = data.compareTo(dataFile.find(node.getRightData()));
                }
                if (resultLeft == 0 || resultRight == 0) {
                    return node;
                } else {
                    if (resultLeft < 0) {
                        //node = file.find(node.getLeftSon());
                        node = findInFileAtAddress(node.getLeftSon());
                    } else if (node.getRightData() == nullValue || resultRight < 0) {
                        //node = file.find(node.getCenterSon());
                        node = findInFileAtAddress(node.getCenterSon());
                    } else {
                        //node = file.find(node.getRightSon());
                        node = findInFileAtAddress(node.getRightSon());
                    }
                }
            }
        }
        return null;
    }



    ///Implementovane pomocou clanku z https://www.cs.princeton.edu/~dpw/courses/cos326-12/ass/2-3-trees.pdf?fbclid=IwAR3SMu7v3IbeMZx0d0tDSglu7Kn4kggtdQixRNW29PemWOi3tJXITn-iH_I
    public void delete(T data) {

        BTreeNode<T> node = findNode(data);
        if (rootAddress == nullValue || node == null) {
            return;
        }
        BTreeNode<T> predecessor = inOrderPredecessor(node, data);
        dataNum--;
        if (node.isLeaf()) {
            //System.out.println("FROM LEAF");
            if (node.getRightData() != nullValue) {
                if (dataFile.find(node.getLeftData()).compareTo(data) == 0) {
                    node.setLeftData(node.getRightData());
                }
                node.setRightData(nullValue);
                file.insertAtAddress(node, node.getAddress());
                return;
            } else {
                node.setLeftData(nullValue);
                if (node.getAddress() == rootAddress) {
                    rootAddress = nullValue;
                    file.insertAtAddress(node, node.getAddress());
                    file.delete(node.getAddress());
                    file.updateTreeData(rootAddress);
                    return;
                }
            }
        } else {
            if (predecessor == null) {
                System.out.println("predecessor is null problem");
                return;
            }
            //System.out.println("FROM !LEAF");
            if (node.getRightData() != nullValue) {
                if (dataFile.find(node.getLeftData()).compareTo(data) == 0) {
                    if (predecessor.getRightData() != nullValue) {
                        node.setLeftData(predecessor.getRightData());
                        predecessor.setRightData(nullValue);

                        file.insertAtAddress(node, node.getAddress());
                        file.insertAtAddress(predecessor, predecessor.getAddress());

                        return;
                    } else {
                        node.setLeftData(predecessor.getLeftData());
                        predecessor.setLeftData(nullValue);

                        file.insertAtAddress(node, node.getAddress());
                        file.insertAtAddress(predecessor, predecessor.getAddress());
                    }

                } else {
                    if (predecessor.getRightData() != nullValue) {
                        node.setRightData(predecessor.getRightData());
                        predecessor.setRightData(nullValue);

                        file.insertAtAddress(node, node.getAddress());
                        file.insertAtAddress(predecessor, predecessor.getAddress());

                        return;
                    } else {
                        node.setRightData(predecessor.getLeftData());
                        predecessor.setLeftData(nullValue);

                        file.insertAtAddress(node, node.getAddress());
                        file.insertAtAddress(predecessor, predecessor.getAddress());
                    }
                }
            } else {
                if (predecessor.getRightData() != nullValue) {
                    node.setLeftData(predecessor.getRightData());
                    predecessor.setRightData(nullValue);

                    file.insertAtAddress(node, node.getAddress());
                    file.insertAtAddress(predecessor, predecessor.getAddress());

                    return;
                } else {
                    node.setLeftData(predecessor.getLeftData());
                    predecessor.setLeftData(nullValue);

                    file.insertAtAddress(node, node.getAddress());
                    file.insertAtAddress(predecessor, predecessor.getAddress());
                }
            }
            node = predecessor;
        }

        int branch = 0;

        while (node != null) {
            if (node.getParent() != nullValue) {
                BTreeNode<T> nodeGetParent = file.find(node.getParent());
                //node.getParent().getLeftSon()
                if (node.getAddress() == nodeGetParent.getLeftSon()) {
                    branch = -1;
                }
                //node.getParent().getCenterSon()
                if (node.getAddress() == nodeGetParent.getCenterSon()) {
                    branch = 0;
                }
                //node.getParent().getRightSon()
                if (node.getAddress() == nodeGetParent.getRightSon()) {
                    branch = 1;
                }
                switch (caseNumber(node, branch)) {
                    case case1:
                        //System.out.println("pripad " + case1);
                        node = deleteCase1(node, branch);
                        break;
                    case case2:
                        //System.out.println("pripad " + case2);
                        deleteCase2(node, branch);
                        /*root = file.find(root.getAddress());
                        rootAddress = root.getAddress();
                        file.updateTreeData(rootAddress);*/
                        return;
                    case case3a:
                        //System.out.println("pripad " + case3a);
                        deleteCase3a(node, branch);
                        /*root = file.find(root.getAddress());
                        rootAddress = root.getAddress();
                        file.updateTreeData(rootAddress);*/
                        return;
                    case case3b:
                        //System.out.println("pripad " + case3b);
                        deleteCase3b(node, branch);
                        /*root = file.find(root.getAddress());
                        rootAddress = root.getAddress();
                        file.updateTreeData(rootAddress);*/
                        return;
                    case case4a:
                        //System.out.println("pripad " + case4a);
                        deleteCase4a(node, branch);
                        /*root = file.find(root.getAddress());
                        rootAddress = root.getAddress();
                        file.updateTreeData(rootAddress);*/
                        return;
                    case case4b:
                        //System.out.println("pripad " + case4b);
                        deleteCase4b(node, branch);
                        /*root = file.find(root.getAddress());
                        rootAddress = root.getAddress();
                        file.updateTreeData(rootAddress);*/
                        return;
                    case -1:
                        System.out.println("Didnt find case that would match the problem");
                        break;
                    default:
                        break;
                }
                if (node.getAddress() == rootAddress) {
                    file.delete(rootAddress);
                    BTreeNode<T> nodeTmp = file.find(node.getLeftSon());
                    nodeTmp.setParent(nullValue);
                    file.insertAtAddress(nodeTmp, nodeTmp.getAddress());
                    rootAddress = nodeTmp.getAddress();
                    file.updateTreeData(rootAddress);
                    return;
                }
            }
        }
    }

    private int caseNumber(BTreeNode<T> node, int branch) {
        BTreeNode<T> nodeGetParent = file.find(node.getParent());

        if (nodeGetParent.getRightData() == nullValue && file.find(nodeGetParent.getCenterSon()).getRightData() == nullValue && branch == -1) {
            return case1;
        }
        if (nodeGetParent.getRightData() == nullValue && file.find(nodeGetParent.getLeftSon()).getRightData() == nullValue && branch == 0) {
            return case1;
        }
        if (nodeGetParent.getRightData() == nullValue && file.find(nodeGetParent.getCenterSon()).getRightData() != nullValue && branch == -1) {
            return case2;
        }
        if (nodeGetParent.getRightData() == nullValue && file.find(nodeGetParent.getLeftSon()).getRightData() != nullValue && branch == 0) {
            return case2;
        }
        if (nodeGetParent.getRightData() != nullValue && file.find(nodeGetParent.getCenterSon()).getRightData() == nullValue && branch == -1) {
            return case3a;
        }
        if (nodeGetParent.getRightData() != nullValue && file.find(nodeGetParent.getLeftSon()).getRightData() == nullValue && branch == 0) {
            return case3a;
        }
        if (nodeGetParent.getRightData() != nullValue && file.find(nodeGetParent.getCenterSon()).getRightData() == nullValue && branch == 1) {
            return case3b;
        }
        if (nodeGetParent.getRightData() != nullValue && file.find(nodeGetParent.getCenterSon()).getRightData() != nullValue && branch == -1) {
            return case4a;
        }
        if (nodeGetParent.getRightData() != nullValue && file.find(nodeGetParent.getLeftSon()).getRightData() != nullValue && branch == 0) {
            return case4a;
        }
        if (nodeGetParent.getRightData() != nullValue && file.find(nodeGetParent.getCenterSon()).getRightData() != nullValue && branch == 1) {
            return case4b;
        }
        return -1;
    }

    private BTreeNode<T> deleteCase1(BTreeNode<T> node, int branch) {
        BTreeNode<T> tmp = null;
        BTreeNode<T> nodeGetParent = file.find(node.getParent());
        if (branch == -1) {

            tmp = file.find(nodeGetParent.getCenterSon());

            node.setLeftData(nodeGetParent.getLeftData());
            node.setRightData(tmp.getLeftData());
            CorrectLeftAndRightData(node);
            nodeGetParent.setLeftData(nullValue);

            node.setCenterSon(tmp.getLeftSon());
            node.setRightSon(tmp.getCenterSon());

            if (node.getCenterSon() != nullValue) {
                changeParentsAddressAtSon(node.getCenterSon(), node.getAddress());
            }
            if (node.getRightSon() != nullValue) {
                changeParentsAddressAtSon(node.getRightSon(), node.getAddress());

            }
            file.insertAtAddress(tmp, tmp.getAddress());
            file.insertAtAddress(node, node.getAddress());
            file.delete(tmp.getAddress());
        }
        if (branch == 0) {
            tmp = file.find(nodeGetParent.getLeftSon());
            tmp.setRightData(nodeGetParent.getLeftData());
            nodeGetParent.setLeftData(nullValue);

            tmp.setRightSon(node.getLeftSon());
            if (tmp.getRightSon() != nullValue) {
                changeParentsAddressAtSon(tmp.getRightSon(), tmp.getAddress());
            }
            file.insertAtAddress(tmp, tmp.getAddress());
            file.insertAtAddress(node, node.getAddress());
            file.delete(node.getAddress());
        }


        node = nodeGetParent;
        node.setCenterSon(nullValue);
        file.insertAtAddress(node, node.getAddress());

        return node;
    }

    private void deleteCase2(BTreeNode<T> node, int branch) {
        BTreeNode<T> tmp = null;
        BTreeNode<T> nodeGetParent = file.find(node.getParent());
        if (branch == -1) {
            tmp = file.find(nodeGetParent.getCenterSon());

            node.setLeftData(nodeGetParent.getLeftData());
            nodeGetParent.setLeftData(tmp.getLeftData());
            tmp.setLeftData(tmp.getRightData());

            tmp.setRightData(nullValue);
            node.setCenterSon(tmp.getLeftSon());
            tmp.setLeftSon(tmp.getCenterSon());
            tmp.setCenterSon(tmp.getRightSon());
            tmp.setRightSon(nullValue);

            if (node.getCenterSon() != nullValue) {
                changeParentsAddressAtSon(node.getCenterSon(), node.getAddress());
            }
        }
        if (branch == 0) {
            tmp = file.find(nodeGetParent.getLeftSon());

            node.setLeftData(nodeGetParent.getLeftData());
            nodeGetParent.setLeftData(tmp.getRightData());
            tmp.setLeftData(tmp.getLeftData());

            tmp.setRightData(nullValue);
            node.setCenterSon(node.getLeftSon());
            node.setLeftSon(tmp.getRightSon());
            tmp.setRightSon(nullValue);

            if (node.getLeftSon() != nullValue) {
                changeParentsAddressAtSon(node.getLeftSon(), node.getAddress());
            }
        }

        file.insertAtAddress(tmp, tmp.getAddress());
        file.insertAtAddress(node, node.getAddress());
        file.insertAtAddress(nodeGetParent, nodeGetParent.getAddress());
    }

    private void deleteCase3a(BTreeNode<T> node, int branch) {
        BTreeNode<T> tmp = null;
        BTreeNode<T> nodeGetParent = file.find(node.getParent());
        if (branch == -1) {
            tmp = file.find(nodeGetParent.getCenterSon());
            node.setLeftData(nodeGetParent.getLeftData());
            node.setRightData(tmp.getLeftData());

            nodeGetParent.setLeftData(nodeGetParent.getRightData());
            nodeGetParent.setRightData(nullValue);

            node.setCenterSon(tmp.getLeftSon());
            node.setRightSon(tmp.getCenterSon());
            nodeGetParent.setCenterSon(nodeGetParent.getRightSon());
            nodeGetParent.setRightSon(nullValue);

            if (node.getLeftSon() != nullValue) {
                changeParentsAddressAtSon(node.getLeftSon(), node.getAddress());
            }
            if (node.getCenterSon() != nullValue) {
                changeParentsAddressAtSon(node.getCenterSon(), node.getAddress());
            }
            if (node.getRightSon() != nullValue) {
                changeParentsAddressAtSon(node.getRightSon(), node.getAddress());
            }

            file.insertAtAddress(tmp, tmp.getAddress());
            file.insertAtAddress(node, node.getAddress());

            file.delete(tmp.getAddress());
        }
        if (branch == 0) {
            tmp = file.find(nodeGetParent.getLeftSon());
            tmp.setRightData(nodeGetParent.getLeftData());

            nodeGetParent.setLeftData(nodeGetParent.getRightData());
            nodeGetParent.setRightData(nullValue);

            tmp.setRightSon(node.getLeftSon());
            if (tmp.getRightSon() != nullValue) {
                changeParentsAddressAtSon(tmp.getRightSon(), tmp.getAddress());
            }

            nodeGetParent.setCenterSon(nodeGetParent.getRightSon());
            nodeGetParent.setRightSon(nullValue);

            file.insertAtAddress(tmp, tmp.getAddress());
            file.insertAtAddress(node, node.getAddress());

            file.delete(node.getAddress());
        }

        file.insertAtAddress(nodeGetParent, nodeGetParent.getAddress());
    }

    private void deleteCase3b(BTreeNode<T> node, int branch) {
        BTreeNode<T> tmp = null;
        BTreeNode<T> nodeGetParent = file.find(node.getParent());
        if (branch == 1) {
            tmp = file.find(nodeGetParent.getCenterSon());
            tmp.setRightData(nodeGetParent.getRightData());
            nodeGetParent.setRightData(nullValue);

            tmp.setRightSon(node.getLeftSon());
            nodeGetParent.setRightSon(nullValue);

            if (node.getLeftSon() != nullValue) {
                changeParentsAddressAtSon(node.getLeftSon(), tmp.getAddress());
            }
            file.insertAtAddress(node, node.getAddress());
            file.delete(node.getAddress());
        }

        file.insertAtAddress(tmp, tmp.getAddress());
        file.insertAtAddress(nodeGetParent, nodeGetParent.getAddress());
    }

    private void deleteCase4a(BTreeNode<T> node, int branch) {
        BTreeNode<T> tmp = null;
        BTreeNode<T> nodeGetParent = file.find(node.getParent());
        if (branch == -1) {
            tmp = file.find(nodeGetParent.getCenterSon());
            node.setLeftData(nodeGetParent.getLeftData());
            nodeGetParent.setLeftData(tmp.getLeftData());

            tmp.setLeftData(tmp.getRightData());
            tmp.setRightData(nullValue);

            node.setCenterSon(tmp.getLeftSon());
            tmp.setLeftSon(tmp.getCenterSon());
            tmp.setCenterSon(tmp.getRightSon());
            tmp.setRightSon(nullValue);

            if (node.getLeftSon() != nullValue) {
                changeParentsAddressAtSon(node.getCenterSon(), node.getAddress());
            }
        }
        if (branch == 0) {
            tmp = file.find(nodeGetParent.getLeftSon());
            node.setLeftData(nodeGetParent.getLeftData());
            nodeGetParent.setLeftData(tmp.getRightData());

            tmp.setRightData(nullValue);

            node.setCenterSon(node.getLeftSon());
            node.setLeftSon(tmp.getRightSon());
            tmp.setRightSon(nullValue);

            if (node.getLeftSon() != nullValue) {
                changeParentsAddressAtSon(node.getLeftSon(), node.getAddress());
            }
        }
        file.insertAtAddress(tmp, tmp.getAddress());
        file.insertAtAddress(node, node.getAddress());
        file.insertAtAddress(nodeGetParent, nodeGetParent.getAddress());
    }

    private void deleteCase4b(BTreeNode<T> node, int branch) {
        BTreeNode<T> tmp = null;
        BTreeNode<T> nodeGetParent = file.find(node.getParent());
        if (branch == 1) {
            tmp = file.find(nodeGetParent.getCenterSon());
            node.setLeftData(nodeGetParent.getRightData());
            nodeGetParent.setRightData(tmp.getRightData());

            tmp.setRightData(nullValue);

            node.setCenterSon(node.getLeftSon());
            node.setLeftSon(tmp.getRightSon());
            tmp.setRightSon(nullValue);

            if (node.getLeftSon() != nullValue) {
                changeParentsAddressAtSon(node.getLeftSon(), node.getAddress());
            }
        }
        file.insertAtAddress(tmp, tmp.getAddress());
        file.insertAtAddress(node, node.getAddress());
        file.insertAtAddress(nodeGetParent, nodeGetParent.getAddress());
    }

   //Implementovane podla vlastnych materialov
    public ArrayList<T> inOrder() {

        if (dataNum < 0) {
            dataNum = 0;
        }
        if (rootAddress == nullValue) {
            return new ArrayList<>();
        }
        ArrayList<T> list = new ArrayList<>(dataNum);
        BTreeNode<T> root = file.find(rootAddress);
        if (rootAddress == nullValue || (root.getLeftData() == nullValue && root.getRightData() == nullValue)) {
            //System.out.println("There is no root or root has no data(inOrder)");
            return list;
        }
        root = file.find(root.getAddress());
        BTreeNode<T> node = root;
        int tmpCompare = -1;
        int level = 0;
        Stack<BTreeNode<T>> leftNode = new Stack<>();
        Stack<BTreeNode<T>> centerNode = new Stack<>();
        Stack<BTreeNode<T>> rightNode = new Stack<>();
        Stack<BTreeNode<T>> nodeStack = new Stack<>();

        nodeStack.push(node);

        while (nodeStack.size() != 0) {
            //System.out.println("IDEME IN IRODER" + nodeStack.size() + "  adresa " + node.getAddress());
            if (nodeStack.peek() != node && node != root) {
                nodeStack.push(node);
            }
            if (node.getLeftSon() != nullValue && (leftNode.isEmpty() || node.getLeftSon() != leftNode.peek().getAddress())) {
                level++;
                node = file.find(node.getLeftSon());
            } else if (node.getCenterSon() != nullValue && (centerNode.isEmpty() || node.getCenterSon() != centerNode.peek().getAddress())) {
                level++;
                list.add(dataFile.find(node.getLeftData()));
                node = file.find(node.getCenterSon());
            } else if (node.getRightData() != nullValue && node.getRightSon() != nullValue && (rightNode.isEmpty() || node.getRightSon() != rightNode.peek().getAddress())) {
                level++;
                list.add(dataFile.find(node.getRightData()));
                node = file.find(node.getRightSon());
            } else {
                if (node.getLeftSon() == nullValue || (leftNode.isEmpty() || node.getLeftSon() == leftNode.peek().getAddress()) && node.getCenterSon() == nullValue || (centerNode.isEmpty() || node.getCenterSon() == centerNode.peek().getAddress()) && node.getRightSon() == -100 || node.getRightSon() != -100 && (rightNode.isEmpty() || node.getRightSon() == rightNode.peek().getAddress())) {
                    if (node.isLeaf()) {
                        if (tmpCompare == -1) {
                            tmpCompare = level;
                        } else {
                            if (tmpCompare != level) {
                                System.out.println("ERROR");
                                return null;
                            }
                        }
                        list.add(dataFile.find(node.getLeftData()));
                        if (node.getRightData() != nullValue) {
                            list.add(dataFile.find(node.getRightData()));
                        }
                        if (node.getParent() != nullValue) {
                            BTreeNode<T> nodeGetParent = file.find(node.getParent());
                            if (nodeGetParent.getLeftSon() == node.getAddress()) {
                                leftNode.push(node);
                            }
                            if (nodeGetParent.getCenterSon() == node.getAddress()) {
                                centerNode.push(node);
                            }
                            if (nodeGetParent.getRightSon() == node.getAddress()) {
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
                        if (node.getParent() != nullValue) {
                            BTreeNode<T> nodeGetParent = file.find(node.getParent());
                            if (nodeGetParent.getLeftSon() == node.getAddress()) {
                                leftNode.push(node);
                            }
                            if (nodeGetParent.getCenterSon() == node.getAddress()) {
                                centerNode.push(node);
                            }
                            if (nodeGetParent.getRightSon() == node.getAddress()) {
                                rightNode.push(node);
                            }
                        }
                    }
                    //System.out.println("node stack size " + nodeStack.size() + " adresa " + node.getAddress());
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
    //Implementovane podla vlastnych materialov
    private BTreeNode<T> inOrderPredecessor(BTreeNode<T> node , T data) {
        if (rootAddress == nullValue) {
            return null;
        }
        if (!node.isLeaf()) {
            if (dataFile.find(node.getLeftData()).compareTo(data) == 0) {
                node = file.find(node.getLeftSon());
            } else {
                node = file.find(node.getCenterSon());
            }
            while (!node.isLeaf()) {
                if (node.getRightSon() != nullValue) {
                    node = file.find(node.getRightSon());
                } else {
                    node = file.find(node.getCenterSon());
                }
            }
        }
        return node;
    }
    //Implementovane podla vlastnych materialov
    public ArrayList<T> getInterval(T minData, T maxData) {
        BTreeNode<T> node = findIntervalMinNode(minData);
        if (node == null) {
            return new ArrayList<>();
        }
        return inOrderIntervalMinMax(node, minData, maxData);
    }

    private BTreeNode<T> findIntervalMinNode(T minData) {
        if (rootAddress == nullValue) {
            return null;
        } else {
            int resultLeft;
            int resultRight = 1000;
            BTreeNode<T> node = file.find(rootAddress);
            BTreeNode<T> tmp = file.find(rootAddress);
            while (node != null) {
                resultLeft = minData.compareTo(dataFile.find(node.getLeftData()));
                if (node.getRightData() != nullValue) {
                    resultRight = minData.compareTo(dataFile.find(node.getRightData()));
                }
                tmp = node;
                if (resultLeft == 0 || resultRight == 0) {
                    return node;
                } else {
                    if (resultLeft < 0) {
                        node = findInFileAtAddress(node.getLeftSon());
                    } else if (node.getRightData() == nullValue || resultRight < 0) {
                        node = findInFileAtAddress(node.getCenterSon());
                    } else {
                        node = findInFileAtAddress(node.getRightSon());
                    }
                }
            }

            if (tmp.getRightData() != nullValue) {
                if (dataFile.find(tmp.getRightData()).compareTo(minData) < 0) {
                    tmp = findInFileAtAddress(tmp.getParent());
                    if (tmp != null) {
                        if (tmp.getRightData() != nullValue) {
                            if (dataFile.find(tmp.getRightData()).compareTo(minData) < 0) {
                                tmp = null;
                            }
                        } else {
                            if (dataFile.find(tmp.getLeftData()).compareTo(minData) < 0) {
                                tmp = null;
                            }
                        }
                    }
                }
            } else {
                if (dataFile.find(tmp.getLeftData()).compareTo(minData) < 0) {
                    tmp = findInFileAtAddress(tmp.getParent());
                    if (tmp != null) {
                        if (tmp.getRightData() != nullValue) {
                            if (dataFile.find(tmp.getRightData()).compareTo(minData) < 0) {
                                tmp = null;
                            }
                        } else {
                            if (dataFile.find(tmp.getLeftData()).compareTo(minData) < 0) {
                                tmp = null;
                            }
                        }
                    }
                }
            }
            return tmp;
        }
    }

    private ArrayList<T> inOrderIntervalMinMax(BTreeNode<T> minNode, T minData, T maxData) {
        ArrayList<T> list = new ArrayList<>();
        BTreeNode<T> node;

        T current = minData;
        node = minNode;

        while (node != null) {
            if (node.isLeaf()) {
                if (node.getRightData() != nullValue) {
                    if (dataFile.find(node.getLeftData()).compareTo(current) > -1) {
                        if (dataFile.find(node.getLeftData()).compareTo(maxData) > 0) {
                            break;
                        }
                        list.add(dataFile.find(node.getLeftData()));
                        if (dataFile.find(node.getLeftData()).compareTo(maxData) == 0) {
                            break;
                        }
                    }
                    if (dataFile.find(node.getRightData()).compareTo(maxData) > 0) {
                        break;
                    }
                    list.add(dataFile.find(node.getRightData()));
                    current = dataFile.find(node.getRightData());
                    if (dataFile.find(node.getRightData()).compareTo(maxData) == 0) {
                        break;
                    }
                } else {
                    if (dataFile.find(node.getLeftData()).compareTo(maxData) > 0) {
                        break;
                    }
                    list.add(dataFile.find(node.getLeftData()));
                    current = dataFile.find(node.getLeftData());
                    if (dataFile.find(node.getLeftData()).compareTo(maxData) == 0) {
                        break;
                    }
                }
                node = inOrderSuccessor(node, current);
            } else {
                if (node.getRightData() != nullValue) {
                    if (dataFile.find(node.getLeftData()).compareTo(current) > -1) {
                        if (dataFile.find(node.getLeftData()).compareTo(maxData) > 0) {
                            break;
                        }
                        list.add(dataFile.find(node.getLeftData()));
                        current = dataFile.find(node.getLeftData());
                        if (dataFile.find(node.getLeftData()).compareTo(maxData) == 0) {
                            break;
                        }
                    } else {
                        if (dataFile.find(node.getRightData()).compareTo(maxData) > 0) {
                            break;
                        }
                        list.add(dataFile.find(node.getRightData()));
                        current = dataFile.find(node.getRightData());
                        if (dataFile.find(node.getRightData()).compareTo(maxData) == 0) {
                            break;
                        }
                    }
                } else {
                    if (dataFile.find(node.getLeftData()).compareTo(maxData) > 0) {
                        break;
                    }
                    list.add(dataFile.find(node.getLeftData()));
                    current = dataFile.find(node.getLeftData());
                    if (dataFile.find(node.getLeftData()).compareTo(maxData) == 0) {
                        break;
                    }
                }
                node = inOrderSuccessor(node, current);
            }
        }
        return list;
    }

    private BTreeNode<T> inOrderSuccessor(BTreeNode<T> node, T data) {
        boolean down = false;
        if (!node.isLeaf()) {
            down = true;
            if (dataFile.find(node.getLeftData()).compareTo(data) == 0) {
                node = findInFileAtAddress(node.getCenterSon());
            }
            if (node.getRightData() != nullValue && dataFile.find(node.getRightData()).compareTo(data) == 0) {
                node = findInFileAtAddress(node.getRightSon());
            }
        }
        while (node != null) {
            if (down) {
                if (node.isLeaf()) {
                    break;
                }
                node = findInFileAtAddress(node.getLeftSon());
                continue;
            }
            if (node.getParent() != nullValue) {
                ;
                //2 node
                if (findInFileAtAddress(node.getParent()).getRightData() == nullValue) {
                    if (file.find(findInFileAtAddress(node.getParent()).getLeftSon()).getAddress() == node.getAddress()) {
                        return findInFileAtAddress(node.getParent());
                    }
                } //3 node
                else {
                    if (file.find(findInFileAtAddress(node.getParent()).getLeftSon()).getAddress() == node.getAddress()) {
                        return findInFileAtAddress(node.getParent());
                    }
                    if (file.find(findInFileAtAddress(node.getParent()).getCenterSon()).getAddress() == node.getAddress()) {
                        return findInFileAtAddress(node.getParent());
                    }
                }
            }
            node = findInFileAtAddress(node.getParent());
        }
        return node;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public void fromByteArray(byte[] array) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public BTree<T> createClass() {
        return new BTree<>(gClass);
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void setValid(boolean valid) {

    }

    @Override
    public long getAddress() {
        return 0;
    }

    @Override
    public void setAddress(long address) {

    }

    @Override
    public int compareTo(BTree o) {
        return 0;
    }

    public long getFileLength() {
        return this.file.getLength();
    }

    public long getFreeSpaceFileLength() {
        return this.file.getFreeSpaceFileLength();
    }

    public ArrayList<BTreeNode<T>> getFileSequentialListing() {
        int countV = 0;
        int countF = 0;
        ArrayList<BTreeNode<T>> sequentialListing = file.sequentialListing(8);
        for (BTreeNode<T> node: sequentialListing) {
            System.out.println(node.toString() + "  "  + node.isValid());
            if (node.isValid()) {
                countV++;
            } else {
                countF++;
            }

        }
        System.out.println();
        System.out.println(countF + " inValid nodes");
        System.out.println();
        System.out.println(countV + " Valid nodes");
        System.out.println();

        return sequentialListing;
    }

    public ArrayList<BTreeNode<T>> getFileSequentialListingFS() {
        int countV = 0;
        int countF = 0;
        ArrayList<BTreeNode<T>> sequentialListing = file.sequentialListingFS(0);
        for (BTreeNode<T> node: sequentialListing) {
            System.out.println(node.toString() + "  "  + node.isValid());
            if (node.isValid()) {
                countV++;
            } else {
                countF++;
            }

        }
        System.out.println();
        System.out.println(countF + " inValid nodes");
        System.out.println();
        System.out.println(countV + " Valid nodes");
        System.out.println();

        return sequentialListing;
    }

    public void clearFiles() {
        this.file.clearFiles();
    }

    public void saveFreeSpaces() {
        this.file.saveFreeSpaces();
    }

    public UFile<BTreeNode<T>> getFile() {
        return file;
    }

    public ArrayList<String[]> getSequentialListing() {
        ArrayList<BTreeNode<T>> nodes = file.sequentialListing(8);

        ArrayList<String[]> text = new ArrayList<>(nodes.size());
        for (BTreeNode<T> node: nodes) {
            text.add(node.data());
        }
        return text;
    }
}
