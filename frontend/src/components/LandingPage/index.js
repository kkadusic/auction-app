import React, {useEffect, useState} from 'react';
import {Button, Image, ListGroup} from 'react-bootstrap';
import {
    getCategories,
    getFeaturedRandomProducts,
    getRandomSubcategories,
    getNewProducts,
    getLastProducts
} from '../../utilities/ServerCall';
import {IoIosArrowForward} from "react-icons/io";

import './landingPage.css';

const LandingPage = ({setBreadcrumb}) => {

    useEffect(() => {
        setBreadcrumb(null, []);
        // eslint-disable-next-line
    }, [])

    const [categories, setCategories] = useState([]);
    const [featuredProducts, setFeaturedProducts] = useState([]);
    const [randomSubcategories, setRandomSubcategories] = useState([]);
    const [newArrivalsLastChanceProducts, setNewArrivalsLastChanceProducts] = useState([]);
    const [activeTab, setActiveTab] = useState(0);

    useEffect(() => {
        const fetchData = async () => {
            setCategories(await getCategories());
            setFeaturedProducts(await getFeaturedRandomProducts());
            setRandomSubcategories(await getRandomSubcategories());
            const newProducts = await getNewProducts();
            const lastProducts = await getLastProducts();
            setNewArrivalsLastChanceProducts([newProducts, lastProducts]);
        }
        fetchData();
    }, [])

    return (
        <>
            <div className="landing-page-top-container">
                <ListGroup variant="categories">
                    <ListGroup.Item style={{color: '#8367D8', fontWeight: 'bold', borderBottom: 'none'}}>
                        CATEGORIES
                    </ListGroup.Item>
                    {categories.map(category => (
                        <ListGroup.Item key={category.name} action>{category.name}</ListGroup.Item>
                    ))}
                    <ListGroup.Item action>
                        All Categories
                    </ListGroup.Item>
                </ListGroup>

                {featuredProducts.length !== 0 ?
                    <div className="featured-product-container">
                        <div className="featured-product-container-inner">
                            <h1>
                                {featuredProducts[0].name}
                            </h1>
                            <div className="featured-product-price">
                                Start from - ${featuredProducts[0].startPrice}
                            </div>
                            <div className="featured-product-description">
                                {featuredProducts[0].description}
                            </div>
                            <Button style={{width: 192}} size="xxl" variant="transparent-black-shadow">
                                BID NOW
                                <IoIosArrowForward style={{fontSize: 24}}/>
                            </Button>
                        </div>
                        <Image width="484px" height="294px" src={featuredProducts[0].images[0].url}/>
                    </div> : null}
            </div>

            {/*<div className="featured-container">*/}
            {/*    <h2>*/}
            {/*        Featured Collections*/}
            {/*    </h2>*/}
            {/*    <div className="grey-line"/>*/}
            {/*    <div className="featured-items-container">*/}
            {/*        {randomSubcategories.map(subcategory => (*/}
            {/*            <div key={subcategory.id} className="featured-item-container">*/}
            {/*                <Image className="featured-item-image-xxl" width="350px" height="350px"*/}
            {/*                       src={subcategory.imageUrl}/>*/}
            {/*                <h3>*/}
            {/*                    {subcategory.name}*/}
            {/*                </h3>*/}
            {/*                Start from ${subcategory.startPrice}*/}
            {/*            </div>*/}
            {/*        ))}*/}
            {/*    </div>*/}
            {/*</div>*/}

            <div className="featured-container">
                <h2>
                    Featured Products
                </h2>
                <div className="grey-line"/>
                <div className="featured-items-container">
                    {featuredProducts.slice(1).map(product => (
                        <div key={product.id} className="featured-item-container">
                            <Image className="featured-item-image-xl" width="260px" height="350px"
                                   src={product.images[0].url}/>
                            <h3>
                                {product.name}
                            </h3>
                            Start from ${product.startPrice}
                        </div>
                    ))}
                </div>
            </div>

            <div className="featured-container">
                <div className="tabs-container">
                    <div style={activeTab === 0 ? {borderBottom: '4px solid #8367D8'} : null} className="custom-tab"
                         onClick={() => setActiveTab(0)}>
                        New Arrivals
                    </div>
                    <div style={activeTab === 1 ? {borderBottom: '4px solid #8367D8'} : null} className="custom-tab"
                         onClick={() => setActiveTab(1)}>
                        Last Chance
                    </div>
                </div>
                <div className="grey-line"/>
                <div className="featured-items-container">
                    {newArrivalsLastChanceProducts.length !== 0 ? newArrivalsLastChanceProducts[activeTab].map(product => (
                        <div key={product.id} className="featured-item-container">
                            <Image className="featured-item-image-lg" width="260px" height="260px"
                                   src={product.images[0].url}/>
                            <h3>
                                {product.name}
                            </h3>
                            Start from ${product.startPrice}
                        </div>
                    )) : null}
                </div>
            </div>
        </>
    );
}

export default LandingPage;
