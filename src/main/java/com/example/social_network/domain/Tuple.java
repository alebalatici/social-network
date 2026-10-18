package com.example.social_network.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class Tuple<E1, E2> implements Serializable {
    private static final long serialVersionUID = 1L;

    private E1 left;
    private E2 right;

    public Tuple() {

    }

    public Tuple(E1 left, E2 right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return (Objects.equals(left, tuple.left) && Objects.equals(right, tuple.right)) ||
                Objects.equals(left, tuple.right) && Objects.equals(right, tuple.left);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right) + Objects.hash(right, left);
    }
}
