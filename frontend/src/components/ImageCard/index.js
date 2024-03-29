import React from 'react';
import {Image} from 'react-bootstrap';
import {useHistory} from 'react-router-dom';

import './imageCard.css';

const ImageCard = ({data, size, url}) => {
    const history = useHistory();

    return (
        <div className="item-container">
            <Image
                className={"item-image-" + size}
                src={data.imageUrl}
                onClick={() => history.push(url)}
            />
            <h3 className={"word-wrap-" + size}
                onClick={() => history.push(url)}>
                {data.name}
            </h3>
            Start from ${data.startPrice}
        </div>
    );
}

export default ImageCard;
