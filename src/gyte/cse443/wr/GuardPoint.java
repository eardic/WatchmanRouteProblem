/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr;

/**
 *
 * @author Emre
 */
public class GuardPoint implements Comparable<GuardPoint> {

    private String name;

    public GuardPoint(String name) {
        this.name = name;
    }

    public GuardPoint() {
        this.name = "";
    }

    @Override
    public String toString() {
        return "(" + name + ")";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(GuardPoint o) {
        return name.compareTo(o.getName());
    }

}
