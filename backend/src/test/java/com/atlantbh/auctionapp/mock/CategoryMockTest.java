package com.atlantbh.auctionapp.mock;

import com.atlantbh.auctionapp.model.Category;
import com.atlantbh.auctionapp.repository.CategoryRepository;
import com.atlantbh.auctionapp.service.CategoryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryMockTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void retrieveCategories() {
        when(categoryRepository.findAll()).thenReturn(
                Arrays.asList(
                        new Category("Fashion"),
                        new Category("Home"),
                        new Category("Mobile")
                )
        );
        List<Category> items = categoryService.getCategories();

        assertEquals("Fashion", items.get(0).getName());
        assertEquals("Home", items.get(1).getName());
        assertEquals("Mobile", items.get(2).getName());
    }
}
