import {useEffect, useState} from 'react';
import {withRouter} from 'react-router-dom';
import {getUserId} from '../../utilities/Common';
import {getBidsForProduct, getProduct} from '../../utilities/ServerCall';
import {Button, Form, Image, Table} from 'react-bootstrap';
import {IoIosArrowForward} from "react-icons/io";
import {RiHeartFill} from "react-icons/ri";
import moment from 'moment';

import './itemPage.css';

const ItemPage = ({match, setBreadcrumb}) => {

    const [product, setProduct] = useState(null);
    const [bids, setBids] = useState([]);
    const [activePhoto, setActivePhoto] = useState(0);

    useEffect(() => {
        formBreadcrumb();
        const fetchData = async () => {
            const productId = match.params.id;
            setProduct(await getProduct(productId, 0));
            setBids(await getBidsForProduct(productId));
        }
        fetchData();
        // eslint-disable-next-line
    }, [])

    const formBreadcrumb = () => {
        const urlElements = match.url.split("/").slice(1, -1);
        setBreadcrumb("SINGLE PRODUCT", [...urlElements.map((el, i) => {
            return {
                text: el.toUpperCase().split("_").join(" "),
                href: "/" + urlElements.slice(0, i + 1).join("/")
            }
        }), {text: "SINGLE PRODUCT"}]);
    }

    return (
        <>
            {product !== null ? (
                <div className="product-container">
                    <div className="images-container">
                        <Image className="product-image-big" key={product.images[0].id} height="438px" width="100%"
                               src={product.images[activePhoto].url}/>
                        {product.images.map((image, i) => (
                            <Image
                                onClick={() => setActivePhoto(i)}
                                key={image.id}
                                width="110px"
                                height="110px"
                                src={image.url}
                                className="product-image-small"
                                style={activePhoto === i ? {border: '2px solid #8367D8'} : null}
                            />
                        ))}
                    </div>

                    <div className="product-info-container">
                        <div>
                            <h1>
                                {product.name}
                            </h1>
                            <div style={{marginTop: 10}} className="featured-product-price">
                                Start from ${product.startPrice}
                            </div>
                        </div>
                        <div className="place-bid-container">
                            <div>
                                <Form.Control className="form-control-gray place-bid-form" size="xl-18" type="text"/>
                                <div className="place-bid-label">
                                    Enter ${bids[0] === undefined ? product.startPrice : bids[0].amount} or more
                                </div>
                            </div>
                            <Button style={{width: 192, padding: 0}} size="xxl" variant="transparent-black-shadow">
                                PLACE BID
                                <IoIosArrowForward style={{fontSize: 24}}/>
                            </Button>
                        </div>
                        <div style={{color: '#9B9B9B'}}>
                            Highest bid: {' '}
                            <span style={{color: '#8367D8', fontWeight: 'bold'}}>
                                ${bids[0] === undefined ? 0 : bids[0].amount}
                            </span>
                            <br/>
                            No bids: {bids.length}
                            <br/>
                            Time left: {moment(product.endDate).diff(moment(), 'days')} days
                        </div>
                        <div>
                            <Button className="wishlist-button" style={product.wished ? {borderColor: '#CD5C5C'} : null}
                                    size="xxl" variant="transparent-gray">
                                Wishlist
                                {product.wished ? (
                                    <RiHeartFill style={{fontSize: 22, marginLeft: 5, color: '#CD5C5C'}}/>
                                ) : (
                                    <RiHeartFill style={{fontSize: 22, marginLeft: 5, color: '#ECECEC'}}/>
                                )}
                            </Button>
                            <div className="font-18" style={{marginTop: 15}}>
                                Details
                                <div className="grey-line"/>
                                <div className="font-15">
                                    {product.description}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ) : null}

            {getUserId() !== null && bids.length !== 0 ? (
                <Table variant="gray-transparent" responsive>
                    <thead>
                    <tr className="product-table-header">
                        <th colSpan="2">Bider</th>
                        <th>Date</th>
                        <th>Bid</th>
                    </tr>
                    </thead>
                    <tbody>
                    {bids.map((bid, i) => (
                        <tr key={bid.id}>
                            <td style={{fontWeight: 'bold'}} colSpan="2">
                                <Image style={{marginRight: 20}} className="avatar-image-small" src={bid.imageUrl}
                                       roundedCircle/>
                                {bid.firstName + ' ' + bid.lastName}
                            </td>
                            <td>{moment(bid.date).format("D MMMM YYYY")}</td>
                            <td style={i === 0 ? {
                                color: '#6CC047',
                                fontWeight: 'bold'
                            } : {fontWeight: 'bold'}}>{'$ ' + bid.amount}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            ) : null}
        </>
    );
}

export default withRouter(ItemPage);
