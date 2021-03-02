import {Button} from 'react-bootstrap';
import {useHistory} from 'react-router-dom';
import {RiAuctionFill} from "react-icons/ri";

import './pageNotFound.css';

const PageNotFound = () => {

    const history = useHistory();

    const goHome = () => {
        history.push("/");
    }

    return (
        <div className="page-not-found-container">
            <div className="page-not-found-header">
                <RiAuctionFill className="auction-icon"/>
                AUCTION
            </div>
            <div className="page-not-found-404">
                404
            </div>
            <div className="page-not-found-text">
                Ooops! Looks like the page is Not Found
            </div>
            <Button onClick={goHome} className="btn-go-back"  variant="transparent-black-shadow">
                GO BACK
            </Button>
        </div>
    );
}

export default PageNotFound;
