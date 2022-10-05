package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.netology.servlet.Methods.*;

public class MainServlet extends HttpServlet {
  private PostController controller;
  final String URL = "/api/posts";

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
    controller = context.getBean(PostController.class);
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
        long parsedPath = parseLong(path);
        controller.getById(parsedPath, resp);
        return;
      }
      if (method.equals(POST.toString()) && path.equals(URL)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE.toString()) && path.matches(URL + "/\\d+")) {
        // easy way
        long parsedPath = parseLong(path);
        controller.removeById(parsedPath, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private Long parseLong(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}

