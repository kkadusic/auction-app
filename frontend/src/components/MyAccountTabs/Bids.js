import React, {useEffect, useState} from 'react';
import {Spinner} from 'react-bootstrap';
import ProductTable from '../Tables/ProductTable';
import {getUserBidProducts} from "../../utilities/ServerCall";
import {useAlertContext} from "../../AppContext";

import './myAccountTabs.css';

const Bids = () => {

    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const {showMessage} = useAlertContext();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setProducts(await getUserBidProducts());
            } catch (e) {
                showMessage("warning", e.response !== undefined ? +e.response.data.message : e.message);
            }
            setLoading(false);
        }
        fetchData();
    }, [])

    return (
        <>
            <ProductTable type="bids" products={products}/>
            {loading || products.length === 0 ?
                <div className="no-table-items font-18">
                    {loading
                        ? <Spinner className="table-spinner" animation="border"/>
                        : "No bids found"}
                </div> : null}
        </>
    );
}

export default Bids;
