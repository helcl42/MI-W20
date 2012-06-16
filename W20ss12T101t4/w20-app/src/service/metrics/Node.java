package service.metrics;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 26.4.12
 * Time: 22:23
 */
public class Node implements Comparable<Node> {

    private String name;

    private Long id;

    private Set<Node> neighbours = new HashSet<Node>();

    private int erdos = -1;

    private float clusterCoeff;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Set<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getErdos() {
        return erdos;
    }

    public void setErdos(int erdos) {
        this.erdos = erdos;
    }

    public float getClusterCoeff() {
        return clusterCoeff;
    }

    public void setClusterCoeff(float clusterCoeff) {
        this.clusterCoeff = clusterCoeff;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!obj.getClass().equals(getClass())) return false;
        return name.equals(((Node) obj).getName());
    }

    @Override
    public int compareTo(Node o) {
        return name.compareTo(o.getName());
    }
}
