import React, {useCallback, useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {Formik} from 'formik';
import {Form} from 'react-bootstrap';
import {myAccountSellerUrl} from "../../utilities/AppUrl";
import SubmitButtons from './SubmitButtons';
import * as yup from 'yup';
import Dropzone from "../Dropzone";
import {uploadImage} from "../../utilities/ServerCall";
import {MdClear} from 'react-icons/md';

import './sellerTabs.css';


const SellTab1 = ({
                      categories: loadedCategories,
                      subcategories: loadedSubcategories,
                      selectCategory,
                      filters,
                      product,
                      setProduct,
                      setActiveTab
                  }) => {
    const history = useHistory();

    const [categories, setCategories] = useState(loadedCategories);
    const [subcategories, setSubcategories] = useState(loadedSubcategories);
    const [colors, setColors] = useState(filters.colors);
    const [sizes, setSizes] = useState(filters.sizes);
    const [images, setImages] = useState(product.images || []);
    const [uploading, setUploading] = useState(false);
    const [loading, setLoading] = useState(false);
    const [color, setColor] = useState(product.color || "Select Color");
    const [size, setSize] = useState(product.size || "Select Size");
    const maxImages = 10;

    const [nameLength, setNameLength] = useState(0);
    const [descriptionLength, setDescriptionLength] = useState(0);

    useEffect(() => {
        setCategories(loadedCategories);
    }, [loadedCategories])

    useEffect(() => {
        setSubcategories(loadedSubcategories);
    }, [loadedSubcategories])

    useEffect(() => {
        setColors(filters.colors);
        setSizes(filters.sizes);
    }, [filters])

    const schema = yup.object().shape({
        name: yup.string()
            .required("*Product name is required")
            .max(60, "*Product name can't be longer than 60 characters"),
        categoryId: yup.string()
            .notOneOf(["Select Category"], "*Category is required"),
        subcategoryId: yup.string()
            .notOneOf(["Select Subcategory"], "*Subcategory is required"),
        description: yup.string()
            .max(700, "*Product description can't be longer than 700 characters"),
    });

    const handleSubmit = async (data) => {
        setLoading(true);
        const newImages = await Promise.all(images.map(async (image, index) => {
            if (image.url !== undefined)
                return image;
            setUploading(true);
            image.url = await uploadImage(image.file);
            return image;
        }));
        setImages(newImages);
        setLoading(false);
        setProduct({...product, ...data, images: newImages});
        setActiveTab(1);
    }

    const onDrop = useCallback(acceptedFiles => {
        if (images.length + acceptedFiles.length > maxImages)
            return;
        acceptedFiles.map(file => {
            const reader = new FileReader();
            reader.onload = function (e) {
                setImages(prevState => [
                    ...prevState,
                    {src: e.target.result, file}
                ]);
            };
            reader.readAsDataURL(file);
            return file;
        });
    }, [images.length]);

    return (
        <div className="tab-container">
            <div className="tab-title">
                DETAIL INFORMATION ABOUT PRODUCT
            </div>
            <div className="tab-content">
                <Formik
                    validationSchema={schema}
                    initialValues={{
                        name: product.name || "",
                        categoryId: product.categoryId || "Select Category",
                        subcategoryId: product.subcategoryId || "Select Subcategory",
                        description: product.description || "",
                        color: product.color || null,
                        size: product.size || null
                    }}
                    onSubmit={handleSubmit}
                >
                    {({
                          handleSubmit,
                          handleChange,
                          touched,
                          errors,
                          setFieldValue
                      }) => (
                        <Form noValidate onSubmit={handleSubmit}>
                            <Form.Group className="sell-form-margin">
                                <Form.Label>What do you sell?</Form.Label>
                                <Form.Control
                                    className="form-control-gray-no-shadow"
                                    size="xl-18"
                                    name="name"
                                    defaultValue={product.name || ""}
                                    onChange={e => {
                                        handleChange(e);
                                        setNameLength(e.target.value.length);
                                    }}
                                    maxLength={60}
                                    isInvalid={touched.name && errors.name}
                                />
                                <Form.Control.Feedback className="inline-feedback-error" type="invalid">
                                    {errors.name}
                                </Form.Control.Feedback>
                                <Form.Text className="form-control-description">2-5 words
                                    ({60 - nameLength} characters)</Form.Text>
                            </Form.Group>

                            <Form.Group style={{display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap'}}>
                                <Form.Group className="form-half-width">
                                    <Form.Control
                                        defaultValue={product.categoryId || "Select Category"}
                                        name="categoryId"
                                        onChange={e => {
                                            selectCategory(e, handleChange);
                                            setFieldValue("subcategoryId", "Select Subcategory")
                                        }}
                                        size="xl-18"
                                        as="select"
                                        isInvalid={touched.categoryId && errors.categoryId}
                                    >
                                        <option value="Select Category" disabled hidden>Select Category</option>
                                        {categories.map(category => (
                                            <option key={category.id} value={category.id}>{category.name}</option>
                                        ))}
                                    </Form.Control>
                                    <Form.Control.Feedback type="invalid">
                                        {errors.categoryId}
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="form-half-width">
                                    <Form.Control
                                        defaultValue={product.subcategoryId || "Select Subcategory"}
                                        name="subcategoryId"
                                        onChange={handleChange}
                                        size="xl-18"
                                        as="select"
                                        isInvalid={touched.subcategoryId && errors.subcategoryId}
                                    >
                                        <option value="Select Subcategory" hidden>Select Subcategory</option>
                                        {subcategories.map(subcategory => (
                                            <option key={subcategory.id}
                                                    value={subcategory.id}>{subcategory.name}</option>
                                        ))}
                                    </Form.Control>
                                    <Form.Control.Feedback type="invalid">
                                        {errors.subcategoryId}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </Form.Group>

                            <Form.Group className="sell-form-margin">
                                <Form.Label>Description</Form.Label>
                                <Form.Control
                                    as="textarea"
                                    className="form-control-gray-no-shadow"
                                    size="xl-18"
                                    name="description"
                                    defaultValue={product.description || ""}
                                    onChange={e => {
                                        handleChange(e);
                                        setDescriptionLength(e.target.value.length);
                                    }}
                                    isInvalid={touched.description && errors.description}
                                    maxLength={700}
                                    rows={5}
                                />
                                <Form.Control.Feedback className="inline-feedback-error" type="invalid">
                                    {errors.description}
                                </Form.Control.Feedback>
                                <Form.Text className="form-control-description">100 words
                                    ({700 - descriptionLength} characters)</Form.Text>
                            </Form.Group>

                            <Form.Group style={{display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap'}}>
                                <Form.Group className="form-half-width">
                                    <Form.Control
                                        defaultValue={product.color || "Select Color"}
                                        name="color"
                                        onChange={e => {
                                            handleChange(e);
                                            setColor(e.target.value);
                                        }}
                                        value={color}
                                        size="xl-18"
                                        as="select"
                                    >
                                        <option value="Select Color" disabled hidden>Select Color</option>
                                        {colors.map(color => (
                                            <option key={color} value={color}>{color}</option>
                                        ))}
                                    </Form.Control>
                                    <MdClear
                                        onClick={() => {
                                            setFieldValue("color", null);
                                            setColor("Select Color");
                                        }}
                                        className="select-clear"
                                    />
                                </Form.Group>

                                <Form.Group className="form-half-width">
                                    <Form.Control
                                        defaultValue={product.size || "Select Size"}
                                        name="size"
                                        onChange={e => {
                                            handleChange(e);
                                            setSize(e.target.value);
                                        }}
                                        value={size}
                                        size="xl-18"
                                        as="select"
                                    >
                                        <option value="Select Size" disabled hidden>Select Size</option>
                                        {sizes.map(size => (
                                            <option key={size} value={size}>{size.replace("_", " ")}</option>
                                        ))}
                                    </Form.Control>
                                    <MdClear
                                        onClick={() => {
                                            setFieldValue("size", null);
                                            setSize("Select Size");
                                        }}
                                        className="select-clear"
                                    />
                                </Form.Group>
                            </Form.Group>

                            <Dropzone onDrop={onDrop} accept={"image/*"} images={images} setImages={setImages}/>
                            <Form.Text style={{marginBottom: 40}} className="form-control-description">
                                {maxImages - images.length !== 0 ?
                                    <>
                                        You can add {maxImages - images.length} photos more
                                    </>
                                    :
                                    <>
                                        You can't add anymore photos
                                    </>
                                }
                            </Form.Text>

                            <SubmitButtons
                                onBack={() => history.push(myAccountSellerUrl)}
                                loading={loading}
                                uploading={uploading}
                            />
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
}

export default SellTab1;
