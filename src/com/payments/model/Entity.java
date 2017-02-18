package com.payments.model;

import java.io.Serializable;

/**
 * Root of all entities which have identifier field.
 *
 * @author Kostiantyn Dubovik
 */
public abstract class Entity implements Serializable {

    private static final long serialVersionUID = 3916002233864144820L;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
