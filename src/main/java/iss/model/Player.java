package iss.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
@Table(name="players")
public class Player extends Entity<Long> {
    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = true, name="shirt_number")
    private int shirtNumber;

    @ElementCollection(targetClass = Position.class, fetch = FetchType.EAGER)
    @CollectionTable(name="player_positions", joinColumns = @JoinColumn(name = "player_id"))
    @Enumerated(EnumType.STRING)
    @Column(name="position_name")
    private List<Position> positionList = new ArrayList<>();

    @Column(nullable = false)
    private String nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="club_id", nullable = false)
    private Club club;

    public Player() {}

    public Player(Long id, String firstName, String lastName, LocalDate birthDate, int shirtNumber, List<Position> positionList, String nationality, Club club) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.shirtNumber = shirtNumber;
        this.positionList = positionList;
        this.nationality = nationality;
        this.club = club;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", shirtNumber=" + shirtNumber +
                ", positionList=" + positionList +
                ", nationality='" + nationality + '\'' +
                ", club=" + club +
                '}';
    }
}
