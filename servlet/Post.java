package servlet;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String userName;
    private List<String> posts;

    public Post(String userName, List<String> posts) {
        this.userName = userName;
        // Ensure the list is always 5 elements long (null for empty slots)
        this.posts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            this.posts.add(i < posts.size() ? posts.get(i) : null);
        }
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getPosts() {
        return posts;
    }

    public String getPost(int index) {
        if (index >= 0 && index < posts.size()) {
            return posts.get(index);
        }
        return null;
    }

    public boolean hasPosts() {
        return posts.stream().anyMatch(post -> post != null && !post.isEmpty());
    }
}