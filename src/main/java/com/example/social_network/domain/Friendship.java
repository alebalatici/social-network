package com.example.social_network.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Friendship extends Entity<Tuple<Long, Long>> {
    private final Long idUser1;
    private final Long idUser2;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime friendsFrom;

    @JsonCreator
    public Friendship(
            @JsonProperty("idUser1") Long idUser1,
            @JsonProperty("idUser2") Long idUser2,
            @JsonProperty("friendsFrom") LocalDateTime friendsFrom) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.friendsFrom = friendsFrom;
        this.setId(new Tuple<>(idUser1, idUser2));
    }

    public Friendship(Long idUser1, Long idUser2) {
        this(idUser1, idUser2, LocalDateTime.now());
    }

    public Long getIdUser1() {
        if (this.getId() != null) {
            return this.getId().getLeft();
        }

        return idUser1;
    }

    public Long getIdUser2() {
        if (this.getId() != null) {
            return this.getId().getRight();
        }

        return idUser2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Friendship{" + "idUser1=" + getIdUser1() + ", idUser2=" + getIdUser2() + ", friendsFrom=" + friendsFrom + '}';
    }
}