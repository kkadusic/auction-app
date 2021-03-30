import React from 'react';
import {useHistory} from 'react-router-dom';
import {Button, Image, OverlayTrigger, Table, Tooltip} from 'react-bootstrap';
import {getDurationBetweenDates, longDateTimeFormat} from "../../utilities/Date";
import {productUrl} from "../../utilities/AppUrl";
import {getUserId} from "../../utilities/Common";
import moment from 'moment';

import './productTable.css';

const ProductTable = ({products, type}) => {

    const history = useHistory();
    const userId = getUserId();

    const getTimeColumnName = () => {
        switch (type) {
            case "scheduled":
                return "Time Start";
            case "sold":
                return "Time End";
            default:
                return "Time Left";
        }
    }

    const getTimeColumn = (product) => {
        switch (type) {
            case "scheduled":
                return moment.utc(product.startDate).local().format(longDateTimeFormat);
            case "sold":
                return moment.utc(product.endDate).local().format(longDateTimeFormat);
            default:
                const productEndDate = moment.utc(product.endDate);
                return moment().isSameOrAfter(productEndDate) ? 0 : getDurationBetweenDates(moment(), productEndDate);
        }
    }

    const getImageSrc = (product) => product.url !== null ? product.url : "https://i.imgur.com/8iwz0tG.png";

    const getMaxBidStyle = (product) => {
        if (product.max === null) {
            return {fontWeight: 'bold'};
        } else if (product.personId === userId) {
            return {color: '#6CC047', fontWeight: 'bold'};
        } else {
            return {color: '#5B9ED6', fontWeight: 'bold'};
        }
    }

    return (
        <Table variant="gray-transparent" responsive style={{marginTop: '94px'}}>
            <thead>
            <tr className="product-table-header">
                <th style={{width: 80}}>Item</th>
                <th>Name</th>
                <th style={{minWidth: 110}}>{getTimeColumnName()}</th>
                <th style={{minWidth: 130}}>Your Price</th>
                <th style={{minWidth: 100}}>No. Bids</th>
                <th style={{minWidth: 130}}>Highest Bid</th>
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
                        <div style={{cursor: 'pointer'}} onClick={() => history.push(productUrl(product))}
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
                    <td style={type === "bids" && product.personId === userId ? {
                        color: '#6CC047',
                        fontWeight: 'bold'
                    } : null}>
                        $ {product.price}
                    </td>
                    <td>{product.bidCount}</td>
                    <td style={getMaxBidStyle(product)}>
                        {product.maxBid !== null ? "$ " + product.maxBid : "/"}
                    </td>
                    <td>
                        {type === "bids" && moment().isSameOrAfter(moment.utc(product.endDate)) && product.personId === userId ?
                            <Button
                                size="lg-2"
                                variant="fill-purple-shadow"
                                style={{width: 105, backgroundColor: '#8367D8', color: 'white'}}
                            >
                                PAY
                            </Button> :
                            <Button
                                size="lg-2"
                                variant="transparent-black-shadow-disabled"
                                style={{width: 105, border: '3px solid #E3E3E3'}}
                                onClick={() => history.push(productUrl(product))}
                            >
                                VIEW
                            </Button>
                        }
                    </td>
                </tr>
            ))}
            </tbody>
        </Table>
    );
}

export default ProductTable;
