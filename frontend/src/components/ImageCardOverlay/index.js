import React, {useState, useEffect} from 'react';
import {Button} from 'react-bootstrap';
import {RiAuctionFill, RiHeartFill} from 'react-icons/ri';
import {useHistory} from 'react-router-dom';
import {removeWishlistProduct, wishlistProduct} from "../../utilities/ServerCall";
import {useAlertContext} from "../../AppContext";
import {getUserId} from "../../utilities/Common";
import {loginUrl} from "../../utilities/AppUrl";

import './imageCardOverlay.css';

const ImageCardOverlay = ({children, data, url}) => {

    const history = useHistory();
    const personId = getUserId();
    const [visible, setVisible] = useState(false);
    const isTouchDevice = (('ontouchstart' in window) || (navigator.msMaxTouchPoints > 0));
    const [loadingWish, setLoadingWish] = useState(false);
    const [wished, setWished] = useState(data.wished);
    const {showMessage} = useAlertContext();

    useEffect(() => {
        setWished(data.wished);
    }, [data]);

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

    return (
        <div style={{position: 'relative'}} onMouseEnter={() => setVisible(!isTouchDevice && true)}
             onMouseLeave={() => setVisible(false)}>
            {visible ? (
                <div className="overlay-container">
                    <Button
                        className="overlay-wishlist-button font-15"
                        style={wished ? {borderColor: '#8367D8'} : {borderColor: 'white'}}
                        variant="fill-white"
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
                        className="overlay-bid-button font-15"
                        variant="fill-white"
                        onClick={() => history.push(url)}
                        style={{borderColor: 'white'}}
                    >
                        Bid
                        <RiAuctionFill className="icon-overlay"/>
                    </Button>
                </div>
            ) : null}
            {children}
        </div>
    );
}

export default ImageCardOverlay;
