package videos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.Persistence;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Integration test for User logic")
class UserServiceIT {
    UserRepository repository;
    UserService userService;
    User user;

    @BeforeEach
    void init(){
        repository = new UserRepository(Persistence.createEntityManagerFactory("pu"));
        userService = new UserService(repository);
        user = new User("Tommyboy", LocalDate.of(2022,3, 5));
    }

    @Test
    @DisplayName("Test: User status is unchanged after video upload.")
    void testUnchangedStatusAfterUpload() {
        repository.saveUser(user);
        assertEquals(UserStatus.BEGINNER, repository.findUserWithVideos(user.getId()).getUserStatus());
        userService.uploadVideo(user.getId(), new Video("Születésnap"));
        assertEquals(UserStatus.BEGINNER, repository.findUserWithVideos(user.getId()).getUserStatus());
    }

    @Test
    @DisplayName("Test: Save video to User.")
    void testSavingVideoToUser() {
        repository.saveUser(user);
        repository.updateUserWithVideo(user.getId(), new Video("Születésnap"));
        repository.updateUserWithVideo(user.getId(), new Video("Lagzi"));
        User found = repository.findUserWithVideos(user.getId());
        assertThat(found.getVideos()).hasSize(2).extracting(Video::getTitle).containsOnly("Születésnap", "Lagzi");
    }

    @Test
    @DisplayName("Test: User status is changed after 5 video upload.")
    void testChangingStatus() {
        repository.saveUser(user);
        userService.uploadVideo(user.getId(), new Video("Esemény1"));
        userService.uploadVideo(user.getId(), new Video("Esemény2"));
        userService.uploadVideo(user.getId(), new Video("Esemény3"));
        userService.uploadVideo(user.getId(), new Video("Esemény4"));
        assertEquals(UserStatus.BEGINNER, repository.findUserWithVideos(user.getId()).getUserStatus());

        userService.uploadVideo(user.getId(), new Video("Esemény5"));

        assertEquals(UserStatus.ADVANCED, repository.findUserWithVideos(user.getId()).getUserStatus());
    }
}
