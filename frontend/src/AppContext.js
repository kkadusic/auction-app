import {createContext, useContext, useState} from "react";
import {getToken} from "./utilities/Common";
import axios from "axios";

export const UserContext = createContext({});
export const AlertContext = createContext({});
export const BreadcrumbContext = createContext({});

export const useUserContext = () => useContext(UserContext);
export const useAlertContext = () => useContext(AlertContext);
export const useBreadcrumbContext = () => useContext(BreadcrumbContext);

export const AppProvider = ({children}) => {

    const [loggedIn, setLoggedIn] = useState(getToken() != null);

    const [breadcrumbTitle, setBreadcrumbTitle] = useState(null);
    const [breadcrumbItems, setBreadcrumbItems] = useState([]);

    const [alertVisible, setAlertVisible] = useState(false);
    const [variant, setVariant] = useState(null);
    const [message, setMessage] = useState(null);

    const handleError = (error) => {
        showMessage("warning", error.response !== undefined ? error.response.data.message : error.message);
        return Promise.reject(error);
    }

    axios.interceptors.response.use((response) => response, handleError);

    const showMessage = (variant, message) => {
        setVariant(variant);
        setMessage(message);
        setAlertVisible(true);
        setTimeout(() => {
            setAlertVisible(false);
        }, 3000);
    }

    const setBreadcrumb = (title, items) => {
        setBreadcrumbTitle(title);
        setBreadcrumbItems(items);
        setAlertVisible(false);
    }

    const removeBreadcrumb = () => {
        setBreadcrumbTitle(null);
        setAlertVisible(false);
    }

    return (
        <UserContext.Provider value={{loggedIn, setLoggedIn}}>
            <BreadcrumbContext.Provider value={{breadcrumbTitle, breadcrumbItems, setBreadcrumb, removeBreadcrumb}}>
                <AlertContext.Provider value={{alertVisible, variant, message, showMessage, setAlertVisible}}>
                    {children}
                </AlertContext.Provider>
            </BreadcrumbContext.Provider>
        </UserContext.Provider>
    );
};
