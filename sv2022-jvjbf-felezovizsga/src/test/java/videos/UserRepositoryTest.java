package videos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing User database access.")
class UserRepositoryTest {

    UserRepository repository;
    User user;

    @BeforeEach
    void init(){
        repository = new UserRepository(Persistence.createEntityManagerFactory("pu"));
        user = new User("Tommyboy", LocalDate.of(2022,3, 5));
    }

    @Test
    @DisplayName("Test: Saving and loading User.")
    void testSaveAndFindUser() {
        repository.saveUser(user);
        User found = repository.findUserWithVideos(user.getId());
        assertThat(found).extracting(User::getName).isEqualTo("Tommyboy");
        assertThat(found).extracting(User::getUserStatus).isEqualTo(UserStatus.BEGINNER);
    }

    @Test
    @DisplayName("Test: Adding video to User.")
    void testAddingVideoToUser() {
        repository.saveUser(user);
        Video video = new Video("Születésnap");
        repository.updateUserWithVideo(user.getId(), video);
        User found = repository.findUserWithVideos(user.getId());
        assertThat(found.getVideos()).hasSize(1).extracting(Video::getTitle).containsOnly("Születésnap");
    }

    @Test
    @DisplayName("Test: Changing status of User.")
    void testChangingUserStatus() {
        repository.saveUser(user);
        assertEquals(UserStatus.BEGINNER, repository.findUserWithVideos(user.getId()).getUserStatus());
        repository.updateUserStatus(user.getId(), UserStatus.ADVANCED);
        User found = repository.findUserWithVideos(user.getId());
        assertEquals(UserStatus.ADVANCED, repository.findUserWithVideos(found.getId()).getUserStatus());
    }

    @Test
    @DisplayName("Test: Listing users with given number of videos.")
    void testFindUsersWithMoreVideosThan() {
        Video video1 = new Video("Esemény1");
        Video video2 = new Video("Esemény2");
        Video video3 = new Video("Esemény3");
        Video video4 = new Video("Esemény4");
        Video video5 = new Video("Esemény5");
        Video video6 = new Video("Esemény6");
        User user2 = new User("MaxSpammer", LocalDate.of(2019, 11, 5));
        User user3 = new User("Troll001", LocalDate.of(2021, 8, 14));
        user.addVideo(video1);
        user.addVideo(video2);
        user2.addVideo(video3);
        user3.addVideo(video4);
        user3.addVideo(video5);
        user3.addVideo(video6);
        repository.saveUser(user);
        repository.saveUser(user2);
        repository.saveUser(user3);

        List<User> result = repository.findUsersWithMoreVideosThan(1);

        assertThat(result).hasSize(2).extracting(User::getName).containsOnly("Tommyboy", "Troll001");
    }
}