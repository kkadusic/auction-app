package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;
import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.NotFoundException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Card;
import com.atlantbh.auctionapp.model.Image;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.projection.ColorCountProjection;
import com.atlantbh.auctionapp.projection.ProductCountProjection;
import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.projection.SizeCountProjection;
import com.atlantbh.auctionapp.projection.UserProductProjection;
import com.atlantbh.auctionapp.repository.CardRepository;
import com.atlantbh.auctionapp.repository.ImageRepository;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.repository.SubcategoryRepository;
import com.atlantbh.auctionapp.request.CardRequest;
import com.atlantbh.auctionapp.request.ProductRequest;
import com.atlantbh.auctionapp.response.CategoryCountResponse;
import com.atlantbh.auctionapp.response.CountResponse;
import com.atlantbh.auctionapp.response.FilterCountResponse;
import com.atlantbh.auctionapp.projection.FullProductProjection;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final PersonRepository personRepository;
    private final CardRepository cardRepository;
    private final Hunspell speller;

    public ProductService(ProductRepository productRepository, ImageRepository imageRepository,
                          SubcategoryRepository subcategoryRepository, PersonRepository personRepository,
                          CardRepository cardRepository, Hunspell speller) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.personRepository = personRepository;
        this.cardRepository = cardRepository;
        this.speller = speller;
    }

    @Autowired


    public List<SimpleProductProjection> getFeaturedRandomProducts() {
        return productRepository.getFeaturedRandomProducts();
    }

    public List<SimpleProductProjection> getNewProducts() {
        return productRepository.getNewArrivalsProducts();
    }

    public List<SimpleProductProjection> getLastProducts() {
        return productRepository.getLastChanceProducts();
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

    public ProductResponse getProduct(Long productId, Long userId) {
        FullProductProjection product = productRepository.getProduct(productId, userId)
                .orElseThrow(() -> new NotFoundException("Wrong product id"));
        List<Image> productPhotos = imageRepository.findAllByProductIdOrderByFeaturedDesc(productId);
        return new ProductResponse(product, productPhotos);
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

        String tsQuery = formTsQuery(query);

        Slice<SimpleProductProjection> searchResult = productRepository.search(
                query.toLowerCase(),
                tsQuery,
                category.toLowerCase(),
                subcategory.toLowerCase(),
                minPrice,
                maxPrice,
                color == null ? "" : color.toString(),
                size == null ? "" : size.toString(),
                pageRequest
        );

        String suggestion = getSuggestion(query);
        if (suggestion.equalsIgnoreCase(query) || !productRepository.searchExists(query, tsQuery)) {
            suggestion = query;
        }

        return new ProductPageResponse(searchResult.getContent(), !searchResult.hasNext(), suggestion);
    }

    public List<CategoryCountResponse> searchCount(String query, Integer minPrice, Integer maxPrice, Color color, Size size) {
        List<ProductCountProjection> productCounts = productRepository.categoryCount(
                query,
                formTsQuery(query),
                minPrice,
                maxPrice,
                color == null ? "" : color.toString(),
                size == null ? "" : size.toString()
        );

        List<CategoryCountResponse> response = new ArrayList<>();

        Set<CountResponse> subcategoryCount = new TreeSet<>();
        for (ProductCountProjection productCount : productCounts) {
            if (productCount.getSubcategoryName() != null) {
                subcategoryCount.add(new CountResponse(productCount.getSubcategoryName(), productCount.getCount()));
            } else if (productCount.getCategoryName() != null) {
                response.add(new CategoryCountResponse(productCount.getCategoryName(), productCount.getCount(), subcategoryCount));
                subcategoryCount = new TreeSet<>();
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

    public void add(ProductRequest productRequest) {
        Subcategory subcategory = subcategoryRepository.findById(productRequest.getSubcategoryId())
                .orElseThrow(() -> new UnprocessableException("Wrong subcategory id"));
        Long personId = JwtTokenUtil.getRequestPersonId();
        if (personId == null)
            throw new UnprocessableException("Invalid JWT signature");
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new UnprocessableException("Wrong person id"));

        if (productRequest.getStartDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Start date can't be before current date");
        if (productRequest.getEndDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("End date can't be before current date");
        if (!productRequest.getEndDate().isAfter(productRequest.getStartDate()))
            throw new BadRequestException("End date must be after start date");

        CardRequest cardRequest = productRequest.getCard();
        if (productRequest.getFeatured() && cardRequest == null)
            throw new BadRequestException("Featured products must have card details");
        if (productRequest.getShipping() && cardRequest == null)
            throw new BadRequestException("Products with shipping must have card details");

        Card card = getAndSaveCard(cardRequest);


        Product product = new Product(
                productRequest.getName(),
                productRequest.getStartPrice(),
                productRequest.getStartDate(),
                productRequest.getEndDate(),
                productRequest.getStreet(),
                productRequest.getCity(),
                productRequest.getZip(),
                productRequest.getCountry(),
                productRequest.getPhone(),
                person,
                subcategory
        );
        product.setDescription(productRequest.getDescription());
        product.setColor(productRequest.getColor());
        product.setSize(productRequest.getSize());
        product.setFeatured(productRequest.getFeatured());
        product.setShipping(productRequest.getShipping());
        product.setCard(card);


        Product savedProduct = productRepository.save(product);
        savePhotos(productRequest.getImages(), savedProduct);
    }

    private Card getAndSaveCard(CardRequest cardRequest) {
        Card card = null;
        if (cardRequest != null) {
            if (cardRequest.getExpirationYear() < Calendar.getInstance().get(Calendar.YEAR) ||
                    cardRequest.getExpirationYear() == Calendar.getInstance().get(Calendar.YEAR) &&
                            cardRequest.getExpirationMonth() <= Calendar.getInstance().get(Calendar.MONTH) + 1)
                throw new BadRequestException("Entered card has expired");
            card = cardRepository.findByNameAndCardNumberAndExpirationYearAndExpirationMonthAndCvc(
                    cardRequest.getName(),
                    cardRequest.getCardNumber(),
                    cardRequest.getExpirationYear(),
                    cardRequest.getExpirationMonth(),
                    cardRequest.getCvc()
            ).orElseGet(() -> {
                Card newCard = new Card(
                        cardRequest.getName(),
                        cardRequest.getCardNumber(),
                        cardRequest.getExpirationYear(),
                        cardRequest.getExpirationMonth(),
                        cardRequest.getCvc()
                );
                cardRepository.save(newCard);
                return newCard;
            });
        }
        return card;
    }

    private void savePhotos(List<String> photoUrls, Product product) {
        if (photoUrls.isEmpty())
            return;
        List<Image> images = photoUrls.stream().map(url -> new Image(url, product)).collect(Collectors.toList());
        images.get(0).setFeatured(true);
        imageRepository.saveAll(images);
    }
}
