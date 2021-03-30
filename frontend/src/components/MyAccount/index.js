import './myAccount.css';
import {useBreadcrumbContext} from "../../AppContext";
import {useEffect} from "react";

const MyAccount = () => {
    const {setBreadcrumb} = useBreadcrumbContext();

    useEffect(() => {
        setBreadcrumb("MY ACCOUNT", []);
        // eslint-disable-next-line
    }, [])

    return (
        <div>
            My Account
        </div>
    );
}

export default MyAccount;
