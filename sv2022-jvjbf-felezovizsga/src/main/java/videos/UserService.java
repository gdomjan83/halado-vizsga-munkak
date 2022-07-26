package videos;

public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void uploadVideo(long id, Video video) {
        User user = repository.findUserWithVideos(id);
        if (checkIfLessThanTenVideosUploaded(user)) {
            repository.updateUserWithVideo(id, video);
            user = repository.findUserWithVideos(id);
            checkUserStatus(user);
        }
    }

    private boolean checkIfLessThanTenVideosUploaded(User user) {
        if (user.getVideos().size() == 10) {
            throw new IllegalStateException("User can not upload more than 10 videos.");
        }
        return true;
    }

    private void checkUserStatus(User user) {
        if (user.getVideos().size() == 5) {
            repository.updateUserStatus(user.getId(), UserStatus.ADVANCED);
        }
    }
}
