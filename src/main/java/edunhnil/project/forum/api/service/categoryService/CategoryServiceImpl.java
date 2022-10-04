package edunhnil.project.forum.api.service.categoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.categoryRepository.Category;
import edunhnil.project.forum.api.dao.categoryRepository.CategoryRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.PostUtils;

@Service
public class CategoryServiceImpl extends AbstractService<CategoryRepository>
                implements CategoryService {

        @Autowired
        private PostRepository postRepository;

        @Autowired
        PostUtils postUtils;

        @Override
        public Optional<List<CategoryResponse>> getCategories(Map<String, String> allParamsC) {
                List<Category> categories = repository.getCategories(allParamsC)
                                .orElseThrow(() -> new ResourceNotFoundException("not found"));
                List<CategoryResponse> result = new ArrayList<>();
                for (Category c : categories) {
                        Map<String, String> allParams = new HashMap<String, String>();
                        allParams.put("categoryId", Integer.toString(c.getId()));
                        allParams.put("deleted", "0");
                        allParams.put("enabled", "0");
                        List<Post> posts = postRepository
                                        .getPostsByAuthorId(allParams, "DESC", 1, 10, "created")
                                        .orElseThrow(() -> new ResourceNotFoundException("Not found any post"));
                        if (posts.size() != 0) {
                                PostResponse newestPost = postUtils.generatePostResponse(posts.get(0), "public", "");
                                result.add(new CategoryResponse(c.getId(), c.getCategoryName(), c.getPath(),
                                                posts.size(), newestPost));
                        } else {
                                result.add(new CategoryResponse(c.getId(), c.getCategoryName(), c.getPath(),
                                                posts.size(), null));
                        }
                }
                return Optional.of(result);
        }

        @Override
        public Optional<CategoryResponse> getCategoryById(int id) {
                Map<String, String> ids = new HashMap<>();
                ids.put("id", Integer.toString(id));
                List<Category> categories = repository.getCategories(ids).get();
                System.out.println(categories.size());
                if (categories.size() == 0)
                        return Optional.of(new CategoryResponse(0,
                                        "Deleted category",
                                        "",
                                        0, null));
                Category category = categories.get(0);
                List<String> searchField = new ArrayList<>();
                searchField.add("category_id");
                Map<String, String> allParams = new HashMap<String, String>();
                allParams.put("category_id", Integer.toString(category.getId()));
                allParams.put("deleted", "0");
                allParams.put("enabled", "0");
                List<Post> posts = postRepository.getPostsByAuthorId(allParams, "DESC", 1,
                                10, "created")
                                .get();
                if (posts.size() != 0) {
                        PostResponse newestPost = postUtils.generatePostResponse(posts.get(0), "public", "");
                        return Optional.of(new CategoryResponse(category.getId(),
                                        category.getCategoryName(),
                                        category.getPath(),
                                        posts.size(),
                                        newestPost));
                } else {
                        return Optional.of(new CategoryResponse(category.getId(),
                                        category.getCategoryName(),
                                        category.getPath(),
                                        posts.size(), null));
                }
        }

        @Override
        public Optional<CategoryResponse> getCategoryDetailById(int id) {
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", Integer.toString(id));
                List<Category> categories = repository.getCategories(allParams).get();
                if (categories.size() == 0)
                        return Optional.of(new CategoryResponse(0,
                                        "Deleted category",
                                        "",
                                        0, null));
                Category category = categories.get(0);
                return Optional.of(new CategoryResponse(category.getId(),
                                category.getCategoryName(),
                                category.getPath(),
                                0, null));
        }

}
