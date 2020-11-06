package com.jacobmekker.aurumancy.rituals;

public class CirclePower {

    public int op;
    public int np;
    public int ep;

    public CirclePower(int op, int np, int ep) {
        this.op = op;
        this.np = np;
        this.ep = ep;
    }

    public CirclePower() { }

    @Override
    public String toString() {
        return "op=" + op +
                " np=" + np +
                " ep=" + ep;
    }

    public int total() {
        return op + np + ep;
    }

    public void add(CirclePower other) {
        if (other != null) {
            op += other.op;
            np += other.np;
            ep += other.ep;
        }
    }

    public void add(int op, int np, int ep) {
        this.op += op;
        this.np += np;
        this.ep += ep;
    }

    public boolean meetsOrExceeds(CirclePower other) {
        return this.op >= other.op && this.np >= other.np && this.ep >= other.ep;
    }
}
