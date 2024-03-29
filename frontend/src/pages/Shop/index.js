import ImageCard from '../../components/ImageCard';
import React, {useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {productUrl} from "../../utilities/AppUrl";
import {BsGrid3X3GapFill} from "react-icons/bs";
import {FaThList} from "react-icons/fa";
import {searchProducts, filterCountProducts} from "../../utilities/ServerCall";
import {Button, Form, Spinner} from 'react-bootstrap';
import CategoriesFilter from '../../components/CategoriesFilter';
import ItemNotFound from '../../components/ItemNotFound';
import ListCard from "../../components/ListCard";
import PriceFilter from "../../components/PriceFilter";
import {removeSpaces} from "../../utilities/AppUrl";
import {useBreadcrumbContext, useAlertContext} from "../../AppContext";
import ImageCardOverlay from "../../components/ImageCardOverlay";
import * as qs from 'query-string';
import ReactTagInput from "@pathofdev/react-tag-input";
import "@pathofdev/react-tag-input/build/index.css";

import './shop.css';

let page = 0;
let queryChanged = true;

const Shop = () => {

    const [products, setProducts] = useState([]);
    const [filterCount, setFilterCount] = useState({});
    const [filter, setFilter] = useState({category: null, subcategory: null, minPrice: null, maxPrice: null});
    const [gridLayout, setGridLayout] = useState(true);
    const [lastPage, setLastPage] = useState(true);
    const [loading, setLoading] = useState(true);
    const history = useHistory();
    const urlParams = qs.parse(history.location.search);
    const {setBreadcrumb} = useBreadcrumbContext();
    const {showMessage} = useAlertContext();
    const [tags, setTags] = useState([]);

    useEffect(() => {
        setLoading(true);
        formBreadcrumb();
        // eslint-disable-next-line
    }, [history.location.pathname, history.location.search])

    useEffect(() => {
        queryChanged = true;
    }, [urlParams.query]);

    const formCategoryName = (name) => {
        if (name === undefined)
            return null;
        name = name.split("_").join(" ");
        return name.charAt(0).toUpperCase() + name.slice(1);
    }

    const capitalizeFirstLetter = (word) => {
        return word.charAt(0).toUpperCase() + word.slice(1);
    }

    const formBreadcrumb = () => {
        const urlElements = history.location.pathname.split("/").slice(1);
        if (urlElements.length === 1) {
            setBreadcrumb("SHOP", []);
        } else {
            setBreadcrumb("SHOP", urlElements.map((el, i) => {
                return {
                    text: el.toUpperCase().split("_").join(" "),
                    href: qs.stringifyUrl({url: "/" + urlElements.slice(0, i + 1).join("/"), query: urlParams})
                }
            }));
        }
        refreshData(urlElements);
    }

    const removeElement = (array, element) => {
        const index = array.indexOf(element);
        array.splice(index, 1);
    }

    const updateTags = (category, subcategory) => {
        let contains = false;
        if (category != null) {
            if (subcategory == null) {
                for (let t of tags) {
                    if (t[0] === category) {
                        contains = true;
                        removeElement(tags, t);
                    }
                }
                if (!contains) {
                    tags.push([category]);
                }
            } else {
                for (let t of tags) {
                    if (getCategorySubcategoryFromTag(t[0]).subcategory === subcategory) {
                        contains = true;
                        removeElement(tags, t);
                    }
                }
                if (!contains) {
                    tags.push([category + ": " + subcategory]);
                }
            }
        }
        setTags([...new Set(tags)]);
    }

    const getCategorySubcategoryFromTag = (tagName) => {
        if (tagName.includes(":")) {
            let list = tagName.split(": ");
            return {category: list[0], subcategory: list[1]};
        }
        return {category: tagName, subcategory: null};
    }

    const updateProducts = (dataProducts, currentProducts) => {
        for (let d of dataProducts) {
            let contains = false;
            for (let t of currentProducts) {
                if (d.id === t.id) {
                    contains = true;
                }
            }
            if (!contains) {
                currentProducts.push(d);
                contains = false;
            }
        }
        return currentProducts;
    }

    const refreshData = async (urlElements) => {
        page = 0;
        const newFilter = {
            category: formCategoryName(urlElements[1]),
            subcategory: formCategoryName(urlElements[2]),
            minPrice: urlParams.minPrice,
            maxPrice: urlParams.maxPrice
        };
        setFilter(newFilter);

        updateTags(newFilter.category, newFilter.subcategory);

        let data = [];
        let currentProducts = [];
        if (tags.length === 0) {
            data = await searchProducts(urlParams.query, null, null, newFilter.minPrice, newFilter.maxPrice, page, urlParams.sort);
            currentProducts = updateProducts(data.products, currentProducts);
        } else {
            for (let i = 0; i < tags.length; i++) {
                let tagName = getCategorySubcategoryFromTag(tags[i][0]);
                data = await searchProducts(urlParams.query, tagName.category, tagName.subcategory, newFilter.minPrice, newFilter.maxPrice, page, urlParams.sort);
                currentProducts = updateProducts(data.products, currentProducts);
            }
        }


        setFilterCount(await filterCountProducts(urlParams.query, newFilter.category, newFilter.subcategory, newFilter.minPrice, newFilter.maxPrice));
        setProducts(currentProducts);
        setLastPage(data.lastPage);

        if (queryChanged && urlParams.query !== undefined && data.didYouMean !== "" && urlParams.query !== data.didYouMean) {
            showMessage("warning", (
                <>
                    Did you mean?
                    <span
                        className="font-18"
                        style={{marginLeft: 20, color: '#8367D8', cursor: 'pointer'}}
                        onClick={() => history.push({
                            search: qs.stringify({...urlParams, query: data.didYouMean})
                        })}
                    >
                            {capitalizeFirstLetter(data.didYouMean)}
                        </span>
                </>
            ));
            queryChanged = false;
        }
        setLoading(false);
    }

    const exploreMore = async () => {
        setLoading(true);
        page++;
        const data = await searchProducts(urlParams.query, filter.category, filter.subcategory, filter.minPrice, filter.maxPrice, page, urlParams.sort);
        setProducts([...products, ...data.products]);
        setLastPage(data.lastPage);
        setLoading(false);
    }

    const sortBy = async (sort) => {
        if ((tags[tags.length - 1] !== tags[tags.length - 2])){
            tags.push(tags[tags.length - 1]);
        }
        page = 0;
        urlParams.sort = sort;
        history.push({
            search: qs.stringify(urlParams)
        });
    }

    const handleClick = (selected) => {
        let categoryPath = "";
        let subcategoryPath = "";
        if (selected.category !== null)
            categoryPath = "/" + removeSpaces(selected.category);
        if (selected.subcategory !== null)
            subcategoryPath = "/" + removeSpaces(selected.subcategory);
        history.push({
            pathname: '/shop' + categoryPath + subcategoryPath,
            search: qs.stringify(urlParams)
        });
    }

    const handlePriceClick = (selected) => {
        if ((tags[tags.length - 1] !== tags[tags.length - 2])){
            tags.push(tags[tags.length - 1]);
        }
        urlParams.minPrice = selected.minPrice;
        urlParams.maxPrice = selected.maxPrice;
        history.push({
            search: qs.stringify(urlParams)
        });
    }

    return (
        <>
            <div style={{marginBottom: "20px", marginTop: "-10px"}}>
                <ReactTagInput
                    tags={tags}
                    placeholder=" "
                    removeOnBackspace={true}
                    editable={false}
                    onChange={(newTags) => {
                        handleClick({category: null, subcategory: null});
                        setTags(newTags);
                    }}
                />
            </div>
            <div>
                <div className="shop-container">
                    <div className="shop-filters-container">
                        <CategoriesFilter filter={filter} handleClick={handleClick} query={urlParams.query}/>
                        <PriceFilter minPrice={urlParams.minPrice} maxPrice={urlParams.maxPrice}
                                     filterCount={filterCount}
                                     handleClick={handlePriceClick}/>
                    </div>

                    <div className="shop-products-container">
                        <div className="shop-sorting-bar">
                            <Form.Control defaultValue={urlParams.sort} onChange={e => sortBy(e.target.value)} size="lg"
                                          as="select" className="sort-select">
                                <option value="default">Default Sorting</option>
                                <option value="start-date-desc">Added: New to Old</option>
                                <option value="end-date-asc">Time Left</option>
                                <option value="price-asc">Price: Low to High</option>
                                <option value="price-desc">Price: High to Low</option>
                            </Form.Control>
                            {loading ? <Spinner className="shop-spinner" animation="border"/> : null}
                            <div style={{display: 'flex'}}>
                                <Button onClick={() => setGridLayout(true)}
                                        style={gridLayout ? {color: 'white', backgroundColor: '#8367D8'} : null}
                                        size="lg"
                                        variant="transparent">
                                    <BsGrid3X3GapFill style={{marginRight: 10}}/>
                                    Grid
                                </Button>
                                <Button onClick={() => setGridLayout(false)}
                                        style={gridLayout ? null : {color: 'white', backgroundColor: '#8367D8'}}
                                        size="lg"
                                        variant="transparent">
                                    <FaThList style={{marginRight: 10}}/>
                                    List
                                </Button>
                            </div>
                        </div>

                        <div style={!gridLayout ? {display: 'unset'} : null} className="shop-products">
                            {products.map(product => gridLayout ? (
                                <ImageCardOverlay key={product.id} data={product} url={productUrl(product)}>
                                    <ImageCard key={product.id} data={product} size="xl" url={productUrl(product)}/>
                                </ImageCardOverlay>
                            ) : (
                                <ListCard key={product.id} data={product} url={productUrl(product)}/>
                            ))}
                        </div>

                        {!loading && products.length === 0 ? <ItemNotFound/> : null}

                        {!lastPage ?
                            <div style={{width: '100%', marginTop: 50}}>
                                <Button disabled={loading}
                                        onClick={exploreMore}
                                        style={{width: 250, margin: '0 auto'}}
                                        variant="fill-purple"
                                        size="xxl">
                                    EXPLORE MORE
                                </Button>
                            </div> : null}
                    </div>
                </div>
            </div>
        </>
    );
}

export default Shop;
