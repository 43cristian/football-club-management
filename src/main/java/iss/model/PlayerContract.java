package iss.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@jakarta.persistence.Entity
@Table(name="player_contracts")
public class PlayerContract extends Entity<Long> {
    @Column(nullable = false, name="starting_date")
    private LocalDate startingDate;

    @Column(nullable = false, name="ending_date")
    private LocalDate endingDate;

    @Column(nullable = false)
    private BigDecimal wage;

    @Column(nullable = true)
    private BigDecimal bonus;

    @Column(nullable = true, length=255)
    private String bonusDesc;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id", nullable = false)
    private Player player;

    public PlayerContract() {}

    public PlayerContract(LocalDate startingDate, LocalDate endingDate, BigDecimal wage, BigDecimal bonus, String bonusDesc, Player player) {
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.wage = wage;
        this.bonus = bonus;
        this.bonusDesc = bonusDesc;
        this.player = player;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public String getBonusDesc() {
        return bonusDesc;
    }

    public void setBonusDesc(String bonusDesc) {
        this.bonusDesc = bonusDesc;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "PlayerContract{" +
                "id=" + id +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                ", wage=" + wage +
                ", bonus=" + bonus +
                ", bonusDesc='" + bonusDesc + '\'' +
                ", player=" + player +
                '}';
    }
}
