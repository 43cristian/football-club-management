package iss.model;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "users")
public class User extends Entity<Long> {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="user_type")
    private UserType userType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="club_id")
    private Club club;

    public User() {}

    public User(Long id, String username, String password, UserType userType, Club club) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.club = club;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                ", club=" + club +
                '}';
    }
}
