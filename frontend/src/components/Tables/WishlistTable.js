import React from 'react';
import {useHistory} from 'react-router-dom';
import {Button, Image, Table} from 'react-bootstrap';
import {getDurationBetweenDates} from "../../utilities/Date";
import {productUrl} from "../../utilities/AppUrl";
import {getUserId} from "../../utilities/Common";
import moment from 'moment';

import './tables.css';

const WishlistTable = ({products}) => {
    const history = useHistory();
    const userId = getUserId();

    const getTimeColumn = (product) => {
        const productEndDate = moment.utc(product.endDate);
        return moment().isSameOrAfter(productEndDate) ? "0s" : getDurationBetweenDates(moment(), productEndDate);
    }

    const isClosed = (endDate) => moment().isSameOrAfter(moment.utc(endDate));

    const getImageSrc = (product) => product.url !== null ? product.url : "/images/placeholder-image-gray.png";

    return (
        <Table variant="gray-transparent" responsive style={{marginTop: '10px'}}>
            <thead>
            <tr className="product-table-header">
                <th style={{width: 80}}>Item</th>
                <th>Name</th>
                <th style={{minWidth: 110}}>Time Left</th>
                <th style={{minWidth: 130}}>Highest Bid</th>
                <th style={{minWidth: 130}}>Status</th>
                <th/>
            </tr>
            </thead>
            <tbody>
            {products.map(product => (
                <tr key={product.id}>
                    <td>
                        <Image className="product-table-image"
                               onClick={() => history.push(productUrl(product))}
                               src={getImageSrc(product)}/>
                    </td>
                    <td>
                        <div style={{cursor: 'pointer'}}
                             onClick={() => history.push(productUrl(product))}
                             className="product-table-name">
                            {product.name}
                        </div>
                        <div className="product-table-id">
                            #{product.id}
                        </div>
                    </td>
                    <td>
                        {getTimeColumn(product)}
                    </td>
                    <td style={product.personId === userId ? {
                        color: '#6CC047',
                        fontWeight: 'bold'
                    } : {fontWeight: 'bold'}}>
                        {product.maxBid !== null ? "$ " + product.maxBid : "/"}
                    </td>
                    <td>
                        <div style={{display: 'flex'}}>
                            <Button
                                size="lg"
                                variant="fill-gray"
                                id="wishlist-table-button"
                                style={isClosed(product.endDate) ? {color: '#A6555F'} : {color: '#417505'}}
                                disabled
                            >
                                {isClosed(product.endDate) ? "CLOSED" : "OPEN"}
                            </Button>
                            <Button
                                size="lg-2"
                                variant="transparent-black-shadow-disabled"
                                style={{
                                    width: 105,
                                    marginLeft: 20,
                                    border: "3px solid #E3E3E3",
                                    boxShadow: "3px 3px 0 0 #ECECEC"
                                }}
                                onClick={() => history.push(productUrl(product))}
                            >
                                {isClosed(product.endDate) ? "VIEW" : "BID"}
                            </Button>
                        </div>
                    </td>
                </tr>
            ))}
            </tbody>
        </Table>
    );
}

export default WishlistTable;
