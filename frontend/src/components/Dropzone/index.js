import React from "react";
import {useDropzone} from "react-dropzone";
import ImageGrid from "../ImageGrid";
import {DndProvider} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";
import {TouchBackend} from "react-dnd-touch-backend";
import update from "immutability-helper";

import './dropzone.css';

const isTouchDevice = () => {
    return "ontouchstart" in window;
};

const backendForDND = isTouchDevice() ? TouchBackend : HTML5Backend;

const getClassName = (className, isActive) => {
    if (!isActive)
        return className;
    return `${className} ${className}-active`;
};

const Dropzone = ({onDrop, accept, images, setImages}) => {
    const {getRootProps, getInputProps, isDragActive} = useDropzone({
        onDrop,
        accept
    });

    const moveImage = (dragIndex, hoverIndex) => {
        const draggedImage = images[dragIndex];
        setImages(
            update(images, {
                $splice: [[dragIndex, 1], [hoverIndex, 0, draggedImage]]
            })
        );
    };

    return (
        <div className={getClassName("dropzone", isDragActive)} {...getRootProps()}>
            <input className="dropzone-input" {...getInputProps()} />
            <div className="dropzone-content">
                <DndProvider backend={backendForDND}>
                    <ImageGrid images={images} moveImage={moveImage}/>
                </DndProvider>
                <div style={{margin: 'auto 10px'}}>
                    {isDragActive ? (
                        "Release to drop the image here"
                    ) : (
                        <>
                                <span style={{color: '#8367D8'}}>
                                    Upload photos
                                </span>
                            {' '} or just drag and drop
                            <div>
                                {images.length > 1 ? "(Tip: Drag the images to change their position)" : null}
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Dropzone;
