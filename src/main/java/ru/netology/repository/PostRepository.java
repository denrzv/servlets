package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


public class PostRepository {
  private List<Post> posts;
  private final AtomicLong counter;
  public PostRepository() {
    posts = new CopyOnWriteArrayList<>();
    counter = new AtomicLong(0);
  }

  public List<Post> all() {
    return Collections.unmodifiableList(posts);
  }

  public Optional<Post> getById(long id) {
    Optional<Post> result = Optional.empty();
    if(id != 0 && counter.get() >= id) {
      result = Optional.ofNullable(posts.get((int) (id - 1)));
    }
    return result;
  }

  public Post save(Post post) {
    Post finalPost = new Post(0,"");
    if (post.getId() == 0) {
      post.setId(counter.incrementAndGet());
      posts.add(post);
    } else if(counter.get() >= post.getId()) {
      posts = posts.parallelStream()
              .map(postItem -> postItem.getId() == post.getId() ? post : postItem)
              .collect(Collectors.toList());
    } else {
      return finalPost;
    }
    return post;
  }

  public boolean removeById(long id) {
    boolean result = false;
    if(id != 0 && counter.get() >= id) {
      counter.decrementAndGet();
      posts.remove((int) id - 1);
      result = true;
    }
    return result;
  }
}
