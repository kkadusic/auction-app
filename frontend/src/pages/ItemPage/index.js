import React, {useEffect, useState} from 'react';
import {Alert, Button, Form, Image, Modal, Table} from 'react-bootstrap';
import {useHistory, withRouter} from 'react-router-dom';
import {getUserId, removeSession} from '../../utilities/Common';
import {IoIosArrowBack, IoIosArrowForward} from "react-icons/io";
import {RiHeartFill} from "react-icons/ri";
import {GiExpand} from "react-icons/gi";
import {MdKeyboardArrowLeft, MdKeyboardArrowRight} from 'react-icons/md';
import {bidForProduct, getBidsForProduct, getProduct, getRelatedProducts} from '../../utilities/ServerCall';
import {useAlertContext, useBreadcrumbContext, useUserContext} from "../../AppContext";
import moment from 'moment';
import {wishlistProduct, removeWishlistProduct} from "../../utilities/ServerCall";
import {validToken} from "../../utilities/Common";
import {getUserInfo} from "../../utilities/ServerCall";
import {GoStar} from "react-icons/go";
import {loginUrl} from "../../utilities/AppUrl";
import SortTh from "../../components/Tables/SortTh";

import './itemPage.css';

const ItemPage = ({match, location}) => {

    let personId = getUserId();

    const history = useHistory();
    const [product, setProduct] = useState(null);
    const [bids, setBids] = useState([]);
    const [activePhoto, setActivePhoto] = useState(0);
    const [showFullscreen, setShowFullscreen] = useState(false);
    const [showFullscreenIcon, setShowFullscreenIcon] = useState(false);
    const [loading, setLoading] = useState(false);
    const [active, setActive] = useState(true);
    const [ownProduct, setOwnProduct] = useState(false);
    const [bidPrice, setBidPrice] = useState(null);
    const [minPrice, setMinPrice] = useState(0);
    const [alertVisible, setAlertVisible] = useState(true);
    const [outbid, setOutbid] = useState(false);
    const {setBreadcrumb} = useBreadcrumbContext();
    const {showMessage} = useAlertContext();
    const [wished, setWished] = useState(false);
    const [loadingWish, setLoadingWish] = useState(false);
    const {loggedIn, setLoggedIn} = useUserContext();
    const [seller, setSeller] = useState(null);
    const withMessage = location.state != null && location.state.withMessage;
    const [relatedProducts, setRelatedProducts] = useState([]);
    const [sort, setSort] = useState("price");
    const [page, setPage] = useState(0);

    const productRoute = (history, product) => {
        history.push(`/shop/${product.categoryName.split(' ').join('_').toLowerCase()}/${product.subcategoryName.split(' ').join('_').toLowerCase()}/${product.id}`);
    }

    useEffect(() => {
        if (personId == null) {
            personId = 0;
        }
        formBreadcrumb();
        const fetchData = async () => {
            const productId = match.params.id;
            try {
                const data = await getProduct(productId, personId);
                setActive(moment().isBetween(moment(data.startDate), moment(data.endDate), null, "[)"));
                setOwnProduct(data.personId === personId);
                setProduct(data);
                if (loggedIn) {
                    setSeller(await getUserInfo(data.personId));
                }
                setRelatedProducts(await getRelatedProducts(productId));
                const bids = await getBidsForProduct(productId);
                const highestBidFromUser = Math.max(...bids.map(bid => bid.personId === personId ? bid.amount : 0), 0);
                setMinPrice(highestBidFromUser === 0 ? data.startPrice : highestBidFromUser + 0.01);
                setWished(data.wished);
                setBids(bids);
                if (location.state !== undefined && location.state.newProduct) {
                    showMessage("success", "You have successfully added a new product!");
                }

                if (!moment().isBetween(moment(data.startDate), moment(data.endDate)) && highestBidFromUser > 0) {
                    setOutbid(true);
                    for (let i = 0; i < bids.length; i++) {
                        if (highestBidFromUser < bids[i].amount) {
                            setOutbid(false);
                        }
                    }
                }
            } catch (e) {
                showMessage("warning", e.response !== undefined ? +e.response.data.message : e.message);
            }
        }

        fetchData();
        // eslint-disable-next-line
    }, [match.params.id])

    const formBreadcrumb = () => {
        const urlElements = match.url.split("/").slice(1, -1);
        setBreadcrumb("SINGLE PRODUCT", [...urlElements.map((el, i) => {
            return {
                text: el.toUpperCase().split("_").join(" "),
                href: "/" + urlElements.slice(0, i + 1).join("/")
            }
        }), {text: "SINGLE PRODUCT"}]);
    }

    const bid = async () => {
        if (personId === null) {
            showMessage("warning", "You have to be logged in to place bids.");
            return;
        }
        setLoading(true);
        try {
            await bidForProduct(parseFloat(bidPrice), product.id);
            const newBids = await getBidsForProduct(product.id);
            if (sort !== "price")
                setSort("price");
            setMinPrice(Math.max(...newBids.map(bid => bid.personId === personId ? bid.amount : 0), 0) + 0.01);
            if (personId === newBids[0].personId) {
                showMessage("success", "Congrats! You are the highest bider!");
            } else {
                showMessage("warning", "There are higher bids than yours. You could give a second try!");
            }
            setBids(newBids);
            setBidPrice("");
        } catch (e) {
            if (!validToken()) {
                showMessage("warning", "Your session has timed out. Please login again.");
                setLoggedIn(false);
                removeSession();
            } else {
                showMessage("warning", "Oops! Something went wrong. Please refresh the page and try again.");
            }
        }
        setLoading(false);
    }

    const getTimeInfo = () => {
        const productStartDate = moment(product.startDate)
        if (moment().isBefore(productStartDate))
            return (
                <>
                    Time start: {productStartDate.format("D MMMM YYYY [at] HH:mm")}
                    <br/>
                    Time end: {moment(product.endDate).format("D MMMM YYYY [at] HH:mm")}
                </>
            );
        const timeLeft = !active ? 0 : moment.duration(moment(product.endDate).diff(moment())).format("D [days] h [hours] m [minutes]");
        return (
            <>
                Time left: {timeLeft}
            </>
        );
    }

    const wishlist = async () => {
        if (personId === null) {
            showMessage("warning", "You have to be logged in to wishlist products.");
            return;
        }
        try {
            if (wished) {
                await removeWishlistProduct(product.id);
                showMessage("success", "You have removed the product from your wishlist.");
            } else {
                await wishlistProduct(product.id);
                showMessage("success", "You have added the product to your wishlist.");
            }
            setWished(!wished);
        } catch (e) {
            showMessage("warning", "Problem with adding or removing product from wishlist");
        }
    }

    const handleWishlist = async () => {
        setLoadingWish(true);
        await wishlist();
        setLoadingWish(false);
    }

    return (
        <>
            {product !== null ? (
                <>
                    <Modal size="xl" centered show={showFullscreen} onHide={() => setShowFullscreen(false)}>
                        {activePhoto !== 0 ?
                            <MdKeyboardArrowLeft
                                onClick={() => setActivePhoto(activePhoto - 1)}
                                className="fullscreen-image-left-arrow"/>
                            : null}
                        <Image onClick={() => setShowFullscreen(false)}
                               width="100%"
                               src={product.images[activePhoto].url}/>
                        {product.images.length !== 0 && activePhoto !== product.images.length - 1 ?
                            <MdKeyboardArrowRight
                                onClick={() => setActivePhoto(activePhoto + 1)}
                                className="fullscreen-image-right-arrow"/>
                            : null}
                    </Modal>
                    <div className="product-container">
                        <div className="images-container">
                            <Image
                                onClick={() => setShowFullscreen(true)}
                                onMouseEnter={() => setShowFullscreenIcon(true)}
                                onMouseLeave={() => setShowFullscreenIcon(false)}
                                key={product.images[0].id}
                                width="100%"
                                height="438px"
                                src={product.images[activePhoto].url}
                                style={product.images[activePhoto] === undefined ? {objectFit: 'cover'} : null}
                                className="product-image-big"
                            />
                            <GiExpand
                                onMouseEnter={() => setShowFullscreenIcon(true)}
                                onMouseLeave={() => setShowFullscreenIcon(false)}
                                style={!showFullscreenIcon ? {display: 'none'} : null}
                                className="fullscreen-image-icon"
                                onClick={() => setShowFullscreen(true)}
                            />
                            {product.images.map((photo, i) => (
                                <Image
                                    onClick={() => setActivePhoto(i)}
                                    key={photo.id}
                                    width="110px"
                                    height="110px"
                                    src={photo.url}
                                    className="product-image-small"
                                    style={activePhoto === i ? {border: '2px solid #8367D8'} : {opacity: 0.7}}
                                />
                            ))}
                        </div>

                        <div className="product-info-container">
                            <div>
                                <h1>
                                    {product.name}
                                </h1>
                                <div style={{marginTop: 10}} className="featured-product-price">
                                    Start from - ${product.startPrice}
                                </div>
                                {outbid !== false ?
                                    <div style={{marginTop: 50, maxWidth: 500, minWidth: 220}}>
                                        <Alert className="congrats-alert" dismissible
                                               onClose={() => setAlertVisible(false)} transition={false}
                                               show={alertVisible} variant="info">
                                            <div style={{marginLeft: '-2vw'}}>
                                                Congratulations!
                                                <span style={{fontWeight: 'normal'}}>
                                            {' '}You outbid the competition.
                                            </span>
                                            </div>
                                        </Alert>
                                        <div style={{marginTop: 10, display: 'flex', alignItems: 'center'}}>
                                            <span style={{color: '#9B9B9B'}}>
                                                Seller:
                                            </span>
                                            <Image style={{marginLeft: 5, marginRight: 5, objectFit: "cover"}}
                                                   className="avatar-image-small-2"
                                                   src={seller.photo}
                                                   roundedCircle
                                            />
                                            {seller.name}
                                            <GoStar
                                                style={{marginLeft: 20, color: '#8367D8', fontSize: 22, marginRight: 5}}
                                            />
                                            {seller.rating !== 0 ? (Math.round((seller.rating + Number.EPSILON) * 100) / 100 + "/5") : "No ratings"}
                                        </div>
                                    </div> : null}
                            </div>
                            {active !== false ?
                                <div className="place-bid-container">
                                    <div>
                                        <Form.Control disabled={ownProduct || !active || loading} maxLength="7"
                                                      className="form-control-gray place-bid-form" size="xl-18"
                                                      type="text"
                                                      onChange={e => setBidPrice(e.target.value)}
                                                      onKeyUp={e => e.key === 'Enter' ? bid() : null}/>
                                        {minPrice < 9999999 ?
                                            <div className="place-bid-label">
                                                Enter ${minPrice} or more
                                            </div> :
                                            <div className="place-bid-label">
                                                Maximum bid amount reached
                                            </div>}
                                    </div>
                                    <Button
                                        disabled={ownProduct || !active || loading || isNaN(bidPrice) || bidPrice < minPrice}
                                        style={{width: 192, padding: 0}} size="xxl" variant="transparent-black-shadow"
                                        onClick={bid}>
                                        PLACE BID
                                        <IoIosArrowForward style={{fontSize: 24}}/>
                                    </Button>
                                </div> : null}
                            <div style={{color: '#9B9B9B'}}>
                                Highest bid: {' '}
                                <span style={{color: '#8367D8', fontWeight: 'bold'}}>
                                    ${bids[0] === undefined ? 0 : bids[0].amount}
                                </span>
                                <br/>
                                No bids: {bids.length}
                                <br/>
                                {getTimeInfo()}
                            </div>
                            <div>
                                <Button
                                    className="wishlist-button"
                                    style={wished ? {borderColor: '#8367D8'} : null}
                                    variant="transparent-gray"
                                    onClick={handleWishlist}
                                    disabled={loadingWish}
                                >
                                    Wishlist
                                    {wished ? (
                                        <RiHeartFill className="wishlist-icon-wished"/>
                                    ) : (
                                        <RiHeartFill className="wishlist-icon"/>
                                    )}
                                </Button>
                                <div className="font-18"
                                     style={{marginTop: 15, maxWidth: '100%', wordWrap: 'break-word'}}>
                                    Details
                                    <div className="grey-line"/>
                                    <div className="font-15">
                                        {product.description}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </>
            ) : null}
            {bids.length !== 0 && product.personId === personId ? (
                <div>
                    <Table variant="gray-transparent" responsive>
                        <thead>
                        <tr className="product-table-header">
                            <SortTh colSpan="2" active={sort} setActive={setSort} data={bids} setData={setBids}
                                    name="firstName"
                                    type="string">Bidder</SortTh>
                            <SortTh style={{minWidth: 190}} active={sort} setActive={setSort} data={bids}
                                    setData={setBids}
                                    name="date" type="date">Date</SortTh>
                            <SortTh style={{minWidth: 205}} active={sort} setActive={setSort} data={bids}
                                    setData={setBids}
                                    name="amount" type="number">Bid</SortTh>
                        </tr>
                        </thead>
                        <tbody>
                        {bids.slice(page, page + 5).map((bid, i) => (
                            <tr key={bid.id}>
                                <td style={{fontWeight: 'bold'}} colSpan="2">
                                    <Image style={{marginRight: 20}}
                                           className="avatar-image-small"
                                           src={bid.imageUrl}
                                           roundedCircle/>
                                    {bid.firstName + ' ' + bid.lastName}
                                </td>
                                <td>{moment(bid.date).format("D MMMM YYYY")}</td>
                                <td style={i === 0 && page === 0 ? {
                                    color: '#6CC047',
                                    fontWeight: 'bold'
                                } : {fontWeight: 'bold'}}>{'$ ' + bid.amount}</td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                    <div style={{display: 'flex', justifyContent: 'space-between'}}>
                        <Button
                            className="sell-submit-button"
                            size="lg"
                            variant={"fill-purple-shadow"}
                            onClick={() => setPage(page <= 0 ? 0 : page - 5)}
                        >
                            <IoIosArrowBack style={{fontSize: 24, marginLeft: -5, marginRight: 5}}/>
                            BACK
                        </Button>
                        <Button
                            className="sell-submit-button"
                            size="lg"
                            variant={"fill-purple-shadow"}
                            onClick={() => page < bids.length - 5 ? setPage(page + 5) : null}
                        >
                            <IoIosArrowForward style={{fontSize: 24, marginRight: 5, marginLeft: 5}}/>
                            NEXT
                        </Button>
                    </div>
                </div>
            ) : <div style={{marginTop: 150}} className="featured-container">
                <h2>
                    Related products
                </h2>
                <div className="grey-line"/>
                <div className="featured-items-container">
                    {relatedProducts.map(product => (
                        <div key={product.id} className="featured-item-container">
                            <Image
                                id="featured-item-image-xxl"
                                src={product.imageUrl}
                                onClick={() => productRoute(history, product)}
                            />
                            <h3>
                                {product.name}
                            </h3>
                            Start from ${product.startPrice}
                        </div>
                    ))}
                </div>
            </div>
            }
        </>
    );
}

export default withRouter(ItemPage);
