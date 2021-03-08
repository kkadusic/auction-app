package com.atlantbh.auctionapp.mock;

import com.atlantbh.auctionapp.model.Category;
import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.repository.SubcategoryRepository;
import com.atlantbh.auctionapp.service.SubcategoryService;
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
public class SubcategoryMockTest {

    @InjectMocks
    private SubcategoryService subcategoryService;

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Test
    public void retrieveSubcategories() {
        when(subcategoryRepository.getRandomSubcategories()).thenReturn(
                Arrays.asList(
                        new Subcategory("Jeans", new Category("Fashion")),
                        new Subcategory("Lamps", new Category("Home")),
                        new Subcategory("Samsung", new Category("Mobile"))
                )
        );
        List<Subcategory> items = subcategoryService.getRandomSubcategories();

        assertEquals("Jeans", items.get(0).getName());
        assertEquals("Lamps", items.get(1).getName());
        assertEquals("Samsung", items.get(2).getName());
    }
}
