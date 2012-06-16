package service.metrics;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 6.5.12
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class EdgeKey implements Comparable<EdgeKey> {

    private String a;
    private String b;

    public EdgeKey(String b, String a)  {
        this.b = b;
        this.a = a;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) return false;
        EdgeKey other = (EdgeKey) obj;

        if (a.equals(other.getA()) && b.equals(other.getB()))
            return true;
        if (a.equals(other.getB()) && b.equals(other.getA()))
            return true;

        return false;
    }

    @Override
    public int hashCode() {
        return a.hashCode()+b.hashCode();
    }

    @Override
    public int compareTo(EdgeKey o) {
        if (a.equals(o.getA()) && b.equals(o.getB()))
            return 0;
        if (a.equals(o.getB()) && b.equals(o.getA()))
            return 0;
        return (this.getA()+this.getB()).compareTo(o.getA()+o.getB());
    }
}
