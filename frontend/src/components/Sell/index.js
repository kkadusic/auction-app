import React, {useEffect, useState} from 'react';
import {useBreadcrumbContext} from "../../AppContext";
import {myAccountSellerUrl, myAccountUrl, myAccountSellerSellUrl} from "../../utilities/AppUrl";
import {Step, Stepper} from 'react-form-stepper';
import {addProduct, getCategories, getSubcategoriesForCategory, getProductFilters} from "../../utilities/ServerCall";
import SellerTab1 from "../SellerTabs/SellTab1";
import SellerTab2 from "../SellerTabs/SellTab2";
import SellTab3 from "../SellerTabs/SellTab3";

import './sell.css';

const Sell = () => {

    const {setBreadcrumb} = useBreadcrumbContext();
    const [activeTab, setActiveTab] = useState(0);
    const [product, setProduct] = useState({});
    const [categories, setCategories] = useState([]);
    const [subcategories, setSubcategories] = useState([]);
    const [filters, setFilters] = useState({colors: [], sizes: []});

    const selectCategory = async (e, handleChange) => {
        handleChange(e);
        setSubcategories([]);
        setSubcategories(await getSubcategoriesForCategory(e.target.value));
    }

    const onDone = async (product) => {
        product.phone = product.callCode + product.phone;
        delete product.callCode;
        try {
            const id = await addProduct(product);
            const categoryName = categories.filter(category => category.id === parseInt(product.categoryId))[0].name;
            const subcategoryName = subcategories.filter(subcategory => subcategory.id === parseInt(product.subcategoryId))[0].name;
            return {
                id, categoryName, subcategoryName
            };
        } catch (e) {
            return null;
        }
    }

    const tabs = [
        <SellerTab1
            categories={categories}
            filters={filters}
            subcategories={subcategories}
            selectCategory={selectCategory}
            product={product}
            setProduct={setProduct}
            setActiveTab={setActiveTab}
        />,
        <SellerTab2 product={product} setProduct={setProduct} setActiveTab={setActiveTab}/>,
        <SellTab3 product={product} setProduct={setProduct} setActiveTab={setActiveTab} onDone={onDone}/>
    ];

    useEffect(() => {
        setBreadcrumb("MY ACCOUNT", [
            {text: "MY ACCOUNT", href: myAccountUrl},
            {text: "SELLER", href: myAccountSellerUrl},
            {text: "SELL", href: myAccountSellerSellUrl}
        ]);

        const fetchData = async () => {
            try {
                setCategories(await getCategories());
                setFilters(await getProductFilters());
            } catch (e) {
            }
        }

        fetchData();
        // eslint-disable-next-line
    }, [])

    const renderStep = (active) => (
        <Step>
            <div className="white-circle">
                <div style={active ? {backgroundColor: '#8367D8'} : {backgroundColor: '#D8D8D8'}}
                     className="purple-circle"/>
            </div>
        </Step>
    )

    return (
        <>
            <Stepper
                activeStep={activeTab}
                styleConfig={{
                    activeBgColor: '#8367D8',
                    circleFontSize: 0,
                    completedBgColor: '#8367D8',
                    inactiveBgColor: '#D8D8D8',
                    size: '28px'
                }}
                connectorStateColors={true}
                connectorStyleConfig={{
                    activeColor: '#8367D8',
                    completedColor: '#8367D8',
                    disabledColor: '#D8D8D8',
                    size: '5px'
                }}
                className="sell-stepper"
            >
                {renderStep(activeTab >= 0)}
                {renderStep(activeTab >= 1)}
                {renderStep(activeTab >= 2)}
            </Stepper>
            {tabs[activeTab]}
        </>
    );
}

export default Sell;
