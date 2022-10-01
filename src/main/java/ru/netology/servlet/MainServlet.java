package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.netology.servlet.Methods.*;

public class MainServlet extends HttpServlet {
  private PostController controller;
  final String URL = "/api/posts";

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(GET.toString()) && path.equals(URL)) {
        controller.all(resp);
        return;
      }

      if (method.equals(GET.toString()) && path.matches(URL + "/\\d+")) {
        // easy way
        long parsedPath = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.getById(parsedPath, resp);
        return;
      }
      if (method.equals(POST.toString()) && path.equals(URL)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE.toString()) && path.matches(URL + "/\\d+")) {
        // easy way
        long parsedPath = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.removeById(parsedPath, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

