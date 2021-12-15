package data;

public class TwoThreeNode<T extends Comparable<T>> {
    private TwoThreeNode<T> parent = null;
    private TwoThreeNode<T> leftSon;
    private TwoThreeNode<T> centerSon;
    private TwoThreeNode<T> rightSon;
    private T leftData;
    private T rightData;

    public TwoThreeNode(T data) {
        leftData = data;
    }

    public boolean isLeaf() {
        return leftSon == null && centerSon == null && rightSon == null;
    }

    public TwoThreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TwoThreeNode<T> parent) {
        this.parent = parent;
    }

    public TwoThreeNode<T> getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(TwoThreeNode<T> leftSon) {
        this.leftSon = leftSon;
    }

    public TwoThreeNode<T> getRightSon() {
        return rightSon;
    }

    public void setRightSon(TwoThreeNode<T> rightSon) {
        this.rightSon = rightSon;
    }

    public TwoThreeNode<T> getCenterSon() {
        return centerSon;
    }

    public void setCenterSon(TwoThreeNode<T> centerSon) {
        this.centerSon = centerSon;
    }

    public T getLeftData() {
        return leftData;
    }

    public void setLeftData(T leftData) {
        this.leftData = leftData;
    }

    public T getRightData() {
        return rightData;
    }

    public void setRightData(T rightData) {
        this.rightData = rightData;
    }


}
