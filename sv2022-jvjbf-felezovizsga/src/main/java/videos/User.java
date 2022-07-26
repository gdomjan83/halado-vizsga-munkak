package videos;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String name;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus = UserStatus.BEGINNER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<Video> videos = new HashSet<>();

    public User() {
    }

    public User(String name, LocalDate registrationDate) {
        this.name = name;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Set<Video> getVideos() {
        return new HashSet<>(videos);
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video video) {
        if (video != null) {
            videos.add(video);
            video.setUser(this);
        } else {
            throw new IllegalArgumentException("Video can not be null.");
        }
    }
}
