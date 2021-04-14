import React, {useState} from 'react';
import {Button} from 'react-bootstrap';
import {RiAuctionFill, RiHeartFill} from 'react-icons/ri';
import {useHistory} from 'react-router-dom';

import './imageCardOverlay.css';

const ImageCardOverlay = ({children, data, url}) => {

    const history = useHistory();
    const [visible, setVisible] = useState(false);
    const isTouchDevice = (('ontouchstart' in window) || (navigator.msMaxTouchPoints > 0));

    return (
        <div style={{position: 'relative'}} onMouseEnter={() => setVisible(!isTouchDevice && true)}
             onMouseLeave={() => setVisible(false)}>
            {visible ? (
                <div className="overlay-container">
                    <Button
                        className="overlay-wishlist-button font-15"
                        variant="fill-white"
                    >
                        Wishlist
                        <RiHeartFill className="icon-overlay"/>
                    </Button>
                    <Button
                        className="overlay-bid-button font-15"
                        variant="fill-white"
                        onClick={() => history.push(url)}
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
