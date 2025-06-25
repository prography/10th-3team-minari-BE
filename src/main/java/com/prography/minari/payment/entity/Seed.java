package com.prography.minari.payment.entity;

import com.prography.minari.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table(name = "SEEDS")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Seed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long total;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    public void charge(long amount) {
        total += amount;
    }

    public Seed(Long total, User user) {
        this.total = total;
        this.user = user;
    }
}
