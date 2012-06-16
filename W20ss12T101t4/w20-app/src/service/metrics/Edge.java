package service.metrics;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 26.4.12
 * Time: 22:29
 */
public class Edge {

    private Node a;
    private Node b;

    private float overlap;

    private int embeddedness;

    public Edge(Node b, Node a) {
        this.b = b;
        this.a = a;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) return false;
        Edge other = (Edge) obj;

        if (a.equals(other.getA()) && b.equals(other.getB()))
            return true;
        if (a.equals(other.getB()) && b.equals(other.getA()))
            return true;

        return false;
    }

    public Node getA() {
        return a;
    }

    public void setA(Node a) {
        this.a = a;
    }

    public Node getB() {
        return b;
    }

    public void setB(Node b) {
        this.b = b;
    }

    public float getOverlap() {
        return overlap;
    }

    public void setOverlap(float overlap) {
        this.overlap = overlap;
    }

    public int getEmbeddedness() {
        return embeddedness;
    }

    public void setEmbeddedness(int embeddedness) {
        this.embeddedness = embeddedness;
    }
}
