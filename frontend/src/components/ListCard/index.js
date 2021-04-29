import React, {useEffect, useState} from 'react';
import {Button, Image} from 'react-bootstrap';
import {RiAuctionFill, RiHeartFill} from "react-icons/ri";
import {useHistory} from 'react-router-dom';
import {removeWishlistProduct, wishlistProduct} from "../../utilities/ServerCall";
import {loginUrl} from "../../utilities/AppUrl";
import {getUserId} from "../../utilities/Common";
import {useAlertContext} from "../../AppContext";

import './listCard.css';

const ListCard = ({data, url}) => {

    const personId = getUserId();
    const history = useHistory();

    const [loadingWish, setLoadingWish] = useState(false);
    const [wished, setWished] = useState(data.wished);
    const {showMessage} = useAlertContext();

    const wishlist = async () => {
        setLoadingWish(true);
        if (personId === null) {
            history.push(loginUrl);
            return;
        }
        try {
            if (wished)
                await removeWishlistProduct(data.id);
            else
                await wishlistProduct(data.id);
            setWished(!wished);
        } catch (e) {
            showMessage("warning", "Problem with adding or removing product from wishlist");
        }
        setLoadingWish(false);
    }

    useEffect(() => {
        setWished(data.wished);
    }, [data]);

    return (
        <div className="list-item-container">
            <Image
                className={"list-item-image"}
                src={data.imageUrl}
                onClick={() => history.push(url)}
            />
            <div className="list-info-container">
                <h3>
                    {data.name}
                </h3>
                <div style={{color: '#9B9B9B'}}>
                    {data.description}
                </div>
                <div className="featured-product-price">
                    Start from ${data.startPrice}
                </div>
                <div style={{display: 'flex'}}>
                    <Button
                        className="wishlist-button"
                        style={wished ? {borderColor: '#8367D8'} : null}
                        variant="transparent-gray"
                        onClick={wishlist}
                        disabled={loadingWish}
                    >
                        Wishlist
                        {wished ? (
                            <RiHeartFill className="wishlist-icon-wished"/>
                        ) : (
                            <RiHeartFill className="wishlist-icon"/>
                        )}
                    </Button>
                    <Button
                        className="bid-button"
                        style={{marginLeft: 10}}
                        variant="transparent-gray"
                        onClick={() => history.push(url)}
                    >
                        Bid
                        <RiAuctionFill className="button-icon"/>
                    </Button>
                </div>
            </div>
        </div>
    );
}

export default ListCard;
