import React, {useRef} from 'react';
import {Formik} from 'formik';
import {Form, InputGroup} from 'react-bootstrap';
import SubmitButtons from './SubmitButtons';
import ReactDatePicker from 'react-datepicker';
import moment from 'moment';
import * as yup from 'yup';

import './sellerTabs.css';

const SellTabTwo = ({product, setProduct, setActiveTab}) => {

    const endDateRef = useRef(null);

    const schema = yup.object().shape({
        startPrice: yup.number()
            .typeError("*Start price must be a number")
            .required("*Start price is required")
            .min(0.01, "*Start price can't be lower than $0.01")
            .max(999999.99, "*Start price can't be higher than $999999.99"),
        startDate: yup.string()
            .nullable()
            .required("*Start date is required"),
        endDate: yup.string()
            .nullable()
            .required("*End date is required")
    });

    const saveValues = (data) => {
        const newData = {...product, ...data};
        setProduct(newData);
    }

    const handleSubmit = (data) => {
        saveValues(data);
        setActiveTab(2);
    }

    const getTimeInfo = (startDate, endDate) => {
        if (startDate === null || endDate === null)
            return "/";
        const productStartDate = moment(startDate);
        const productEndDate = moment(endDate);
        return moment.duration(productEndDate.diff(productStartDate)).format("D [days] h [hours] m [minutes]", {trim: "all"});
    }

    return (
        <div className="tab-container">
            <div className="tab-title">
                SET PRICES
            </div>
            <div className="tab-content">
                <Formik
                    validationSchema={schema}
                    initialValues={{
                        startPrice: product.startPrice || "",
                        startDate: product.startDate || null,
                        endDate: product.endDate || null
                    }}
                    onSubmit={handleSubmit}
                >
                    {({
                          handleSubmit,
                          handleChange,
                          touched,
                          errors,
                          setFieldValue,
                          setFieldTouched,
                          values
                      }) => (
                        <Form noValidate onSubmit={handleSubmit}>
                            <Form.Group style={{marginBottom: 60}}>
                                <Form.Label>Your start price</Form.Label>
                                <InputGroup>
                                    <InputGroup.Prepend>
                                        <InputGroup.Text>$</InputGroup.Text>
                                    </InputGroup.Prepend>
                                    <Form.Control
                                        className="form-control-gray-no-shadow"
                                        size="xl-18"
                                        name="startPrice"
                                        defaultValue={product.startPrice || ""}
                                        onChange={handleChange}
                                        maxLength={9}
                                        isInvalid={touched.startPrice && errors.startPrice}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        {errors.startPrice}
                                    </Form.Control.Feedback>
                                </InputGroup>
                            </Form.Group>

                            <Form.Group className="form-sell-tab-two">
                                <Form.Group className="form-half-width">
                                    <Form.Label>Start date</Form.Label>
                                    <div>
                                        <ReactDatePicker
                                            className="form-control form-control-xl-18 form-control-gray-no-shadow"
                                            placeholderText="DD/MM/YYYY"
                                            dateFormat="dd/MM/yyyy"
                                            name="startDate"
                                            minDate={new Date()}
                                            selected={values.startDate}
                                            onChange={date => {
                                                if (date === null) {
                                                    setFieldValue("startDate", date);
                                                    return;
                                                }
                                                setFieldValue("startDate", moment(date).isBefore(moment()) ? moment().toDate() : moment(date).startOf('day').toDate());
                                                if (values.endDate === null) {
                                                    endDateRef.current.setOpen(true);
                                                } else if (!moment(values.endDate).isAfter(date)) {
                                                    setFieldValue("endDate", moment(date).endOf('day').toDate());
                                                    endDateRef.current.setOpen(true);
                                                }
                                            }}
                                            disabledKeyboardNavigation
                                            useWeekdaysShort={true}
                                            onBlur={() => values.startDate !== null ? setFieldTouched("startDate", true) : null}
                                        />
                                    </div>
                                    <Form.Control.Feedback
                                        className={touched.startDate && errors.startDate ? "d-block" : null}
                                        type="invalid">
                                        {errors.startDate}
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="form-half-width">
                                    <Form.Label>End date</Form.Label>
                                    <div>
                                        <ReactDatePicker
                                            className="form-control form-control-xl-18 form-control-gray-no-shadow"
                                            placeholderText="DD/MM/YYYY"
                                            dateFormat="dd/MM/yyyy"
                                            name="endDate"
                                            minDate={values.startDate !== null ? moment(values.startDate).toDate() : moment().toDate()}
                                            selected={values.endDate}
                                            onChange={date => {
                                                if (date === null) {
                                                    setFieldValue("endDate", date);
                                                    return;
                                                }
                                                setFieldValue("endDate", moment(date).endOf('day').toDate());
                                            }}
                                            useWeekdaysShort={true}
                                            ref={endDateRef}
                                            disabledKeyboardNavigation
                                            onBlur={() => values.endDate !== null ? setFieldTouched("endDate", true) : null}
                                        />
                                    </div>
                                    <Form.Control.Feedback
                                        className={touched.startDate && errors.endDate ? "d-block" : null}
                                        type="invalid">
                                        {errors.endDate}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </Form.Group>
                            <Form.Text style={{textAlign: 'left', marginBottom: 80}}
                                       className="form-control-description">
                                Active time: {getTimeInfo(values.startDate, values.endDate)}
                                <br/>
                                The auction will be automatically closed when the time comes. The highest bid will win
                                the auction.
                            </Form.Text>
                            <SubmitButtons
                                onBack={() => {
                                    saveValues(values);
                                    setActiveTab(0)
                                }}
                            />
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
}

export default SellTabTwo;
