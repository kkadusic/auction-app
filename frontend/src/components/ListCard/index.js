import {Button, Image} from 'react-bootstrap';
import {RiAuctionFill, RiHeartFill} from "react-icons/ri";
import {useHistory} from 'react-router-dom';

import './listCard.css';

const ListCard = ({data, url}) => {

    const history = useHistory();

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
                        variant="transparent-gray">
                        Wishlist
                        <RiHeartFill className="button-icon"/>
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
