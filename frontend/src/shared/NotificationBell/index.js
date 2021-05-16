import React, {useEffect, useState, useRef} from 'react';
import {useHistory} from 'react-router-dom';
import {ListGroup, Spinner} from 'react-bootstrap';
import {BsBell, BsBellFill} from 'react-icons/bs';
import {getNotifications, checkNotifications} from "../../utilities/ServerCall";
import {getUserId} from "../../utilities/Common";
import notificationSoundMp3 from "../../assets/audio/notification-sound.mp3";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import {useAlertContext} from "../../AppContext";

import './notificationBell.css';

let newNotificationCount = 0;
const hostUrl = process.env.REACT_APP_API_URL;

const NotificationBell = () => {

    const personId = getUserId();
    const history = useHistory();
    const notificationSound = new Audio(notificationSoundMp3);

    const myRef = useRef(null);

    const [page, setPage] = useState(0);
    const [size, setSize] = useState(16);
    const [lastPage, setLastPage] = useState(true);

    const [count, setCount] = useState(0);
    const [notifications, setNotifications] = useState([]);
    const [menuVisible, setMenuVisible] = useState(false);
    const [loading, setLoading] = useState(false);

    const [notification, setNotification] = useState(null);
    const {showMessage} = useAlertContext();

    const receiveNotification = (message) => {
        setNotification(JSON.parse(message.body));
    }

    useEffect(() => {
        if (page !== 0) {
            const fetchData = async () => {
                try {
                    setLoading(true);
                    let newPage = page;
                    let newSize = size;
                    if (newNotificationCount !== 0) {
                        newPage = 1;
                        newSize = page * size + newNotificationCount;
                        setPage(newPage);
                        setSize(newSize);
                        newNotificationCount = 0;
                    }
                    const data = await getNotifications(newPage, newSize);
                    const uncheckedIds = data.notifications
                        .filter(notif => !notif.checked)
                        .map(notif => notif.id);
                    setNotifications([...notifications, ...data.notifications]);
                    setLastPage(data.lastPage);
                    setCount(count + uncheckedIds.length);
                    setLoading(false);
                    if (uncheckedIds.length !== 0)
                        await checkNotifications(uncheckedIds);
                } catch (e) {
                    setLoading(false);
                }
            }
            fetchData();
        }
        // eslint-disable-next-line
    }, [page]);


    useEffect(() => {
        if (notification !== null) {
            setNotifications([notification, ...notifications]);
            setCount(count + 1);
            newNotificationCount++;
            notificationSound.play();
        }
        // eslint-disable-next-line
    }, [notification]);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const data = await getNotifications(page, size);
                const uncheckedCount = data.notifications.filter(notif => !notif.checked).length;
                setNotifications(data.notifications);
                setLastPage(data.lastPage);
                setCount(uncheckedCount);
            } catch (e) {
                showMessage("warning", "Error occurred while loading notifications");
            }
            setLoading(false);
        }

        const socket = new SockJS(hostUrl + '/push');
        const stompClient = Stomp.over(socket);
        stompClient.debug = null;

        stompClient.connect({}, () => {
            if (stompClient.connected)
                stompClient.subscribe('/topic/' + personId, receiveNotification);
        });

        fetchData();
        return () => stompClient.disconnect();
        // eslint-disable-next-line
    }, [personId])

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (myRef.current && !myRef.current.contains(event.target) && document.body.contains(event.target) && menuVisible) {
                setCount(0);
                setMenuVisible(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
        // eslint-disable-next-line
    }, [myRef, menuVisible, count, notifications])

    const itemClick = (notification) => {
        notification.checked = true;
        history.push(notification.url);
        setMenuVisible(false);
    }

    const bellClick = async () => {
        const isOpened = !menuVisible;
        setMenuVisible(isOpened);
        if (count > 0 && isOpened) {
            const uncheckedIds = notifications
                .filter(notif => !notif.checked)
                .map(notif => notif.id);
            try {
                await checkNotifications(uncheckedIds);
                setCount(0);
            } catch (e) {
                showMessage("warning", "Error occurred while opening notifications page");
            }
        }
    }

    const nextPage = () => {
        if (!loading) {
            setPage(page + 1);
        }
    }

    return (
        <div style={{position: 'relative'}} ref={myRef}>
            {count !== 0 ?
                <>
                    <BsBellFill className="notification-bell" onClick={bellClick}/>
                    <div className="notification-count" onClick={bellClick}>
                        {count}
                    </div>
                </> : <BsBell className="notification-bell" onClick={bellClick}/>}
            {menuVisible ?
                <ListGroup variant="notifications">
                    {notifications.length !== 0 ?
                        <>
                            {notifications.map((notification, i) => (
                                <ListGroup.Item
                                    style={!notification.checked ? {backgroundColor: '#F2eFFB'} : null}
                                    key={notification.id}
                                    onClick={() => itemClick(notification)}
                                >
                                    <div className={"dot-" + notification.type}>•</div>
                                    <div className="notification-description">
                                        {notification.description}
                                    </div>
                                    <div className="notification-separator"/>
                                    <div className="notification-product-name">
                                        {notification.name}
                                        <br/>
                                        <div style={{margin: '0 auto', fontSize: '12px'}} className="product-table-id">
                                            #{notification.productId}
                                        </div>
                                    </div>
                                </ListGroup.Item>
                            ))}
                            {!lastPage ?
                                <ListGroup.Item
                                    className={"notification-load-more " + (loading ? "notification-loading" : "")}
                                    onClick={nextPage}>
                                    {loading ?
                                        <Spinner className="notification-spinner" animation="border"
                                                 size="sm"/>
                                        : "Load more"}
                                </ListGroup.Item> : null}
                        </>
                        : loading ?
                            <Spinner className="notification-spinner" animation="border" size="sm"/>
                            :
                            <div className="no-notification-items">
                                No notifications found
                            </div>
                    }
                </ListGroup> : null}
        </div>
    )
}

export default NotificationBell;
