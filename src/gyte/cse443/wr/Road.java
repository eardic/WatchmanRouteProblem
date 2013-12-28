/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr;

/**
 * Edge in graph
 *
 * @author Emre
 */
public class Road {

    private double length;
    private String id;

    /**
     * Sets length to given length and id to given id
     *
     * @param length
     * @param id
     */
    public Road(double length, String id) {
        this.length = length;
        this.id = id;
    }

    /**
     * Sets weight to +infinity and id to "None"
     */
    Road() {
        this(Double.POSITIVE_INFINITY, "None");
    }

    @Override
    public String toString() {
        return "(" + id + "," + length + ")";
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
