package videos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit test with mocked User repository.")
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Test: Throwing exception with more then 10 video upload.")
    void testExceptionWithTenVideos() {
        List<Video> videos = List.of(
                new Video("Video1"),
                new Video("Video2"),
                new Video("Video3"),
                new Video("Video4"),
                new Video("Video5"),
                new Video("Video6"),
                new Video("Video7"),
                new Video("Video8"),
                new Video("Video9"),
                new Video("Video10"));
        User user = new User("Tommyboy", LocalDate.of(2022, 5, 10));
        videos.forEach(user::addVideo);

        when(userRepository.findUserWithVideos(any())).thenReturn(user);

        assertThatIllegalStateException()
                .isThrownBy(() -> userService.uploadVideo(1L, new Video("Video11")))
                .withMessage("User can not upload more than 10 videos.");
        verify(userRepository).findUserWithVideos(any());
    }
}