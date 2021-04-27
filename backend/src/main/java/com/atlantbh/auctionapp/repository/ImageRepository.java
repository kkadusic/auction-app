package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByProductIdOrderByFeaturedDesc(Long productId);

    List<Image> findAllByProductId(Long productId);

}
