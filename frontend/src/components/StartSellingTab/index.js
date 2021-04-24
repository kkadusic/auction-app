import React from 'react';
import {Button} from 'react-bootstrap';
import {useHistory} from 'react-router-dom';
import {IoIosArrowForward} from 'react-icons/io';
import {HiOutlineShoppingBag} from 'react-icons/hi';
import {myAccountSellerSellUrl} from "../../utilities/AppUrl";

import './startSellingTab.css';

const StartSellingTab = () => {
    const history = useHistory();

    return (
        <div className="start-sell-container">
            <div className="sell-container">
                <div className="sell-cart-container font-18">
                    <HiOutlineShoppingBag style={{fontSize: 200, color: '#8367D8'}}/>
                    You do not have any scheduled items for sale.
                </div>
                <Button
                    style={{width: 303, boxShadow: "3px 3px 0 0 rgba(131, 103, 216, 0.31)"}}
                    size="xxl"
                    variant="transparent-black-shadow"
                    onClick={() => history.push(myAccountSellerSellUrl)}
                >
                    START SELLING
                    <IoIosArrowForward style={{fontSize: 24, marginRight: -5, marginLeft: 5}}/>
                </Button>
            </div>
        </div>
    );
}

export default StartSellingTab;
