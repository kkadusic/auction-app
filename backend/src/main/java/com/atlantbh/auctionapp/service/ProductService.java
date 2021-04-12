package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;
import com.atlantbh.auctionapp.exception.NotFoundException;
import com.atlantbh.auctionapp.model.Image;
import com.atlantbh.auctionapp.projection.ColorCountProjection;
import com.atlantbh.auctionapp.projection.ProductCountProjection;
import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.projection.SizeCountProjection;
import com.atlantbh.auctionapp.projection.UserProductProjection;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.response.CategoryCountResponse;
import com.atlantbh.auctionapp.response.CountResponse;
import com.atlantbh.auctionapp.response.FilterCountResponse;
import com.atlantbh.auctionapp.response.FullProductResponse;
import com.atlantbh.auctionapp.response.PriceCountResponse;
import com.atlantbh.auctionapp.response.ProductPageResponse;
import com.atlantbh.auctionapp.response.ProductResponse;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import com.atlascopco.hunspell.Hunspell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Hunspell speller;

    @Autowired
    public ProductService(ProductRepository productRepository, Hunspell speller) {
        this.productRepository = productRepository;
        this.speller = speller;
    }

    public List<SimpleProductProjection> getFeaturedRandomProducts() {
        return productRepository.getFeaturedRandomProducts();
    }

    private String getSuggestion(String query) {
        String[] queryWords = query.split(" ");
        List<String> suggestedWords = new ArrayList<>(queryWords.length);
        for (String queryWord : queryWords) {
            if (!speller.spell(queryWord)) {
                List<String> suggestions = speller.suggest(queryWord);
                if (suggestions.isEmpty())
                    suggestedWords.add(queryWord);
                else
                    suggestedWords.add(suggestions.get(0));
            } else
                suggestedWords.add(queryWord);
        }
        return String.join(" ", suggestedWords);
    }

    public List<SimpleProductProjection> getNewProducts() {
        return productRepository.getNewArrivalsProducts();
    }

    public List<SimpleProductProjection> getLastProducts() {
        return productRepository.getLastChanceProducts();
    }

    public ProductResponse getProduct(Long productId, Long userId) {
        List<FullProductResponse> fullProducts = productRepository.getProduct(productId, userId);
        if (fullProducts.isEmpty()) {
            throw new NotFoundException("Wrong product id");
        }

        ProductResponse productResponse = new ProductResponse(
                fullProducts.get(0).getId(),
                fullProducts.get(0).getPersonId(),
                fullProducts.get(0).getName(),
                fullProducts.get(0).getDescription(),
                fullProducts.get(0).getStartPrice(),
                fullProducts.get(0).getStartDate(),
                fullProducts.get(0).getEndDate(),
                fullProducts.get(0).getWished(),
                new ArrayList<>());

        if (fullProducts.get(0).getImageId() != null) {
            for (var fullProductResponse : fullProducts) {
                productResponse.getImages().add(new Image(
                        fullProductResponse.getImageId(),
                        fullProductResponse.getImageUrl(),
                        fullProductResponse.getImageFeatured()
                ));
            }
        }

        return productResponse;
    }

    public ProductPageResponse search(String query, String category, String subcategory, Integer page, String sort,
                                      Integer minPrice, Integer maxPrice, Color color, Size size) {
        PageRequest pageRequest;
        switch (sort) {
            case "start-date-desc":
                pageRequest = PageRequest.of(page, 12, Sort.by("start_date").descending());
                break;
            case "end-date-asc":
                pageRequest = PageRequest.of(page, 12, Sort.by("end_date"));
                break;
            case "price-asc":
                pageRequest = PageRequest.of(page, 12, Sort.by("start_price"));
                break;
            case "price-desc":
                pageRequest = PageRequest.of(page, 12, Sort.by("start_price").descending());
                break;
            default:
                pageRequest = PageRequest.of(page, 12, JpaSort.unsafe(Sort.Direction.DESC,
                        "(similarity)").and(Sort.by("name")).and(Sort.by("id")));
                break;
        }

        Slice<SimpleProductProjection> searchResult = productRepository.search(
                query.toLowerCase(),
                query.replaceAll("[\\p{P}\\p{S}]", "").trim().replace(" ", " & "),
                category.toLowerCase(),
                subcategory.toLowerCase(),
                minPrice,
                maxPrice,
                color == null ? "" : color.toString(),
                size == null ? "" : size.toString(),
                pageRequest
        );

        return new ProductPageResponse(searchResult.getContent(), !searchResult.hasNext(), getSuggestion(query));
    }

    public List<CategoryCountResponse> searchCount(String query, Integer minPrice, Integer maxPrice, Color color, Size size) {
        List<ProductCountProjection> data = productRepository.categoryCount(
                query,
                formTsQuery(query),
                minPrice,
                maxPrice,
                color == null ? "" : color.toString(),
                size == null ? "" : size.toString()
        );

        List<CategoryCountResponse> response = new ArrayList<>();

        for (ProductCountProjection product : data) {
            CategoryCountResponse newCategory = new CategoryCountResponse(product.getCategoryName(), product.getCount(), new TreeSet<>());
            int i = response.indexOf(newCategory);
            if (i == -1) {
                newCategory.addSubcategory(new CountResponse(product.getSubcategoryName(), product.getCount()));
                response.add(newCategory);
            } else {
                CategoryCountResponse oldCategory = response.get(i);
                oldCategory.setCount(oldCategory.getCount() + product.getCount());
                oldCategory.addSubcategory(new CountResponse(product.getSubcategoryName(), product.getCount()));
            }
        }
        response.sort(Comparator.comparing(CategoryCountResponse::getCount).reversed());
        return response;
    }

    public FilterCountResponse filterCount(String query, String category, String subcategory, Integer minPrice,
                                           Integer maxPrice, Color color, Size size) {
        List<ColorCountProjection> colors = productRepository.colorCount(
                query,
                formTsQuery(query),
                category,
                subcategory,
                minPrice,
                maxPrice,
                size == null ? "" : size.toString()
        );
        List<SizeCountProjection> sizes = productRepository.sizeCount(
                query,
                formTsQuery(query),
                category,
                subcategory,
                minPrice,
                maxPrice,
                color == null ? "" : color.toString()
        );
        List<BigDecimal> prices = productRepository.prices(
                query,
                formTsQuery(query),
                category,
                subcategory,
                color == null ? "" : color.toString(),
                size == null ? "" : size.toString()
        );
        PriceCountResponse price = getPriceInfo(prices, 24);
        return new FilterCountResponse(colors, sizes, price);
    }

    private PriceCountResponse getPriceInfo(List<BigDecimal> prices, int bars) {
        if (prices.isEmpty()) {
            return new PriceCountResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new int[bars]);
        }
        if (prices.size() == 1) {
            return new PriceCountResponse(prices.get(0), prices.get(0), prices.get(0), new int[bars]);
        }
        PriceCountResponse price = new PriceCountResponse();
        price.setMinPrice(prices.get(0));
        price.setMaxPrice(prices.get(prices.size() - 1));
        price.setAvgPrice(average(prices, RoundingMode.HALF_UP));
        price.setPrices(priceHistogram(prices, prices.get(0), prices.get(prices.size() - 1), bars));
        return price;
    }

    private int[] priceHistogram(List<BigDecimal> prices, BigDecimal min, BigDecimal max, int bars) {
        int[] pricesCount = new int[bars];
        BigDecimal divider = max.subtract(min).divide(new BigDecimal(bars - 1), 8, RoundingMode.HALF_UP);
        for (BigDecimal price : prices) {
            ++pricesCount[price.subtract(min).divide(divider, 0, RoundingMode.HALF_UP).intValue()];
        }
        return pricesCount;
    }

    private BigDecimal average(List<BigDecimal> bigDecimals, RoundingMode roundingMode) {
        BigDecimal sum = bigDecimals.stream()
                .map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(bigDecimals.size()), roundingMode);
    }

    private String formTsQuery(String query) {
        return query.replaceAll("[\\p{P}\\p{S}]", "").trim().replace(" ", " & ");
    }

    public List<UserProductProjection> getUserBidProducts() {
        Long personId = JwtTokenUtil.getRequestPersonId();
        return productRepository.getUserBidProducts(personId);
    }
}
