package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
  private final PostRepository repository;

  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public List<Post> all() {
    return repository.all();
  }

  public Optional<Post> getById(long id) {
    return repository.getById(id);
  }

  public Post save(Post post) {
    return repository.save(post);
  }

  public boolean removeById(long id) {
    return repository.removeById(id);
  }
}

