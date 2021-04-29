import React, {useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {categoryUrl, homeUrl, subcategoryUrl} from '../../utilities/AppUrl';
import {getSubcategories} from '../../utilities/ServerCall';
import {useAlertContext, useBreadcrumbContext} from "../../AppContext";

import './allCategories.css';

const AllCategories = () => {

    const history = useHistory();
    const [subcategories, setSubcategories] = useState([]);
    const {setBreadcrumb} = useBreadcrumbContext();
    const {showMessage} = useAlertContext()

    useEffect(() => {
        setBreadcrumb("ALL CATEGORIES", [{text: "HOME", href: homeUrl}, {text: "ALL CATEGORIES"}]);

        const fetchData = async () => {
            try {
                setSubcategories(await getSubcategories());
            } catch (e) {
                showMessage("warning", "Error fetching data");
            }
        }
        fetchData();
        // eslint-disable-next-line
    }, [])

    return (
        <div className="all-categories-cont">
            {subcategories.map(category => (
                <div className="category-list">
                    <h3
                        style={{marginBottom: 10}}
                        className="category-list-item"
                        key={category.id}
                        onClick={() => history.push({
                            pathname: categoryUrl(category),
                            state: {fromLandingPage: true}
                        })}
                    >
                        {category.name}
                    </h3>
                    {category.subcategories.map(subcategory => (
                        <div
                            style={{marginBottom: 5}}
                            className="category-list-item"
                            key={subcategory.id}
                            onClick={() => history.push({
                                pathname: subcategoryUrl(subcategory, category),
                                state: {fromLandingPage: true}
                            })}
                        >
                            {subcategory.name}
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
}

export default AllCategories;
