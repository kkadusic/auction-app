package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;
import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.NotFoundException;
import com.atlantbh.auctionapp.exception.UnauthorizedException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Bid;
import com.atlantbh.auctionapp.model.Card;
import com.atlantbh.auctionapp.model.Image;
import com.atlantbh.auctionapp.model.Payment;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.model.Wishlist;
import com.atlantbh.auctionapp.projection.ColorCountProjection;
import com.atlantbh.auctionapp.projection.ProductCountProjection;
import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.projection.SizeCountProjection;
import com.atlantbh.auctionapp.projection.UserProductProjection;
import com.atlantbh.auctionapp.repository.BidRepository;
import com.atlantbh.auctionapp.repository.CardRepository;
import com.atlantbh.auctionapp.repository.ImageRepository;
import com.atlantbh.auctionapp.repository.PaymentRepository;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.repository.SubcategoryRepository;
import com.atlantbh.auctionapp.repository.WishlistRepository;
import com.atlantbh.auctionapp.request.CardRequest;
import com.atlantbh.auctionapp.request.PaymentRequest;
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
import com.stripe.exception.StripeException;
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
import java.util.Optional;
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
    private final BidRepository bidRepository;
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;
    private final WishlistRepository wishlistRepository;
    private final Hunspell speller;

    @Autowired
    public ProductService(ProductRepository productRepository, ImageRepository imageRepository,
                          SubcategoryRepository subcategoryRepository, PersonRepository personRepository,
                          CardRepository cardRepository, BidRepository bidRepository,
                          PaymentRepository paymentRepository, StripeService stripeService,
                          WishlistRepository wishlistRepository, Hunspell speller) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.personRepository = personRepository;
        this.cardRepository = cardRepository;
        this.bidRepository = bidRepository;
        this.paymentRepository = paymentRepository;
        this.stripeService = stripeService;
        this.wishlistRepository = wishlistRepository;
        this.speller = speller;
    }

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

        Long id;
        try {
            id = JwtTokenUtil.getRequestPersonId();
        } catch (UnauthorizedException ignore) {
            id = -1L;
        }

        String tsQuery = formTsQuery(query);

        Slice<SimpleProductProjection> searchResult = productRepository.search(
                query.toLowerCase(),
                tsQuery,
                category.toLowerCase(),
                subcategory.toLowerCase(),
                id,
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
        PriceCountResponse price = getPriceInfo(prices);
        return new FilterCountResponse(colors, sizes, price);
    }

    private PriceCountResponse getPriceInfo(List<BigDecimal> prices) {
        if (prices.isEmpty()) {
            return new PriceCountResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new int[24]);
        }
        if (prices.size() == 1) {
            return new PriceCountResponse(prices.get(0), prices.get(0), prices.get(0), new int[24]);
        }
        PriceCountResponse price = new PriceCountResponse();
        price.setMinPrice(prices.get(0));
        price.setMaxPrice(prices.get(prices.size() - 1));
        price.setAvgPrice(average(prices));
        price.setPrices(priceHistogram(prices, prices.get(0), prices.get(prices.size() - 1), 24));
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

    private BigDecimal average(List<BigDecimal> bigDecimals) {
        BigDecimal sum = bigDecimals.stream()
                .map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(bigDecimals.size()), RoundingMode.HALF_UP);
    }

    private String formTsQuery(String query) {
        return query.replaceAll("[\\p{P}\\p{S}]", "").trim().replace(" ", " & ");
    }

    public List<UserProductProjection> getUserBidProducts() {
        Long personId = JwtTokenUtil.getRequestPersonId();
        return productRepository.getUserBidProducts(personId);
    }

    public Long add(ProductRequest productRequest) {
        Subcategory subcategory = subcategoryRepository.findById(productRequest.getSubcategoryId())
                .orElseThrow(() -> new UnprocessableException("Wrong subcategory id"));
        Long personId = JwtTokenUtil.getRequestPersonId();
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new UnauthorizedException("Wrong person id"));

        if (productRequest.getEndDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("End date can't be before current date");
        if (!productRequest.getEndDate().isAfter(productRequest.getStartDate()))
            throw new BadRequestException("End date must be after start date");

        CardRequest cardRequest = productRequest.getCard();

        if (productRequest.getFeatured() && cardRequest == null)
            throw new BadRequestException("Featured products must have payment details");
        if (productRequest.getShipping() && cardRequest == null)
            throw new BadRequestException("Products with shipping must have payment details");

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

        Product savedProduct = productRepository.save(product);

        BigDecimal amount = BigDecimal.ZERO;
        String description = person.getFirstName() + " " + person.getLastName() + " (" + person.getId() + ") paid for ";
        if (productRequest.getShipping()) {
            amount = amount.add(BigDecimal.valueOf(10));
            description += "shipping ";
        }
        if (productRequest.getFeatured()) {
            if (productRequest.getShipping())
                description += ", ";
            description += "featuring ";
            amount = amount.add(BigDecimal.valueOf(5));
        }
        description += product.getName() + " (" + product.getId() + ")";

        if (!amount.equals(BigDecimal.ZERO)) {
            payWithCard(amount, cardRequest, person, savedProduct, description, Optional.empty());
        }

        savePhotos(productRequest.getImages(), savedProduct);
        return savedProduct.getId();
    }

    private Card getAndSaveCard(CardRequest cardRequest, Person person) {
        Card card = null;
        if (cardRequest != null) {
            if (cardRequest.getExpirationYear() < Calendar.getInstance().get(Calendar.YEAR) ||
                    cardRequest.getExpirationYear() == Calendar.getInstance().get(Calendar.YEAR) &&
                            cardRequest.getExpirationMonth() < Calendar.getInstance().get(Calendar.MONTH) + 1)
                throw new BadRequestException("Entered card has expired");
            if (!cardRequest.getCardNumber().matches("^(\\d*)$")) {
                Card existingCard = cardRepository.findByPersonIdAndSavedIsTrue(person.getId())
                        .orElseThrow(() -> new BadRequestException("Card number can only contain digits"));
                if (!existingCard.getMaskedCardNumber().equals(cardRequest.getCardNumber()))
                    throw new BadRequestException("Card number can only contain digits");
                if (!existingCard.getName().equals(cardRequest.getName()) ||
                        !existingCard.getExpirationYear().equals(cardRequest.getExpirationYear()) ||
                        !existingCard.getExpirationMonth().equals(cardRequest.getExpirationMonth()) ||
                        !existingCard.getCvc().equals(cardRequest.getCvc()))
                    throw new BadRequestException("Wrong card info");
                return existingCard;
            }
            card = cardRepository.findByNameAndCardNumberAndExpirationYearAndExpirationMonthAndCvcAndPerson(
                    cardRequest.getName(),
                    cardRequest.getCardNumber(),
                    cardRequest.getExpirationYear(),
                    cardRequest.getExpirationMonth(),
                    cardRequest.getCvc(),
                    person
            ).orElseGet(() -> {
                Card newCard = new Card(
                        cardRequest.getName(),
                        cardRequest.getCardNumber(),
                        cardRequest.getExpirationYear(),
                        cardRequest.getExpirationMonth(),
                        cardRequest.getCvc(),
                        person,
                        false
                );
                String stripeCardId;
                try {
                    stripeCardId = stripeService.saveCard(newCard, person, false);
                } catch (StripeException e) {
                    throw new BadRequestException(e.getStripeError().getMessage());
                }
                newCard.setStripeCardId(stripeCardId);
                cardRepository.save(newCard);
                return newCard;
            });
        }
        return card;
    }

    private void savePhotos(List<String> photoUrls, Product product) {
        if (photoUrls == null || photoUrls.isEmpty())
            return;
        List<Image> images = photoUrls.stream().map(url -> new Image(url, product)).collect(Collectors.toList());
        images.get(0).setFeatured(true);
        imageRepository.saveAll(images);
    }

    public List<UserProductProjection> getUserProducts() {
        Long personId = JwtTokenUtil.getRequestPersonId();
        return productRepository.getUserProducts(personId);
    }

    private void payWithCard(BigDecimal amount, CardRequest cardRequest, Person person, Product product,
                             String description, Optional<PaymentRequest> paymentRequest) {
        Card card = getAndSaveCard(cardRequest, person);
        String chargeId;
        try {
            chargeId = stripeService.pay(
                    amount.multiply(BigDecimal.valueOf(100)).intValue(),
                    person.getStripeCustomerId(),
                    card.getStripeCardId(),
                    description
            );
            Payment payment = new Payment(amount, person, product);
            payment.setCard(card);
            payment.setStripeChargeId(chargeId);
            paymentRequest.ifPresent(payRequest -> {
                payment.setStreet(payRequest.getStreet());
                payment.setCountry(payRequest.getCountry());
                payment.setCity(payRequest.getCity());
                payment.setZip(payRequest.getZip());
                payment.setPhone(payRequest.getPhoneNumber());
            });
            paymentRepository.save(payment);
        } catch (StripeException e) {
            throw new BadRequestException(e.getStripeError().getMessage());
        }
    }

    public void pay(PaymentRequest paymentRequest) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new UnauthorizedException("Wrong person id"));
        Product product = productRepository.findById(paymentRequest.getProductId())
                .orElseThrow(() -> new UnprocessableException("Wrong product id"));
        if (product.getEndDate().isAfter(LocalDateTime.now()))
            throw new BadRequestException("Auction hasn't ended for this product");

        CardRequest cardRequest = paymentRequest.getCard();
        if (cardRequest == null)
            throw new BadRequestException("Payment details missing");

        Bid bid = bidRepository.getHighestBidForProduct(product.getId())
                .orElseThrow(() -> new BadRequestException("This product doesn't have any bids"));
        if (!bid.getPerson().getId().equals(person.getId()))
            throw new BadRequestException("You aren't the highest bidder for this product");

        boolean alreadyPaid = paymentRepository.isProductPaidByUser(person.getId(), product.getId());
        if (alreadyPaid)
            throw new BadRequestException("You already paid for this product");

        String description = person.getFirstName() + " " + person.getLastName() + " (" + person.getId() + ") "
                + "paid for " + product.getName() + " (" + product.getId() + ")";
        payWithCard(bid.getAmount(), cardRequest, person, product, description, Optional.of(paymentRequest));
    }

    public List<UserProductProjection> getUserWishlistProducts() {
        Long personId = JwtTokenUtil.getRequestPersonId();
        return productRepository.getUserWishlistProducts(personId);
    }

    public void remove(Long productId) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new UnprocessableException("Wrong product id"));
        if (!product.getPerson().getId().equals(personId))
            throw new UnauthorizedException("You can't remove this product");
        List<Bid> bids = bidRepository.findAllByProductId(product.getId());
        List<Wishlist> wishlists = wishlistRepository.findAllByProductId(product.getId());
        List<Image> images = imageRepository.findAllByProductId(product.getId());
        bidRepository.deleteAll(bids);
        wishlistRepository.deleteAll(wishlists);
        imageRepository.deleteAll(images);
        productRepository.delete(product);
    }
}
