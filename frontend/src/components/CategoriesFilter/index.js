import React, {useEffect, useState} from 'react';
import {ListGroup} from 'react-bootstrap';
import {TiPlus, TiMinus} from 'react-icons/ti';
import {searchCountProducts} from "../../utilities/ServerCall";

import './categoriesFilter.css';

const activeItemStyle = {
    fontWeight: 'bold',
    backgroundColor: '#ECECEC'
};

const CategoriesFilter = ({query, filter, handleClick}) => {

    const [categories, setCategories] = useState([]);
    const [activeCategory, setActiveCategory] = useState("");
    const [activeSubcategory, setActiveSubcategory] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            setCategories(await searchCountProducts(query));
        }
        fetchData();
    }, [query]);

    useEffect(() => {
        setActiveCategory(filter.category || "");
        setActiveSubcategory(filter.subcategory || "");
    }, [filter])

    const allCategoryClick = () => {
        setActiveCategory("");
        setActiveSubcategory("");
        handleClick({category: null, subcategory: null});
    }

    const categoryClick = (categoryName) => {
        setActiveSubcategory("");
        if (activeCategory === categoryName) {
            setActiveCategory("");
            handleClick({category: null, subcategory: null});
        } else {
            setActiveCategory(categoryName);
            handleClick({category: categoryName, subcategory: null});
        }
    }

    const subcategoryClick = (subcategoryName) => {
        setActiveSubcategory(subcategoryName);
        handleClick({category: activeCategory, subcategory: subcategoryName});
    }

    return (
        <ListGroup variant="categories-filter">
            <ListGroup.Item className="categories-filter-title">PRODUCT CATEGORIES</ListGroup.Item>
            <ListGroup.Item
                action
                style={activeCategory === "" && activeSubcategory === "" ? activeItemStyle : {color: '#252525'}}
                onClick={allCategoryClick}
            >
                All Categories
            </ListGroup.Item>
            {categories.map(category => (
                <React.Fragment key={category.name}>
                    <ListGroup.Item
                        action
                        onClick={() => categoryClick(category.name)}
                        style={category.name === activeCategory && activeSubcategory === "" ? activeItemStyle : {color: '#252525'}}
                    >
                        {category.name}
                        {' (' + category.count + ')'}
                        {category.name === activeCategory ?
                            <TiMinus style={{fontSize: 24, color: '#8367D8'}}/> : <TiPlus style={{fontSize: 24}}/>}
                    </ListGroup.Item>
                    {category.name === activeCategory ? category.subcategories.map(subcategory => (
                        <ListGroup.Item
                            style={category.name === activeCategory && subcategory.name === activeSubcategory ? activeItemStyle : {color: '#9B9B9B'}}
                            key={subcategory.name}
                            action
                            onClick={() => subcategoryClick(subcategory.name)}
                        >
                            {subcategory.name}
                            {' (' + subcategory.count + ')'}
                        </ListGroup.Item>
                    )) : null}
                </React.Fragment>
            ))}
        </ListGroup>
    );
}

export default CategoriesFilter;
