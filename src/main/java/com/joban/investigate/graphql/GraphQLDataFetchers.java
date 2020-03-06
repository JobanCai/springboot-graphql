package com.joban.investigate.graphql;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GraphQLDataFetchers {

  // 初始化一些数据，这些数据可以从数据库里查询出来
  // 创建books集合
  private static List<Map<String, String>> books = Arrays.asList(
      ImmutableMap.of("id", "book-1",
          "name", "Harry Potter and the Philosopher's Stone",
          "pageCount", "223",
          "authorId", "author-1",
          "categoryId", "category-2"),
      ImmutableMap.of("id", "book-2",
          "name", "Moby Dick",
          "pageCount", "635",
          "authorId", "author-2",
          "categoryId", "category-1"),
      ImmutableMap.of("id", "book-3",
          "name", "Interview with the vampire",
          "pageCount", "371",
          "authorId", "author-3")
  );

  // 创建authors集合
  private static List<Map<String, String>> authors = Arrays.asList(
      ImmutableMap.of("id", "author-1",
          "firstName", "Joanne",
          "lastName", "Rowling"),
      ImmutableMap.of("id", "author-2",
          "firstName", "Herman",
          "lastName", "Melville"),
      ImmutableMap.of("id", "author-3",
          "firstName", "Anne",
          "lastName", "Rice")
  );

  // 创建categories集合
  private static List<Map<String, String>> categories = Arrays.asList(
      ImmutableMap.of("id", "category-1",
          "name", "Programmer"),
      ImmutableMap.of("id", "category-2",
          "name", "Science"),
      ImmutableMap.of("id", "category-3",
          "name", "History")
      );

  // 根据bookId获取book对象数据
  public DataFetcher getBookByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String bookId = dataFetchingEnvironment.getArgument("id");
      if (StringUtils.isEmpty(bookId)) {
        return books;
      }
      return books
          .stream()
          .filter(book -> book.get("id").equals(bookId))
          .collect(Collectors.toList());
    };
  }

  // 从book对象里拿到authorId，然后再获取Author对象数据
  public DataFetcher getAuthorDataFetcher() {
    return dataFetchingEnvironment -> {
      Map<String, String> book = dataFetchingEnvironment.getSource();
      String authorId = book.get("authorId");
      return authors
              .stream()
              .filter(author -> author.get("id").equals(authorId))
              .findFirst()
              .orElse(null);
    };
  }

  // 从book对象里拿到categoryId，然后再获取Category对象数据
  public DataFetcher getCategoryDataFetcher() {
    return dataFetchingEnvironment -> {
      String categoryId = dataFetchingEnvironment.getArgument("id");
      return books.stream().filter(book -> book.get("categoryId").equals(categoryId)).findFirst().orElse(null);
    };
  }
}